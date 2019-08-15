package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.DateUtils;
import com.newblack.coffit.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeControlActivity extends AppCompatActivity {
    APIInterface apiInterface;
    Button btn;
    EditText txt;

    int studentId = 1;
    List<Schedule> scheduleList;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_time_control);
        scheduleList = new ArrayList<>();
        txt = findViewById(R.id.editText);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> schedule = new HashMap<>();

                //2019.08.14.17.30 이런식으로 일단.
                String date = txt.getText().toString();
                System.out.println(date);
                int year = Integer.parseInt(date.split(",")[0]);
                int month = Integer.parseInt(date.split(",")[1]);
                int day = Integer.parseInt(date.split(",")[2]);
                int hour = Integer.parseInt(date.split(",")[3]);
                int min = Integer.parseInt(date.split(",")[4]);

                Calendar cal = Calendar.getInstance();
//                int hour = Integer.parseInt(time.split(":")[0]);
//                int min = Integer.parseInt(time.split(":")[1]);
//                cal.set(selectedDay.getYear(),selectedDay.getMonth(),selectedDay.getDay(),hour,min);
                cal.set(year,month-1,day,hour,min);

                Date input = new Date(cal.getTimeInMillis());
//                String dateString = DateUtils.dateToString(date);
//                Log.d("TAG", dateString);

                Date input2 = DateUtils.stringToDate("2019-08-15T18:00:00.000Z");

                Date input3 = DateUtils.forServerTime(year,month,day,hour,min);
//                Log.d("TAG", "time in string : "+ DateUtils.dateToString(input));
                schedule.put("state",0);
                schedule.put("date",input3);
                schedule.put("is_trainer",false);
                schedule.put("student_id",1);
                schedule.put("trainer_id",1);
                schedule.put("past_schedule_id",-1);
                //pt id는 내부것을 사용하는게 나을듯
                schedule.put("pt_id",1);
                //여기 고른 시간 id 넣어줘야함
                schedule.put("trainer_schedule_id",1);


                Log.d("TAG", "post 점검 : student "+schedule.get("student_id")+
                        " trainer "+schedule.get("trainer_id")+
                        " pt_id " +schedule.get("pt_id") );

                retrofit_postSchedule(schedule);

            }
        });


        retrofit_getSchedule();
    }





    public void retrofit_getSchedule(){
        //스케줄 받아서 표시하는 부분
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Schedule>> call = apiInterface.getSchedule(studentId);
        call.enqueue(new Callback<List<Schedule>>(){
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                List<Schedule> schedules = response.body();
                scheduleList.addAll(schedules);
                Log.d("TAG","schedulelist size : " +scheduleList.size());

                for(Schedule test : scheduleList){
                    Log.d("TAG" , " 텍스트 타임 : id " + test.getId()+ " time " +test.getDate());

                    int test_y = test.createdAt.getYear();
                    int test_M = test.createdAt.getMonth();
                    int test_d = test.createdAt.getDate();
                    int test_h = test.createdAt.getHours();
                    int test_m = test.createdAt.getMinutes();
                    Log.d("TAG" ," Date 시간 테스트 : " + test.createdAt +" 숫자값 : " +test_y + test_M+test_d+test_h+test_m);
                    Calendar cale = Calendar.getInstance();
                    Log.d("TAG","실제 Date 내의 값 : " + DateUtils.fromServerTime(test.createdAt));
                    cale.setTime(test.createdAt);
                    Log.d("TAG","Calendar 시간 테스트 : "+cale.get(Calendar.YEAR)
                        +cale.get(Calendar.MONTH)
                        +cale.get(Calendar.DATE)
                        +cale.get(Calendar.HOUR_OF_DAY)
                        +cale.get(Calendar.MINUTE));

                    Calendar cale2 = Calendar.getInstance();
                }
                //초기 화면만 설정
//                todayList = getTodaySchedule(CalendarDay.today(),scheduleList);
//                adapter.setSchedules(todayList);
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
            }
        });
    }


    //새로운 스케줄 만들기
    public void retrofit_postSchedule(HashMap<String,Object> schedule){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiInterface.postSchedule(schedule).enqueue(new Callback<Schedule>() {
            @Override
            public void onResponse(@NonNull Call<Schedule> call, @NonNull Response<Schedule> response) {
                Toast.makeText(activity,"예약 요청을 보냈습니다",Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(activity, ScheduleActivity.class);
//                startActivity(intent);
//                finish();

            }

            @Override
            public void onFailure(Call<Schedule> call, Throwable t) {
                Log.d("TAG", "통신 실패");
                Toast.makeText(activity, "통신 오류 발생",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
