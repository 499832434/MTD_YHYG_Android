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
import com.google.gson.Gson;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.order.OrderDetailActivity;
import com.htyhbz.yhyg.activity.order.OrderSettlementActivity;
import com.htyhbz.yhyg.fragment.OrderTypeFragment;
import com.htyhbz.yhyg.view.MyListView;
import com.htyhbz.yhyg.vo.OrderInfo;
import com.htyhbz.yhyg.vo.Product;
import com.htyhbz.yhyg.vo.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/11.
 */
public class OrderTypeAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<OrderInfo> mData;
    private List<UserInfo> mData1;
    private Context context;

    public OrderTypeAdapter(FragmentActivity context, List<OrderInfo> data,List<UserInfo> data1) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mData1 = data1;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
//        final ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = mInflater.inflate(R.layout.item_order_type, null);
//            holder.orderIDTV = (TextView) convertView.findViewById(R.id.orderIDTV);
//            holder.orderSendTimeTV = (TextView) convertView.findViewById(R.id.orderSendTimeTV);
//            holder.countTV = (TextView) convertView.findViewById(R.id.countTV);
//            holder.deleteOrderTV = (TextView) convertView.findViewById(R.id.deleteOrderTV);
//            holder.checkOrderTV = (TextView) convertView.findViewById(R.id.checkOrderTV);
//            holder.orderMLV = (MyListView) convertView.findViewById(R.id.orderMLV);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        final View view;
        ViewHolder holder;

        if (convertView==null) {
            view =mInflater.inflate(R.layout.item_order_type, parent, false);
            setViewHolder(view);
        }
        else if (((ViewHolder)convertView.getTag()).needInflate) {
            view = mInflater.inflate(R.layout.item_order_type, parent, false);
            setViewHolder(view);
        }
        else {
            view = convertView;
        }

        holder = (ViewHolder)view.getTag();

        if(mData.get(position).getList().size()>0){
            holder.orderMLV.setAdapter(new OrderTypeContentAdapter((FragmentActivity) context, mData.get(position).getList()));
        }
        holder.checkOrderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("shoppingAccount", "");
                map.put("shoppingTotalPrice", mData.get(position).getOrderAllPrice());
                ArrayList<HashMap> list = new ArrayList<HashMap>();
                for (int i = 0; i < mData.get(position).getList().size(); i++) {
                    Product product = mData.get(position).getList().get(i);
                    HashMap<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("shoppingsingleTotal", product.getorderProductCount());
                    map1.put("productId", product.getproductId());
                    map1.put("productName", product.getproductName());
                    map1.put("productPictureUrl", product.getproductPictureUrl());
                    map1.put("productPrice", product.getProductPrice());
                    list.add(map1);
                }
                map.put("shoppinglist", list);
                Gson gson = new Gson();
                String str = gson.toJson(map);

                if ("0".equals(mData.get(position).getOrderType())) {
                    Intent intent = new Intent(context, OrderSettlementActivity.class);
                    intent.putExtra("flag", "order");
                    intent.putExtra("map", str);
                    intent.putExtra("orderId", mData.get(position).getOrderID());
                    intent.putExtra("userinfo", mData1.get(position));
                    context.startActivity(intent);
                } else {
                    Intent intent=new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("flag",mData.get(position).getOrderType());
                    intent.putExtra("map", str);
                    intent.putExtra("useIntegralCount",mData.get(position).getUseIntegralCount());
                    intent.putExtra("actualPayPrice",mData.get(position).getActualPayPrice());
                    intent.putExtra("userinfo", mData1.get(position));
                    context.startActivity(intent);
                }
            }
        });
        if("0".equals(mData.get(position).getOrderType())){
            holder.deleteOrderTV.setVisibility(View.VISIBLE);
            holder.deleteOrderTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        ((MainActivity)context).getOrderFragment().getFragments().get(0).deleteOrder(mData.get(position).getOrderID(),view,position);
                    }catch (Exception e){

                    }
                }
            });
        }else{
            holder.deleteOrderTV.setVisibility(View.INVISIBLE);
        }
        holder.orderIDTV.setText("订单号:"+mData.get(position).getOrderID());
        holder.orderSendTimeTV.setText("提交时间:"+mData.get(position).getOrderSendTime());
        holder.countTV.setText("¥"+mData.get(position).getOrderAllPrice());
        return view;
    }

//    public final class ViewHolder {
//        public TextView orderIDTV;
//        public TextView orderSendTimeTV;
//        public MyListView orderMLV;
//        public TextView countTV,deleteOrderTV,checkOrderTV;
//    }

    public class ViewHolder {
        public TextView orderIDTV;
        public TextView orderSendTimeTV;
        public MyListView orderMLV;
        public TextView countTV,deleteOrderTV,checkOrderTV;
        public boolean needInflate;
    }

    private void setViewHolder(View view) {
        ViewHolder vh = new ViewHolder();
        vh.orderIDTV = (TextView) view.findViewById(R.id.orderIDTV);
        vh.orderSendTimeTV = (TextView) view.findViewById(R.id.orderSendTimeTV);
        vh.countTV = (TextView) view.findViewById(R.id.countTV);
        vh.deleteOrderTV = (TextView) view.findViewById(R.id.deleteOrderTV);
        vh.checkOrderTV = (TextView) view.findViewById(R.id.checkOrderTV);
        vh.orderMLV = (MyListView) view.findViewById(R.id.orderMLV);
        vh.needInflate = false;
        view.setTag(vh);
    }

}
