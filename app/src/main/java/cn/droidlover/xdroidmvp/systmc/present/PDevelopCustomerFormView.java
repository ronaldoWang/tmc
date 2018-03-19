package cn.droidlover.xdroidmvp.systmc.present;

import com.blankj.utilcode.util.ToastUtils;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.systmc.db.OrmLiteManager;
import cn.droidlover.xdroidmvp.systmc.model.DevelopCustomerModel;
import cn.droidlover.xdroidmvp.systmc.net.Api;
import cn.droidlover.xdroidmvp.systmc.ui.DevelopCustomerFormViewActivity;

/**
 * Created by haoxi on 2017/5/11.
 */

public class PDevelopCustomerFormView extends XPresent<DevelopCustomerFormViewActivity> {
    /**
     * 在线查询单个
     *
     * @param id
     */
    public void queryOne(final String id) {
        Api.getDevelopCustomerService().queryOne(id)
                .compose(XApi.<DevelopCustomerModel>getApiTransformer())
                .compose(XApi.<DevelopCustomerModel>getScheduler())
                .compose(getV().<DevelopCustomerModel>bindToLifecycle())
                .subscribe(new ApiSubscriber<DevelopCustomerModel>() {
                    @Override
                    protected void onFail(NetError error) {
                        ToastUtils.showShort(error.getMessage());
                    }

                    @Override
                    public void onNext(DevelopCustomerModel developCustomerModel) {
                        if (developCustomerModel.isSuccess()) {
                            if (!developCustomerModel.getData().isEmpty()) {
                                getV().showData(developCustomerModel.getData().get(0));
                            }
                        } else {
                            ToastUtils.showShort(developCustomerModel.getMessage());
                        }
                    }
                });
    }

    /**
     * 离线查询单个
     *
     * @param id
     */
    public void queryNativeOne(final String id) {
        DevelopCustomerModel.DevelopCustomer data = OrmLiteManager.queryById(getV(), DevelopCustomerModel.DevelopCustomer.class, id);
        if (null == data) {
            ToastUtils.showShort("未查询到数据");
        } else {
            getV().showData(data);
        }
    }
}
