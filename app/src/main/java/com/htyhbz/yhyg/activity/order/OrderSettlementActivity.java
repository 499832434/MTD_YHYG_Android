package com.htyhbz.yhyg.activity.order;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.adapter.OrderTypeContentAdapter;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.view.MyListView;
import com.htyhbz.yhyg.vo.Product;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/14.
 */
public class OrderSettlementActivity extends BaseActivity{
    private MyListView orderMLV;
    private OrderTypeContentAdapter adapter;
    private List<Product> list =new ArrayList<Product>();
    private EditText orderSendTimeET;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_settlement);

        initView();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        orderMLV= (MyListView) findViewById(R.id.orderMLV);
        adapter=new OrderTypeContentAdapter(OrderSettlementActivity.this,list);
        orderMLV.setAdapter(adapter);

        orderSendTimeET= (EditText) findViewById(R.id.orderSendTimeET);
        orderSendTimeET.setInputType(InputType.TYPE_NULL);
        orderSendTimeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(OrderSettlementActivity.this, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            updateDisplay();
                        }
                    }, mYear, mMonth, mDay).show();
                }
            }
        });
        orderSendTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(OrderSettlementActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateDisplay();
                    }
                }, mYear, mMonth, mDay).show();
            }
        });
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
    }



    /**

     * 更新日期

     */

    private void updateDisplay() {
        orderSendTimeET.setText(new StringBuilder().append(mYear).append("-").append(
                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append(
                (mDay < 10) ? "0" + mDay : mDay));
    }
}
