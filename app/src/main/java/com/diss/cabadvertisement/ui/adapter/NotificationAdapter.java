package com.diss.cabadvertisement.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.NotiBean;
import com.diss.cabadvertisement.ui.util.AppData;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<NotiBean> list;
    private Context context;
    private NotificationClick notiClick;
    AppData appdata;

    public NotificationAdapter(Context context, List<NotiBean> list, NotificationClick notiClick) {
        this.context = context;
        this.list = list;
        this.notiClick = notiClick;
        appdata=new AppData(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvNotiTitle.setText(list.get(position).getNotiTitle());
        holder.tvNotiDsc.setText(list.get(position).getNotiDsc());

        holder.tvNotiTime.setText(list.get(position).getNotiTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notiClick.onClick(position,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotiTitle,tvNotiDsc,tvNotiTime;
        ImageView ivPic;
        public MyViewHolder(View view) {
            super(view);
            ivPic = view.findViewById(R.id.iv_image);
            tvNotiTitle = view.findViewById(R.id.tv_noti_title);
            tvNotiDsc = view.findViewById(R.id.tv_noti_dsc);
            tvNotiTime = view.findViewById(R.id.tv_noti_time);
        }
    }

    public interface NotificationClick{
        void onClick(int position, int diff);
    }
}
