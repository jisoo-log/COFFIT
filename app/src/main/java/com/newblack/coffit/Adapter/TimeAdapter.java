package com.newblack.coffit.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newblack.coffit.R;

import java.util.ArrayList;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeHolder> {
    Context mContext;
    private List<String> times = new ArrayList<>();


    //아이템 클릭을 처리하기 위한 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(View v, String time) ;
    }

    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }


    @NonNull
    @Override
    public TimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time, parent, false);
        return new TimeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeHolder holder, int position) {
        Log.d("TAG","timeAdapter onBindViewHolder : " + position);
        String curTime = times.get(position);
        holder.tv_time.setText(curTime);
        holder.tv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onItemClick(holder.tv_time,times.get(position));
                }
                else{
                    Log.d("TAG","already mListener made");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return times.size();
    }


    public void setTimes(List<String> times){
        this.times = times;
        notifyDataSetChanged();
    }

    //timeHolder 클래스 부분
    class TimeHolder extends RecyclerView.ViewHolder{
        private TextView tv_time;
        private TextView tv_request;


        public TimeHolder(@NonNull View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_request = itemView.findViewById(R.id.tv_request);



//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if(position != RecyclerView.NO_POSITION) {
//                        if (mListener != null) {
//                            //위치와 id까지 한번에 가져올 수 있도록
//                            mListener.onItemClick(v, position, times.get(position).getId());
//                        }
//                    }
//
//                    Log.d("test", "position = "+ getAdapterPosition());
//                }
//            });

        }
    }
}


