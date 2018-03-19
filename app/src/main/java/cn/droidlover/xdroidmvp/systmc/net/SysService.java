package cn.droidlover.xdroidmvp.systmc.net;

import cn.droidlover.xdroidmvp.systmc.model.MessageModel;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ThinkPad on 2017/10/21.
 */

public interface SysService {

    /**
     * 更新app
     *
     * @param versionCode
     * @return
     */
    @GET("api/sysapi/doUpdateApp")
    Flowable<MessageModel> doUpdateApp(@Query("versionCode") int versionCode);

    /**
     * 更新离线db
     *
     * @param tbVersion
     * @return
     */
    @GET("rest/sys/doUpdateDb")
    Flowable<MessageModel> doUpdateDb(@Query("tbVersion") int tbVersion);
}
