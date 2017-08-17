package com.htyhbz.yhyg.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.htyhbz.yhyg.R;


/**
 * Created by maning on 2017/4/10.
 * 美团外卖下拉刷新
 */
public class MTWMRefreshHeaderView extends RelativeLayout implements SwipeTrigger, SwipeRefreshTrigger {

//    private ImageView ivRefresh;
//    private LinearLayout llRefresh;
//
//    private AnimationDrawable mAnimDrawable;
//
//    private int mHeaderHeight;
//
//
//    public MTWMRefreshHeaderView(Context context) {
//        this(context, null);
//    }
//
//    public MTWMRefreshHeaderView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public MTWMRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        mHeaderHeight = context.getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_100);
//    }
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
//        llRefresh = (LinearLayout) findViewById(R.id.llRefresh);
//        mAnimDrawable = (AnimationDrawable) ivRefresh.getBackground();
//        if (!mAnimDrawable.isRunning()) {
//            mAnimDrawable.start();
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//        if (!mAnimDrawable.isRunning()) {
//            mAnimDrawable.start();
//        }
//    }
//
//    @Override
//    public void onPrepare() {
//        llRefresh.setAlpha(0.3f);
//        ivRefresh.setScaleX(0.4f);
//        ivRefresh.setScaleY(0.4f);
//        if (!mAnimDrawable.isRunning()) {
//            mAnimDrawable.start();
//        }
//    }
//
//    @Override
//    public void onMove(int y, boolean isComplete, boolean automatic) {
//        if (!isComplete) {
//            float scale = (float) y / (float) mHeaderHeight;
//            if (y >= mHeaderHeight) {
//                ivRefresh.setScaleX(1);
//                ivRefresh.setScaleY(1);
//                llRefresh.setAlpha(1.0f);
//            } else if (y > 0 && y < mHeaderHeight) {
//                ivRefresh.setScaleX(scale);
//                ivRefresh.setScaleY(scale);
//                llRefresh.setAlpha(scale);
//            } else {
//                ivRefresh.setScaleX(0.4f);
//                ivRefresh.setScaleY(0.4f);
//                llRefresh.setAlpha(0.3f);
//            }
//        }
//    }
//
//    @Override
//    public void onRelease() {
////        mAnimDrawable.stop();
//    }
//
//    @Override
//    public void onComplete() {
//        mAnimDrawable.stop();
//        llRefresh.setAlpha(1.0f);
//    }
//
//    @Override
//    public void onReset() {
//        mAnimDrawable.stop();
//        llRefresh.setAlpha(1.0f);
//    }
private ImageView ivSpeed;

    private ImageView ivRefresh;
    private LinearLayout llRefresh;
    private ImageView ivBox;

    private AnimationDrawable mAnimDrawable;

    private Animation mTwinkleAnim;

    private int mHeaderHeight;


    public MTWMRefreshHeaderView(Context context) {
        this(context, null);
    }

    public MTWMRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MTWMRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = context.getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_100);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
        llRefresh = (LinearLayout) findViewById(R.id.llRefresh);
        ivSpeed = (ImageView) findViewById(R.id.ivSpeed);
        ivBox = (ImageView) findViewById(R.id.ivBox);
        mAnimDrawable = (AnimationDrawable) ivRefresh.getBackground();
        mTwinkleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.twinkle);
        ivSpeed.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        ivSpeed.setVisibility(VISIBLE);
        ivBox.setVisibility(GONE);
        ivSpeed.startAnimation(mTwinkleAnim);
        llRefresh.setAlpha(1.0f);
        ivBox.setAlpha(1.0f);
        if (!mAnimDrawable.isRunning()) {
            mAnimDrawable.start();
        }
    }

    @Override
    public void onPrepare() {
        ivSpeed.clearAnimation();
        ivSpeed.setVisibility(GONE);
        ivBox.setVisibility(VISIBLE);
        llRefresh.setAlpha(0.3f);
        ivBox.setAlpha(0.3f);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            float scale = (float) y / (float) mHeaderHeight;
            if (y >= mHeaderHeight) {
                ivRefresh.setScaleX(1);
                ivRefresh.setScaleY(1);
                llRefresh.setAlpha(1.0f);
                ivBox.setAlpha(1.0f);
                ivBox.setScaleX(1);
                ivBox.setScaleY(1);
            } else if (y > 0 && y < mHeaderHeight) {
                ivRefresh.setScaleX(scale);
                ivRefresh.setScaleY(scale);
                llRefresh.setAlpha(scale);
                ivBox.setAlpha(scale);
                ivBox.setScaleX(scale);
                ivBox.setScaleY(scale);
            } else {
                ivRefresh.setScaleX(0.4f);
                ivRefresh.setScaleY(0.4f);
                ivBox.setScaleX(0.5f);
                ivBox.setScaleY(0.5f);
                llRefresh.setAlpha(0.3f);
                ivBox.setAlpha(0.3f);
            }
        }
    }

    @Override
    public void onRelease() {
        mAnimDrawable.stop();
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onReset() {
        mAnimDrawable.stop();
        ivSpeed.clearAnimation();
        ivSpeed.setVisibility(GONE);
        ivBox.setVisibility(VISIBLE);
        llRefresh.setAlpha(1.0f);
        ivBox.setAlpha(1.0f);
    }
}
