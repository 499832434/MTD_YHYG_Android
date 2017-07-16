package com.htyhbz.yhyg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.order.OrderDetailActivity;
import com.htyhbz.yhyg.view.MyListView;
import com.htyhbz.yhyg.vo.OrderInfo;

import java.util.List;

/**
 * Created by zongshuo on 2017/7/11.
 */
public class OrderTypeAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<OrderInfo> mData;
    private Context context;

    public OrderTypeAdapter(FragmentActivity context, List<OrderInfo> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int arg0) {
        return mData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_order_type, null);
            holder.orderIDTV = (TextView) convertView.findViewById(R.id.orderIDTV);
            holder.orderSendTimeTV = (TextView) convertView.findViewById(R.id.orderSendTimeTV);
            holder.countTV = (TextView) convertView.findViewById(R.id.countTV);
            holder.deleteOrderTV = (TextView) convertView.findViewById(R.id.deleteOrderTV);
            holder.checkOrderTV = (TextView) convertView.findViewById(R.id.checkOrderTV);
            holder.orderMLV = (MyListView) convertView.findViewById(R.id.orderMLV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.orderMLV.setAdapter(new OrderTypeContentAdapter((FragmentActivity)context,null));
        holder.checkOrderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, OrderDetailActivity.class));
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public TextView orderIDTV;
        public TextView orderSendTimeTV;
        public MyListView orderMLV;
        public TextView countTV,deleteOrderTV,checkOrderTV;
    }

}
