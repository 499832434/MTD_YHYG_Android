package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.enterprise.EnterpriseDetailActivity;
import com.htyhbz.yhyg.adapter.OrderTypeAdapter;
import com.htyhbz.yhyg.vo.OrderInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/6.
 */
public class EnIntroductFragment extends Fragment {
    private View currentView = null;
    private EnterpriseDetailActivity mActivity;
    private final static String INTRODUCT = "introduct";
    private String content;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_en_introduct, container, false);
        initView();
        return currentView;
    }

    public static EnIntroductFragment newInstance(String param1) {
        EnIntroductFragment fragment = new EnIntroductFragment();
        Bundle args = new Bundle();
        args.putString(INTRODUCT,param1);
        fragment.setArguments(args);
        return fragment;
    }


    private void initView() {
        content = getArguments().getString(INTRODUCT);
        ((TextView)currentView.findViewById(R.id.contentTV)).setText(content);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (EnterpriseDetailActivity)context;
    }
}
