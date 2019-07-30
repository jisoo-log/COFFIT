package com.newblack.coffit.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newblack.coffit.Activity.MainActivity;
import com.newblack.coffit.R;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.Adapter.TrainerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainerListFragment extends Fragment {


    public TrainerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trainer_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_trainer);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //recyclerView.setHasFixedSize(true);


        //잠시 예시 트레이너 데이터 생성
        //이것도 뷰모델에 따로 빼놔야겠네...
        Trainer tr1 = new Trainer(1,"이지수", "소마에서 운동 한판!", 4, 20);
        Trainer tr2 = new Trainer(2,"배지훈", "랄라라라랄라ㅏ라라라", 5, 100);
        Trainer tr3 = new Trainer(3,"정은석", "뉴블랙뉴블랙뉴블랙!", 1, 2);
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(tr1);
        trainers.add(tr2);
        trainers.add(tr3);
        Log.d("TAG","MainActivity onCreate : trainer list is total " + trainers.size());


        final TrainerAdapter adapter = new TrainerAdapter();
        recyclerView.setAdapter(adapter);

        //데이터 넣는 부분. Trainer Fragment와 코드 반복이 걸린다
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
