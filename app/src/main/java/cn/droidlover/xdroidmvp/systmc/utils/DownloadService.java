package cn.droidlover.xdroidmvp.systmc.utils;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;


/**
 * Created by gary on 2016-08-23 下午5:23.
 */
public class DownloadService extends Service {
    public static final String KEY_URL = "url";// 下载链接
    public static final String KEY_TITLE = "title";// 通知标题
    public static final String KEY_DESC = "desc";// 副标题（文件大小）
    private DownloadManager downloadManager;
    private long enqueue;
    private BroadcastReceiver receiver;

    private String title;

    public static void startService(Context context, String url, String title, String desc) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, "下载链接为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_DESC, desc);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(KEY_TITLE)) {
            title = intent.getStringExtra(KEY_TITLE);
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == enqueue) {
                    Uri downloadFileUri = downloadManager.getUriForDownloadedFile(downloadId);
                    if (downloadFileUri != null) {
//                        intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
//                        startActivity(intent);
//                        stopSelf();

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), title + ".apk")), "application/vnd.android.package-archive");
                        startActivity(intent);

                        stopSelf();
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        if (intent != null && intent.hasExtra(KEY_URL)) {
            startDownload(this, intent.getStringExtra(KEY_URL), intent.getStringExtra(KEY_TITLE), intent.getStringExtra(KEY_DESC));
        } else {
            stopSelf();
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void startDownload(Context context, String downloadUrl, String title, String description) {
        if (downloadManager == null) {
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        }

        if (queryDownloadStatus() == DownloadManager.STATUS_RUNNING) {
            Toast.makeText(context, "正在下载", Toast.LENGTH_SHORT).show();
            return;
        }

        //删除以前下载的apk
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), title + ".apk");
        if (file.exists()) {
            file.delete();
        }

        Uri uri = Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // 只显示下载中通知，下载完成不显示
        // request.setShowRunningNotification(true);
        //不显示下载界面
        request.setVisibleInDownloadsUi(false);
        //title
        request.setTitle(title);
        request.setDescription(description);
        //表示下载进行中和下载完成的通知栏是否显示。
        // 默认只显示下载中通知。
        //VISIBILITY_HIDDEN表示不显示任何通知栏提示，这个需要在AndroidMainfest中添加权限android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.
        // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);// 下载完成显示通知
        //表示允许MediaScanner扫描到这个文件，默认不允许
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + ".apk");
        enqueue = downloadManager.enqueue(request);
        Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
    }

    private int queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(enqueue);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    //LogUtil.v("STATUS_PAUSED");
                case DownloadManager.STATUS_PENDING:
                    //LogUtil.v("STATUS_PENDING");
                case DownloadManager.STATUS_RUNNING:
                    //正在下载，不做任何事情
                    //LogUtil.v("STATUS_RUNNING");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //完成
                    //LogUtil.v("下载完成");
                    break;
                case DownloadManager.STATUS_FAILED:
                    //清除已下载的内容，重新下载
                    //LogUtil.v("STATUS_FAILED");
                    downloadManager.remove(enqueue);
                    break;
            }
            return status;
        }
        return -1;
    }

}
