package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.newblack.coffit.Adapter.TimeAdapter;
import com.newblack.coffit.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.newblack.coffit.Activity.ScheduleActivity.dateFormat;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;

public class AddScheduleActivity extends AppCompatActivity {
    Activity activity;
    Toolbar toolbar;
    MaterialCalendarView calendar;
    TextView tv_today;
    TextView tv_info;
//    APIInterface apiInterface;

    RecyclerView mRecyclerView;
    TimeAdapter adapter;
    List<String> times;
    String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_add_schedule);
        toolbar = findViewById(R.id.toolbar);
        tv_today = findViewById(R.id.tv_today);
        tv_info = findViewById(R.id.tv_info);
        toolbar.setTitle("새로운 PT 신청");

        mRecyclerView = findViewById(R.id.rv_time);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TimeAdapter();
        mRecyclerView.setAdapter(adapter);
        times = new ArrayList<>();


        //삭제해야함! retrofit으로 받아와야하는 임시 데이터
        times.add("07:00");
        times.add("12:30");
        times.add("13:00");
        times.add("15:00");
        times.add("15:30");

        adapter.setOnItemClickListener(new TimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String time) {

                //스케줄 요청 위한 최종 대화상자
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("PT 예약 요청");
                builder.setMessage(today+time+"으로 PT 예약 요청을 보낼까요?\n\n트레이너가 확정하기 전까지 예약이 확정되지 않습니다");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //확인 클릭 시 retrofit으로 스케줄 보내줘야함! 나중에 구현할 것


                                //retrofit으로 스케줄 보낸 뒤엔 ScheduleActivity로 돌아감
                                finish();
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

        calendar.setSelectedDate(CalendarDay.today());
        checkDate(CalendarDay.today());
        today = dateFormat(CalendarDay.today());
        tv_today.setText(today);
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay date, boolean b) {
                today = dateFormat(date);
                checkDate(date);
            }
        });

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


            //일단 임시. adapter도 retrofit 안에서 해야한다는 점 유의
            tv_info.setText("모든 수업은 30분 단위로 진행됩니다.\n아래에서 가능한 수업 시간을 골라주세요.");
            mRecyclerView.setVisibility(View.VISIBLE);
            adapter.setTimes(times);


        }

    }


    //레트로핏 한번만 돌려서 데이터 전체를 받아온다면? 어차피 2주치만 가져올 거니까.

//
//    public void retrofit(){
//        apiInterface = APIClient.getClient().create(APIInterface.class);
//
//        Call<List<Date>> call = apiInterface.getAvailableList();
//        call.enqueue(new Callback<List<Date>>(){
//            @Override
//            public void onResponse(Call<List<Date>> call, Response<List<Date>> response){
//                Log.d("TAG", "apiInterface callback onResponse");
//                List<Date> dateList = response.body();
//                for (Trainer trainer : trainerList ){
//                    trainers2.add(trainer);
//                    Log.d("TAG","time : " + trainer.getAt());
//                    Log.d("TAG","time : " + trainer.getAt().getMinutes());
//                    Log.d("TAG","time : " + trainer.getAt().getSeconds());
//                    //Log.d("TAG","date : " + trainer.getAt().get(Calendar.MONTH));
//                    //Log.d("TAG","time22 : " + trainer.getAt2());
//
//                    Log.d("TAG", "check trainer name : "+trainer.getUsername());
//                }
//                adapter.setTrainers(trainers2);
//                Log.d("TAG", "check trainers2 size : " + trainers2.size());
//            }
//
//            @Override
//            public void onFailure(Call<List<Trainer>> call, Throwable t) {
//                t.printStackTrace();
//                Log.d("TAG", "통신 실패");
//            }
//        });
//    }

}
