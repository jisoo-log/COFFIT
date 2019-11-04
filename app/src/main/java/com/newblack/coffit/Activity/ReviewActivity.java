package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Adapter.ReviewAdapter;
import com.newblack.coffit.Data.Review;
import com.newblack.coffit.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    int trainer_id;
    String trainer_name;
    RecyclerView recyclerView;
    ReviewAdapter reviewAdapter;
    List<Review> reviewList;
    Toolbar toolbar;
    APIInterface apiInterface;
    TextView tv_no_review;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent intent = getIntent();
        trainer_id = intent.getIntExtra("trainer_id",-1);
        trainer_name = intent.getStringExtra("trainer_name");
        if(trainer_id == -1){
            Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_LONG).show();
            finish();
        }

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_review);
        tv_no_review = findViewById(R.id.tv_no_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);
        reviewAdapter = new ReviewAdapter();
        recyclerView.setAdapter(reviewAdapter);
        reviewList = new ArrayList<>();

        String title = trainer_name + " 트레이너와의 PT후기";
        toolbar.setTitle(title);

    }

    @Override
    protected void onStart() {
        super.onStart();
        retrofit_review();
    }

    public void write_review(View view){
        Intent intent = new Intent(this, ReviewEditActivity.class);
        intent.putExtra("trainer_id",trainer_id);
        startActivity(intent);
    }


    public void retrofit_review(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Review>> call = apiInterface.getReviewList(trainer_id);
        call.enqueue(new Callback<List<Review>>(){
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                reviewList = response.body();
                if(reviewList != null){
                    reviewAdapter.setReviews(reviewList);
                }
                else{
                    recyclerView.setVisibility(View.GONE);
                    tv_no_review.setVisibility(View.VISIBLE);
                    Log.d("TAG", "no review");
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });
    }



}
