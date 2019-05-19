package com.company.project.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Size;
import androidx.core.content.FileProvider;

import com.company.project.BuildConfig;
import com.company.project.MyApplication;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressWarnings("unused")
public class FileUtils {

    private static final String APK = "apk";
    private static final String TAG = "FileUtils";
    private static final String DEFAULT_CATEGORY = "android.intent.category.DEFAULT";

    private FileUtils() {
    }

    /**
     * @param url 文件路径
     * @return 从下载连接中解析出文件名
     */
    public static String getNameFromUrl(String url) {
        if (url != null && url.contains("/") && !url.endsWith("/")) {
            return url.substring(url.lastIndexOf("/") + 1);
        } else {
            return "";
        }
    }

    /**
     * 在存储路径下创建子文件夹
     *
     * @param saveDir 子文件夹绝对路径
     */
    public static String createDirInStorage(@Size(min = 1) String saveDir) {
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.exists()) {
            Log.i(TAG, "创建文件夹" +
                    (downloadFile.mkdirs() ? "成功" : "失败") + ": "
                    + downloadFile.getAbsolutePath());
        } else {
            Log.i(TAG, "文件夹" + downloadFile.getAbsolutePath() + "存在");
        }
        return downloadFile.getAbsolutePath();
    }

    /**
     * 创建上传文件封装
     *
     * @param path 文件路径
     */
    public static MultipartBody.Part createBodyByPath(@Size(min = 1) String path) {
        return createBodyByPath("file", path);
    }

    /**
     * 文件
     *
     * @param path
     * @return
     */
    public static boolean isExists(String path) {
        if (Check.empty(path)) {
            return false;
        }
        return new File(path).exists();
    }

    /**
     * 创建上传文件封装
     *
     * @param path 文件路径
     */
    public static MultipartBody.Part createBodyByPath(@Size(min = 1) String partName, @Size(min = 1) String path) {
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MultipartBody.FORM, file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    /**
     * 创建上传文件封装
     *
     * @param path 文件路径
     */
    public static MultipartBody.Part createBodyByPath(@Size(min = 1) String partName, @Size(min = 1) String fileName, @Size(min = 1) String path) {
        RequestBody requestFile =
                RequestBody.create(MultipartBody.FORM, new File(path));
        return MultipartBody.Part.createFormData(partName, fileName, requestFile);
    }

    /**
     * 获取文件Uri
     *
     * @param path 文件路径
     * @return 文件Uri
     */
    public static Uri createUriFromPath(String path) {
        File data = new File(path);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return FileProvider.getUriForFile(MyApplication.getAppContext(), BuildConfig.APPLICATION_ID + ".file_provider", data);
        } else {
            return Uri.fromFile(data);
        }
    }

    /**
     * 获取文件Uri
     *
     * @param file 文件
     * @return 文件Uri
     */
    public static Uri createUriFromFile(File file) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return FileProvider.getUriForFile(MyApplication.getAppContext(), BuildConfig.APPLICATION_ID + ".file_provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 关闭流
     *
     * @param streams 可关闭流对象
     */
    public static void closeStream(Closeable... streams) {
        for (Closeable stream : streams) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件后缀
     *
     * @param filePath 文件路径
     * @return 文件后缀
     */
    public static String getFileType(@Size(min = 1) String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        }
        /* 取得扩展名 */
        return file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 获取打开文件Intent
     *
     * @param filePath 文件路径
     * @return 打开文件Intent
     */
    public static Intent getFileOpenIntent(@Size(min = 1) String filePath) {
        /* 取得扩展名 */
        String end = getFileType(filePath);
        if ("".equals(end)) {
            return null;
        }
        switch (end) {
            case "m4a":
            case "mp3":
            case "mid":
            case "xmf":
            case "ogg":
            case "wav":
                return getAudioFileIntent(filePath);
            case "3gp":
            case "mp4":
                return getAudioFileIntent(filePath);
            case "jpg":
            case "gif":
            case "png":
            case "jpeg":
            case "bmp":
                return getImageFileIntent(filePath);
            case APK:
                return getApkFileIntent(filePath);
            case "ppt":
                return getPptFileIntent(filePath);
            case "pptx":
                return getPptxIntent(filePath);
            case "xls":
                return getExcelFileIntent(filePath);
            case "xlsx":
                return getXlsxIntent(filePath);
            case "doc":
                return getWordFileIntent(filePath);
            case "docx":
                return getDocxFileIntent(filePath);
            case "pdf":
                return getPdfFileIntent(filePath);
            case "chm":
                return getChmFileIntent(filePath);
            case "txt":
                return getTextFileIntent(filePath);
            default:
                return getAllIntent(filePath);
        }
    }

    /**
     * Android获取一个用于打开APK文件的intent
     */
    private static Intent getAllIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        intent.setDataAndType(createUriFromPath(param), "*/*");
        return intent;
    }

    /**
     * Android获取一个用于打开APK文件的intent
     */
    private static Intent getApkFileIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        //判读版本是否在7.0以上
        intent.setDataAndType(createUriFromPath(param), "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * Android获取一个用于打开VIDEO文件的intent
     */
    @SuppressWarnings("unused")
    public static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(createUriFromPath(param), "video/*");
        return intent;
    }

    /**
     * Android获取一个用于打开AUDIO文件的intent
     */
    private static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(createUriFromPath(param), "audio/*");
        return intent;
    }

    /**
     * Android获取一个用于打开Html文件的intent
     */
    @SuppressWarnings("unused")
    public static Intent getHtmlFileIntent(String param) {
        Uri uri = createUriFromPath(param)
                .buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content")
                .encodedPath(param)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(createUriFromPath(param), "text/html");
        return intent;
    }

    /**
     * Android获取一个用于打开图片文件的intent
     */
    private static Intent getImageFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(DEFAULT_CATEGORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(createUriFromPath(param), "image/*");
        return intent;
    }

    /**
     * Android获取一个用于打开PPT文件的intent
     */
    private static Intent getPptFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(DEFAULT_CATEGORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(createUriFromPath(param), "application/vnd.ms-powerpoint");
        return intent;
    }

    private static Intent getPptxIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(DEFAULT_CATEGORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(createUriFromPath(param), "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        return intent;
    }

    /**
     * Android获取一个用于打开Excel文件的intent
     */
    private static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(DEFAULT_CATEGORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(createUriFromPath(param), "application/vnd.ms-excel");
        return intent;
    }

    private static Intent getXlsxIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(DEFAULT_CATEGORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(createUriFromPath(param), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return intent;
    }

    /**
     * Android获取一个用于打开Word文件的intent
     */
    private static Intent getWordFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(DEFAULT_CATEGORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(createUriFromPath(param), "application/msword");
        return intent;
    }

    private static Intent getDocxFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(DEFAULT_CATEGORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(createUriFromPath(param), "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        return intent;
    }

    /**
     * Android获取一个用于打开CHM文件的intent
     */
    private static Intent getChmFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(DEFAULT_CATEGORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(createUriFromPath(param), "application/x-chm");
        return intent;
    }

    /**
     * Android获取一个用于打开文本文件的intent
     */
    private static Intent getTextFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(DEFAULT_CATEGORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(createUriFromPath(param), "text/plain");

        return intent;
    }

    /**
     * Android获取一个用于打开PDF文件的intent
     */
    private static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(DEFAULT_CATEGORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(createUriFromPath(param), "application/pdf");
        return intent;
    }

    public static String getFileDisk() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return Environment.getRootDirectory().getAbsolutePath() + BuildConfig.APP_NAME;
    }

    public static String getPublicDiskSafe() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File xiaok = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), BuildConfig.APP_NAME);
            if (xiaok.exists() && xiaok.isDirectory()) {
                return xiaok.getAbsolutePath();
            } else {
                if (xiaok.mkdirs()) {
                    return xiaok.getAbsolutePath();
                }
            }
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String saveAsJpgToSdCard(Bitmap bmp) {
        // 检查sd card是否存在
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Tog.i("SD卡状态不满足保存条件");
            return null;
        }
        // 为图片命名啊
        String name = BuildConfig.APP_NAME + DateTimeUtils.fmtYMDhmssNow() + ".jpg";
        Tog.i(name);
        // 解析返回的图片成bitmap
        // 保存文件
        FileOutputStream fos = null;
        File file = new File(Environment.getExternalStorageDirectory(), "xiaok/");
        if (!file.exists()) {
            if (file.mkdirs()) {
                Tog.i(file.getAbsolutePath() + "文件夹生成成功");
            }
        }
        Tog.i(file.getAbsolutePath() + "文件夹存在");
        String fileName = file.getAbsolutePath() + "/" + name;
        Tog.i(fileName);
        try {
            fos = new FileOutputStream(fileName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                    Tog.i("保存图片流关闭完成");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    public static String createNewFilePath() {
        String path = getPublicDiskSafe() + "/" + BuildConfig.APP_NAME + DateTimeUtils.fmtYMDhmssNow() + ".jpg";
        File file = new File(path);
        try {
            if (file.exists() && file.isFile()) {
                return path;
            } else {
                if (file.createNewFile()) {
                    return path;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createNewFileName() {
        return BuildConfig.APP_NAME + DateTimeUtils.fmtYMDhmssNow() + ".jpg";
    }

    public static boolean deleteImage(Context context, String path) {
        int delete = 0;
        try {
            delete = context.getApplicationContext()
                    .getContentResolver()
                    .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            MediaStore.Images.Media.DATA + "=?",
                            new String[]{path});
        } catch (Exception e) {
            Tog.w(e);
        }
        deleteFileOrDir(path);
        return delete > 0;
    }

//    public static void main(String[] args) {
//        System.out.println(deleteFileOrDir(new File("E:\\test\\")));
//    }

    public static boolean deleteImage(Context context, String path, String sourceId) {
        int delete = context.getApplicationContext()
                .getContentResolver()
                .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Images.ImageColumns._ID + "=?",
                        new String[]{sourceId});
        deleteFileOrDir(path);
        return delete > 0;
    }

    public static boolean deleteFileOrDir(String path) {
        return deleteFileOrDir(new File(path));
    }

    public static boolean deleteFileOrDir(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File file1 : files) {
                        deleteFileOrDir(file1);
                    }
                    return file.delete();
                } else {
                    return file.delete();
                }
            } else {
                return file.delete();
            }
        }
        return false;
    }

    public interface Tails {
        String JPEG = ".jpg";
        String APK = ".apk";
    }
}
