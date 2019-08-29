package com.diss.cabadvertisement.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.MyCampaignsActivity;
import com.diss.cabadvertisement.ui.model.AddCompaignLocBean;
import com.diss.cabadvertisement.ui.model.CampaignBean;
import com.diss.cabadvertisement.ui.util.AppData;

import java.util.List;

public class MyCampaigListAdapter extends RecyclerView.Adapter<MyCampaigListAdapter.MyViewHolder> {
    private List<CampaignBean> list;
    private Context context;
    private CompainDetailClick compaignClick;
    AppData appdata;

    public MyCampaigListAdapter(Context context, List<CampaignBean> list, CompainDetailClick compaignClick) {
        this.context = context;
        this.list = list;
        this.compaignClick = compaignClick;
        appdata=new AppData(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_campaign, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvCampNm.setText(list.get(position).getC_name());
        holder.tvComAmount.setText("Rs. "+list.get(position).getPlan_amount());
//        holder.tvValidate.setText(list.get(position).get);
        holder.tvValidate.setText("( "+appdata.ConvertDate(list.get(0).getAdded_on())+" - "+appdata.ConvertDate01(list.get(0).getLastdate())+" )");//tvValidityDate
        holder.tvNoOfCab.setText(list.get(position).getNumber_of_cabs());

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
        TextView tvComAmount,tvValidate,tvNoOfCab,tvCampNm;
        LinearLayout lyMain;
        public MyViewHolder(View view) {
            super(view);
            tvCampNm = view.findViewById(R.id.tv_compaign_name);
            tvComAmount = view.findViewById(R.id.tv_compaign_amount);
            tvValidate = view.findViewById(R.id.tv_validity);
            tvNoOfCab = view.findViewById(R.id.tv_no_of_cab);
            lyMain = view.findViewById(R.id.ly_main);
        }
    }

    public interface CompainDetailClick{
        void onClick(int position, int diff);
    }
}
