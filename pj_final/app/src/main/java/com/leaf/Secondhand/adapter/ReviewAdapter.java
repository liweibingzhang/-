package com.leaf.Secondhand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leaf.Secondhand.R;
import com.leaf.Secondhand.bean.Review;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 评论的适配器
 */
public class ReviewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private LinkedList<Review> reviews = new LinkedList<>();
    HashMap<Integer,View> location = new HashMap<>();


    public ReviewAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(LinkedList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
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
        if(location.get(position) == null) {
            convertView = layoutInflater.inflate(R.layout.layout_commodity_review,null);
            Review review = (Review) getItem(position);
            holder = new ViewHolder(convertView,review);
            location.put(position,convertView);
            convertView.setTag(holder);
        }else {
            convertView = location.get(position);
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }

    /**
     * 定义静态ViewHolder类
     */
    static class ViewHolder {

        TextView tvStuId,tvTime,tvContent;

        public ViewHolder(View itemView, Review review) {
            tvStuId = itemView.findViewById(R.id.tv_number);
            tvTime = itemView.findViewById(R.id.tv_current_time);
            tvContent = itemView.findViewById(R.id.tv_comment);
            tvStuId.setText(review.getStuId());
            tvTime.setText(review.getCurrentTime());
            tvContent.setText(review.getContent());
        }
    }
}
