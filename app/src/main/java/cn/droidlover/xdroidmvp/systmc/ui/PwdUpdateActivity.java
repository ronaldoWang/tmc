package cn.droidlover.xdroidmvp.systmc.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.model.UserModel;
import cn.droidlover.xdroidmvp.systmc.model.common.Constent;
import cn.droidlover.xdroidmvp.systmc.net.Api;
import cn.droidlover.xdroidmvp.systmc.ui.style.ButtonBootstrapStyle;
import cn.droidlover.xdroidmvp.systmc.widget.LoadingDialog;

public class PwdUpdateActivity extends XActivity {
    @BindView(R.id.et_old_pwd)
    BootstrapEditText et_old_pwd;
    @BindView(R.id.et_new_pwd)
    BootstrapEditText et_new_pwd;
    @BindView(R.id.et_confirm_new_pwd)
    BootstrapEditText et_confirm_new_pwd;
    @BindView(R.id.button_save_upload)
    BootstrapButton button_save_upload;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void initData(Bundle savedInstanceState) {
        toolbar.setTitle("修改密码");
        setSupportActionBar(toolbar);
        button_save_upload.setBootstrapBrand(new ButtonBootstrapStyle(this));
    }

    @OnClick({R.id.button_save_upload})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.button_save_upload://上传
                doUpdatePwd();
                break;
        }
    }

    private void doUpdatePwd() {
        String oldPwd = et_old_pwd.getText().toString();
        String newPwd = et_new_pwd.getText().toString();
        String confirmPwd = et_confirm_new_pwd.getText().toString();
        if (StringUtils.isTrimEmpty(oldPwd)) {
            ToastUtils.showShort("原密码不能为空");
            return;
        }
        if (StringUtils.isTrimEmpty(newPwd)) {
            ToastUtils.showShort("新密码不能为空");
            return;
        }
        if (StringUtils.isTrimEmpty(confirmPwd)) {
            ToastUtils.showShort("确认新密码不能为空");
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            ToastUtils.showShort("新密码两次输入不一致");
            return;
        }
        Api.getUserService().doUpdatePwd(oldPwd, newPwd, Constent.userId)
                .compose(XApi.<UserModel>getApiTransformer())
                .compose(XApi.<UserModel>getScheduler())
                .compose(this.<UserModel>bindToLifecycle())
                .subscribe(new ApiSubscriber<UserModel>() {
                    @Override
                    protected void onFail(NetError error) {
                        LoadingDialog.cancelDialogForLoading();
                        ToastUtils.showLong("修改失败");
                    }

                    @Override
                    public void onNext(UserModel userModel) {
                        LoadingDialog.cancelDialogForLoading();
                        if (userModel.isSuccess()) {
                            ToastUtils.showShort(userModel.getMessage());
                        } else {
                            ToastUtils.showLong(userModel.getMessage());
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Router.pop(context);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pwd_update;
    }

    @Override
    public Object newP() {
        return null;
    }
}
