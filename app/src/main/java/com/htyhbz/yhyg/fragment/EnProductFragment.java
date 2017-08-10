package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.enterprise.EnterpriseDetailActivity;
import com.htyhbz.yhyg.adapter.EnterpriseAdapter;
import com.htyhbz.yhyg.adapter.EnterpriseProductAdapter;

/**
 * Created by zongshuo on 2017/7/6.
 */
public class EnProductFragment extends Fragment {
    private View currentView = null;
    private EnterpriseDetailActivity mActivity;
    private ListView productLV;
    private EnterpriseProductAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_en_product, container, false);
        initView();
        return currentView;
    }


    private void initView() {
        productLV= (ListView) currentView.findViewById(R.id.productLV);
        adapter=new EnterpriseProductAdapter(mActivity,null);
        productLV.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (EnterpriseDetailActivity)context;
    }
}
