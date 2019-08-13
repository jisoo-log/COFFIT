package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Adapter.ScheduleAdapter;
import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.Data.TrainerSchedule;
import com.newblack.coffit.DateUtils;
import com.newblack.coffit.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tv_today;
    MaterialCalendarView calendar;
    List<Schedule> scheduleList;
    String today;
    String today_object;
    String selectedDay;
    int studentId;

    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    Activity activity;
    APIInterface apiInterface;
    SharedPreferences sp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("PT일정관리");
        tv_today = findViewById(R.id.tv_today);
        activity = this;

        sp = getSharedPreferences("coffit",MODE_PRIVATE);
        studentId = sp.getInt("student_id",0);



//        calendar.addDecorators(new SundayDecorator(), new SaturdayDecorator(), oneDayDecorater);
        calendar = findViewById(R.id.calendar);
        calendar.setSelectedDate(CalendarDay.today());
        today_object = dateObject(CalendarDay.today());
        selectedDay = today_object;
        today = dateFormat(CalendarDay.today());
        tv_today.setText(today);
        scheduleList = new ArrayList<>();
        //여기서 처음 한번 retrofit 돌려서 전체 스케쥴 받아오기!! 굳이 여러번 돌리지 맙시다
        retrofit_getSchedule();




        //받아온 것 중 그 날짜에 맞는 것 표시하도록 - 오 여기 머리아프겠다...

        recyclerView = findViewById(R.id.rv_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ScheduleAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //여기서 스케줄 수정용 액티비티 띄우기
                //액티비티 띄우는게 맞는 것 같아서 일단 다시 구현
                Schedule schedule = scheduleList.get(position);
                Intent intent = new Intent(activity,ScheduleDialogActivity.class);
                intent.putExtra("schedule",schedule);
                startActivity(intent);
            }
        });


        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay date, boolean b) {
                selectedDay = dateObject(date);
                today = dateFormat(date);
                tv_today.setText(today);
                adapter.setSchedules(getTodaySchedule(date,scheduleList));

                //레트로핏 결과에 따라 스케쥴 보여주기.. 스케쥴리스트를 잘 받아다가 ㅎㅎ

            }
        });

    }

    public void newPT(View view){
        Intent intent = new Intent(this, AddScheduleActivity.class);
        intent.putExtra("date",selectedDay);
        Log.d("TAG","selectedDay : " + selectedDay);
        startActivity(intent);
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
                    Log.d("TAG" , "id " + test.getId()+ " time " +test.getDate());
                }
                //초기 화면만 설정
                adapter.setSchedules(getTodaySchedule(CalendarDay.today(),scheduleList));
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
            }
        });
    }


    //날짜별 캘린더 색 설정


    //오늘 날짜의 스케줄만 가져오는 용도
    public List<Schedule> getTodaySchedule(CalendarDay day, List<Schedule> schedules ){
        List<Schedule> result = new ArrayList<>();
        CalendarDay selected;
        for (Schedule schedule : schedules){
            Date date = DateUtils.stringToDate(schedule.getDate());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            selected = CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DATE));
//            Log.d("TAG","selected : "+dateFormat(selected)+" 일치 여부 : "+ selected.equals(day));
            if(selected.equals(day)){
                //날짜 같을때 추가
                result.add(schedule);
            }
        }
        return result;
    }


    public static String dateFormat(CalendarDay date){
        String day = date.getYear() +"년 " + date.getMonth()+"월 " + date.getDay()+"일  ";
        Log.d("TAG","date Format : "+ day);
        return day;
    }

    public static String dateObject(CalendarDay date){
        String day = date.getYear()+"-"+date.getMonth()+"-"+date.getDay();
        return day;
    }


}
