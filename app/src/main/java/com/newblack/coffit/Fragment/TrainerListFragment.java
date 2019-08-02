package com.newblack.coffit.Fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Activity.MainActivity;
import com.newblack.coffit.R;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.Adapter.TrainerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainerListFragment extends Fragment {
    List<Trainer> trainerList;
    APIInterface apiInterface;
    TrainerAdapter adapter;
    Context context;

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

        trainerList = new ArrayList<>();
        context = getContext();
        adapter = new TrainerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TrainerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int id) {
                ((MainActivity)getActivity()).goTrainerDetail(id);
            }
        });

        retrofit_getTrainer();

        return view;
    }

    public void retrofit_getTrainer(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Trainer>> call = apiInterface.getTrainerList();
        call.enqueue(new Callback<List<Trainer>>(){
            @Override
            public void onResponse(Call<List<Trainer>> call, Response<List<Trainer>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                List<Trainer> trainers = response.body();
                if(trainers != null) {
                    for (Trainer trainer : trainers) {
                        trainerList.add(trainer);
                        Log.d("TAG", "check trainer name : " + trainer.getUsername());
                    }
                }
                adapter.setTrainers(trainerList);
                Log.d("TAG", "check trainers size : " + trainers.size());
            }

            @Override
            public void onFailure(Call<List<Trainer>> call, Throwable t) {
                Log.d("TAG", "통신 실패");
                Toast.makeText(context, "통신 오류 발생",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
