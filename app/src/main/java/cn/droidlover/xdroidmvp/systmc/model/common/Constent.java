package cn.droidlover.xdroidmvp.systmc.model.common;

/**
 * Created by haoxi on 2017/5/12.
 */

public class Constent {
    public static final Integer PAGE_SIZE = 10;
    public static boolean ONLINE = false;

    public static String overhaulUser = "1";//检修
    public static String teamUser = "2";//班组

    public static String userId = "";//用户id
    public static String userName = "";//用户姓名
    public static Integer taskOrgId = -1;//班组id
    public static String taskOrgCode = "";//班组编号
    public static String taskOrgName = "";//班组名称
    public static String postName;// 岗位名称
    public static String userType;// 用户类型（1、检修，2、班组）
    public static String personId = "";//用户id



    public static boolean taskReadOnly = false;//抢修任务是否只读

    public static boolean taskOneReadOnly = false;
    public static boolean taskTwoReadOnly = false;
    public static boolean taskThreeReadOnly = false;
    public static boolean taskFourReadOnly = false;
    public static boolean taskFiveReadOnly = false;

    public static final String KEY_UPDATE_APK_TIME = "update_apk_time";//apk更新时间
    public static final String KEY_UPDATE_DB_TIME = "update_db_time";//db更新时间
}
