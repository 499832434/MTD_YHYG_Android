package com.htyhbz.yhyg.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.htyhbz.yhyg.R;

/**
 * Created by zongshuo on 2017/8/15 0015.
 */
public class ErrorsFragment extends Fragment {

    /**
     * 显示异常提示
     *
     * @param i 0 服务器开小差
     *          1 抱歉，没有相关搜索结果
     *          2 网络不给力
     *          3 没有连接网络
     *          4 无相关内容展示
     *          5 你尚未收藏内容
     */
    public void showErrorLayout(final View view, View.OnClickListener errorListener, int i) {
        switch (i) {
//            case 0:
//                setErrorLayout(view, R.drawable.no_server_error, "服务器开小差了，请稍后再试", "点击屏幕刷新", errorListener);
//                break;
//            case 1:
//                setErrorLayout(view, R.drawable.no_data_error, "抱歉，没有相关搜索结果", "", errorListener);
//                break;
//            case 2:
//                setErrorLayout(view, R.drawable.no_network_instability_error, "网络不给力，请稍后重试", "点击屏幕刷新", errorListener);
//                break;
//            case 3:
//                setErrorLayout(view, R.drawable.no_network_error, "没有连接网络", "请连接之后，点击屏幕刷新", errorListener);
//                break;
            case 4:
                setErrorLayout(view, R.drawable.icon_empty, "无相关内容展示", "点击屏幕刷新", errorListener);
                break;
            case 5:
                setErrorLayout(view, R.drawable.no_collect_error, "您尚未收藏内容", "", errorListener);
                break;
            case 6:
                setErrorLayout(view, R.drawable.no_collect_error, "无法查看更多相关信息", "", errorListener);
                break;
            default:
                break;
        }
    }
    /**
     * 修改异常布局
     */

    private void setErrorLayout(View view, int errorImage, String errorTitle, String errorText, View.OnClickListener errorListener) {
        ((ImageView) view.findViewById(R.id.errorImageView)).setImageDrawable(getResources().getDrawable(errorImage));
        ((TextView) view.findViewById(R.id.detailTextView)).setText(errorTitle);
        ((TextView) view.findViewById(R.id.errorTextView)).setText(errorText);
        if (errorListener != null)
            view.setOnClickListener(errorListener);
    }

}