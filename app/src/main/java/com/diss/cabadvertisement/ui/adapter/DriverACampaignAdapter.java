package com.diss.cabadvertisement.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.CampaignBean;
import com.diss.cabadvertisement.ui.model.DriverCampaignBean;
import com.diss.cabadvertisement.ui.util.AppData;

import java.util.List;

public class DriverACampaignAdapter extends RecyclerView.Adapter<DriverACampaignAdapter.MyViewHolder> {
    private List<DriverCampaignBean> list;
    private Context context;
    private CompainDetailClick compaignClick;
    AppData appdata;

    public DriverACampaignAdapter(Context context, List<DriverCampaignBean> list, CompainDetailClick compaignClick) {
        this.context = context;
        this.list = list;
        this.compaignClick = compaignClick;
        appdata=new AppData(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_driver_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvDriverId.setText("# "+list.get(position).getDriver_id());

        holder.lyMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compaignClick.onClick(position,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView /*tvComAmount,tvValidate,tvNoOfCab,*/tvDriverId;
        LinearLayout lyMain;
        public MyViewHolder(View view) {
            super(view);
            tvDriverId = view.findViewById(R.id.tv_driver_id);
            lyMain = view.findViewById(R.id.ly_main_driver);
        }
    }

    public interface CompainDetailClick{
        void onClick(int position, int diff);
    }
}
