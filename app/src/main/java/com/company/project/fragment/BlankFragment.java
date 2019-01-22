package com.company.project.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.company.project.R;
import com.company.project.base.BaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends BaseFragment {
    private static final String TEXT = "text";
    private static final String COLOR = "color";
    @BindView(R.id.tv_msg)
    TextView textView;
    private int mTextRes;
    private String mColor;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param text  Parameter 1.
     * @param color Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    public static BlankFragment newInstance(@StringRes int text, String color) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putInt(TEXT, text);
        args.putString(COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTextRes = getArguments().getInt(TEXT);
            mColor = getArguments().getString(COLOR);
        }
    }

    @Override
    protected void initView() {
        textView.setBackgroundColor(Color.parseColor(mColor));
        textView.setText(mTextRes);
        textView.setOnClickListener(v -> openWebsite("https:www.baidu.com", true, "", false));
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_blank;
    }

}
