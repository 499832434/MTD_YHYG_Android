package com.htyhbz.yhyg.activity.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.view.CustomTitleBar;

/**
 * Created by zongshuo on 2017/7/10.
 */
public class ResetPasswordActivity extends BaseActivity{

    private EditText usernameET,passwordET;
    private Button commitB;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initView();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        commitB= (Button) findViewById(R.id.commitB);
        commitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiddenSoftInput();
            }
        });
        usernameET= (EditText) findViewById(R.id.usernameET);
        passwordET= (EditText) findViewById(R.id.passwordET);
        usernameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(usernameET.getText().toString())) {
                    if (!TextUtils.isEmpty(passwordET.getText().toString())) {
                        commitB.setBackgroundResource(R.drawable.ic_bg_login_button);
                        commitB.setClickable(true);
                    } else {
                        commitB.setBackgroundResource(R.drawable.ic_bg_login_button_un);
                        commitB.setClickable(false);
                    }
                } else {
                    commitB.setBackgroundResource(R.drawable.ic_bg_login_button_un);
                    commitB.setClickable(false);
                }
            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(passwordET.getText().toString())) {
                    if (!TextUtils.isEmpty(usernameET.getText().toString())) {
                        commitB.setBackgroundResource(R.drawable.ic_bg_login_button);
                        commitB.setClickable(true);
                    } else {
                        commitB.setBackgroundResource(R.drawable.ic_bg_login_button_un);
                        commitB.setClickable(false);
                    }
                } else {
                    commitB.setBackgroundResource(R.drawable.ic_bg_login_button_un);
                    commitB.setClickable(false);
                }
            }
        });
    }
}
