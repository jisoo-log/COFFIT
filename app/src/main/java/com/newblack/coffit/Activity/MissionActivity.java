package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Data.Mission;
import com.newblack.coffit.DateUtils;
import com.newblack.coffit.Decorators.BadEventDecorator;
import com.newblack.coffit.Decorators.GoodEventDecorator;
import com.newblack.coffit.Decorators.SosoEventDecorator;
import com.newblack.coffit.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.myId;
import static com.newblack.coffit.DateUtils.forServerTime;
import static com.newblack.coffit.DateUtils.getCalendarDay;

public class MissionActivity extends AppCompatActivity {
    Toolbar toolbar;
    final int BACK_PRESSED = 99;
    final int FILE_UPLOAD = 88;

    int student_id;
    MaterialCalendarView calendar;
    List<CalendarDay> goodDayList;
    List<CalendarDay> sosoDayList;
    List<CalendarDay> badDayList;
    TextView tv_score;

    CalendarDay selected;
    String selectedDay;
    APIInterface apiInterface;
    Activity activity;
    List<Mission> missionList;
    int total_num;
    int total_rate;
    int pt_id;
    int trainer_id;
    int blocked_id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        activity = this;

        toolbar = findViewById(R.id.toolbar);

        //get student id and name from DashBoard activity
        Intent intent = getIntent();
        student_id = myId;
//        pt_id = intent.getIntExtra("pt_id",-1);
//        if(student_id==-1 || pt_id ==-1){
//            Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_LONG).show();
//            finish();
//        }

        //initialize
        goodDayList = new ArrayList<>();
        sosoDayList = new ArrayList<>();
        badDayList = new ArrayList<>();
        total_num = 0;
        total_rate = 0;


        missionList = new ArrayList<>();

        String title = "일일 미션";
        toolbar.setTitle(title);

        tv_score = findViewById(R.id.tv_score);

        calendar = findViewById(R.id.calendar);
        calendar.setSelectedDate(CalendarDay.today());
        selected = CalendarDay.today();


        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay date, boolean b) {
                Intent intent = new Intent(activity,MissionDetailActivity.class);
                int mission_id=0;
                String selected="";
                for(Mission m : missionList){
                    CalendarDay c = getCalendarDay(m.getDate());
                    if(c.equals(date)){
                        mission_id = m.getId();
                        selected = DateUtils.dateObject(m.getDate());
                        Log.d("TAG","selected mission id : "+mission_id);
                        break;
                    }
                }
                Log.d("TAG","현재 blocked_id : "+blocked_id + " 현재 선택된 미션 : "+mission_id );
                if(mission_id==0){
                    Toast.makeText(activity,"미션이 설정되지 않았습니다",Toast.LENGTH_LONG).show();
                }
                else if(mission_id == blocked_id){
                    Toast.makeText(activity,"영상 업로드 중입니다",Toast.LENGTH_LONG).show();
                }
                else{
                    intent.putExtra("mission_id",mission_id);
                    intent.putExtra("date",selected);
                    intent.putExtra("trainer_id",trainer_id);
                    startActivityForResult(intent,9999);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        retrofit_getMissionList(student_id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(myLocalBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TAG","message came");
            String result = intent.getStringExtra("result");
            //TODO: change event when upload end
            blocked_id = 0;
        }
    };

    //check the app finished because of upload file | backpressed
    //if upload file, block to enter that day
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==FILE_UPLOAD){
            blocked_id = data.getIntExtra("blocked_id",-1);
            Log.d("TAG","check blocked id : "+blocked_id);
        }
    }

    public void retrofit_getMissionList(int student_id){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Mission>> call = apiInterface.getMissionList(student_id);
        call.enqueue(new Callback<List<Mission>>(){
            @Override
            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {
                Log.d("TAG", "apiInterface callback onResponse");
                List<Mission> missions = response.body();
                if(missions == null || missions.size()==0){
                    Toast.makeText(activity, "빈 응답",Toast.LENGTH_LONG).show();
                }
                else{
                    missionList = new ArrayList<>();
                    missionList.addAll(missions);
                }

                if (missions != null & missionList.size()!=0) {
                    total_num=0;
                    total_rate=0;
                    for (Mission ms : missionList) {
                        Log.d("TAG", " id : "+ms.getId() + " 날짜 확인 : "+ms.getDate()+ " content : "+ms.getContent() + " ptid :" + ms.getPt_id());
                        if (ms.getRate() != 0) {
                            total_num++;
                            total_rate += ms.getRate();
                            if (ms.getRate() >= 4) goodDayList.add(getCalendarDay(ms.getDate()));
                            else if (ms.getRate() >= 2)
                                sosoDayList.add(getCalendarDay(ms.getDate()));
                            else badDayList.add(getCalendarDay(ms.getDate()));
                        }

                    }

                    trainer_id = missionList.get(0).getTrainer_id();
                    Log.d("TAG", "total rate : "+total_rate);
                    set_score(total_num, total_rate);
                    calendar.addDecorator(new GoodEventDecorator(activity, goodDayList));
                    calendar.addDecorator(new SosoEventDecorator(activity, sosoDayList));
                    calendar.addDecorator(new BadEventDecorator(activity, badDayList));
                    String test = missionList.get(0).getContent();

                }
            }
            @Override
            public void onFailure(Call<List<Mission>> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
            }
        });
    }

    public void set_score(int t_num, double t_score){
        double score;
        if(t_num==0){
            score = 0;
        }
        else {
            score = t_score/t_num;}
        String string_score = String.format("%.1f",score);
        String s = "총 운동 회수 : " + t_num + " 회  |  평균 점수 : " + string_score;
        tv_score.setText(s);
    }

    public List<Mission> tmp_mission(){
        List<Mission> ms = new ArrayList<>();
        Mission ms1 = new Mission();
        Mission ms2 = new Mission();
        Mission ms3 = new Mission();
        Mission ms4 = new Mission();
        Mission ms5 = new Mission();
        Mission ms6 = new Mission();
        ms.add(ms1);
        ms.add(ms2);
        ms.add(ms3);
        ms.add(ms4);
        ms.add(ms5);
        ms.add(ms6);
        int x = 1;
        int day = 5;
        for( Mission m : ms){
            m.setId(x);
            m.setComment("ggg");
            m.setConverted(true);
            m.setRate(x++);
            Date d = forServerTime(2019,9,day++,12,0);
            m.setDate(d);
        }

        return ms;
    }
}

