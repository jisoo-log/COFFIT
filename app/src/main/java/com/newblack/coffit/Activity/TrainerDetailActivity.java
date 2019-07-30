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
import android.widget.TextView;

import com.newblack.coffit.R;
import com.newblack.coffit.Data.Trainer;

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
    Button btn_apply;
    Toolbar toolbar;
    Bitmap bitmap;

    //선택받은 트레이너 서버에서 정보 받아옴
    Trainer selectedTrainer;


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
        btn_apply = findViewById(R.id.btn_apply);

        Intent intent = getIntent();
        trainerId = intent.getIntExtra("trainerId",-1);
        if (trainerId < 0){
            tv_summary.setText("해당 트레이너가 없습니다");
        }
        else {
            tv_summary.setText(trainerId+"번 사람이 선택됐습니다");
            //여기서 해당 트레이너 정보 서버에서 받아올 수 있도록 하...자?
        }

        toolbar.setTitle(trainerId +"번 트레이너!!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //임시 데이터
        tv_introDetail.setText("안녕하세요! 여러분을 득근득근하게 만들어주는 트레이너\n" +
                "배지훈입니다. 제 특기를 간단하게 세 가지로 말씀드리죠\n" +
                "\n" +
                "- 1달만에 팔뚝살 평범하게 만들어주기\n" +
                "- 삼중턱을 과일칼턱으로 만들기\n" +
                "- 코딩은 없는 세상은 살 수 없어!!\n" +
                "\n" +
                "후회하지 않으실겁니다.");
        tv_careerDetail.setText("- 2010 탄생\n" +
                "- 2018 소마 초등학교 입학\n" +
                "- 2020 소마 인증");

//
//        //사진 넣는 쓰레드 임시 작성
//        Thread mThread = new Thread(){
//            @Override
//            public void run() {
//                try{
//                    URL url = new URL("https://coffit.s3.ap-northeast-2.amazonaws.com/1563369432246_%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-07-11%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%203.07.39.png");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setDoInput(true);
//                    conn.connect();
//
//                    InputStream is = conn.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(is);
//                } catch(Exception e){
//                    Log.d("TAG","problem in mThread : from url");
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        mThread.start();
//        try{
//            mThread.join(); //이게 없으면 메인 ㅅ화면 나오기 전에 작동이 안되는듯
//            iv_mainPic.setImageBitmap(bitmap);
//            iv_subPic1.setImageBitmap(bitmap);
//            iv_subPic2.setImageBitmap(bitmap);
//            iv_subPic3.setImageBitmap(bitmap);
//
//        } catch (Exception e){
//            Log.d("TAG" , "problem in mThread : after getting image");
//            e.printStackTrace();
//        }
//        //사진 넣기 테스트


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



    
}
