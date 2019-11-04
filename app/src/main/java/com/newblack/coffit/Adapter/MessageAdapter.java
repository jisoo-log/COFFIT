package com.newblack.coffit.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.newblack.coffit.Data.Message;
import com.newblack.coffit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> mMessages = new ArrayList<>();
    private final int SELF_VIEW = 1;
    private final int RECEIVED_VIEW = 2;

    public MessageAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflator = LayoutInflater.from(parent.getContext());
        View view = null;
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_chatroom, parent, false);

        switch (viewType){
            case SELF_VIEW :
                view = inflator.inflate(R.layout.item_chat, parent, false);
                return new SelfMessageHolder(view);
            case RECEIVED_VIEW :
                view = inflator.inflate(R.layout.item_chat_received, parent, false);
                return new ReceivedMessageHolder(view);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position){
        Message curMsg = mMessages.get(position);
        String type = curMsg.getType();
        if("student".equals(type)){
            return SELF_VIEW;
        }
        else return RECEIVED_VIEW;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Message msg = mMessages.get(position);

        if(viewType == SELF_VIEW){
            SelfMessageHolder selfHolder = (SelfMessageHolder) holder;
            selfHolder.tv_body.setText(msg.getMessage());
            selfHolder.tv_time.setText(msg.getTime());
        }
        else if(viewType == RECEIVED_VIEW){
            ReceivedMessageHolder receiveHolder = (ReceivedMessageHolder) holder;
            receiveHolder.tv_body.setText(msg.getMessage());
            receiveHolder.tv_time.setText(msg.getTime());
            receiveHolder.tv_name.setText(msg.getUsername());
            Picasso.get().load(msg.getPic()).into(receiveHolder.iv_profile);
        }
        else{
            Log.d("TAG","has wrong view type");
        }

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void addItems(Message msg){
        this.mMessages.add(msg);
        notifyDataSetChanged();
    }

    public void setMessages(List<Message> msgList){
        this.mMessages = msgList;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView){
            super(itemView);
        }
    }


    public class SelfMessageHolder extends ViewHolder {
        private TextView tv_body;
        private TextView tv_time;

        public SelfMessageHolder(View itemView) {
            super(itemView);
            tv_body = itemView.findViewById(R.id.tv_body);
            tv_time = itemView.findViewById(R.id.tv_time);
        }

    }

    public class ReceivedMessageHolder extends ViewHolder {
        private TextView tv_name;
        private TextView tv_body;
        private TextView tv_time;
        private CircleImageView iv_profile;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_title);
            tv_body = itemView.findViewById(R.id.tv_body);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_profile = itemView.findViewById(R.id.iv_profile);
        }

    }

}
