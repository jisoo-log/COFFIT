package com.newblack.coffit.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrainerAdapter extends RecyclerView.Adapter<TrainerAdapter.TrainerHolder> {
    Context mContext;
    private List<Trainer> trainers = new ArrayList<>();


    //아이템 클릭을 처리하기 위한 인터페이스 정의
   public interface OnItemClickListener {
        void onItemClick(View v, int position, int id) ;
    }

    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }


    @NonNull
    @Override
    public TrainerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trainer, parent, false);
        return new TrainerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerHolder holder, int position) {
        Log.d("TAG","TrainerAdapter onBindViewHolder : " + position);
        Trainer curTrainer = trainers.get(position);
        holder.tv_summary.setText(curTrainer.getSummary());
        holder.tv_username.setText(curTrainer.getUsername());
//        holder.rating.setRating(curTrainer.getStar());
        //holder.tv_numreview.setText(curTrainer.getNumReview());
        Picasso.get().load(curTrainer.getPictureURL()).into(holder.iv_mainpic);

    }

    @Override
    public int getItemCount() {
        return trainers.size();
    }


    public void setTrainers(List<Trainer> trainers){
        this.trainers = trainers;
        notifyDataSetChanged();
    }

    class TrainerHolder extends RecyclerView.ViewHolder{
        private ImageView iv_mainpic;
        private TextView tv_summary;
        private TextView tv_username;
        private TextView tv_numreview;
        private RatingBar rating;

        TrainerHolder(@NonNull View itemView) {
            super(itemView);
            tv_summary = itemView.findViewById(R.id.tv_summary);
            tv_numreview = itemView.findViewById(R.id.tv_numreview);
            iv_mainpic = itemView.findViewById(R.id.iv_mainpic);
            tv_username = itemView.findViewById(R.id.tv_username);
            rating = itemView.findViewById(R.id.rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            //위치와 id까지 한번에 가져올 수 있도록
                            mListener.onItemClick(v, position, trainers.get(position).getId());
                        }
                    }

                }
            });

        }
    }
}


