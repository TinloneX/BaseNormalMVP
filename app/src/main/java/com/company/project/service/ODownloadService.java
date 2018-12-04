package com.company.project.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.company.project.BuildConfig;
import com.company.project.MyApplication;
import com.company.project.R;
import com.company.project.util.TLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 下载服务，包含通知(适配Android7+)
 */
public class ODownloadService extends Service {
    private NotificationManager notificationManager;
    private Notification.Builder builder;
    private OkHttpClient okHttpClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        okHttpClient = new OkHttpClient();
        return new DownloadBinder();
    }

    //初始化通知
    private void initNotification() {
        notificationManager = (NotificationManager) MyApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(MyApplication.getAppContext());
        if (Build.VERSION.SDK_INT >= 26) {
            //第三个参数设置通知的优先级别
            NotificationChannel channel =
                    new NotificationChannel(BuildConfig.APPLICATION_ID, "下载及更新", NotificationManager.IMPORTANCE_DEFAULT);
            channel.canBypassDnd();//是否可以绕过请勿打扰模式
            channel.canShowBadge();//是否可以显示icon角标
            channel.enableLights(true);//是否显示通知闪灯
            channel.setBypassDnd(true);//设置绕过免打扰
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
            channel.setLightColor(Color.RED);//设置闪光灯颜色
            channel.getAudioAttributes();//获取设置铃声设置
            channel.setVibrationPattern(new long[]{100, 200, 100});//设置震动模式
            channel.shouldShowLights();//是否会闪光
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            if (builder != null) {
                builder.setChannelId(BuildConfig.APPLICATION_ID);//这个id参数要与上面channel构建的第一个参数对应
            }
        }
        builder.setContentTitle("正在下载...") //设置通知标题
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_MAX) //设置通知的优先级：最大
                .setAutoCancel(false)//设置通知被点击一次是否自动取消
                .setContentText("下载进度:" + "0%")
                .setProgress(100, 0, false);
        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

    /**
     * @param url      下载连接
     * @param saveDir  储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String saveDir, final OnDownloadListener listener) {
        TLog.i(url + "****" + saveDir);
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
                TLog.i("onDownloadFailed");
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                // buffer写入大小
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    ResponseBody body = response.body();
                    if (body == null) {
                        return;
                    }
                    //取字节流
                    is = body.byteStream();
                    //获取流大小
                    long total = body.contentLength();
                    TLog.i("onResponse total = " + total);
                    File file = new File(savePath, getNameFromUrl(url));
                    TLog.i(file.getAbsolutePath());
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                        builder.setProgress(100, progress, true);
                        notificationManager.notify(1, builder.build());
                    }
                    fos.flush();
                    builder.setContentTitle("下载完成")
                            .setContentText("点击安装")
                            .setAutoCancel(true);//设置通知被点击一次是否自动取消
                    notificationManager.notify(1, builder.build());
                    installApk(file);
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    TLog.e(e);
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        TLog.e(e);
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        TLog.e(e);
                    }
                }
            }
        });
    }

    /**
     * @param saveDir 文件路径
     */
    private String isExistDir(String saveDir) {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.exists()) {
            TLog.i("创建文件" +
                    (downloadFile.mkdirs() ? "成功" : "失败") + ": "
                    + downloadFile.getAbsolutePath());

        } else {
            TLog.i("文件夹" + downloadFile.getAbsolutePath() + "存在");
        }
        return downloadFile.getAbsolutePath();
    }

    /**
     * @param url 文件路径
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        if (url == null || !url.contains("/") || !url.toLowerCase().endsWith(".apk")) {
            return BuildConfig.APPLICATION_ID + ".apk";
        } else return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 安装apk
     */
    private void installApk(File file) {
        //新下载apk文件存储地址
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".file_provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        this.startActivity(intent);
        //取消通知
//        notificationManager.icon_cancel(1);
    }

    /**
     * binder
     */
    public class DownloadBinder extends Binder {

        public void backgroundDown(String url, String saveDir, OnDownloadListener listener) {
            initNotification();
            download(url, saveDir, listener);
        }

    }

}
