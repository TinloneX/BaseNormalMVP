package com.company.project.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.company.project.R;
import com.company.project.util.DensityUtil;


/**
 * @author Administrator
 * @date 2017/11/30
 */

public class LoadingProgressDialog {

    private static Dialog dialog;

    /**
     * 显示dialog
     *
     * @param context 上下文对象
     */
    public static void showProgressDialog(Context context) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null, false);
        dialog.setCancelable(true);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        if (window!=null) {
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams p = window.getAttributes();
            p.width = (int) (DensityUtil.getScreenWidth() * 0.6);
            p.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(p);
            dialog.show();
        }
    }

    /**
     * 让dialog消失
     */
    public static void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (dialog != null) {
            dialog = null;
        }
    }
}
