package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.DateUtils;
import com.newblack.coffit.R;

import org.w3c.dom.Text;

import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.DateUtils.dateObject;

public class ScheduleDialogActivity extends Activity {
    Schedule schedule;
    APIInterface apiInterface;
    Activity activity;
//    SharedPreferences sp;
    //요청들 넣기

    LinearLayout cv_btn;
    TextView tv_left;
    TextView tv_right;
    TextView tv_username;
    TextView tv_starttime;
    TextView tv_endtime;
    TextView tv_noti;
    TextView tv_content;

    int student_id;
    int trainer_id;
    int pt_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule_dialog);
        activity= this;
//        sp = getSharedPreferences("coffit",MODE_PRIVATE);

        //TODO : Need to consider dialog come from noti later
        Intent intent = getIntent();
        schedule = (Schedule) intent.getSerializableExtra("schedule");
        student_id = schedule.getStudentId();
        trainer_id = schedule.getTrainerId();

//        trainer_id = intent.getIntExtra("trainer_id",-1);
        pt_id = intent.getIntExtra("pt_id",-1);
        cv_btn = findViewById(R.id.ll_button);
        tv_left = findViewById(R.id.tv_left);
        tv_right = findViewById(R.id.tv_right);
        tv_username = findViewById(R.id.tv_username);
        tv_starttime = findViewById(R.id.tv_starttime);
        tv_endtime = findViewById(R.id.tv_endtime);
        tv_noti = findViewById(R.id.tv_noti);
        tv_content = findViewById(R.id.tv_content);

        Log.d("TAG","id :" + schedule.getId() + "state : " + schedule.getState());

//        String user = sp.getString("trainer_name","")+ " 트레이너";
        tv_username.setText("아래 시간에 PT를 진행합니다");
        tv_starttime.setText(schedule.getStartEndTime(0));
        tv_endtime.setText(schedule.getStartEndTime(1));
        tv_noti.setVisibility(View.INVISIBLE);

        tv_content.setText(schedule.stateText().get(1));

        switch(schedule.getState()){
            case 0:
                if(schedule.stateText().get(0).equals("확정 필요")){
                    tv_left.setText("일정 거절");
                    tv_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            schedule.setState(3);
                            retrofit_edit(schedule, 3);
                        }
                    });

                    tv_right.setText("일정 승인");
                    tv_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            schedule.setState(1);
                            retrofit_edit(schedule,1);
                        }
                    });

                }
                else{
                    tv_left.setVisibility(View.INVISIBLE);
                    tv_right.setText("요청 삭제");
                    tv_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            retrofit_delete();
                        }
                    });
                }
                break;
            case 1:
                tv_left.setText("일정 삭제");
                tv_left.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        retrofit_delete();
                    }
                });
                tv_right.setText("일정 변경");
                tv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //변경요청이라는 플래그, 기존 날짜, 기존 id 함께 넘김
                        Intent intent1 = new Intent(activity, AddScheduleActivity.class);
                        intent1.putExtra("edit",1);
                        String date = dateObject(schedule.getDate());
                        intent1.putExtra("date",date);
                        intent1.putExtra("pastId",schedule.getId());
                        intent1.putExtra("trainer_id",trainer_id);
                        intent1.putExtra("pt_id",pt_id);
                        startActivity(intent1);
                        finish();
                    }
                });
                break;
            case 2:
                cv_btn.setVisibility(View.INVISIBLE);
                break;
            case 4:
                cv_btn.setVisibility(View.INVISIBLE);
                break;
        }
    }




    public void retrofit_edit(Schedule schedule, int state){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("state",state);
        jsonObject.addProperty("student_id",student_id);
        jsonObject.addProperty("trainer_id",trainer_id);
        jsonObject.addProperty("past_schedule_id",schedule.getPastId());
        jsonObject.addProperty("trainer_schedule_id",schedule.getTs_id());

        Call<JsonObject> call = apiInterface.putSchedule(jsonObject, schedule.getId());
        call.enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response){
                if(response.isSuccessful()){
                    Log.d("TAG","retrofit edit 수행을 완료했습니다.");
                }
                else{
                    Log.d("TAG","retrofit edit onResponse의 response에 문제가 있습니다.");
                }
                finish();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });
    }

    public void retrofit_delete(){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.deleteSchedule(schedule.getId());
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                if(response.isSuccessful()){
                    Log.d("TAG","retrofit delete 수행을 완료했습니다.");
                    Toast.makeText(activity, "삭제 완료", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("TAG","retrofit delete onResponse의 response에 문제가 있습니다.");
                }
                finish();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });
    }




}
