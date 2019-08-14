package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
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
import com.newblack.coffit.Adapter.TimeAdapter;
import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.Data.TrainerSchedule;
import com.newblack.coffit.DateUtils;
import com.newblack.coffit.R;
import com.newblack.coffit.Response.TrainerScheduleResponse;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.ScheduleActivity.dateFormat;

public class AddScheduleActivity extends AppCompatActivity {
    Activity activity;
    SharedPreferences sp;
    int studentId;
    int trainerId;
    int pt_id;
    APIInterface apiInterface;
    Toolbar toolbar;
    MaterialCalendarView calendar;
    TextView tv_today;
    TextView tv_info;

    RecyclerView mRecyclerView;
    TimeAdapter adapter;
    List<String> times;
    List<TrainerSchedule> trainerScheduleList;
    CalendarDay selectedDay;
    String today;
    String initialDay;
    CalendarDay initial;
    int pastId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_add_schedule);
        toolbar = findViewById(R.id.toolbar);
        tv_today = findViewById(R.id.tv_today);
        tv_info = findViewById(R.id.tv_info);


        mRecyclerView = findViewById(R.id.rv_time);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TimeAdapter();
        mRecyclerView.setAdapter(adapter);
        times = new ArrayList<>();
        trainerScheduleList = new ArrayList<>();

        sp = getSharedPreferences("coffit", Context.MODE_PRIVATE);
        studentId = sp.getInt("student_id",0);
        trainerId = sp.getInt("trainer_id", 0);
        pt_id = sp.getInt("pt_id",0);
        Log.d("TAG", "studentId : " +studentId + " trainerId" + trainerId + " pt_id :"+ pt_id );

        //새로운 PT 신청이거나, PT 일정 변경 요청이거나
        Intent intent = getIntent();
        if(intent.getIntExtra("edit",0)==0){
            //새로운 PT
            toolbar.setTitle("새로운 PT 신청");
        }
        else{
            //기존 PT
            toolbar.setTitle("PT 일정 변경");
        }
        //과거 id
        pastId = intent.getIntExtra("pastId",-1);
        initialDay = intent.getStringExtra("date");
        Log.d("TAG","initialDay : "+initialDay);
        int year = Integer.parseInt(initialDay.split("-")[0]);
        int month = Integer.parseInt(initialDay.split("-")[1]);
        int day = Integer.parseInt(initialDay.split("-")[2]);
        Log.d("TAG", "parsed initialDay" + year + " " + month + " "+ day);
        initial = CalendarDay.from(year,month,day);
        Log.d("TAG", "initial year :" + initial.getYear());
        selectedDay = CalendarDay.from(year,month,day);


        adapter.setOnItemClickListener(new TimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String time, int id) {

                //스케줄 요청 위한 최종 대화상자
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("PT 예약 요청");
                builder.setMessage(today+time+"으로 PT 예약 요청을 보낼까요?\n\n트레이너가 확정하기 전까지 예약이 확정되지 않습니다");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //확인 클릭 시 retrofit으로 스케줄 보내줘야함! 나중에 구현할 것
                                HashMap<String, Object> schedule = new HashMap<>();


                                Calendar cal = Calendar.getInstance();
                                int hour = Integer.parseInt(time.split(":")[0]);
                                int min = Integer.parseInt(time.split(":")[1]);
                                cal.set(selectedDay.getYear(),selectedDay.getMonth(),selectedDay.getDay(),hour,min);

                                Date date = DateUtils.forServerTime(selectedDay.getYear(),selectedDay.getMonth(),selectedDay.getDay(),hour,min);
//                                String dateString = DateUtils.dateToString(date);
                                Log.d("TAG", DateUtils.fromServerTime(date));
                                schedule.put("state",0);
                                schedule.put("date",date);
                                schedule.put("is_trainer",false);
                                schedule.put("student_id",studentId);
                                schedule.put("trainer_id",trainerId);
                                schedule.put("past_schedule_id",pastId);
                                //pt id는 내부것을 사용하는게 나을듯
                                schedule.put("pt_id",pt_id);
                                //여기 고른 시간 id 넣어줘야함
                                schedule.put("trainer_schedule_id",id);


                                Log.d("TAG", "post 점검 : student "+schedule.get("student_id")+
                                        " trainer "+schedule.get("trainer_id")+
                                        " pt_id " +schedule.get("pt_id") );

                                retrofit_postSchedule(schedule);
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //취소 클릭 시 아무 반응 없음
                                Log.d("TAG","Dialog : 취소");
                            }
                        });
                builder.show();

            }
        });


        //캘린더 초기화
        calendar = findViewById(R.id.calendar);

        calendar.setSelectedDate(initial);
        checkDate(initial);
        today = dateFormat(initial);
        tv_today.setText(today);
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay date, boolean b) {
                today = dateFormat(date);
                checkDate(date);
                selectedDay = date;
            }
        });

        //트레이너 스케줄 받아와서 리스트에 저장
        //여기서 순서 꼬이지 않도록 주의해야함. 초기 데이터는 넣자.
        retrofit_getTrainerSchedule();
    }


    public void checkDate(CalendarDay date){
        tv_today.setText(today);
        LocalDate now = LocalDate.now();
        LocalDate selected = LocalDate.of(date.getYear(),date.getMonth(),date.getDay());
        if(now.isAfter(selected)){
            tv_info.setText("이미 지난 날짜입니다");
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
        else if(selected.isAfter(now.plusWeeks(2))){
            tv_info.setText("PT 예약은 2주 뒤까지 가능합니다.");
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            //오늘부터 2주 내의 날짜를 선택했다면 retrofit으로 트레이너 스케줄 가져옴
            //retrofit에서 받아왔을 데이터 가지고 ["6:00","7:30",.. ] 이런 리스트를 만들어서 넣을 것임

            mRecyclerView.setVisibility(View.VISIBLE);
            Log.d("TAG","리스트 사이즈 : "+ trainerScheduleList.size());
            if(trainerScheduleList.size()!=0) {
                List<TrainerSchedule> test_schedule = getTodaySchedule(date, trainerScheduleList);
                for(TrainerSchedule schedule : test_schedule){
                    Log.d("TAG", schedule.getStringTime() );
                }
                adapter.setTimes(getTodaySchedule(date, trainerScheduleList));
                tv_info.setText("모든 수업은 30분 단위로 진행됩니다.\n아래에서 가능한 수업 시간을 골라주세요.");
            }
            else{
                tv_info.setText("수업 가능한 시간이 없습니다\n다른 날을 선택해주세요.");
            }
        }

    }


    public void retrofit_getTrainerSchedule(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<TrainerScheduleResponse> call = apiInterface.getTrainerSchedule(trainerId);
        call.enqueue(new Callback<TrainerScheduleResponse>(){
            @Override
            public void onResponse(Call<TrainerScheduleResponse> call, Response<TrainerScheduleResponse> response) {
                Log.d("TAG", "apiInterface callback onResponse");
                TrainerScheduleResponse scheduleResponse = response.body();
                trainerScheduleList.addAll(scheduleResponse.getAvailables());


                //초기 화면만 설정
                if (trainerScheduleList.size() != 0) {
                    adapter.setTimes(getTodaySchedule(initial, trainerScheduleList));
                    tv_info.setText("모든 수업은 30분 단위로 진행됩니다.\n아래에서 가능한 수업 시간을 골라주세요.");
                }
            }
            @Override
            public void onFailure(Call<TrainerScheduleResponse> call, Throwable t) {
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
                finish();

            }

            @Override
            public void onFailure(Call<Schedule> call, Throwable t) {
                Log.d("TAG", "통신 실패");
                Toast.makeText(activity, "통신 오류 발생",Toast.LENGTH_SHORT).show();
            }
        });

    }

    //오늘 날짜의 스케줄만 가져오는 용도
    public List<TrainerSchedule> getTodaySchedule(CalendarDay day, List<TrainerSchedule> schedules ){
        List<TrainerSchedule> result = new ArrayList<>();
        CalendarDay selected;
        for (TrainerSchedule schedule : schedules){
            Date date = DateUtils.stringToDate(schedule.getStartTime());
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
}
