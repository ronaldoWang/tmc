package cn.droidlover.xdroidmvp.systmc.utils;

import java.util.List;

/**
 * Created by ronaldo on 2017/7/15.
 */

public class StringUtils {

    public static String listToString(List<String> collectIds) {
        String str = collectIds.toString();
        int len = str.length() - 1;
        String ids = str.substring(1, len).replace(" ", "");
        return ids;
    }
}
