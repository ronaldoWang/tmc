package cn.droidlover.xdroidmvp.systmc.net.order;

import java.util.Map;

import cn.droidlover.xdroidmvp.systmc.model.order.OrderModel;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by haoxi on 2017/8/16.
 */

public interface OrderService {
    /**
     * 查询单个
     *
     * @param id
     * @return
     */
    @GET("api/orderapi/queryOne")
    Flowable<OrderModel> queryOne(@Query("id") String id);

    /**
     * 分页查询数据
     *
     * @param pageNum
     * @param conditionMap
     * @return
     */
    @GET("api/orderapi/query")
    Flowable<OrderModel> query(@Query("pageNo") int pageNum, @QueryMap(encoded = true) Map<String, Object> conditionMap);
}
