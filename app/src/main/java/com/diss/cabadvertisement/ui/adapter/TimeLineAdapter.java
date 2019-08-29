package com.diss.cabadvertisement.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.TimeLineModel;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<TimeLineModel> timeLineModelList;
    public Context context;
    public CompaignDetailClick compaignClick;
//    TimeLineAdapter.OnItemClickList onItemClickList;
public TimeLineAdapter(Context context, List<TimeLineModel> timeLineModelList, CompaignDetailClick planClick) {
        this.timeLineModelList = timeLineModelList;
        this.context = context;
        this.compaignClick = planClick;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(view, viewType);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ((ViewHolder) holder).textView.setText(timeLineModelList.get(position).getName());
        if (timeLineModelList.get(position).getStatus().equals("inactive")){

            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(R.drawable.time_line_blanck_button));
        }
        else {
            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(R.drawable.timeline_button));
        }

        ((ViewHolder) holder).lyMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compaignClick.CompaignOnClick(position,1);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return timeLineModelList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView textView;
        LinearLayout lyMain;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            timelineView = itemView.findViewById(R.id.timeline_id);
            textView = itemView.findViewById(R.id.text_id);
            lyMain = itemView.findViewById(R.id.ly_main);
            timelineView.initLine(viewType);
        }
    }
    public interface CompaignDetailClick{
        void CompaignOnClick(int position, int diff);
    }
    /*public  interface OnItemClickList {
         void  onItemClick(int position);
    }

    public void setOnItemClickList(TimeLineAdapter.OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }*/
}