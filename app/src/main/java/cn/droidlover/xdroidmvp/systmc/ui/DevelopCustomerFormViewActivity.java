package cn.droidlover.xdroidmvp.systmc.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapLabel;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.model.DevelopCustomerModel;
import cn.droidlover.xdroidmvp.systmc.model.common.Constent;
import cn.droidlover.xdroidmvp.systmc.present.PDevelopCustomerFormView;
import cn.droidlover.xdroidmvp.systmc.utils.DictUtil;
import cn.droidlover.xstatecontroller.XStateController;

public class DevelopCustomerFormViewActivity extends XActivity<PDevelopCustomerFormView> {

    @BindView(R.id.customer_label_customerName)
    BootstrapLabel label_customerName;
    @BindView(R.id.customer_label_sex)
    BootstrapLabel label_sex;
    @BindView(R.id.customer_label_mobilePhone)
    BootstrapLabel label_mobilePhone;
    @BindView(R.id.customer_label_summary)
    BootstrapLabel label_summary;
    @BindView(R.id.customer_label_type)
    BootstrapLabel label_type;
    @BindView(R.id.customer_label_email)
    BootstrapLabel label_email;
    @BindView(R.id.customer_label_recentDate)
    BootstrapLabel label_recentDate;
    @BindView(R.id.customer_label_recentResult)
    BootstrapLabel label_recentResult;

    @BindView(R.id.contentLayout)
    XStateController controller;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    DevelopCustomerModel.DevelopCustomer data;

    @Override
    public void initData(Bundle savedInstanceState) {
        controller.showLoading();
        String id = getIntent().getStringExtra("id");
        if (Constent.ONLINE) {
            getP().queryOne(id);
        } else {
            getP().queryNativeOne(id);
        }
    }

    @Override
    public void initView(Bundle bundle) {
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

    public void showData(DevelopCustomerModel.DevelopCustomer data) {
        this.data = data;
        label_customerName.setText(data.getCustomerName());
        label_sex.setText(DictUtil.getDictLabel(context, "sex", data.getSex(), ""));
        label_mobilePhone.setText(data.getMobilePhone());
        label_summary.setText(data.getSummary());
        label_type.setText(DictUtil.getDictLabel(context, "customer_type", data.getType(), ""));
        label_email.setText(data.getEmail());
        label_recentDate.setText(data.getRecentDate());
        label_recentResult.setText(data.getRecentResult());
        controller.showContent();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_developcustomerformview;
    }

    @Override
    public PDevelopCustomerFormView newP() {
        return new PDevelopCustomerFormView();
    }
}
