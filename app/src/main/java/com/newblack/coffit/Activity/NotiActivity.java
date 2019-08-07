package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Adapter.NotiAdapter;
import com.newblack.coffit.Data.Noti;
import com.newblack.coffit.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotiActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotiAdapter notiAdapter;
    Toolbar toolbar;
    APIInterface apiInterface;
    List<Noti> notiList;

    SharedPreferences sp;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);

        sp = getSharedPreferences("coffit",MODE_PRIVATE);
        id = sp.getInt("studentId",1);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_noti);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notiAdapter = new NotiAdapter();
        recyclerView.setAdapter(notiAdapter);

        toolbar.setTitle("알람");
        //서버 통신
        retrofit_noti();
    }


    public void retrofit_noti(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Noti>> call = apiInterface.getNotiList(id);
        call.enqueue(new Callback<List<Noti>>(){
            @Override
            public void onResponse(Call<List<Noti>> call, Response<List<Noti>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                List<Noti> notis = response.body();
                notiList.addAll(notis);
                notiAdapter.setNotis(notiList);
            }

            @Override
            public void onFailure(Call<List<Noti>> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });
    }




}
