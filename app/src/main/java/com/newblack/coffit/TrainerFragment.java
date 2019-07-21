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
import java.util.List;


public class TrainerFragment extends Fragment  {
    TextView tv_more;
    final int TL_FRAGMENT = 4;


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

        View view = inflater.inflate(R.layout.fragment_trainer, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_trainer);
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
        Trainer tr1 = new Trainer(1,"이지수", "소마에서 운동 한판!", 4, 20);
        Trainer tr2 = new Trainer(2,"배지훈", "랄라라라랄라ㅏ라라라", 5, 100);
        Trainer tr3 = new Trainer(3,"정은석", "뉴블랙뉴블랙뉴블랙!", 1, 2);
        Trainer tr4 = new Trainer(3,"정은석", "뉴블랙뉴블랙뉴블랙!", 1, 2);
        Trainer tr5 = new Trainer(3,"정은석", "뉴블랙뉴블랙뉴블랙!", 1, 2);
        Trainer tr6 = new Trainer(3,"정은석", "뉴블랙뉴블랙뉴블랙!", 1, 2);
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(tr1);
        trainers.add(tr2);
        trainers.add(tr3);
        trainers.add(tr4);
        Log.d("TAG","MainActivity onCreate : trainer list is total " + trainers.size());


        final TrainerAdapter adapter = new TrainerAdapter();
        recyclerView.setAdapter(adapter);

        //데이터 넣는 부분
        adapter.setTrainers(trainers);
        adapter.setOnItemClickListener(new TrainerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int id) {
                ((MainActivity)getActivity()).goTrainerDetail(id);
            }
        });
        return view;
    }



}
