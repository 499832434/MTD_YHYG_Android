package com.htyhbz.yhyg.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;

/**
 * Created by zongshuo on 2017/7/10.
 */
public class LoginActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView(){
        findViewById(R.id.forgotTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
    }
}
