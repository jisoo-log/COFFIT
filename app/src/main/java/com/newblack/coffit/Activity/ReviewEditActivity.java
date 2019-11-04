package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.myId;

public class ReviewEditActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText et_title;
    EditText et_content;
    RatingBar rating;

    int trainer_id;
    APIInterface apiInterface; 
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_edit);

        Intent intent = getIntent();
        trainer_id = intent.getIntExtra("trainer_id",-1);
        if(trainer_id == -1){
            Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_LONG).show();
            finish();
        }
        toolbar = findViewById(R.id.toolbar);
        et_content = findViewById(R.id.et_content);
        et_title = findViewById(R.id.et_title);
        rating = findViewById(R.id.rating);
        toolbar.setTitle("PT 후기 입력");
    }

    public void saveReview(View view){
        if(et_title.getText().equals("")){
            Toast.makeText(this,"제목을 입력해주세요",Toast.LENGTH_LONG).show();
            return;
        }
        HashMap<String, Object> review = new HashMap<>();
        review.put("trainer_id",trainer_id);
        review.put("student_id",myId);
        review.put("title", et_title.getText().toString());
        review.put("contents", et_content.getText().toString());
        review.put("star",(int)rating.getRating());
        retrofit_review(review);

    }

    public void retrofit_review(HashMap<String, Object> review){

        //TODO : post 준비과정. 여기서 영상format을 보내줘야함
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Void> call = apiInterface.postReview(review);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("TAG","retrofit post review 수행을 완료했습니다.");

                }
                else{
                    Log.d("TAG","retrofit post review onResponse의 response에 문제가 있습니다.");
                }
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG","retrofit post review failed.");
            }
        });
    }


}
