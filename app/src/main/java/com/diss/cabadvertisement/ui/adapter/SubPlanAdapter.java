package com.diss.cabadvertisement.ui.adapter;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.SubPlanBean;
import java.util.List;
public class SubPlanAdapter extends RecyclerView.Adapter<SubPlanAdapter.MyViewHolder> {
    private List<SubPlanBean> list;
    private Context context;
    private PlanClick planClick;

    public SubPlanAdapter(Context context, List<SubPlanBean> list, PlanClick planClick) {
        this.context = context;
        this.list = list;
        this.planClick = planClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sub_plan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvPlanNm.setText(list.get(position).getPlan_name());
        holder.tvPlanTime.setText(list.get(position).getPlan_days());
        holder.tvNoOfCab.setText(list.get(position).getNumber_of_cabs());
       int pos=position%3;
       switch (pos)
       {
           case 0:
               holder.rlMain.setBackgroundResource(R.drawable.card_first_background);
               break;
           case 1:
               holder.rlMain.setBackgroundResource(R.drawable.card_second_background);
               break;
           case 2:
               holder.rlMain.setBackgroundResource(R.drawable.card_third_background);
               break;
          default:
              holder.rlMain.setBackgroundResource(R.drawable.card_first_background);
                   break;
       }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlanNm,tvPlanTime,tvNoOfCab;
        RelativeLayout rlMain;
        public MyViewHolder(View view) {
            super(view);
            tvPlanNm = view.findViewById(R.id.tv_plan_nm);
            tvPlanTime = view.findViewById(R.id.tv_plan_time);
            tvNoOfCab = view.findViewById(R.id.tv_number_of_cab);
            rlMain = view.findViewById(R.id.classic_relative_id);

        }
    }

    public interface PlanClick{
        void onClick(int position);
    }
}
