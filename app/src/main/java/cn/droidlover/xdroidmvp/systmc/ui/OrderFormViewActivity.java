package cn.droidlover.xdroidmvp.systmc.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.model.DevelopCustomerModel;
import cn.droidlover.xdroidmvp.systmc.model.common.Constent;
import cn.droidlover.xdroidmvp.systmc.model.order.GuestModel;
import cn.droidlover.xdroidmvp.systmc.model.order.OrderModel;
import cn.droidlover.xdroidmvp.systmc.net.Api;
import cn.droidlover.xdroidmvp.systmc.present.PDevelopCustomerFormView;
import cn.droidlover.xdroidmvp.systmc.utils.DictUtil;
import cn.droidlover.xdroidmvp.systmc.widget.LoadingDialog;
import cn.droidlover.xstatecontroller.XStateController;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class OrderFormViewActivity extends XActivity {

    @BindView(R.id.order_label_shopName)
    BootstrapLabel order_label_shopName;
    @BindView(R.id.order_label_checkInDate)
    BootstrapLabel order_label_checkInDate;
    @BindView(R.id.order_label_checkOutDate)
    BootstrapLabel order_label_checkOutDate;
    @BindView(R.id.order_label_masterOrderNo)
    BootstrapLabel order_label_masterOrderNo;
    @BindView(R.id.order_label_subOrderNo)
    BootstrapLabel order_label_subOrderNo;
    @BindView(R.id.order_label_orderConfirmNo)
    BootstrapLabel order_label_orderConfirmNo;
    @BindView(R.id.order_label_totalSalePrice)
    BootstrapLabel order_label_totalSalePrice;
    @BindView(R.id.order_label_tmcName)
    BootstrapLabel order_label_tmcName;
    @BindView(R.id.order_label_shopContactDesc)
    BootstrapLabel order_label_shopContactDesc;
    @BindView(R.id.order_label_brandName)
    BootstrapLabel order_label_brandName;
    @BindView(R.id.order_label_roomTypeDesc)
    BootstrapLabel order_label_roomTypeDesc;
    @BindView(R.id.order_label_roomNumber)
    BootstrapLabel order_label_roomNumber;
    @BindView(R.id.order_label_guest)
    BootstrapLabel order_label_guest;
    @BindView(R.id.order_label_guest2)
    BootstrapLabel order_label_guest2;
    @BindView(R.id.order_iv_shopContactDesc)
    ImageView order_iv_shopContactDesc;

    @BindView(R.id.contentLayout)
    XStateController controller;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    OrderModel.Order data;

    @Override
    public void initData(Bundle savedInstanceState) {
        controller.showLoading();
        String id = getIntent().getStringExtra("id");
        Api.getOrderService().queryOne(id).compose(XApi.<OrderModel>getApiTransformer())
                .compose(XApi.<OrderModel>getScheduler())
                .compose(this.<OrderModel>bindToLifecycle())
                .subscribe(new ApiSubscriber<OrderModel>() {
                    @Override
                    protected void onFail(NetError error) {
                        ToastUtils.showShort(error.getMessage());
                    }

                    @Override
                    public void onNext(OrderModel orderModel) {
                        if (orderModel.isSuccess()) {
                            if (!orderModel.getData().isEmpty()) {
                                showData(orderModel.getData().get(0));
                            }
                        } else {
                            ToastUtils.showShort(orderModel.getMessage());
                        }
                    }
                });
    }

    @SuppressLint("MissingPermission")
    @OnClick({R.id.order_iv_shopContactDesc, R.id.order_label_shopContactDesc})
    public void click(View v) {
        requestCallPhonePermission();
    }

    private static final int CALL_PHONE = 0x01;

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(CALL_PHONE)
    public void requestCallPhonePermission() {
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.getShopContactDesc()));
            startActivity(intent);
        } else {
            EasyPermissions.requestPermissions(this, "请求拨打电话权限！", CALL_PHONE, perms);
        }
    }

    @Override
    public void initView(Bundle bundle) {
        toolbar.setTitle("订单详细");
        setSupportActionBar(toolbar);
        controller.loadingView(View.inflate(context, R.layout.view_loading, null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Router.pop(context);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showData(OrderModel.Order data) {
        this.data = data;
        order_label_shopName.setText(data.getShopCnName().toString());
        order_label_checkInDate.setText(data.getCheckInDate().toString());
        order_label_checkOutDate.setText(data.getCheckOutDate().toString());
        order_label_masterOrderNo.setText(data.getMasterOrderNo().toString());
        order_label_subOrderNo.setText(data.getSubOrderNo().toString());
        order_label_orderConfirmNo.setText(data.getOrderConfirmNo().toString());
        order_label_totalSalePrice.setText(new BigDecimal(data.getTotalSalePrice()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_UP) +
                "元");
        order_label_tmcName.setText(data.getTmcName().toString());
        order_label_shopContactDesc.setText(data.getShopContactDesc().toString());
        order_label_brandName.setText(data.getBrandName().toString());
        order_label_roomTypeDesc.setText(data.getRoomTypeDesc().toString());
        order_label_roomNumber.setText(data.getRoomNumber().toString());
        List<GuestModel.Guest> guests = data.getGuests();
        for (int i = 0; i < guests.size(); i++) {
            GuestModel.Guest po = guests.get(i);
            if (i == 0) {
                order_label_guest.setText(po.getFirstName() + "  " + po.getMobilePhone());
            } else if (i == 1) {
                order_label_guest2.setText(po.getFirstName() + "  " + po.getMobilePhone());
            }
        }
        controller.showContent();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_orderformview;
    }

    @Override
    public OrderFormViewActivity newP() {
        return null;
    }
}
