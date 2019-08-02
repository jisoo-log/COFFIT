package com.newblack.coffit.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Activity.MainActivity;
import com.newblack.coffit.Data.Banner;
import com.newblack.coffit.Data.PT;
import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.R;
import com.newblack.coffit.Response.HomeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    Button btn_schedule;
    APIInterface apiInterface;

    SharedPreferences sp;
    Activity activity;

    TextView tv_username;
    TextView tv_summary;
    ImageView iv_mainpic;
    TextView tv_comment;
    TextView tv_ptnum;

    //임시 아이디
    //pf에 트레이너 이름도 넣어두는게 좋을 것 같다. 기본정보들. 사진,
    int studentId = 1;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        tv_summary = view.findViewById(R.id.tv_summary);
        tv_username = view.findViewById(R.id.tv_username);
        tv_ptnum = view.findViewById(R.id.tv_ptnum);
        tv_comment = view.findViewById(R.id.tv_comment);
        iv_mainpic = view.findViewById(R.id.iv_mainpic);

        btn_schedule = view.findViewById(R.id.btn_schedule);
        btn_schedule.setOnClickListener(new View.OnClickListener(){
            //일정 관리로 넘어감
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).goSchedule();
            }
        });

        Context context = getActivity();
        sp = context.getSharedPreferences("coffit",Context.MODE_PRIVATE);
        tv_summary.setText(sp.getString("summary",""));
        tv_username.setText(sp.getString("trainer_name",""));
        String pic_url = sp.getString("trainer_pic","");
        if(!pic_url.equals("")){
            Picasso.get().load(pic_url).into(iv_mainpic);
        }

        //통신
        retrofit_home();
        return view;
    }


    public void startPT(View view){

    }

    public void retrofit_home(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<HomeResponse>> call = apiInterface.getPT(1);
        call.enqueue(new Callback<List<HomeResponse>>(){
            @Override
            public void onResponse(Call<List<HomeResponse>> call, Response<List<HomeResponse>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                List<HomeResponse> hrs = response.body();
                //일단 리스트 중 가장 최근 것인데 나중엔 아예 state = 1 인게 하나만 있어야 할 것!
                int size = hrs.size();
                if(size > 0) {
                    HomeResponse hr = hrs.get(size - 1);

                    //받아온걸로 데이터 세팅
                    PT.PTcomment comment = hr.getPtComment();
                    List<Schedule> schedules = hr.getSchedules();
                    String info = hr.getRestNum()+ "회 완료 " +"(전체 "+ hr.getTotalNum()+"회 중)";
                    tv_ptnum.setText(info);
                    if(comment !=null){
                        tv_comment.setText(comment.getComment());
                    }
                }
                else {
                    Toast.makeText(activity, "PT가 없습니다",Toast.LENGTH_SHORT).show();
                    Log.d("TAG","HomeResponse : onResponse PT 데이터 존재 안함");
                }

            }

            @Override
            public void onFailure(Call<List<HomeResponse>> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });
    }

}
