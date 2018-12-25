package com.company.project.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.company.project.R;
import com.company.project.util.DensityUtil;


/**
 * 普通信息提示框
 * 定制化多按键
 * 包含水平进度条，默认不展示
 */
public class TMessageDialog {

    private Context mContext;
    private Dialog mDialog;
    private View view;
    private TextView tvTitle;
    private TextView tvLine;
    private TextView tvContent;
    private TextView tvLine2;
    private TextView tvLeft;
    private TextView tvMid;
    private TextView tvRight;
    private ProgressBar progressBar;
    private LinearLayout llProgress;
    private LinearLayout llButton;
    private TextView tvProgress;
    private DoClickListener mListener = new DoClickListener() {
        @Override
        public void onClickLeft(View v) {

        }

        @Override
        public void onClickRight(View v) {

        }
    };
    private View.OnClickListener defaultListener = v -> {
    };

    public TMessageDialog(@NonNull Context context) {
        mContext = context;
        initDialog();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    private void initDialog() {
        mDialog = new Dialog(mContext);
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_do_progress, null);
        findView();
        mDialog.setCancelable(false);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams p = window.getAttributes();
            p.width = (int) (DensityUtil.getScreenWidth(mContext) * 0.8);
            window.setAttributes(p);
        }
    }

    private void findView() {
        tvTitle = view.findViewById(R.id.tv_title);
        tvLine = view.findViewById(R.id.tv_line);
        tvContent = view.findViewById(R.id.tv_content);
        tvLine2 = view.findViewById(R.id.tv_line2);
        tvLeft = view.findViewById(R.id.tv_left);
        tvMid = view.findViewById(R.id.tv_mid);
        tvRight = view.findViewById(R.id.tv_right);
        progressBar = view.findViewById(R.id.progressBar);
        tvProgress = view.findViewById(R.id.tv_progress);
        llProgress = view.findViewById(R.id.ll_progress);
        llButton = view.findViewById(R.id.ll_button);
    }

    public TMessageDialog doClick(@NonNull DoClickListener listener) {
        mListener = listener;
        return this;
    }

    private CharSequence getString(@StringRes int textRes) {
        return mContext.getResources().getText(textRes);
    }

    private int getColor(int colorRes) {
        return mContext.getResources().getColor(colorRes);
    }

    public TMessageDialog title(@StringRes int textRes) {
        return title(getString(textRes));
    }

    public TMessageDialog title(CharSequence title) {
        return title(title, "#222222");
    }

    public TMessageDialog title(CharSequence title, @Size(min = 1) String colorStr) {
        return title(title, Color.parseColor(colorStr));
    }

    public TMessageDialog title(CharSequence title, @ColorInt int colorInt) {
        tvTitle.setText(title);
        tvTitle.setTextColor(colorInt);
        tvTitle.setOnClickListener(v -> mListener.onClickTitle(tvTitle));
        return this;
    }

    public TMessageDialog noTitle() {
        tvTitle.setVisibility(View.GONE);
        tvLine.setVisibility(View.GONE);
        return this;
    }

    public TMessageDialog content(@StringRes int textRes) {
        return content(getString(textRes), "#999999");
    }

    public TMessageDialog content(CharSequence content) {
        return content(content, "#999999");
    }

    public TMessageDialog content(CharSequence content, @Size(min = 1) String colorStr) {
        return content(content, Color.parseColor(colorStr));
    }

    public TMessageDialog content(CharSequence content, @ColorInt int colorInt) {
        tvContent.setText(content);
        tvContent.setTextColor(colorInt);
        tvContent.setOnClickListener(v -> mListener.onClickTitle(tvContent));
        return this;
    }

    public TMessageDialog left(@StringRes int textRes) {
        return left(getString(textRes), Color.parseColor("#BEC3CC"));
    }

    public TMessageDialog left(CharSequence text) {
        return left(text, Color.parseColor("#BEC3CC"));
    }

    public TMessageDialog left(CharSequence text, @ColorInt int colorInt) {
        return left(text, colorInt, defaultListener);
    }

    public TMessageDialog left(CharSequence text, @ColorInt int colorInt, @NonNull View.OnClickListener listener) {
        tvLeft.setText(text);
        tvLeft.setTextColor(colorInt);
        tvLeft.setOnClickListener(v -> {
            listener.onClick(tvLeft);
            mListener.onClickLeft(tvLeft);
        });
        return this;
    }

    public TMessageDialog mid(CharSequence text) {
        return mid(text, Color.parseColor("#999999"));
    }

    public TMessageDialog mid(@StringRes int textRes) {
        return mid(getString(textRes), Color.parseColor("#999999"));
    }

    public TMessageDialog mid(CharSequence text, @ColorInt int colorInt) {
        return mid(text, colorInt, defaultListener);
    }

    public TMessageDialog mid(CharSequence text, @ColorInt int colorInt, @NonNull View.OnClickListener listener) {
        tvMid.setText(text);
        tvMid.setTextColor(colorInt);
        tvMid.setOnClickListener(v -> {
            listener.onClick(tvMid);
            mListener.onClickMid(tvMid);
        });
        return this;
    }

    public TMessageDialog right(@StringRes int textRes) {
        return right(getString(textRes), Color.parseColor("#ff6d2a"));
    }

    public TMessageDialog right(@StringRes int textRes, @ColorRes int colorRes) {
        return right(getString(textRes), getColor(colorRes));
    }

    public TMessageDialog right(CharSequence text) {
        return right(text, Color.parseColor("#ff6d2a"));
    }

    public TMessageDialog right(CharSequence text, @ColorInt int colorInt) {
        return right(text, colorInt, defaultListener);
    }

    public TMessageDialog right(CharSequence text, @ColorInt int colorInt, @NonNull View.OnClickListener listener) {
        tvRight.setText(text);
        tvRight.setTextColor(colorInt);
        tvRight.setOnClickListener(v -> {
            listener.onClick(tvRight);
            mListener.onClickRight(tvRight);
        });
        return this;
    }

    public TMessageDialog onlyMid() {
        tvMid.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        return this;
    }

    public TMessageDialog withoutMid() {
        tvMid.setVisibility(View.GONE);
        tvLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        return this;
    }

    public TMessageDialog threeChoose() {
        tvLeft.setVisibility(View.VISIBLE);
        tvMid.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        return this;
    }

    public TMessageDialog noButton() {
        llButton.setVisibility(View.GONE);
        return this;
    }

    public TMessageDialog withProgress(int max) {
        llProgress.setVisibility(View.VISIBLE);
        progressBar.setMax(max);
        return this;
    }

    public TMessageDialog progress(int progress) {
        progressBar.setProgress(progress);
        tvProgress.setText(String.format("%s%%", progress));
        return this;
    }

    public TMessageDialog progressColor(@ColorInt int color) {
        ProgressDrawableHelper.change(progressBar, color);
        tvProgress.setTextColor(color);
        return this;
    }

    public TMessageDialog progressColor(@Size(min = 1) String color) {
        ProgressDrawableHelper.change(progressBar, color);
        tvProgress.setTextColor(Color.parseColor(color));
        return this;
    }

    public void show() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void update() {
        show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public static abstract class DoClickListener {

        public void onClickTitle(View view) {
        }

        public void onClickContent(View view) {
        }

        public void onClickLeft(View view) {
        }

        public void onClickMid(View view) {
        }

        public void onClickRight(View view) {
        }
    }

}
