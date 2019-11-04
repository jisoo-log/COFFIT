package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Data.PT;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.H_FRAGMENT;
import static com.newblack.coffit.Activity.MainActivity.myId;

public class PayActivity extends AppCompatActivity {
    Toolbar toolbar;

    TextView tv_count;
    TextView tv_summary;
    TextView tv_username;
    TextView tv_price;
    CheckBox checkBox;
    Button payBtn;
    RatingBar rating;
    ImageView iv_mainPic;

    int ptNum;
    Trainer trainer;
    APIInterface apiInterface;
    Activity activity;
    Date startDt;
    Date endDt;

    //나중에 SharedPreference로 계정 정보 가지고 있을 것.
    //지금은 일단 임시로 넣어 놓겠음
    SharedPreferences sp;
    int studentId = myId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        activity = this;
        sp = getSharedPreferences("coffit",MODE_PRIVATE);
//        studentId = sp.getInt("studentId",1);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("PT 결제");
        checkBox = findViewById(R.id.checkbox);
        payBtn = findViewById(R.id.btn_pay);
        tv_count = findViewById(R.id.tv_count);
        tv_summary = findViewById(R.id.tv_summary);
        tv_username = findViewById(R.id.tv_username);
        tv_price = findViewById(R.id.tv_price);
        rating = findViewById(R.id.rating);
        iv_mainPic =findViewById(R.id.iv_mainpic);

        payBtn.setEnabled(false);
        ptNum=4;
        tv_count.setText(String.valueOf(ptNum));

        Calendar cal = Calendar.getInstance();
        startDt = cal.getTime();
        cal.add(Calendar.MONTH,1);
        endDt = cal.getTime();

        //받아온 트레이너로 화면 초기화
        Intent intent = getIntent();
        trainer = (Trainer)intent.getSerializableExtra("Trainer");
        if(trainer != null) {
            tv_summary.setText(trainer.getSummary());
            tv_username.setText(trainer.getUsername());
            Log.d("TAG", "retrofit trainer price is " + trainer.getPrice());
            String price = trainer.getPrice() + "원";
            tv_price.setText(price);
            Picasso.get().load(trainer.getPictureURL()).into(iv_mainPic);
            rating.setRating(trainer.getRate());
        }




        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    Log.d("TAG","checkBox Checked!");
                    payBtn.setEnabled(true);
                    payBtn.setBackgroundColor(Color.parseColor("#424242"));
                } else {
                    Log.d("TAG","checkBox UnChecked!");
                    payBtn.setEnabled(false);
                    payBtn.setBackgroundColor(Color.parseColor("#c9e9ff"));
                }
            }
        });


    }

    @Override
    public void finish() {
        super.finish();

    }

    public void clickPay(View view){
        //결제화면 나중에 붙이기
        HashMap<String, Object> pt = new HashMap<>();
        pt.put("studentId",studentId);
        pt.put("state",1);
        pt.put("price",trainer.getPrice());
        pt.put("total_number",ptNum);
        pt.put("rest_number",ptNum);
        pt.put("trainerId",trainer.getId());
        pt.put("start_date", startDt);
        pt.put("end_date",endDt);

        retrofit_postPT(pt);
    }


    public void plus(View view){
        ptNum++;
        tv_count.setText(String.valueOf(ptNum));
    }

    public void minus(View view){
        if(ptNum == 4){
            Toast.makeText(this,"수업 최소 단위는 4회입니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            ptNum--;
            tv_count.setText(String.valueOf(ptNum));
        }

    }


    public void retrofit_postPT(HashMap<String,Object> pt){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiInterface.postPT(pt).enqueue(new Callback<PT>() {
            @Override
            public void onResponse(@NonNull Call<PT> call, @NonNull Response<PT> response) {
                Toast.makeText(activity,"결제가 완료되었습니다",Toast.LENGTH_SHORT).show();

                //sp에 저장
                SharedPreferences.Editor editor = sp.edit();
                String name = trainer.getUsername();
                String pic = trainer.getPictureURL();
                String summary = trainer.getSummary();
                editor.putInt("trainer_id",trainer.getId());
                editor.putString("trainer_name",name);
                editor.putString("trainer_pic",pic);
                editor.putString("summary",summary);
                sp = getSharedPreferences("coffit",MODE_PRIVATE);
                editor.putBoolean("hasPT",true);
                editor.commit();


                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<PT> call, Throwable t) {
                Log.d("TAG", "통신 실패");
                Toast.makeText(activity, "통신 오류 발생",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
