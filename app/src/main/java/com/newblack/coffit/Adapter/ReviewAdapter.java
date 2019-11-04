package com.newblack.coffit.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newblack.coffit.Data.Review;
import com.newblack.coffit.DateUtils;
import com.newblack.coffit.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>{

    private List<Review> reviews = new ArrayList<>();


    @NonNull
    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewAdapter.ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewHolder holder, int position) {
        Log.d("TAG", "timeAdapter onBindViewHolder : " + position);
        Review curReview = reviews.get(position);
        holder.tv_title.setText(curReview.getTitle());
        holder.tv_contents.setText(curReview.getContents());
        holder.rating.setRating(curReview.getStar());

        holder.tv_date.setText(curReview.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    //NotiHolder 클래스 부분
    class ReviewHolder extends RecyclerView.ViewHolder {
//        private TextView tv_name;
        private TextView tv_date;
        private TextView tv_contents;
        private TextView tv_title;
        private RatingBar rating;
        


        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
//            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_contents = itemView.findViewById(R.id.tv_body);
            tv_title = itemView.findViewById(R.id.tv_title);
            rating = itemView.findViewById(R.id.rating);

        }
    }
}

