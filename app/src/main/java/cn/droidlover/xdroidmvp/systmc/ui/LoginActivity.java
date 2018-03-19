package cn.droidlover.xdroidmvp.systmc.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.cache.SharedPref;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.model.MessageModel;
import cn.droidlover.xdroidmvp.systmc.model.UserModel;
import cn.droidlover.xdroidmvp.systmc.model.common.Constent;
import cn.droidlover.xdroidmvp.systmc.net.Api;
import cn.droidlover.xdroidmvp.systmc.present.PUser;
import cn.droidlover.xdroidmvp.systmc.ui.style.ButtonBootstrapStyle;
import cn.droidlover.xdroidmvp.systmc.utils.DownloadService;
import cn.droidlover.xdroidmvp.systmc.widget.LoadingDialog;

public class LoginActivity extends XActivity<PUser> {
    @BindView(R.id.login_edit_name)
    EditText et_userName;
    @BindView(R.id.login_edit_pwd)
    EditText et_userPwd;
    @BindView(R.id.login_cb_savepwd)
    CheckBox cb_save;
    @BindView(R.id.login_btn_login_online)
    BootstrapButton btn_login_online;
    SharedPref sharedPref;
    String userName = "";
    String userPwd = "";

    @Override
    public void initView(Bundle bundle) {
        btn_login_online.setBootstrapBrand(new ButtonBootstrapStyle(this));
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        sharedPref = SharedPref.getInstance(context);
        userName = sharedPref.getString("userName", "");
        userPwd = sharedPref.getString("userPwd", "");
        boolean isChecked = sharedPref.getBoolean("isChecked", false);
        et_userName.setText(userName);
        et_userPwd.setText(userPwd);
        cb_save.setChecked(isChecked);
        appUpdate();//检测app更新
    }

    private void appUpdate() {
        if (!true && SPUtils.getInstance().getLong(Constent.KEY_UPDATE_APK_TIME) + 1000 * 60 * 60 * 12L > System.currentTimeMillis()) {
            return;
        }
        Api.getSysService().doUpdateApp(AppUtils.getAppVersionCode())
                .compose(XApi.<MessageModel>getApiTransformer())
                .compose(XApi.<MessageModel>getScheduler())
                .compose(this.<MessageModel>bindToLifecycle())
                .subscribe(new ApiSubscriber<MessageModel>() {
                    @Override
                    protected void onFail(NetError error) {
                        LoadingDialog.cancelDialogForLoading();
                        //ToastUtils.showLong("更新失败");
                    }

                    @Override
                    public void onNext(MessageModel messageModel) {
                        LoadingDialog.cancelDialogForLoading();
                        if (messageModel.isSuccess()) {
                            final String url = messageModel.getData();
                            Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialog) {
                                @Override
                                public void onPositiveActionClicked(DialogFragment fragment) {
                                    super.onPositiveActionClicked(fragment);
                                    SPUtils.getInstance().put(Constent.KEY_UPDATE_APK_TIME, System.currentTimeMillis());
                                    DownloadService.startService(LoginActivity.this, url, getString(R.string.app_name), "正在下载");
                                }

                                @Override
                                public void onNegativeActionClicked(DialogFragment fragment) {
                                    super.onNegativeActionClicked(fragment);
                                }
                            };
                            ((SimpleDialog.Builder) builder).message("app有更新，是否立刻更新?")
                                    .positiveAction("确认")
                                    .negativeAction("取消");
                            DialogFragment fragment = DialogFragment.newInstance(builder);
                            fragment.show(getSupportFragmentManager(), null);
                        } else {
                            //ToastUtils.showLong(messageModel.getMessage());
                        }
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public PUser newP() {
        return new PUser();
    }

    /**
     * 登录
     */
    @OnClick({R.id.login_btn_login_online})
    public void click(View v) {
        userName = et_userName.getText().toString();
        userPwd = et_userPwd.getText().toString();
        if (StringUtils.isTrimEmpty(userName)) {
            ToastUtils.showShort("用户名不能为空");
            return;
        }
        if (StringUtils.isTrimEmpty(userPwd)) {
            ToastUtils.showShort("密码不能为空");
            return;
        }
        LoadingDialog.showDialogForLoading(context);
        switch (v.getId()) {
            case R.id.login_btn_login_online:
                Constent.ONLINE = true;
                getP().login(userName, userPwd);//在线登录
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     *
     * @param user
     */

    public void doLogin(UserModel.User user) {
        if (cb_save.isChecked()) {
            sharedPref.putString("userName", userName);
            sharedPref.putString("userPwd", userPwd);
            sharedPref.putBoolean("isChecked", true);
        } else {
            sharedPref.putString("userName", "");
            sharedPref.putString("userPwd", "");
            sharedPref.putBoolean("isChecked", false);
        }
        Constent.userId = user.getId();
        Constent.userName = user.getLoginName();
        Constent.taskOrgId = user.getTaskOrgId();
        Constent.taskOrgCode = user.getTaskOrgCode();//班组编号
        Constent.taskOrgName = user.getTaskOrgName();//班组名称
        Constent.postName = user.getPostName();// 岗位名称
        Constent.userType = user.getUserType();// 用户类型（1、检修，2、班组）
        Constent.personId = user.getPersonId();
        sharedPref.put("curUser", user);//设置当前登陆人
        Router.newIntent(context)
                .to(MainActivity.class)    //to()指定目标context
                .launch();
        context.finish();
    }

}
