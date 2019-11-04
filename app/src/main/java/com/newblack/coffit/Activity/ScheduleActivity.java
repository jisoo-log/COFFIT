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
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.myId;

public class ScheduleActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tv_today;
    MaterialCalendarView calendar;
    List<Schedule> scheduleList;
    List<Schedule> todayList;
    String today;
    String today_object;
    String selectedDay;
    CalendarDay selected;
    int studentId = myId;
    int trainer_id;
    int pt_id;

    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    Activity activity;
    APIInterface apiInterface;
//    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("PT일정 관리");
        tv_today = findViewById(R.id.tv_today);
        activity = this;
        recyclerView = findViewById(R.id.rv_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




//        sp = getSharedPreferences("coffit",MODE_PRIVATE);
//        studentId = sp.getInt("student_id",0);


        calendar = findViewById(R.id.calendar);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        trainer_id = intent.getIntExtra("trainer_id",-1);
        pt_id = intent.getIntExtra("pt_id",-1);
        if(trainer_id==-1 || pt_id==-1){
            Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_LONG).show();
            finish();
        }
        if(type==null) {
            calendar.setSelectedDate(CalendarDay.today());
            selected = CalendarDay.today();
            today_object = dateObject(CalendarDay.today());
            selectedDay = today_object;
            today = dateFormat(CalendarDay.today());
            tv_today.setText(today);
        }

        else {
            String s = intent.getStringExtra("date");
            int year = Integer.parseInt(s.split("-")[0]);
            int month = Integer.parseInt(s.split("-")[1]);
            int day = Integer.parseInt(s.split("-")[2]);
            CalendarDay c = CalendarDay.from(year,month,day);
            calendar.setSelectedDate(c);
            selected = c;
            today_object = dateObject(c);
            today = dateFormat(c);
            tv_today.setText(today);
        }

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay date, boolean b) {
                selected = date;
                selectedDay = dateObject(date);
                today = dateFormat(date);
                tv_today.setText(today);
                if (scheduleList != null) {
                    todayList = getTodaySchedule(date, scheduleList);
                    adapter.setSchedules(todayList);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //to refresh schedule
        scheduleList = new ArrayList<>();
        todayList = new ArrayList<>();
        adapter = new ScheduleAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Schedule schedule = todayList.get(position);
                Intent intent = new Intent(activity,ScheduleDialogActivity.class);
                intent.putExtra("schedule",schedule);
                intent.putExtra("trainer_id",trainer_id);
                intent.putExtra("pt_id",pt_id);
                startActivity(intent);
            }
        });

        retrofit_getSchedule();
    }


    public void retrofit_getSchedule(){
        //get student schedule from server
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Schedule>> call = apiInterface.getSchedule(studentId);
        call.enqueue(new Callback<List<Schedule>>(){
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                List<Schedule> schedules = response.body();

                //schedules must not to be null
                if(schedules!=null){
                    scheduleList.addAll(schedules);
                    Log.d("TAG","schedulelist size : " +scheduleList.size());

                    Collections.sort(scheduleList, new Comparator<Schedule>(){
                        public int compare(Schedule s1, Schedule s2){
                            return s1.getDate().compareTo(s2.getDate());
                        }
                    });

                    //need to make count later
                    for(int i = 1; i< scheduleList.size(); i++){
                        scheduleList.get(i-1).setCount(i);
                    }
                    todayList = getTodaySchedule(selected,scheduleList);

                    if(todayList.size()!=0){
                        adapter.setSchedules(todayList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
            }
        });
    }


    public void newPT(View view){
        Intent intent = new Intent(this, AddScheduleActivity.class);
        intent.putExtra("date",selectedDay);
        Log.d("TAG","selectedDay : " + selectedDay);
        intent.putExtra("trainer_id",trainer_id);
        intent.putExtra("pt_id",pt_id);
        startActivity(intent);
    }


    //오늘 날짜의 스케줄만 가져오는 용도
    public List<Schedule> getTodaySchedule(CalendarDay day, List<Schedule> schedules ){
        List<Schedule> result = new ArrayList<>();
        CalendarDay selected;
        for (Schedule schedule : schedules){
            Date date = schedule.getDate();
            selected = DateUtils.getCalendarDay(date);
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
