package cn.droidlover.xdroidmvp.systmc.net;

import java.util.Map;

import cn.droidlover.xdroidmvp.systmc.model.DevelopCustomerModel;
import io.reactivex.Flowable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by ronaldo on 2017/4/22.
 */

public interface DevelopCustomerService {

    @GET("frontapi/developCustomer/queryList")
    Flowable<DevelopCustomerModel> query(@Query("pageNo") int pageNum, @QueryMap(encoded = true) Map<String, Object> conditionMap);

    @GET("frontapi/developCustomer/queryOne")
    Flowable<DevelopCustomerModel> queryOne(@Query("customerNo") String id);

    @POST("frontapi/developCustomer/save")
    Flowable<DevelopCustomerModel> save(@QueryMap(encoded = true) Map<String, Object> map);

    @DELETE("frontapi/developCustomer/delete")
    Flowable<DevelopCustomerModel> delete(@Query("customerNo") String customerNo);
}
