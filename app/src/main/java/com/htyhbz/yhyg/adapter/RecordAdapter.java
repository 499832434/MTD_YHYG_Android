package com.htyhbz.yhyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.order.OrderDetailActivity;
import com.htyhbz.yhyg.view.MyListView;
import com.htyhbz.yhyg.vo.OrderInfo;
import com.htyhbz.yhyg.vo.WithDrawal;

import java.util.List;

/**
 * Created by zongshuo on 2017/7/11.
 */
public class RecordAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<WithDrawal> mData;
    private Context context;

    public RecordAdapter(FragmentActivity context, List<WithDrawal> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return mData.size();
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
            convertView = mInflater.inflate(R.layout.item_record, null);
            holder.amountTV= (TextView) convertView.findViewById(R.id.amountTV);
            holder.statusTV= (TextView) convertView.findViewById(R.id.statusTV);
            holder.timeTV= (TextView) convertView.findViewById(R.id.timeTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.amountTV.setText("提现:  "+mData.get(position).getWithdrawalAmount()+"元");
        holder.statusTV.setText(mData.get(position).getWithdrawalStatus());
        holder.timeTV.setText(mData.get(position).getWithdrawalTime());
        return convertView;
    }

    public final class ViewHolder {
        public TextView amountTV,statusTV,timeTV;
    }

}
