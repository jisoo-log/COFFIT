package com.newblack.coffit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    Toolbar toolbar;
    ConstraintLayout layout;
    TextView tv_today;
    MaterialCalendarView calendar;
    Calendar myCal;
    List<CalendarDay> PTday;
    List<CalendarDay> availTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("PT일정관리");
        myCal = Calendar.getInstance();

//        calendar.addDecorators(new SundayDecorator(), new SaturdayDecorator(), oneDayDecorater);
        calendar = findViewById(R.id.calendar);
        calendar.setCurrentDate(CalendarDay.from(myCal.get(Calendar.YEAR),myCal.get(Calendar.MONTH),myCal.get(Calendar.DATE)));

    }

    public void newPT(View view){
        Intent intent = new Intent(this, AddScheduleActivity.class);
        startActivity(intent);
    }


    public void retrofit(){
        //스케줄 받아서 표시하는 부분
        //일단 표시할 내용만 구현


    }
}
