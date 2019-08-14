package com.newblack.coffit.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newblack.coffit.Activity.NotiActivity;
import com.newblack.coffit.Data.Noti;
import com.newblack.coffit.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.NotiHolder> {
    Context mContext;
    private List<Noti> notis = new ArrayList<>();
    SharedPreferences sp;

    public NotiAdapter(Context context){
        mContext = context;
    }

    //아이템 클릭을 처리하기 위한 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(View v, int positon);
    }

    private NotiAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(NotiAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public NotiAdapter.NotiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_noti, parent, false);
        return new NotiAdapter.NotiHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiAdapter.NotiHolder holder, int position) {
        Log.d("TAG", "timeAdapter onBindViewHolder : " + position);
        Noti curNoti = notis.get(position);
        //날짜 문제 해결 필요 -> 전부 스트링으로 받는 쪽으로!
//        SimpleDateFormat dayFormat = new SimpleDateFormat("MM월 dd일 hh:mm", Locale.getDefault());
//        String time = dayFormat.format(curNoti.getTime());
//        holder.tv_time.setText(time);
        holder.tv_noti.setText(curNoti.getContent());
    }

    @Override
    public int getItemCount() {
        return notis.size();
    }


    public void setNotis(List<Noti> notis) {
        this.notis = notis;
        notifyDataSetChanged();
    }

    //NotiHolder 클래스 부분
    class NotiHolder extends RecyclerView.ViewHolder {
        private TextView tv_username;
        private TextView tv_time;
        private TextView tv_noti;



        public NotiHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_noti = itemView.findViewById(R.id.tv_noti);

            sp = mContext.getSharedPreferences("coffit",Context.MODE_PRIVATE);
            tv_username.setText(sp.getString("trainer_name",""));

            itemView.setOnClickListener(new View.OnClickListener() {
                //노티 클릭 어떻게 할건지 구현해야함
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
