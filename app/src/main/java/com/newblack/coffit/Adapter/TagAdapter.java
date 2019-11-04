package com.newblack.coffit.Adapter;


import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newblack.coffit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagHolder> {
    private List<String> times = new ArrayList<>();
    private Bitmap thumbnail;

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
    public TagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tag, parent, false);
        return new TagHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TagHolder holder, int position) {
        Log.d("TAG","timeAdapter onBindViewHolder : " + position);
        String curTime = times.get(position);
        holder.tv_tag.setText(curTime);
        try {
            holder.iv_tag.setImageBitmap(thumbnail);
        } catch(Exception e){
            e.printStackTrace();
            Log.d("TAG", "no thumbnail");
        }
        holder.tv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onItemClick(holder.tv_tag,curTime);
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


    public void setTimes(List<String> times, Bitmap thumbnail){
        Log.d("TAG","TagAdapter List size : "+ times.size());
        this.times = times;
        this.thumbnail = thumbnail;
        notifyDataSetChanged();
    }

    //timeHolder 클래스 부분
    class TagHolder extends RecyclerView.ViewHolder{
        private TextView tv_tag;
        private ImageView iv_tag;


        public TagHolder(@NonNull View itemView) {
            super(itemView);
            tv_tag = itemView.findViewById(R.id.tv_tag_time);
            iv_tag = itemView.findViewById(R.id.iv_tag_image);

        }
    }
}

