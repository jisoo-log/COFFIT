package com.newblack.coffit.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newblack.coffit.Data.ChatRoom;
import com.newblack.coffit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomHolder> {
    private List<ChatRoom> roomList = new ArrayList<>();

    //아이템 클릭을 처리하기 위한 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(View v, int positon);
    }

    private ChatRoomAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(ChatRoomAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ChatRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chatroom, parent, false);
        return new ChatRoomAdapter.ChatRoomHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomHolder holder, int position) {
        Log.d("TAG", "ChatRoomAdapter onBindViewHolder : " + position);
        ChatRoom curRoom = roomList.get(position);
        if(curRoom.getNot_seen()>0){
            holder.tv_new_msg.setVisibility(View.VISIBLE);
        }
        holder.tv_name.setText(curRoom.getTrainer_name());
        holder.tv_body.setText(curRoom.getMessage());
        holder.tv_new_msg.setText(String.valueOf(curRoom.getNot_seen()));
        holder.tv_time.setText(curRoom.getTime());
        Picasso.get().load(curRoom.getTrainer_pic()).into(holder.iv_profile);
    }

    public void setRoomList(List<ChatRoom> list){
        this.roomList = list;
        notifyDataSetChanged();
    }

    class ChatRoomHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView tv_body;
        private TextView tv_new_msg;
        private TextView tv_time;
        private CircleImageView iv_profile;


        public ChatRoomHolder(@NonNull View itemView){
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_title);
            tv_body = itemView.findViewById(R.id.tv_body);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_new_msg = itemView.findViewById(R.id.tv_new_msg);
            iv_profile = itemView.findViewById(R.id.iv_profile);

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
