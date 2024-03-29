package com.newblack.coffit.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Activity.MainActivity;
import com.newblack.coffit.Data.Banner;
import com.newblack.coffit.R;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.Adapter.TrainerAdapter;
import com.newblack.coffit.Response.TrainerResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrainerFragment extends Fragment  {
    TextView tv_more;
    ImageView iv_banner;
    final int TL_FRAGMENT = 4;
    APIInterface apiInterface;
    List<Trainer> trainerList;
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
        trainerList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_trainer, container, false);
        recyclerView = view.findViewById(R.id.rv_trainer);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        iv_banner = view.findViewById(R.id.iv_banner);
        tv_more = view.findViewById(R.id.tv_more);
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((MainActivity)getActivity()).setFrag(TrainerListFragment.newInstance());
                ((MainActivity)getActivity()).setFrag(TL_FRAGMENT);
                Log.d("TAG","tv_more onClick Listener");
            }
        });


//        final TrainerAdapter adapter = new TrainerAdapter();
        adapter = new TrainerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TrainerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int id) {
                ((MainActivity)getActivity()).goTrainerDetail(id);
            }
        });

        retrofit_trainer();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }



    public void retrofit_trainer(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<TrainerResponse> call = apiInterface.getMain();
        call.enqueue(new Callback<TrainerResponse>(){
            @Override
            public void onResponse(Call<TrainerResponse> call, Response<TrainerResponse> response){
                Log.d("TAG", "apiInterface callback onResponse");
                TrainerResponse hr = response.body();

                List<Trainer> trainers = hr.getTrainers();
                if(trainers != null) {
                    trainerList.addAll(trainers);
                    adapter.setTrainers(trainerList);
                }

                List<Banner> banners = hr.getBanners();
                Random r = new Random();
                int bound = banners.size()-1;
                int i;
                if (bound==-1) i = 0;
                else i = bound;
                Picasso.get().load(banners.get(i).getPictureURL()).into(iv_banner);

                Log.d("TAG", "check trainers2 size : " + trainerList.size());
            }

            @Override
            public void onFailure(Call<TrainerResponse> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });
    }
}
