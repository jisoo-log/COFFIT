package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Adapter.NotiAdapter;
import com.newblack.coffit.Data.Noti;
import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.DateUtils;
import com.newblack.coffit.R;
import com.newblack.coffit.Response.NotiResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.myId;

public class NotiActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotiAdapter notiAdapter;
    Toolbar toolbar;
    APIInterface apiInterface;
    List<Noti> notiList;
    List<NotiResponse> notis;

    int id;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);
        activity = this;

        id = myId;
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_noti);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notiAdapter = new NotiAdapter();
        recyclerView.setAdapter(notiAdapter);
        DividerItemDecoration deco = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(deco);
        notiList = new ArrayList<>();

        notiAdapter.setOnItemClickListener(new NotiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Schedule schedule = notis.get(position).getSchedule();
                if(schedule==null) return;
                Intent intent = new Intent(activity, ScheduleActivity.class);
                Date date = schedule.getDate();
                String time = DateUtils.dateObject(date);
                intent.putExtra("type","noti");
                intent.putExtra("date",time);
//                intent.putExtra("schedule",schedule);
                startActivity(intent);
                finish();
            }
        });

        toolbar.setTitle("알람");
        //서버 통신
        retrofit_noti();
    }

    public void retrofit_noti(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<NotiResponse>> call = apiInterface.getNotiList(id);
        call.enqueue(new Callback<List<NotiResponse>>(){
            @Override
            public void onResponse(Call<List<NotiResponse>> call, Response<List<NotiResponse>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                notis = response.body();
                if(notis != null){
                    notiList.addAll(notis);
                    notiAdapter.setNotis(notiList);
                }
            }

            @Override
            public void onFailure(Call<List<NotiResponse>> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });
    }





}
