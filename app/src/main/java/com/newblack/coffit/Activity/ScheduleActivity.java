package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.newblack.coffit.Adapter.TimeAdapter;
import com.newblack.coffit.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    Toolbar toolbar;
    ConstraintLayout layout;
    TextView tv_today;
    MaterialCalendarView calendar;
    List<CalendarDay> PTday;
    List<CalendarDay> availTime;
    String today;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("PT일정관리");



//        calendar.addDecorators(new SundayDecorator(), new SaturdayDecorator(), oneDayDecorater);
        calendar = findViewById(R.id.calendar);
        calendar.setSelectedDate(CalendarDay.today());
        today = dateFormat(CalendarDay.today());
        tv_today.setText(today);
        //여기서 처음 한번 retrofit 돌려서 전체 스케쥴 받아오기!! 굳이 여러번 돌리지 맙시다

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay date, boolean b) {
                today = dateFormat(date);
                tv_today.setText(today);

                //레트로핏 결과에 따라 스케쥴 보여주기.. 스케쥴리스트를 잘 받아다가 ㅎㅎ

            }
        });

    }

    public void newPT(View view){
        Intent intent = new Intent(this, AddScheduleActivity.class);
        startActivity(intent);
    }


    public void retrofit(){
        //스케줄 받아서 표시하는 부분
        //일단 표시할 내용만 구현


    }

    public static String dateFormat(CalendarDay date){
        String day = date.getYear() +"년 " + date.getMonth()+"월 " + date.getDay()+"일  ";
        return day;
    }
}
