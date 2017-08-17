package com.htyhbz.yhyg.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.login.LoginActivity;

/**
 * Created by zongshuo on 2017/8/16.
 */
public class SplashActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if(TextUtils.isEmpty(getUserInfo(0))){
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }else{
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 3000);
    }
}
