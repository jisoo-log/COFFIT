package com.newblack.coffit.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.DateUtils;
import com.newblack.coffit.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {
    Context mContext;
    private List<Schedule> schedules = new ArrayList<>();


    //아이템 클릭을 처리하기 위한 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(View v, int positon);
    }

    private ScheduleAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(ScheduleAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ScheduleAdapter.ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ScheduleAdapter.ScheduleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleHolder holder, int position) {
        Log.d("TAG", "timeAdapter onBindViewHolder : " + position);
        //현재 회차를 제대로 셀 수 없음
        Schedule curSchedule = schedules.get(position);
        int hh = DateUtils.stringToDate(curSchedule.getDate()).getHours();
        int mm = DateUtils.stringToDate(curSchedule.getDate()).getMinutes();
        String time1 = hh + ":" + mm;
        String time2;
        if(mm==00){
            time2 = hh + ":30";
        }
        else{
            time2 = (hh+1) + ":00";
        }
        String time = time1 + " ~ "+time2;
        holder.tv_time.setText(time);
        int ptNum = 0;
        for(Schedule schedule : schedules){
            //이번 pt가 몇번째인지 카운트 하는 용도. 나중엔 다른 방식이 나을듯
            if(schedule.getState()==1 || schedule.getState()==4){
                ptNum++;
            }
            Log.d("TAG","this pt num is "+ptNum);
        }
        String ptNumInfo = ptNum +"회차 PT";
        holder.tv_ptnum.setText(ptNumInfo);
        holder.tv_state.setText(curSchedule.stateText().get(0));

    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }


    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    //ScheduleHolder 클래스 부분
    class ScheduleHolder extends RecyclerView.ViewHolder {
        private TextView tv_ptnum;
        private TextView tv_time;
        private TextView tv_state;


        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            tv_ptnum = itemView.findViewById(R.id.tv_ptnum);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_state = itemView.findViewById(R.id.tv_state);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            //위치와 id까지 한번에 가져올 수 있도록
                            mListener.onItemClick(v, position);
                        }
                    }

                    Log.d("test", "position = "+ getAdapterPosition());
                }
            });
        }
    }
}