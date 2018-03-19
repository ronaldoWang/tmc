package cn.droidlover.xdroidmvp.systmc.net;

import cn.droidlover.xdroidmvp.systmc.model.UserModel;
import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ronaldo on 2017/4/22.
 */

public interface UserService {

    @GET("api/sysapi/login")
    Flowable<UserModel> login(@Query("loginName") String userName,
                              @Query("password") String userPwd);


    /**
     * 更新密码
     *
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return
     */
    @FormUrlEncoded
    @POST("rest/appuser/updatePwd")
    Flowable<UserModel> doUpdatePwd(@Field("oldPwd") String oldPwd, @Field("newPwd") String newPwd, @Field("userId") String userId);
}
