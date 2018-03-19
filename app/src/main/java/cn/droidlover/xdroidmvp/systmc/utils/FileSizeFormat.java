package cn.droidlover.xdroidmvp.systmc.utils;

import java.text.DecimalFormat;

/**
 * Created by ThinkPad on 2017/10/22.
 */

public class FileSizeFormat {
    private static String kB_UNIT_NAME = "KB";
    private static String B_UNIT_NAME = "B";
    private static String MB_UNIT_NAME = "MB";

    public static String getSizeString(long size) {
        if (size < 1024) {
            return String.valueOf(size) + B_UNIT_NAME;
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            return String.valueOf(size) + kB_UNIT_NAME;
        } else {
            size = size * 100 / 1024;
        }

        return String.valueOf((size / 100)) + "."
                + ((size % 100) < 10 ? "0" : "") + String.valueOf((size % 100))
                + MB_UNIT_NAME;
    }

    /**
     * 以Mb为单位保留两位小数
     *
     * @param dirSize
     * @return
     */
    public static String getMbSize(long dirSize) {
        double size = 0;
        size = (dirSize + 0.0) / (1024 * 1024);
        DecimalFormat df = new DecimalFormat("0.00");// 以Mb为单位保留两位小数
        String filesize = df.format(size);
        return filesize;
    }

    /**
     * 以Mb为单位保留两位小数
     *
     * @param dirSize
     * @return
     */
    public static String getKBSize(long dirSize) {
        double size = 0;
        size = (dirSize + 0.0) / 1024;
        DecimalFormat df = new DecimalFormat("0.00");// 以KB为单位保留两位小数
        String filesize = df.format(size);
        return filesize;
    }
}
