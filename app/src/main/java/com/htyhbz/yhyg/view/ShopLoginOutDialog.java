package com.htyhbz.yhyg.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.login.LoginActivity;
import com.htyhbz.yhyg.adapter.PopupDishAdapter;
import com.htyhbz.yhyg.imp.ShopCartImp;
import com.htyhbz.yhyg.utils.DensityUtil;
import com.htyhbz.yhyg.vo.ShopCart;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class ShopLoginOutDialog extends Dialog{

    private LinearLayout linearLayout;
    private Context context;
    private Button logoutB,cancelB;


    public ShopLoginOutDialog(Context context, int themeResId) {
        super(context,themeResId);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loginout);
        linearLayout= (LinearLayout) findViewById(R.id.LL);
        logoutB= (Button) findViewById(R.id.logoutB);
        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity)context).clearLoginInfo();
                context.startActivity(new Intent(context,LoginActivity.class));
                ((BaseActivity)context).finish();
            }
        });
        cancelB= (Button) findViewById(R.id.cancelB);
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopLoginOutDialog.super.dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        animationShow(500);
    }

    @Override
    public void dismiss() {
        animationHide(500);
    }



    private void animationShow(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY", 1000, 0).setDuration(mDuration)
        );
        animatorSet.start();
    }

    private void animationHide(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY",0,1000).setDuration(mDuration)
        );
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ShopLoginOutDialog.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


}
