package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.login.LoginActivity;
import com.htyhbz.yhyg.activity.login.ModifyPasswordActivity;
import com.htyhbz.yhyg.activity.login.ResetPasswordActivity;

/**
 * Created by zongshuo on 2017/7/3.
 */
public class AccountFragment extends Fragment implements View.OnClickListener{
    private View currentView = null;
    private TextView loginTV;
    protected MainActivity mActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_account, container, false);
        initView();
        return currentView;
    }

    private void initView(){
        loginTV= (TextView) currentView.findViewById(R.id.loginTV);
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });
        currentView.findViewById(R.id.integralLiftingRL).setOnClickListener(this);
        currentView.findViewById(R.id.orderSummaryRL).setOnClickListener(this);
        currentView.findViewById(R.id.currentRecordRL).setOnClickListener(this);
        currentView.findViewById(R.id.modifyPasswordRL).setOnClickListener(this);
        currentView.findViewById(R.id.myCollectionRL).setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.integralLiftingRL:
                break;
            case R.id.orderSummaryRL:
                break;
            case R.id.currentRecordRL:
                break;
            case R.id.modifyPasswordRL:
                startActivity(new Intent(mActivity, ModifyPasswordActivity.class));
                break;
            case R.id.myCollectionRL:
                break;
        }
    }
}
