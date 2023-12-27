package com.leaf.Secondhand.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leaf.Secondhand.R;
import com.leaf.Secondhand.bean.Collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 收藏适配器Adapter类
 */
public class MyCollectionAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<Collection> collections = new ArrayList<>();

    HashMap<Integer,View> location = new HashMap<>();

    public MyCollectionAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Collection> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int position) {
        return collections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取适配器中指定位置的视图
     * @param position    要获取视图的位置
     * @param convertView 可重复使用的视图，用于提高性能
     * @param parent      父视图组
     * @return 返回指定位置的视图
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(location.get(position) == null){
            convertView = layoutInflater.inflate(R.layout.layout_my_collection,null);
            Collection collection = (Collection) getItem(position);
            holder = new ViewHolder(convertView,collection);
            location.put(position,convertView);
            convertView.setTag(holder);
        }else{
            convertView = location.get(position);
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    //定义静态类,包含每一个item的所有元素
    static class ViewHolder {
        ImageView ivCommodity;
        TextView tvTitle,tvDescription,tvPrice,tvPhone;

        public ViewHolder(View itemView,Collection collection) {
            tvTitle = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            ivCommodity = itemView.findViewById(R.id.iv_commodity);
            tvTitle.setText(collection.getTitle());
            tvDescription.setText(collection.getDescription());
            tvPrice.setText(String.valueOf(collection.getPrice())+"元");
            tvPhone.setText(collection.getPhone());
            byte[] picture = collection.getPicture();
            Bitmap img = BitmapFactory.decodeByteArray(picture,0,picture.length);
            ivCommodity.setImageBitmap(img);
        }
    }
}
