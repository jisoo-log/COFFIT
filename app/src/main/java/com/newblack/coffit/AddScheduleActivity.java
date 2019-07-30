package com.newblack.coffit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddScheduleActivity extends AppCompatActivity {
    Toolbar toolbar;
    MaterialCalendarView calendar;
    TextView tv_today;
    TextView tv_info;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        toolbar = findViewById(R.id.toolbar);
        tv_today = findViewById(R.id.tv_today);
        tv_info = findViewById(R.id.tv_info);
        toolbar.setTitle("새로운 PT 신청");



        calendar = findViewById(R.id.calendar);
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay date, boolean b) {
                checkDate(date);
            }
        });

    }


    public void checkDate(CalendarDay date){
        LocalDate now = LocalDate.now();
        LocalDate selected = LocalDate.of(date.getYear(),date.getMonth(),date.getDay());
        if(now.isAfter(selected)){
            tv_info.setText("이미 지난 날짜입니다");
        }
        else if(selected.isAfter(now.plusWeeks(2))){
            tv_info.setText("PT 예약은 2주 뒤까지 가능합니다.");
        }
        else {
            //오늘부터 2주 내의 날짜를 선택했다면 retrofit으로 트레이너 스케줄 가져옴

        }

    }

    public void retrofit(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Date>> call = apiInterface.getAvailableList();
        call.enqueue(new Callback<List<Date>>(){
            @Override
            public void onResponse(Call<List<Date>> call, Response<List<Date>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                List<Date> dateList = response.body();
                for (Trainer trainer : trainerList ){
                    trainers2.add(trainer);
                    Log.d("TAG","time : " + trainer.getAt());
                    Log.d("TAG","time : " + trainer.getAt().getMinutes());
                    Log.d("TAG","time : " + trainer.getAt().getSeconds());
                    //Log.d("TAG","date : " + trainer.getAt().get(Calendar.MONTH));
                    //Log.d("TAG","time22 : " + trainer.getAt2());

                    Log.d("TAG", "check trainer name : "+trainer.getUsername());
                }
                adapter.setTrainers(trainers2);
                Log.d("TAG", "check trainers2 size : " + trainers2.size());
            }

            @Override
            public void onFailure(Call<List<Trainer>> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
            }
        });
    }

}
