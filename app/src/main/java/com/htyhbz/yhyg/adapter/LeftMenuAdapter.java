package com.htyhbz.yhyg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.vo.ProductMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class LeftMenuAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<ProductMenu> mMenuList;
    private int mSelectedNum;
    private List<onItemSelectedListener> mSelectedListenerList;

    public interface onItemSelectedListener{
        public void onLeftItemSelected(int postion,ProductMenu menu);
    }

    public void addItemSelectedListener(onItemSelectedListener listener){
        if(mSelectedListenerList!=null)
            mSelectedListenerList.add(listener);
    }

    public void removeItemSelectedListener(onItemSelectedListener listener){
        if(mSelectedListenerList!=null && !mSelectedListenerList.isEmpty())
            mSelectedListenerList.remove(listener);
    }

    public LeftMenuAdapter(Context mContext, ArrayList<ProductMenu> mMenuList){
        this.mContext = mContext;
        this.mMenuList = mMenuList;
        this.mSelectedNum = -1;
        this.mSelectedListenerList = new ArrayList<onItemSelectedListener>();
        if(mMenuList.size()>0)
            mSelectedNum = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_menu_item, parent, false);
        LeftMenuViewHolder viewHolder = new LeftMenuViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductMenu productMenu = mMenuList.get(position);
        LeftMenuViewHolder viewHolder = (LeftMenuViewHolder)holder;
        viewHolder.menuName.setText(productMenu.getCatagory().getCatalog());
//        switch (position%4){
//            case 0:
//                viewHolder.menuNameIV.setImageResource(R.drawable.icon_yanhualei);
//                break;
//            case 1:
//                viewHolder.menuNameIV.setImageResource(R.drawable.icon_baozhulei);
//                break;
//            case 2:
//                viewHolder.menuNameIV.setImageResource(R.drawable.icon_taocanlei);
//                break;
//            case 3:
//                viewHolder.menuNameIV.setImageResource(R.drawable.icon_xiaoyanhua);
//                break;
//        }
        if(!TextUtils.isEmpty(productMenu.getCatagory().getCategoryImageUrl())){
            ((BaseActivity)mContext).getNetWorkPicture(productMenu.getCatagory().getCategoryImageUrl(),viewHolder.menuNameIV);
        }else{
            viewHolder.menuNameIV.setImageResource(R.drawable.icon_loading);
        }
        if(mSelectedNum==position){
            viewHolder.menuLayout.setSelected(true);
        }else{
            viewHolder.menuLayout.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    public void setSelectedNum(int selectedNum) {
        if(selectedNum<getItemCount() && selectedNum>=0 ) {
            this.mSelectedNum = selectedNum;
            notifyDataSetChanged();
        }
    }

    public int getSelectedNum() {
        return mSelectedNum;
    }

    private class LeftMenuViewHolder extends RecyclerView.ViewHolder{

        TextView menuName;
        ImageView menuNameIV;
        LinearLayout menuLayout;

        public LeftMenuViewHolder(final View itemView) {
            super(itemView);
            menuName = (TextView)itemView.findViewById(R.id.left_menu_textview);
            menuNameIV= (ImageView) itemView.findViewById(R.id.left_menu_imageview);
            menuLayout = (LinearLayout)itemView.findViewById(R.id.left_menu_item);
            menuLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickPosition = getAdapterPosition();
//                    setSelectedNum(clickPosition);
                    notifyItemSelected(clickPosition);
                }
            });
        }
    }

    private void notifyItemSelected(int position) {
        if(mSelectedListenerList!=null && !mSelectedListenerList.isEmpty()){
            for(onItemSelectedListener listener:mSelectedListenerList){
                listener.onLeftItemSelected(position,mMenuList.get(position));
            }
        }
    }
}
