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
import java.util.Date;
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
        Log.d("TAG", "scheduleAdapter onBindViewHolder : " + position);
        //현재 회차를 제대로 셀 수 없
        Schedule curSchedule = schedules.get(position);
        Date date = curSchedule.getDate();
        String newDate = DateUtils.fromServerTime(date);
        int hh = DateUtils.getValueFromDate(newDate,DateUtils.HOUR);
        int mm = DateUtils.getValueFromDate(newDate,DateUtils.MINUTE);
        String time1, time2;
        if(mm==0){
            time1 = hh + ":00";
            time2 = hh + ":30";
        }
        else{
            time1 = hh + ":" + mm;
            time2 = (hh+1) + ":00";
        }
        String time = time1 + " ~ "+time2;
        holder.tv_time.setText(time);
        int ptNum = curSchedule.getPTId();

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
        //TODO : need to add count
        private TextView tv_ptnum;
        private TextView tv_time;
        private TextView tv_state;


        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            tv_ptnum = itemView.findViewById(R.id.tv_pt_num);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_state = itemView.findViewById(R.id.tv_state);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.d("test", "position = "+ getAdapterPosition());
                    if(position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            //위치와 id까지 한번에 가져올 수 있도록
                            Log.d("TAG", "클릭 위치 : "+position);
                            mListener.onItemClick(v, position);

                        }
                    }
                }
            });
        }
    }
}