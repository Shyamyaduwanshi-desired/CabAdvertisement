package com.diss.cabadvertisement.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.ImageBean;

import java.io.File;
import java.util.List;

public class StickerImageAdapter extends RecyclerView.Adapter<StickerImageAdapter.MyViewHolder> {
    private List<ImageBean> list;
    private Context context;
    private ImageClick planClick;

    public StickerImageAdapter(Context context, List<ImageBean> list, ImageClick planClick) {
        this.context = context;
        this.list = list;
        this.planClick = planClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_selected_pic, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

              if(TextUtils.isEmpty(list.get(position).getImagePath())) {
                  holder.ivPic.setImageResource(R.drawable.flat);
              }
              else {
                  Log.e("","image url= "+list.get(position).getImagePath());
                  Glide.with(context)
                          .load(list.get(position).getImagePath())
                          .placeholder(R.mipmap.ic_launcher)
                          .into(holder.ivPic);
              }
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planClick.onClick(position,2);
            }
        });


        holder.rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planClick.onClick(position,1);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        public MyViewHolder(View view) {
            super(view);
            ivPic = view.findViewById(R.id.img_pic);
        }
    }

    public interface ImageClick{
        void PhotonClick(int position, int diff);
    }
}
