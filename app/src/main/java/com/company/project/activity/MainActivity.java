package com.company.project.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chaychan.library.BottomBarLayout;
import com.company.project.MyApplication;
import com.company.project.R;
import com.company.project.R2;
import com.company.project.adapter.PagerFragmentAdapter;
import com.company.project.base.BaseActivity;
import com.company.project.base.IPresenter;
import com.company.project.fragment.BlankFragment;
import com.company.project.receiver.OpenFileReceiver;
import com.company.project.service.ODownloadService;
import com.company.project.service.OnDownloadListener;
import com.company.project.util.TLog;
import com.company.project.widget.TMessageDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * @author Tinlone
 * <p>
 * We read the world wrong and say that it deceives us.
 */
public class MainActivity extends BaseActivity {

    @BindView(R2.id.vp_fragments)
    ViewPager vpFragments;
    @BindView(R2.id.bottom_bar)
    BottomBarLayout bottomBar;
    String msg = "";
    private List<Fragment> fragments = new ArrayList<Fragment>() {
        {
            add(BlankFragment.newInstance(R.string.homepage, "#00B7EE"));
            add(BlankFragment.newInstance(R.string.info, "#EEB7EE"));
            add(BlankFragment.newInstance(R.string.discover, "#00EEB7"));
            add(BlankFragment.newInstance(R.string.mine, "#FFFACD"));
        }
    };
    private ServiceConnection conn;
    private ODownloadService.DownloadBinder downloadBinder;
    private TMessageDialog updateDialog;

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean inAll() {
        return false;
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        statusTransparentFontWhite();
        vpFragments.setAdapter(new PagerFragmentAdapter(getSupportFragmentManager(), fragments));
        bottomBar.setViewPager(vpFragments);
//        IndexFragmentAdapter adapter = new IndexFragmentAdapter(this, fragments);
//        vpFragments.setAdapter(adapter);
        vpFragments.setOffscreenPageLimit(fragments.size());
//        vpFragments.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                bottomBar.setCurrentItem(position);
//                Log.i("DEBUG.T.LOG","滑选：" + position);
//            }
//        });

        bottomBar.getBottomItem(0).setUnreadNum(10000);
        bottomBar.getBottomItem(2).setMsg("aloha");
        bottomBar.setOnItemSelectedListener((bottomBarItem, i, i1) -> {
            vpFragments.setCurrentItem(i1, true);
            Log.i("DEBUG.T.LOG", "点选：" + i1);
            switch (i1) {
                case 0:
                    statusTransparentFontWhite();
                    msg = "透明状态栏白字";
                    break;
                case 1:
                    statusTransparentFontBlack();
                    msg = "透明状态栏黑字";
                    break;
                case 2:
                    bottomBarItem.hideMsg();
                    break;
                case 3:
                    statusWhiteFontBlack();
                    msg = "白底状态栏黑字";
                    break;
                default:
                    break;
            }
            ToastUtils.showShort(msg);
        });
        PermissionUtils.permission(PermissionConstants.STORAGE).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {
//                Intent dealIntent = FileUtils.getFileOpenIntent("/storage/emulated/0/Android/data/com.company.project/files/file/file/qqlite_4.0.0.1025_537062065.apk");
//                startActivity(dealIntent);
                preDownLoad();
            }

            @Override
            public void onDenied() {

            }
        }).request();
    }


    private void downloadFile(String url) {
        downloadBinder.backgroundDown(url, "files", new OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                TLog.i("下载完成，保存路径：" + file.getAbsolutePath());
                runOnUiThread(() -> {
                    if (updateDialog != null) {
                        updateDialog.dismiss();
                    }
                    updateDialog = new TMessageDialog(MainActivity.this)
                            .title("安装更新")
                            .content("安装包下载完毕")
                            .withoutMid()
                            .left("稍后安装", Color.GRAY, v -> updateDialog.dismiss())
                            .right("立即安装", Color.RED, v -> {
                                Intent clickIntent = new Intent(MyApplication.getAppContext(), OpenFileReceiver.class);
                                clickIntent.setAction(OpenFileReceiver.ACTION);
                                clickIntent.putExtra(OpenFileReceiver.KEY_PATH, file.getAbsolutePath());
                                sendBroadcast(clickIntent);
                            });
                    updateDialog.show();
                });
            }

            @Override
            public void onDownloading(int progress) {
                TLog.i("下载进度：" + progress);
                runOnUiThread(() -> {
                    if (updateDialog != null) {
                        if (!updateDialog.progressShow()) {
                            updateDialog.withProgress(100)
                                    .title("下载")
                                    .content("正在下载更新")
                                    .noButton();
                        }
                        updateDialog.progress(progress).update();
                    }
                });
            }

            @Override
            public void onDownloadFailed(Call call, Exception e) {
                ToastUtils.showShort("下载失败，请稍后再试");
                TLog.i("下载失败，请稍后再试");
                TLog.i(e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (updateDialog != null) {
                            updateDialog.hideProgress()
                                    .withoutMid()
                                    .left("稍后再试", Color.GRAY, v -> updateDialog.dismiss())
                                    .right("重新下载", Color.RED, v -> downloadFile("https://qd.myapp.com/myapp/qqteam/QQ_JS/qqlite_4.0.0.1025_537062065.apk")).update();
                        }
                    }
                });
            }
        });
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
                showUpdateDialog();
//                downloadFile("https://qd.myapp.com/myapp/qqteam/QQ_JS/qqlite_4.0.0.1025_537062065.apk");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private void showUpdateDialog() {
        if (updateDialog == null) {
            updateDialog = new TMessageDialog(this)
                    .onlyMid()
                    .title("发现新版本")
                    .content("测试下载服务是否正常")
                    .mid("下载新版本", Color.RED, v -> downloadFile("https://qd.myapp.com/myapp/qqteam/QQ_JS/qqlite_4.0.0.1025_537062065.apk"));
        }
        updateDialog.show();
    }


}
