package cn.droidlover.xdroidmvp.systmc.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.db.OrmLiteManager;
import cn.droidlover.xdroidmvp.systmc.model.MessageModel;
import cn.droidlover.xdroidmvp.systmc.model.VersionModel;
import cn.droidlover.xdroidmvp.systmc.model.common.Constent;
import cn.droidlover.xdroidmvp.systmc.net.Api;
import cn.droidlover.xdroidmvp.systmc.present.PUser;
import cn.droidlover.xdroidmvp.systmc.utils.DownloadService;
import cn.droidlover.xdroidmvp.systmc.utils.FileSizeFormat;
import cn.droidlover.xdroidmvp.systmc.widget.LoadingDialog;

import static cn.droidlover.xdroidmvp.systmc.model.common.Constent.userName;

/**
 * Created by ronaldo on 2017/6/6.
 */

public class UserFragment extends XFragment {
    @BindView(R.id.sys_item_pwd)
    LinearLayout sys_item_pwd;
    @BindView(R.id.sys_item_unline)
    LinearLayout sys_item_unline;
    @BindView(R.id.sys_item_update)
    LinearLayout sys_item_update;
    @BindView(R.id.sys_item_out)
    LinearLayout sys_item_out;
    @BindView(R.id.work_top_name)
    TextView work_top_name;
    @BindView(R.id.work_top_post)
    TextView work_top_post;
    @BindView(R.id.user_tv_version)
    TextView user_tv_version;
    @BindView(R.id.user_tv_unline_date)
    TextView user_tv_unline_date;

    long fileSize = 0;// 记录下载的文件大小
    long curSize = 0;// 当前下载大小
    private ProgressDialog pdialogData;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        work_top_name.setText("帐号：" + userName);
        //work_top_post.setText("岗位：" + Constent.postName);
        user_tv_version.setText("v " + AppUtils.getAppVersionName());
        user_tv_unline_date.setText(SPUtils.getInstance().getString(Constent.KEY_UPDATE_DB_TIME));//离线db更新时间
    }

    @OnClick({R.id.sys_item_pwd, R.id.sys_item_unline, R.id.sys_item_update, R.id.sys_item_out})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.sys_item_pwd://修改密码
                Router.newIntent(context).to(PwdUpdateActivity.class).launch();
                break;
            case R.id.sys_item_unline://更新离线数据
                dbUpdate();
                break;
            case R.id.sys_item_update://软件更新
                appUpdate();
                break;
            case R.id.sys_item_out://退出登陆
                outLogin();
                break;
        }
    }

    /**
     * 更新离线数据
     */
    private void dbUpdate() {
        List<VersionModel.Version> versionList = OrmLiteManager.getInstance(getActivity()).getLiteOrm(getActivity())
                .query(new QueryBuilder<VersionModel.Version>(VersionModel.Version.class));
        if (versionList.isEmpty()) {
            ToastUtils.showLong("暂无更新");
            return;
        } else {
            VersionModel.Version version = versionList.get(0);
            Integer tbVersion = version.getVersion();
            Api.getSysService().doUpdateDb(tbVersion)
                    .compose(XApi.<MessageModel>getApiTransformer())
                    .compose(XApi.<MessageModel>getScheduler())
                    .compose(this.<MessageModel>bindToLifecycle())
                    .subscribe(new ApiSubscriber<MessageModel>() {
                        @Override
                        protected void onFail(NetError error) {
                            LoadingDialog.cancelDialogForLoading();
                            ToastUtils.showLong("更新失败");
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
                                        DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                        SPUtils.getInstance().put(Constent.KEY_UPDATE_DB_TIME, TimeUtils.date2String(new Date(), DEFAULT_FORMAT));
                                        new MyLoadAsyncTask().execute(url, OrmLiteManager.DB_PATH, OrmLiteManager.DB_NAME);// 参数依次是：db的URL，存放本地目录，文件名
                                    }

                                    @Override
                                    public void onNegativeActionClicked(DialogFragment fragment) {
                                        super.onNegativeActionClicked(fragment);
                                    }
                                };
                                ((SimpleDialog.Builder) builder).message("离线数据有更新，是否立刻更新?")
                                        .positiveAction("确认")
                                        .negativeAction("取消");
                                DialogFragment fragment = DialogFragment.newInstance(builder);
                                fragment.show(getFragmentManager(), null);
                            } else {
                                ToastUtils.showLong(messageModel.getMessage());
                            }
                        }
                    });
        }
    }

    /**
     * 更新软件
     */
    private void appUpdate() {
        // 12h之后检测
//        if (!true && SPUtils.getInstance().getLong(Constent.KEY_UPDATE_APK_TIME) + 1000 * 60 * 60 * 12L > System.currentTimeMillis()) {
//            return;
//        }
        Api.getSysService().doUpdateApp(AppUtils.getAppVersionCode())
                .compose(XApi.<MessageModel>getApiTransformer())
                .compose(XApi.<MessageModel>getScheduler())
                .compose(this.<MessageModel>bindToLifecycle())
                .subscribe(new ApiSubscriber<MessageModel>() {
                    @Override
                    protected void onFail(NetError error) {
                        LoadingDialog.cancelDialogForLoading();
                        ToastUtils.showLong("更新失败");
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
                                    DownloadService.startService(getActivity(), url, getString(R.string.app_name), "正在下载");
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
                            fragment.show(getFragmentManager(), null);
                        } else {
                            ToastUtils.showLong(messageModel.getMessage());
                        }
                    }
                });
    }

    /**
     * 退出登陆
     */
    private void outLogin() {
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialog) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                super.onPositiveActionClicked(fragment);
                Router.newIntent(getActivity()).to(LoginActivity.class).launch();
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };
        ((SimpleDialog.Builder) builder).message("确认退出登陆?").positiveAction("确认").negativeAction("取消");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }

    /* 异步任务，后台处理与更新UI */
    class MyLoadAsyncTask extends AsyncTask<String, String, String> {
        /* 后台线程 */
        @Override
        protected String doInBackground(String... params) {
            /* 所下载文件的URL */
            try {
                URL url = new URL(params[0]);// 访问地址
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                /* URL属性设置 */
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.setConnectTimeout(6 * 500 * 1000);
                conn.setReadTimeout(6 * 500 * 1000);
                /* URL建立连接 */
                conn.connect();
                /* 下载文件的大小 */
                long fileOfLength = conn.getContentLength();
                fileSize = fileOfLength;
                /* 每次下载的大小与总下载的大小 */
                long totallength = 0;
                int length = 0;
                /* 输入流 */
                InputStream in = conn.getInputStream();
                String dirPath = params[1];// 保存地址
                String saveName = params[2];// 保存文件名
                FileOutputStream out = new FileOutputStream(new File(dirPath,
                        saveName));
                /* 缓存模式，下载文件 */
                byte[] buff = new byte[1024 * 1024];
                while ((length = in.read(buff)) > 0) {
                    totallength += length;
                    curSize = totallength;
                    String str1 = ""
                            + (int) ((totallength * 100) / fileOfLength);
                    publishProgress(str1);
                    out.write(buff, 0, length);
                }
                /* 关闭输入输出流 */
                in.close();
                out.flush();
                out.close();

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        /* 预处理UI线程 */
        @Override
        protected void onPreExecute() {
            /* 实例化进度条对话框 */
            pdialogData = new ProgressDialog(getActivity());
            /* 进度条对话框属性设置 */
            pdialogData.setMessage("数据正在下载中...");
            /* 进度值最大100 */
            pdialogData.setMax(100);
            /* 水平风格进度条 */
            pdialogData.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            /* 无限循环模式 */
            pdialogData.setIndeterminate(false);
			/* 可取消 */
            pdialogData.setCancelable(false);
			/* 显示对话框 */
            pdialogData.show();
            super.onPreExecute();
        }

        /* 结束时的UI线程 */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pdialogData.dismiss();
                ToastUtils.showLong("下载完成！");
                user_tv_unline_date.setText(SPUtils.getInstance().getString(Constent.KEY_UPDATE_DB_TIME));//离线db更新时间
            } catch (Exception e) {
                e.printStackTrace();
                Router.pop(getActivity());
            }
        }

        /* 处理UI线程，会被多次调用,触发事件为publicProgress方法 */
        @Override
        protected void onProgressUpdate(String... values) {
			/* 进度显示 */
            pdialogData.setProgress(Integer.parseInt(values[0]));
            pdialogData.setMessage("数据正在下载中("
                    + FileSizeFormat.getMbSize(curSize) + "/"
                    + FileSizeFormat.getMbSize(fileSize) + "MB)...");

        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public PUser newP() {
        return null;
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }
}
