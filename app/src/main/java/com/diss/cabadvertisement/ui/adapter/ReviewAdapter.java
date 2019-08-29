package com.diss.cabadvertisement.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.ReviewBean;
import com.diss.cabadvertisement.ui.util.AppData;


import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private List<ReviewBean> list;
    private Context context;
    private ReviewClick reviewClick;
    AppData appdata;

    public ReviewAdapter(Context context, List<ReviewBean> list, ReviewClick reviewClick) {
        this.context = context;
        this.list = list;
        this.reviewClick = reviewClick;
        appdata=new AppData(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_review, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvName.setText(list.get(position).getReviewName());
        holder.tvDsc.setText(list.get(position).getReviewDsc());
        holder.tvDate.setText(appdata.ConvertDate02(list.get(position).getReviewDate()));
        float rate=0;
        if(list.get(position).getRateValue().equals("0")|| TextUtils.isEmpty(list.get(position).getRateValue()))
        {
            rate=0;
        }
        else {
             rate = Float.parseFloat(list.get(position).getRateValue());
            }
        holder.rbRate.setRating(rate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewClick.onClick(position,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvDsc,tvDate;
        ImageView ivPic;
        RatingBar rbRate;
        public MyViewHolder(View view) {
            super(view);
            ivPic = view.findViewById(R.id.iv_pic);
            tvName = view.findViewById(R.id.tv_name);
            tvDsc = view.findViewById(R.id.tv_dsc);
            tvDate = view.findViewById(R.id.tv_date);//3 July 2019
            rbRate = view.findViewById(R.id.go_rating);//3 July 2019
        }
    }

    public interface ReviewClick{
        void onClick(int position, int diff);
    }
}
