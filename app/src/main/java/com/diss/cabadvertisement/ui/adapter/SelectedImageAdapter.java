package com.diss.cabadvertisement.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.AddCompaignLocBean;
import com.diss.cabadvertisement.ui.model.ImageBean;

import java.io.File;
import java.util.List;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.MyViewHolder> {
    private List<ImageBean> list;
    private Context context;
    private ImageClick planClick;

    public SelectedImageAdapter(Context context, List<ImageBean> list, ImageClick planClick) {
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


                Glide.with(context)
                .load(new File(list.get(position).getImagePath()))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivPic);
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
//        TextView tvLocNm;
        ImageView ivPic;
//        LinearLayout lyMain;
//        RelativeLayout rlAdd;
        public MyViewHolder(View view) {
            super(view);
//            tvLocNm = view.findViewById(R.id.tv_loc_nm);
            ivPic = view.findViewById(R.id.img_pic);
//            lyMain = view.findViewById(R.id.ly_main);
//            rlAdd = view.findViewById(R.id.rl_add_location);
        }
    }

    public interface ImageClick{
        void PhotonClick(int position, int diff);
    }
}
