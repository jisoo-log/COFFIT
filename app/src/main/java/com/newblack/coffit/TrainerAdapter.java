package com.newblack.coffit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrainerAdapter extends RecyclerView.Adapter {
    private List<Trainer> trainers = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trainer, parent, false);
        return new TrainerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerHolder holder, int position) {
        Trainer curTrainer = trainers.get(position);
        holder.tv_summary.setText(curTrainer.getSummary());
        holder.tv_username.setText(curTrainer.getUsername());
        holder.rating.setRating(curTrainer.getTotStar());
        holder.tv_rating.setText(curTrainer.getTotStar());
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
        private TextView tv_rating;
        private RatingBar rating;


        public TrainerHolder(@NonNull View itemView) {
            super(itemView);
            tv_summary = itemView.findViewById(R.id.tv_summary);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            iv_mainpic = itemView.findViewById(R.id.iv_mainpic);
            tv_username = itemView.findViewById(R.id.tv_username);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private List<Note> notes = new ArrayList<>();
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged(); //이거말고 데이터 추가 같은걸 감지하는 아이도 있음.. 나중에 더 효율적으로 바꿀것
    }

}

