package com.company.project.util;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ThreadUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * @author zhaojinlong
 * 2016/12/20 0020
 * 封版时关闭日志
 */
@SuppressWarnings("unused")
public class TLog {


    private static final String TAG = "DEBUG.T.LOG";
        private static boolean showLog = true;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.getDefault());

    private TLog() {
    }

    public static void enable(boolean showlog) {
        showLog = showlog;
    }

    public static void d(Object object) {
        if (showLog) {
            Log.d(TAG, valueOf(object));
        }
    }

    public static void d(String tag, Object object) {
        if (showLog) {
            Log.d(tag, valueOf(object));
        }
    }

    public static void i(Object object) {
        if (showLog) {
            Log.i(TAG, valueOf(object));
        }
    }

    public static void i(String tag, Object object) {
        if (showLog) {
            Log.i(tag, valueOf(object));
        }
    }


    public static void e(Object object) {
        if (showLog) {
            Log.e(TAG, valueOf(object));
        }
    }

    public static void e(String tag, Object object) {
        if (showLog) {
            Log.e(tag, valueOf(object));
        }
    }

    public static void w(Object object) {
        if (showLog) {
            Log.w(TAG, valueOf(object));
        }
    }

    public static void w(String tag, Object object) {
        if (showLog) {
            Log.w(tag, valueOf(object));
        }
    }

    public static String valueOf() {
        return "";
    }

    public static String valueOf(Object object) {
        if (object == null) {
            return "null";
        }
        String logText;
        if (object instanceof Throwable) {
            logText = getStackTraceString((Throwable) object);
        } else if (object instanceof String) {
            logText = String.valueOf(object);
        } else if (object instanceof Object[]) {
            logText = Arrays.toString((Object[]) object);
        } else {
            logText = JSONObject.toJSONString(object);
        }
        String clip;
        while (logText.length() > 4000) {
            clip = logText.substring(0, 4000);
            if (showLog) {
                i(TAG, clip);
            }
        }
        return logText;
    }

    public static void keep(Object object) {
        i(valueOf(object));
    }

    public static void keep(String tag, Object object) {
        i(tag, object);
    }

    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }


    public static synchronized void writeLog(Context context, final Object object) {
        if (!showLog) {
            return;
        }
        if (PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                &&
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            fLog(object);
        } else {
            w("无文件存储权限，无法写入LOG");
        }
    }


    public static synchronized void fLog(final Object object) {
        try {
            i(object);
            if (!showLog) {
                return;
            }
            ThreadUtils.executeByCached(new ThreadUtils.Task<Object>() {
                @Nullable
                @Override
                public Object doInBackground() {
                    try {
                        String text;
                        if (object == null) {
                            text = "null";
                        } else if (object instanceof Throwable) {
                            text = getStackTraceString((Throwable) object);
                        } else if (object instanceof String) {
                            text = String.valueOf(object);
                        } else if (object instanceof Object[]) {
                            text = Arrays.toString((Object[]) object);
                        } else {
                            text = JSONObject.toJSONString(object);
                        }
                        File directory = new File(FileUtils.getDataCacheStorage(), "/log/");
                        if (!directory.exists()) {
                            if (directory.mkdirs()) {
                                i("创建文件夹" + directory.getAbsolutePath() + "成功");
                            }
                        }
                        i("文件夹" + directory.getAbsolutePath() + "已存在");
                        File[] files = directory.listFiles();
                        if (files != null && files.length >= 10) {
                            for (int i = 5; i >= 0; i--) {
                                delFile(files[i]);
                            }
                        }
                        String yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                        File file = new File(directory.getAbsolutePath(), "log-" + yyyyMMdd + ".txt");
                        if (!file.exists() || file.isDirectory()) {
                            if (file.createNewFile()) {
                                i("创建文件" + file.getAbsolutePath() + "成功");
                            }
                        }
                        i("文件" + file.getAbsolutePath() + "已存在");

                        try (FileWriter fileWriter = new FileWriter(file, true)) {
                            fileWriter.append("\n").append("\n");
                            String className = object == null ? "null" : object.getClass().getName();
                            fileWriter.append(sdf.format(new Date())).append(" -->>").append(className);
                            fileWriter.append("\n").append("\n");
                            fileWriter.append(text);
                            fileWriter.flush();
                        } catch (IOException e) {
                            e(e);
                        }
                    } catch (IOException e) {
                        e(e);
                    }
                    return null;
                }

                @Override
                public void onSuccess(@Nullable Object result) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onFail(Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                w(String.format("文件%s删除%s", file.getAbsolutePath(), file.delete()));
            } else {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        delFile(f);
                    }
                    w(String.format("文件夹%s删除%s", file.getAbsolutePath(), file.delete()));
                } else {
                    w(String.format("文件夹%s删除%s", file.getAbsolutePath(), file.delete()));
                }
            }
        }
    }

}