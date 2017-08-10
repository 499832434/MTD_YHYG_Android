package com.htyhbz.yhyg.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.vo.Product;

import java.util.List;

/**
 * Created by zongshuo on 2017/7/11.
 */
public class OrderTypeContentAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<Product> mData;
    private Context context;

    public OrderTypeContentAdapter(FragmentActivity context, List<Product> data) {
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
            convertView = mInflater.inflate(R.layout.item_order_type_content, null);
            holder.productNameTV = (TextView) convertView.findViewById(R.id.productNameTV);
            holder.productPriceTV = (TextView) convertView.findViewById(R.id.productPriceTV);
            holder.orderProductionsCountTV = (TextView) convertView.findViewById(R.id.orderProductionsCountTV);
            holder.pictureIV = (ImageView) convertView.findViewById(R.id.pictureIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public final class ViewHolder {
        public ImageView pictureIV;
        public TextView productNameTV,productPriceTV,orderProductionsCountTV;
    }

}
