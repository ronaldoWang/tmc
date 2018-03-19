package cn.droidlover.xdroidmvp.systmc.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.blankj.utilcode.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.cache.MemoryCache;
import cn.droidlover.xdroidmvp.systmc.db.OrmLiteManager;
import cn.droidlover.xdroidmvp.systmc.model.sys.Dict;

/**
 * Created by ronaldo on 2017/5/9.
 */

public class DictUtil {

    private static final String CACHE_DICT_MAP = "dictMap";//字典项缓存

    /**
     * 获得字典项目的缓存
     *
     * @param context
     * @param type
     * @return
     */
    public static List<Dict> getDictList(Context context, String type) {
        @SuppressWarnings("unchecked")
        Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>) MemoryCache.getInstance().get(CACHE_DICT_MAP);
        if (dictMap == null) {
            dictMap = new HashMap<>();
            for (Dict dict : OrmLiteManager.getInstance(context).getLiteOrm(context).query(Dict.class)) {
                List<Dict> dictList = dictMap.get(dict.getType());
                if (dictList != null) {
                    dictList.add(dict);
                } else {
                    List<Dict> list = new ArrayList<Dict>();
                    list.add(dict);
                    dictMap.put(dict.getType(), list);
                }
            }
            MemoryCache.getInstance().put(CACHE_DICT_MAP, dictMap);
        }
        List<Dict> dictList = dictMap.get(type);
        if (dictList == null) {
            dictList = new ArrayList<>();
        }
        return dictList;
    }

    /**
     * 根据type和value，获得该value在集合中的下标
     *
     * @param context
     * @param type
     * @param value
     * @return
     */
    public static Integer getDictIndex(Context context, String type, String value) {
        List<Dict> list = getDictList(context, type);
        for (int i = 0; i < list.size(); i++) {
            Dict dict = list.get(i);
            if (dict.getValue().equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据type ，value获得label
     *
     * @param context
     * @param type         字典类型
     * @param value        key
     * @param defaultLabel 默认label
     * @return
     */
    public static String getDictLabel(Context context, String type, String value, String defaultLabel) {
        if (!StringUtils.isTrimEmpty(type) && !StringUtils.isTrimEmpty(value)) {
            for (Dict dict : getDictList(context, type)) {
                if (type.equals(dict.getType()) && value.equals(dict.getValue())) {
                    return dict.getLabel();
                }
            }
        }
        return defaultLabel;
    }

    /**
     * 根据adapter和value，获得该value在集合中的下标
     *
     * @param adapter
     * @param value
     * @return
     */
    public static Integer getDictIndex(ArrayAdapter<Dict> adapter, String value) {
        int k = adapter.getCount();
        for (int i = 0; i < k; i++) {
            if (adapter.getItem(i).getValue().equals(value)) {
                return i;
            }
        }
        return -1;
    }
}
