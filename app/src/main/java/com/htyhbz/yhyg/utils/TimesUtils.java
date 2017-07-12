package com.htyhbz.yhyg.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import com.htyhbz.yhyg.R;

/**
 * Created by zongshuo on 2017/7/10.
 */
public class TimesUtils extends CountDownTimer {
    private TextView tv;
    private Context context;

    public TimesUtils(long millisInFuture, long countDownInterval, TextView tv, Context context) {
        super(millisInFuture, countDownInterval);
        this.tv=tv;
        this.context=context;
    }

    @Override
    public void onFinish() {
        tv.setText(R.string.change_phone1);
        tv.setBackgroundResource(R.drawable.ic_bg_login_button);
        tv.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        String str=context.getResources().getString(R.string.change);
        tv.setText(millisUntilFinished / 1000 + str);
        tv.setBackgroundResource(R.drawable.ic_bg_login_button_un);
        tv.setEnabled(false);
    }


}