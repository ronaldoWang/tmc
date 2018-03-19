package cn.droidlover.xdroidmvp.systmc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.blankj.utilcode.util.StringUtils;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.SQLiteHelper;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import cn.droidlover.xdroidmvp.systmc.App;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.model.common.Constent;

/**
 * ormlite数据库管理类
 */
public class OrmLiteManager {
    public static final String DB_NAME = "pda.db";
    public static final String PACKAGE_NAME = App.getInstance().getPackageName();
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases/";

    private static LiteOrm liteOrm;
    private static OrmLiteManager instance;

    private OrmLiteManager(Context context) {
        File dbDir = new File(DB_PATH);
        File[] dbFiles = dbDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if (s.contains("pda") && !s.equals(DB_NAME) && !s.equals(DB_NAME + "-journal")) {
                    return true;
                }
                return false;
            }
        });
        if (null != dbFiles) {
            for (File dbfile : dbFiles) {
                dbfile.delete();
            }
        }
        copyRawDB(context);
    }

    private void copyRawDB(Context context) {
        try {
            String databaseFilename = DB_PATH + DB_NAME;
            File dir = new File(DB_PATH);

            if (!dir.exists()) {
                dir.mkdir();
            }
            File dataFile = new File(databaseFilename);
            if (!dataFile.exists()) {
                InputStream is = context.getResources().openRawResource(
                        R.raw.pda);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[1024];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OrmLiteManager getInstance(Context context) {
        if (instance == null) {
            instance = new OrmLiteManager(context);
        }
        return instance;
    }

    public LiteOrm getLiteOrm(Context context) {
        if (liteOrm == null) {
            DataBaseConfig config = new DataBaseConfig(context, DB_PATH + DB_NAME);
            config.debugged = true; // open the log
            config.dbVersion = 1; // set database version
            // set database update listener
            config.onUpdateListener = new SQLiteHelper.OnUpdateListener() {
                @Override
                public void onUpdate(SQLiteDatabase var1, int oldCode, int newCode) {

                }

            };
            liteOrm = LiteOrm.newCascadeInstance(config);
        }
        return liteOrm;
    }

    /**
     * 查询集合
     *
     * @param context  上下文
     * @param clazz    对象类
     * @param distinct 去重
     * @param where    条件查询sql
     * @param groupBy  分组
     * @param having   查询
     * @param orderBy  排序
     * @param pageNum  页码
     * @param <T>      类型
     * @return 集合
     */
    public static <T> List<T> query(Context context, Class<T> clazz, boolean distinct, String where,
                                    String groupBy, String having, String orderBy, Integer pageNum) {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(clazz);
        queryBuilder.distinct(distinct);
        queryBuilder.where(where);
        queryBuilder.groupBy(groupBy);
        queryBuilder.having(having);
        queryBuilder.orderBy(orderBy);
        queryBuilder.limit((pageNum - 1) * Constent.PAGE_SIZE, Constent.PAGE_SIZE);
        return liteOrm.query(queryBuilder);
    }

    /**
     * 查询集合
     *
     * @param context      上下文
     * @param clazz        对象类
     * @param distinct     去重
     * @param where        条件查询sql
     * @param groupBy      分组
     * @param having       查询
     * @param orderBy      排序
     * @param pageNum      页码
     * @param conditionMap 查询条件
     * @param <T>          类型
     * @return 集合
     */
    public static <T> List<T> query(Context context, Class<T> clazz, boolean distinct, String where,
                                    String groupBy, String having, String orderBy, Integer pageNum, Map<String, Object> conditionMap) {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(clazz);
        queryBuilder.distinct(distinct);
        String querySb = getConditionSql(clazz, where, conditionMap);
        queryBuilder.where(querySb);
        queryBuilder.groupBy(groupBy);
        queryBuilder.having(having);
        queryBuilder.orderBy(orderBy);
        queryBuilder.limit((pageNum - 1) * Constent.PAGE_SIZE, Constent.PAGE_SIZE);
        return liteOrm.query(queryBuilder);
    }

    /**
     * 构造查询where
     *
     * @param clazz        类
     * @param where        查询条件
     * @param conditionMap 查询参数集合
     * @param <T>          类型
     * @return 构造的sql
     */
    private static <T> String getConditionSql(Class<T> clazz, String where, Map<String, Object> conditionMap) {
        try {
            StringBuilder querySb = new StringBuilder(" 1=1 ");
            if (where != null) {
                querySb.append(" and ").append(where);
            }
            Set<String> set = conditionMap.keySet();
            Map<String, List<String>> sqlMap = new HashMap<String, List<String>>();
            for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                if (key.startsWith("searchcondition")) {
                    String[] keys = key.split("_");
                    String attributeName = keys[1];// 查询字段如CompanyBaseMessageId0companyName
                    String typeName = keys[2];// 字段类型如string
                    String relOp = keys[3];//查询类型如like
                    Object obj = conditionMap.get(key);//查询的值
                    if (null != obj && !obj.equals("")) {
                        String value = obj.toString();
                        Object vObject = null;
                        if (StringUtils.equalsIgnoreCase(typeName, "int")) {
                            vObject = Integer.valueOf(value);
                        } else if (StringUtils.equalsIgnoreCase(typeName, "string[]")) {
                            String[] arrString = value.split(",");
                            String[] arrInteger = new String[arrString.length];
                            for (int i = 0; i < arrString.length; i++) {
                                arrInteger[i] = arrString[i];
                            }
                            vObject = arrInteger;
                        } else if (StringUtils.equalsIgnoreCase(typeName, "double")) {
                            vObject = Double.valueOf(value);
                        } else if (StringUtils.equalsIgnoreCase(typeName, "boolean")) {
                            vObject = Boolean.valueOf(value);
                        } else if (StringUtils.equalsIgnoreCase(typeName, "string")) {
                            if (StringUtils.equalsIgnoreCase(relOp, "like")) {
                                vObject = "'%" + value + "%'";
                            } else if (StringUtils.equalsIgnoreCase(relOp, "rightlike")) {
                                vObject = "'" + value + "%";
                            } else if (StringUtils.equalsIgnoreCase(relOp, "leftlike")) {
                                vObject = "'%" + value + "'";
                            } else {
                                vObject = value;
                            }
                        } else if (StringUtils.equalsIgnoreCase(typeName, "date")) {
                            vObject = "'" + value + "'";
                        } else {
                            System.out.println("unknown dataType");
                        }
                        if ("eq".equals(relOp)) {
                            relOp = "=";
                        } else if ("neq".equals(relOp)) {
                            relOp = "=";
                        } else if ("lt".equals(relOp)) {
                            relOp = "<";
                        } else if ("le".equals(relOp)) {
                            relOp = "<=";
                        } else if ("gt".equals(relOp)) {
                            relOp = ">";
                        } else if ("ge".equals(relOp)) {
                            relOp = ">=";
                        } else if ("in".equals(relOp)) {
                            relOp = "in";
                        } else if ("like".equals(relOp)
                                || "rightlike".equals(relOp)
                                || "leftlike".equals(relOp)) {
                            relOp = relOp;
                        } else if ("isnotnull".equals(relOp)) {
                            relOp = " is not null";
                            vObject = "";
                        } else {
                            System.out.println("unknown relation operation");
                        }
                        String[] attributeNames = attributeName.split("0");
                        if (attributeNames.length > 1) {
                            String className = attributeNames[0];// 外键 字段
                            String column = attributeNames[1];// 外键类的列
                            StringBuilder subwhere = new StringBuilder();// 用于子查询
                            subwhere.append(" and ").append(column).append(" ").append(relOp).append(" ").append(vObject);
                            if (null == sqlMap.get(className)) {
                                List<String> list = new ArrayList<>();
                                list.add(subwhere.toString());
                                sqlMap.put(className, list);
                            } else {
                                List<String> list = sqlMap.get(className);
                                list.add(subwhere.toString());
                            }
                        } else {
                            String column = attributeNames[0];// 主表字段
                            Field field = clazz.getDeclaredField(column);
                            String columnName = getColumnByField(field);
                            querySb.append(" and ").append(columnName).append(" ").append(relOp).append(" ").append(vObject);
                        }
                    }
                }
            }
            /**
             * 组织子表的子查询 语句
             */
            Set classSet = sqlMap.keySet();
            for (Iterator it = classSet.iterator(); it.hasNext(); ) {
                String object = (String) it.next();
                List classNameList = sqlMap.get(object);
                Class fkClass = clazz.getDeclaredField(object).getType();
                querySb.append(" and ").append(object).append(" in (select id from ").append(getTableName(fkClass)).append(" where 1=1");
                StringBuilder whereSb = new StringBuilder();
                for (int i = 0; i < classNameList.size(); i++) {
                    whereSb.append(classNameList.get(i));
                }
                querySb.append(whereSb).append(")");
            }
            return querySb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return where;
        }
    }

    /**
     * 获得表名
     *
     * @param clazz 类
     * @return 表名
     */
    private static String getTableName(Class clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        if (table == null || StringUtils.isEmpty(table.value())) {
            // 当没有注解的时候默认用类的名称作为表名,并把点（.）替换为下划线(_)
            return clazz.getName().toLowerCase().replace('.', '_');
        }
        return table.value();
    }

    /**
     * 获得表中字段名
     *
     * @param field 反射字段
     * @return 字段名
     */
    private static String getColumnByField(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (null != column && !StringUtils.isTrimEmpty(column.value())) {
            return column.value();
        }
        return field.getName();
    }

    /**
     * 查询集合
     *
     * @param context 上下文
     * @param clazz   类
     * @param where   查询sql
     * @param <T>     类型
     * @return 集合
     */
    public static <T> List<T> query(Context context, Class<T> clazz, String where) {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(clazz);
        queryBuilder.where(where);
        return liteOrm.query(queryBuilder);
    }

    /**
     * 查询单个
     *
     * @param context 上下文
     * @param clazz   类
     * @param where   查询sql
     * @param <T>     类型
     * @return 单个对象
     */
    public static <T> T queryUnique(Context context, Class<T> clazz, String where) throws Exception {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(clazz);
        queryBuilder.where(where);
        List<T> list = liteOrm.query(queryBuilder);
        if (list.isEmpty()) {
            return null;
        } else if (list.size() > 1) {
            throw new Exception("the result has more than one Object");
        } else {
            return list.get(0);
        }
    }

    /**
     * 根据id查询单个
     *
     * @param context 上下文
     * @param clazz   类
     * @param id      主键id 字符串
     * @param <T>     类型
     * @return 对象
     */
    public static <T> T queryById(Context context, Class<T> clazz, String id) {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        return liteOrm.queryById(id, clazz);
    }

    /**
     * 根据id查询单个
     *
     * @param context 上下文
     * @param clazz   类
     * @param id      主键id 整形
     * @param <T>     类型
     * @return 对象
     */
    public static <T> T queryById(Context context, Class<T> clazz, Integer id) {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        return liteOrm.queryById(Long.valueOf(id), clazz);
    }


    /**
     * 获得总数
     *
     * @param context 上下文
     * @param clazz   类
     * @param where   查询sql
     * @return 总数
     */
    public static <T> Long getCount(Context context, Class<T> clazz, String where) {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(clazz);
        queryBuilder.where(where);
        return liteOrm.queryCount(queryBuilder);
    }

    /**
     * 获得总数
     *
     * @param context 上下文
     * @param clazz   类
     * @return 总数
     */
    public static <T> Long getCount(Context context, Class<T> clazz) {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        return liteOrm.queryCount(clazz);
    }

    /**
     * 保存
     *
     * @param context 上下文
     * @param entity  对象
     * @return 主键
     */
    public static Long save(Context context, Objects entity) {
        try {
            LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
            Class clazz = entity.getClass();
            Field id = clazz.getDeclaredField("id");
            if (null == id.get(entity)) {
                liteOrm.insert(entity);
            } else {
                liteOrm.update(entity);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    /**
     * 删除对象
     *
     * @param context 上下文
     * @param entity  对象
     * @return 是否成功
     */
    public static boolean delete(Context context, Objects entity) {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        int del = liteOrm.delete(entity);
        return del > 0;
    }

    /**
     * 根据where删除
     *
     * @param context 上下文
     * @param clazz   类
     * @param id      主键id
     * @param <T>     类型
     * @return 是否删除
     */
    public static <T> boolean delete(Context context, Class<T> clazz, Object id) {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        int del;
        if (id instanceof String) {
            del = liteOrm.delete(queryById(context, clazz, (String) id));
        } else {
            del = liteOrm.delete(queryById(context, clazz, (Integer) id));
        }
        return del > 0;
    }


    /**
     * 根据where删除
     *
     * @param context 上下文
     * @param clazz   类
     * @param where   查询sql
     * @param <T>     类型
     * @return 是否删除
     */
    public static <T> boolean delete(Context context, Class<T> clazz, String where) {
        LiteOrm liteOrm = OrmLiteManager.getInstance(context).getLiteOrm(context);
        WhereBuilder whereBuilder = new WhereBuilder(clazz);
        whereBuilder.setWhere(where);
        int del = liteOrm.delete(whereBuilder);
        return del > 0;
    }
}
