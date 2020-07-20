package com.company.project.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.project.R;
import com.company.project.R2;
import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.temp.TemplateAdapter;
import com.company.project.base.BaseFragment;
import com.company.project.testbean.Bean1;
import com.company.project.testbean.TestDataJSON;

import java.util.List;

import butterknife.BindView;

/**
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends BaseFragment {
    private static final String TEXT = "text";
    private static final String COLOR = "color";
    @BindView(R2.id.tv_msg)
    TextView textView;
    @BindView(R2.id.rv_content)
    RecyclerView rvContent;
    private int mTextRes;
    private String mColor;
    private TemplateAdapter adapter;

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
        if ("首页".equals(getString(mTextRes))) {
            rvContent.setLayoutManager(new LinearLayoutManager(mContext));
            adapter = new TemplateAdapter<Bean1>(mContext);
            rvContent.setAdapter(adapter);
            List<BaseListBean> beans = TestDataJSON.getJson();
            adapter.setDataList(beans);
            adapter.setOnItemClickListener((view, position) -> openWebsite("https:www.baidu.com", true, "", false));
        }else {
            rvContent.setVisibility(View.GONE);
        }
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_blank;
    }

}
