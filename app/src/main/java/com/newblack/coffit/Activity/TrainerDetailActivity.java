package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Data.Banner;
import com.newblack.coffit.R;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.Response.HomeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    Bitmap bitmap;

    //선택받은 트레이너 서버에서 정보 받아옴
    Trainer selectedTrainer;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG","TrainerDetailActivity onCreate");
        setContentView(R.layout.activity_trainer_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        trainerId = intent.getIntExtra("trainerId",-1);
        if (trainerId < 0){
            tv_summary.setText("해당 트레이너가 없습니다");
        }
        else {
//            tv_summary.setText(trainerId+"번 사람이 선택됐습니다");
            //여기서 해당 트레이너 정보 서버에서 받아올 수 있도록 하...자?
            retrofit_detail();
        }

//        toolbar.setTitle(trainerId +"번 트레이너!!");

//
//        //임시 데이터
//        tv_introDetail.setText("안녕하세요! 여러분을 득근득근하게 만들어주는 트레이너\n" +
//                "배지훈입니다. 제 특기를 간단하게 세 가지로 말씀드리죠\n" +
//                "\n" +
//                "- 1달만에 팔뚝살 평범하게 만들어주기\n" +
//                "- 삼중턱을 과일칼턱으로 만들기\n" +
//                "- 코딩은 없는 세상은 살 수 없어!!\n" +
//                "\n" +
//                "후회하지 않으실겁니다.");
//        tv_careerDetail.setText("- 2010 탄생\n" +
//                "- 2018 소마 초등학교 입학\n" +
//                "- 2020 소마 인증");


    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void clickApply(View view){
        Intent intent = new Intent(this, PayActivity.class);
        //위의 정보 그대 가져가도 되는건가? 그러면 클래스를 아예 넘겨주는 편이 낫지 않을까 싶네
        Log.d("TAG","TrainerDetailActivity : clickApply 진입");
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("selectedTrainer", selectedTrainer);
//        intent.putExtras(bundle);
        startActivity(intent);

    }


    public void retrofit_detail(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<Trainer> call = apiInterface.getTrainer(trainerId);
        call.enqueue(new Callback<Trainer>(){
            @Override
            public void onResponse(Call<Trainer> call, Response<Trainer> response){
                Log.d("TAG", "apiInterface callback onResponse");

                //트레이너 가지고 화면 세팅
                Trainer trainer = response.body();
                if(trainer != null) {
                    toolbar.setTitle(trainer.getUsername() + " 트레이너");
                    tv_summary.setText(trainer.getSummary());
                    tv_careerDetail.setText(trainer.getCareer());
                    tv_introDetail.setText(trainer.getDescription());
                    tv_username.setText(trainer.getUsername());
                    Log.d("TAG","retrofit trainer price is "+trainer.getPrice());
                    String price = trainer.getPrice()+"원";
                    tv_price.setText(price);
                    Picasso.get().load(trainer.getPictureURL()).into(iv_mainPic);
                    List<Trainer.ExtraPic> pics = trainer.getExtraPics();

                    //추가 사진도 recyclerview로 해야하나,,,?
                    //일단 넣긴 하는데 정말 거지같은 코드라고 느껴진다..
                    if (pics != null) {
                        for (int i = 1; i < pics.size() + 1; i++) {
                            switch (i) {
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
                        lo_subpic.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<Trainer> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });
    }





    
}
