package cn.droidlover.xdroidmvp.systmc.present;

import com.blankj.utilcode.util.ToastUtils;

import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.db.OrmLiteManager;
import cn.droidlover.xdroidmvp.systmc.model.DevelopCustomerModel;
import cn.droidlover.xdroidmvp.systmc.net.Api;
import cn.droidlover.xdroidmvp.systmc.ui.DevelopCustomerActivity;
import cn.droidlover.xdroidmvp.systmc.widget.LoadingDialog;

/**
 * Created by haoxi on 2017/4/25.
 */

public class PDevelopCustomer1 extends XPresent<DevelopCustomerActivity> {
    /**
     * 在线加载数据
     *
     * @param page
     * @param conditionMap 查询条件
     */
    public void loadData(final int page, final Map<String, Object> conditionMap) {
        LoadingDialog.showDialogForLoading(getV());
        Api.getDevelopCustomerService().query(page, conditionMap)
                .compose(XApi.<DevelopCustomerModel>getApiTransformer())
                .compose(XApi.<DevelopCustomerModel>getScheduler())
                .compose(getV().<DevelopCustomerModel>bindToLifecycle())
                .subscribe(new ApiSubscriber<DevelopCustomerModel>() {
                    @Override
                    protected void onFail(NetError error) {
                        LoadingDialog.cancelDialogForLoading();
                        ToastUtils.showShort(error.getMessage());
                    }

                    @Override
                    public void onNext(DevelopCustomerModel developCustomerModel) {
                        if (developCustomerModel.isSuccess()) {
                            getV().showData(page, developCustomerModel.getData());
                        } else {
                            ToastUtils.showShort(developCustomerModel.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        LoadingDialog.cancelDialogForLoading();
                    }
                });
    }

    /**
     * 在线删除
     *
     * @param customerNo
     */
    public void delete(final String customerNo) {
        LoadingDialog.showDialogForLoading(getV());
        Api.getDevelopCustomerService().delete(customerNo)
                .compose(XApi.<DevelopCustomerModel>getApiTransformer())
                .compose(XApi.<DevelopCustomerModel>getScheduler())
                .compose(getV().<DevelopCustomerModel>bindToLifecycle())
                .subscribe(new ApiSubscriber<DevelopCustomerModel>() {
                    @Override
                    protected void onFail(NetError error) {
                        LoadingDialog.cancelDialogForLoading();
                        ToastUtils.showShort(error.getMessage());
                    }

                    @Override
                    public void onNext(DevelopCustomerModel developCustomerModel) {
                        if (developCustomerModel.isSuccess()) {
                            getV().loadData(1);
                        } else {
                            ToastUtils.showShort(developCustomerModel.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        LoadingDialog.cancelDialogForLoading();
                    }
                });
    }

    /**
     * 离线加载数据
     *
     * @param page
     * @param conditionMap 查询条件
     */
    public void loadNativeData(final int page, final Map<String, Object> conditionMap) {
        LoadingDialog.showDialogForLoading(getV());
        getV().showData(page, OrmLiteManager.query(getV(), DevelopCustomerModel.DevelopCustomer.class, true, null, null, null, null, page, conditionMap));
        LoadingDialog.cancelDialogForLoading();
    }


    /**
     * 离线删除数据
     *
     * @param id 主键id
     */
    public void deleteNativeData(final String id) {
        LoadingDialog.showDialogForLoading(getV());
        boolean flag = OrmLiteManager.delete(getV(), DevelopCustomerModel.DevelopCustomer.class, id);
        LoadingDialog.cancelDialogForLoading();
        if (flag) {
            getV().loadData(1);
        } else {
            ToastUtils.showShort(getV().getResources().getString(R.string.cancel_fail));
        }
    }
}
