package com.htyhbz.yhyg.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.enterprise.EnterpriseMainActivity;
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
                    startActivity(intent);
                }else{
                    if("6".equals(getUserInfo(9))||"7".equals(getUserInfo(9))){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }else if("3".equals(getUserInfo(9))){
                        Intent intent1 = new Intent(SplashActivity.this, EnterpriseMainActivity.class);
                        intent1.putExtra("flag", "3");
                        startActivity(intent1);
                    }else if("2".equals(getUserInfo(9))){
                        Intent intent2 = new Intent(SplashActivity.this, EnterpriseMainActivity.class);
                        intent2.putExtra("flag", "2");
                        startActivity(intent2);
                    }
                }
                SplashActivity.this.finish();
            }
        }, 3000);
    }
}
