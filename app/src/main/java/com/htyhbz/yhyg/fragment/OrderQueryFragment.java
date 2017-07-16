package com.htyhbz.yhyg.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.order.OrderQueryActivity;
import com.htyhbz.yhyg.adapter.OrderTypeAdapter;
import com.htyhbz.yhyg.vo.OrderInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/14.
 */
public class OrderQueryFragment extends Fragment {
    private View currentView = null;
    private final static String FRAGMENT_TYPE = "fragmentType";
    private OrderQueryActivity parentActivity = null;
    private int fragmentType = 0;
    private TextView startTimeTV,endTimeTV,areaTV;
    private int mYear;
    private int mMonth;
    private int mDay;
    private OrderQueryActivity mActivity;
    private ListView orderLV;
    private OrderTypeAdapter adapter;
    private List<OrderInfo> orderInfoList=new ArrayList<OrderInfo>();
    private View footView;
    private LinearLayout timeLL,areaLL;
    private int flagNum=0;

    public static OrderQueryFragment newInstance(OrderQueryActivity parentActivity,  int fragmentType) {
        OrderQueryFragment fr = new OrderQueryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_TYPE, fragmentType);
        fr.setArguments(bundle);
        fr.parentActivity = parentActivity;
        return fr;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_order_query, container, false);
        initView();
        return currentView;
    }
    private void initView(){
        fragmentType = getArguments().getInt(FRAGMENT_TYPE);
        timeLL= (LinearLayout) currentView.findViewById(R.id.timeLL);
        areaLL= (LinearLayout) currentView.findViewById(R.id.areaLL);
        if(0==fragmentType){
            timeLL.setVisibility(View.VISIBLE);
            areaLL.setVisibility(View.GONE);
        }else{
            timeLL.setVisibility(View.GONE);
            areaLL.setVisibility(View.VISIBLE);
        }
        orderLV= (ListView) currentView.findViewById(R.id.orderLV);
        adapter=new OrderTypeAdapter(mActivity,null);
        orderLV.setAdapter(adapter);
        footView=LayoutInflater.from(mActivity).inflate(R.layout.footview_order_query,null);
        orderLV.addFooterView(footView);
        startTimeTV= (TextView) currentView.findViewById(R.id.startTimeTV);
        endTimeTV= (TextView) currentView.findViewById(R.id.endTimeTV);
        areaTV= (TextView) currentView.findViewById(R.id.areaTV);
        areaTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setIcon(R.drawable.icon);
                builder.setTitle("请选择乡镇");
                final String[] sex = {"乡镇1", "乡镇2", "乡镇3","乡镇4", "乡镇5", "乡镇6"};
                builder.setSingleChoiceItems(sex, flagNum, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        flagNum=which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        areaTV.setText(sex[flagNum]);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
            }
        });
        startTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(0);
            }
        });
        endTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(0);
            }
        });
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay(0);
        updateDisplay(1);
    }

    private void updateDisplay(int flag) {
        if(0==flag){
            startTimeTV.setText(new StringBuilder().append("开始时间:").append(mYear).append("-").append(
                    (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append(
                    (mDay < 10) ? "0" + mDay : mDay));
        }else{
            endTimeTV.setText(new StringBuilder().append("结束时间:").append(mYear).append("-").append(
                    (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append(
                    (mDay < 10) ? "0" + mDay : mDay));
        }
    }

    private void setDate(final int flag){
        new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                updateDisplay(flag);
            }
        }, mYear, mMonth, mDay).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity=(OrderQueryActivity)context;
    }


}
