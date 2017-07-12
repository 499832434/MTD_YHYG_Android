package com.htyhbz.yhyg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.htyhbz.yhyg.R;

/**
 * Created by zongshuo on 2017/7/10.
 */
public class CustomTitleBar extends LinearLayout {
    private ImageView leftImageView;
    private TextView titleTextView;

    private String text;
    private int imageLeft;

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View titleBar = LayoutInflater.from(context).inflate(R.layout.custom_title_bar_view, null);
        titleTextView = (TextView) titleBar.findViewById(R.id.titleTextView);
        leftImageView = (ImageView) titleBar.findViewById(R.id.leftImageView);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);
        text = ta.getString(R.styleable.CustomTitleBar_text);
        imageLeft = ta.getResourceId(R.styleable.CustomTitleBar_imageLeft, R.drawable.icon_back);
        titleTextView.setText(text);
        leftImageView.setImageResource(imageLeft);
        ta.recycle();

        addView(titleBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    public void setLeftImageOnClickListener(OnClickListener listener) {
        leftImageView.setVisibility(View.VISIBLE);
        leftImageView.setOnClickListener(listener);
    }


    /**
     * 修改中间标题
     */
    public void setTitleTextView(String title) {
        if (title != null)
            titleTextView.setText(title);
    }


    /**
     * 获取中间标题内容
     */
    public String getTitleTextView() {

        return titleTextView.getText().toString().trim();
    }

}
