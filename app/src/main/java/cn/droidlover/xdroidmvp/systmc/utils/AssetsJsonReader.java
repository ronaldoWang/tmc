package cn.droidlover.xdroidmvp.systmc.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ronaldo on 2017/7/14.
 */

public class AssetsJsonReader {

    /**
     * 读取asset下的json文件
     *
     * @param context
     * @param path    assets下json文件路径
     * @return
     */
    public static String getJsonString(Context context, String path) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer stringBuffer = new StringBuffer();
            String str = null;
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
