package com.company.project.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.company.project.MyApplication;
import com.company.project.R;
import com.company.project.R2;
import com.company.project.base.BaseActivity;
import com.company.project.receiver.OpenFileReceiver;
import com.company.project.service.ODownloadService;
import com.company.project.service.OnDownloadListener;
import com.company.project.util.FileUtils;
import com.company.project.widget.TMessageDialog;

import java.io.File;

import butterknife.BindView;
import okhttp3.Call;

/**
 * <h2>用于加载网页的页面</h2>
 */
public class WebsiteActivity extends BaseActivity {
    private static final String TAG = "WebsiteActivity";

    @BindView(R2.id.toolbar)
    LinearLayout llTitle;
    @BindView(R2.id.wv_website)
    WebView wvWebsite;
    private boolean isFullScreen;
    private String titText;
    private String url;
    private String rightText;
    private boolean isAutoTitle;
    private TMessageDialog messageDialog;
    private ODownloadService.DownloadBinder downloadBinder;
    private ServiceConnection conn;

    @Override
    public int layoutId() {
        return R.layout.activity_website;
    }

    @Override
    public void initParams(Bundle params) {
        if (params != null) {
            isFullScreen = params.getBoolean(FULL_SCREEN, false);
            isAutoTitle = params.getBoolean(AUTO_TITLE, false);
            titText = params.getString(TITLE, getString(R.string.app_name));
            rightText = params.getString(RIGHT_TEXT, "");
            url = params.getString(URL, getString(R.string.app_name));
        }
    }

    @Override
    protected void initView() {
        if (isFullScreen) {
            llTitle.setVisibility(View.GONE);
            statusTransparentFontBlack();
        } else {
            setTitleBack(titText, rightText);
            statusWhiteFontBlack();
        }
        initSettings();
        wvWebsite.loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initSettings() {
        WebSettings settings = wvWebsite.getSettings();
        //水平不显示
        wvWebsite.setHorizontalScrollBarEnabled(false);
        //垂直不显示
        wvWebsite.setVerticalScrollBarEnabled(false);
        //支持JS
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowContentAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(true);
        // 设置可以支持缩放
        settings.setSupportZoom(false);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(false);
        //扩大比例的缩放
        settings.setUseWideViewPort(false);
        wvWebsite.setWebViewClient(new MyWebViewClient());
        wvWebsite.setWebChromeClient(new MyChromeClient());
        preDownLoad();
        // 自行实现下载服务
        wvWebsite.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
                    Log.i("aaa", String.format("url = %s\n," +
                                    "userAgent = %s\n," +
                                    "contentDisposition = %s\n," +
                                    "mimetype = %s\n," +
                                    "contentLength = %s",
                            url, userAgent, contentDisposition, mimetype, contentLength));
                    whenDownload();
                }
        );
    }

    private void whenDownload() {
        initDialog();
        messageDialog.content("确认下载该文件吗？")
                .doClick(new TMessageDialog.DoClickListener() {
                    @Override
                    public void onClickLeft(View view) {
                        dismiss(messageDialog);
                    }

                    @Override
                    public void onClickRight(View view) {
                        messageDialog.withProgress(100)
                                .content("正在下载...")
                                .update();
                        downloadBinder.backgroundDown(url, FileUtils.getFileDisk(), new OnDownloadListener() {

                            @Override
                            public void onDownloadSuccess(File file) {
                                runOnUiThread(() -> {
                                    Intent intent = new Intent(MyApplication.getAppContext(), OpenFileReceiver.class);
                                    intent.putExtra("path", file.getAbsolutePath());
                                    sendBroadcast(intent);
                                    dismiss(messageDialog);
                                });
                            }

                            @Override
                            public void onDownloading(int progress) {
                                runOnUiThread(() -> messageDialog.progress(progress).update());
                            }

                            @Override
                            public void onDownloadFailed(Call call, Exception e) {
                                ToastUtils.showShort("下载失败，请稍后再试");
                                dismiss(messageDialog);
                            }
                        });
                    }
                }).show();
    }

    /**
     * 文件下载准备工作
     */
    private void preDownLoad() {
        Intent intent = new Intent(this, ODownloadService.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                downloadBinder = (ODownloadService.DownloadBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 预初始化弹窗
     */
    private void initDialog() {
        if (messageDialog == null) {
            messageDialog = new TMessageDialog(this)
                    .withoutMid()
                    .title("下载")
                    .left("取消")
                    .right("下载");
        }
    }

    @Override
    protected void onTitleRightClick() {
    }

    /**
     * 主动注入JS语句
     *
     * @param javaScript JavaScript语句
     */
    private void injectJavaScript(@NonNull String javaScript) {
        if (!javaScript.toLowerCase().startsWith("javascript:")) {
            javaScript = "javascript:" + javaScript;
        }
        wvWebsite.loadUrl(javaScript);
    }

    @Override
    protected void onDestroy() {
        if (conn != null) {
            downloadBinder = null;
            unbindService(conn);
        }
        dismiss(messageDialog);
        super.onDestroy();
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.i(TAG, request.getUrl().getPath() + "#####" + error.getDescription());
            }
        }
    }

    public class MyChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.i(TAG, String.format("onProgressChanged:%s", newProgress));
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (isAutoTitle) {
                titText = title;
                setTitleBack(title, rightText);
            }
            Log.i(TAG, title);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.i(TAG, String.format("onJsAlert: url->%s; message->%s", url, message));
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Log.i(TAG, String.format("onJsConfirm: url->%s; message->%s", url, message));
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.i(TAG, String.format("onJsPrompt: url->%s; message->%s; defaultValue->%s", url, message, defaultValue));
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.i(TAG, String.format("level:%s; line->%s; sourceId->%s; message->%s",
                    consoleMessage.messageLevel(), consoleMessage.lineNumber(),
                    consoleMessage.sourceId(), consoleMessage.message()));
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    }
}
