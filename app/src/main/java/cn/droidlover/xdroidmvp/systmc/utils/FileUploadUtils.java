package cn.droidlover.xdroidmvp.systmc.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by ronaldo on 2017/9/16.
 */

public class FileUploadUtils {

    public static List<MultipartBody.Part> pathToPart(List<String> pathList) {
        List<MultipartBody.Part> parts = new ArrayList<>(pathList.size());
        //MultipartBody.Builder builder = new MultipartBody.Builder()
        //        .setType(MultipartBody.FORM)//表单类型
        ;//ParamKey.TOKEN 自定义参数key常量类，即参数名
        //多张图片
        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));//filePath 图片地址
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            //builder.addFormDataPart("imgfile" + i, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
            MultipartBody.Part part = MultipartBody.Part.createFormData("multipartFiles", file.getName(), requestBody);
            parts.add(part);
        }

        return parts;
    }

    public static List<MultipartBody.Part> pathToPart(String path) {
        List<MultipartBody.Part> parts = new ArrayList<>(1);
        //多张图片
        File file = new File(path);//filePath 图片地址
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //builder.addFormDataPart("imgfile" + i, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        MultipartBody.Part part = MultipartBody.Part.createFormData("multipartFiles", file.getName(), requestBody);
        parts.add(part);

        return parts;
    }
}
