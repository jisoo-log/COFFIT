package com.newblack.coffit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrainerFragment extends Fragment  {
    TextView tv_more;
    final int TL_FRAGMENT = 4;
    APIInterface apiInterface;
    List<Trainer> trainers2;
    RecyclerView recyclerView;
    TrainerAdapter adapter;


    public TrainerFragment() {
        // Required empty public constructor
    }

    //외부 액티비티에서 프래그먼트 전환할 수 있도록!
    public static TrainerFragment newInstance(){
        return new TrainerFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        trainers2 = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_trainer, container, false);
        recyclerView = view.findViewById(R.id.rv_trainer);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //recyclerView.setHasFixedSize(true);
        tv_more = view.findViewById(R.id.tv_more);
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((MainActivity)getActivity()).setFrag(TrainerListFragment.newInstance());
                ((MainActivity)getActivity()).setFrag(TL_FRAGMENT);
                Log.d("TAG","tv_more onClick Listener");
            }
        });



        //잠시 예시 트레이너 데이터 생성
//        Trainer tr1 = new Trainer(1,"이지수", "소마에서 운동 한판!", 4, 20);
//        Trainer tr2 = new Trainer(2,"배지훈", "랄라라라랄라ㅏ라라라", 5, 100);
//        Trainer tr3 = new Trainer(3,"정은석", "뉴블랙뉴블랙뉴블랙!", 1, 2);
//        Trainer tr4 = new Trainer(3,"정은석", "뉴블랙뉴블랙뉴블랙!", 1, 2);
//        Trainer tr5 = new Trainer(3,"정은석", "뉴블랙뉴블랙뉴블랙!", 1, 2);
//        Trainer tr6 = new Trainer(3,"정은석", "뉴블랙뉴블랙뉴블랙!", 1, 2);
//        List<Trainer> trainers = new ArrayList<>();
//        trainers.add(tr1);
//        trainers.add(tr2);
//        trainers.add(tr3);
//        trainers.add(tr4);
//        Log.d("TAG","MainActivity onCreate : trainer list is total " + trainers.size());


//        final TrainerAdapter adapter = new TrainerAdapter();
        adapter = new TrainerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TrainerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int id) {
                ((MainActivity)getActivity()).goTrainerDetail(id);
            }
        });

        retrofitTest();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void retrofitTest(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Trainer>> call = apiInterface.getTrainerList();
        call.enqueue(new Callback<List<Trainer>>(){
            @Override
            public void onResponse(Call<List<Trainer>> call, Response<List<Trainer>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                List<Trainer> trainerList = response.body();
                for (Trainer trainer : trainerList ){
                    trainers2.add(trainer);
                    Log.d("TAG","time : " + trainer.getAt());
                    Log.d("TAG","time : " + trainer.getAt().getMinutes());
                    Log.d("TAG","time : " + trainer.getAt().getSeconds());
                    //Log.d("TAG","date : " + trainer.getAt().get(Calendar.MONTH));
                    Log.d("TAG", "check trainer name : "+trainer.getUsername());
                }
                adapter.setTrainers(trainers2);
                Log.d("TAG", "check trainers2 size : " + trainers2.size());
            }

            @Override
            public void onFailure(Call<List<Trainer>> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });
    }
}
