package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Data.ChatRoom;
import com.newblack.coffit.R;
import com.newblack.coffit.Data.Trainer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.PTing;
import static com.newblack.coffit.Activity.MainActivity.myId;

public class TrainerDetailActivity extends AppCompatActivity {
    int trainerId;
    TextView tv_summary;
    TextView tv_username;
    ImageView iv_mainPic;
    ImageView iv_subPic1;
    ImageView iv_subPic2;
    ImageView iv_subPic3;
    TextView tv_careerDetail;
    TextView tv_introDetail;
    TextView tv_price;
    Button btn_apply;
    Toolbar toolbar;
    LinearLayout lo_subpic;
    RatingBar rating;
    TextView tv_rate;

    //선택받은 트레이너 서버에서 정보 받아옴
    APIInterface apiInterface;
    Trainer trainer;
    int room_id;
    Activity activity;
    int student_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG","TrainerDetailActivity onCreate");
        setContentView(R.layout.activity_trainer_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        student_id=myId;

        tv_summary = findViewById(R.id.tv_summary);
        tv_username = findViewById(R.id.tv_username);
        tv_careerDetail = findViewById(R.id.tv_career_detail);
        tv_introDetail = findViewById(R.id.tv_introduce_detail);
        iv_mainPic = findViewById(R.id.iv_mainpic);
        iv_subPic1 = findViewById(R.id.iv_subpic1);
        iv_subPic2 = findViewById(R.id.iv_subpic2);
        iv_subPic3 = findViewById(R.id.iv_subpic3);
        tv_price = findViewById(R.id.tv_price);
        lo_subpic = findViewById(R.id.lo_subpic);
        btn_apply = findViewById(R.id.btn_apply);
        rating = findViewById(R.id.rating);
        tv_rate  = findViewById(R.id.tv_rate);

        if(PTing){
            btn_apply.setBackgroundColor(Color.parseColor("#c9e9ff"));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        trainerId = intent.getIntExtra("trainerId",-1);
        if (trainerId < 0){
            tv_summary.setText("해당 트레이너가 없습니다");
        }
        else {
            retrofit_detail();
        }


    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void clickApply(View view) {
        if (!PTing) {
            Intent intent = new Intent(this, PayActivity.class);
            Log.d("TAG", "TrainerDetailActivity : clickApply");
            intent.putExtra("Trainer", trainer);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "이미 수강중인 PT가 있습니다", Toast.LENGTH_SHORT).show();
        }
    }

    public void goReview(View view){
        Intent intent = new Intent(this, ReviewActivity.class);
        Log.d("TAG","TrainerDetailActivity : goReview");
        intent.putExtra("trainer_id",trainerId);
        intent.putExtra("trainer_name",trainer.getUsername());
        startActivity(intent);
    }

    public void clickAsk(View view){
        retrofit_getRoom(student_id);
    }


    public void retrofit_detail(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<Trainer> call = apiInterface.getTrainer(trainerId);
        call.enqueue(new Callback<Trainer>(){
            @Override
            public void onResponse(Call<Trainer> call, Response<Trainer> response){
                Log.d("TAG", "apiInterface callback onResponse");

                //트레이너 가지고 화면 세팅
                trainer = response.body();
                if(trainer != null) {
                    toolbar.setTitle(trainer.getUsername() + " 트레이너");
                    tv_summary.setText(trainer.getSummary());
                    tv_careerDetail.setText(trainer.getCareer());
                    tv_introDetail.setText(trainer.getDescription());
                    tv_username.setText(trainer.getUsername());
                    Log.d("TAG","retrofit trainer price is "+trainer.getPrice());
                    String price = trainer.getPrice()+"원";
                    tv_price.setText(price);

                    tv_rate.setText(String.valueOf(Math.round(trainer.getRate()*10)/10.0));
                    rating.setRating(trainer.getRate());

                    Picasso.get().load(trainer.getPictureURL()).into(iv_mainPic);
                    List<Trainer.ExtraPic> pics = trainer.getExtraPics();

                    //추가 사진도 recyclerview로 해야하나,,,?
                    //일단 넣긴 하는데 정말 거지같은 코드라고 느껴진다..
                    if (pics != null) {
                        for (int i = 0; i < pics.size() + 1; i++) {
                            switch (i) {
                                case 0:
                                    lo_subpic.setVisibility(View.GONE);
                                    break;
                                case 1:
                                    Picasso.get().load(pics.get(i - 1).getUrl()).into(iv_subPic1);
                                    break;
                                case 2:
                                    Picasso.get().load(pics.get(i - 1).getUrl()).into(iv_subPic2);
                                    break;
                                case 3:
                                    Picasso.get().load(pics.get(i - 1).getUrl()).into(iv_subPic3);
                                    break;
                            }
                        }
                    }
                    else{
                        Log.d("TAG","retrofit_detail : no pics!!");
                    }
                }

            }

            @Override
            public void onFailure(Call<Trainer> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });
    }


    public void retrofit_getRoom(int student_id){
        //TODO : 속한 채팅방을 전부 불러옴
        Log.d("TAG", "retrofit_GetRoom");
        List<ChatRoom> roomList = new ArrayList<>();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<ChatRoom>> call = apiInterface.getRoomList(student_id);
        call.enqueue(new Callback<List<ChatRoom>>(){
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                List<ChatRoom> rooms = response.body();

                room_id=-1;
                if(rooms!=null) {
                    roomList.addAll(rooms);
                    for(ChatRoom room : roomList){
                        if(room.getStudent_id()==student_id){
                            room_id = room.getId();
                            break;
                        }
                    }
                }

                Intent intent = new Intent(activity, ChatActivity.class);
                Log.d("TAG","TrainerDetailActivity : clickAsk");
                intent.putExtra("from","detail");
                intent.putExtra("trainer_id",trainerId);
                intent.putExtra("trainer_name",trainer.getUsername());
                intent.putExtra("trainer_pic",trainer.getPictureURL());
                if(room_id!=-1) intent.putExtra("room_id",room_id);

                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
            }
        });
    }





    
}
