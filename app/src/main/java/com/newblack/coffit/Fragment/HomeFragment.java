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
import com.newblack.coffit.Data.PT;
import com.newblack.coffit.Data.PTComment;
import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.R;
import com.newblack.coffit.Response.HomeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    Button btn_schedule;
    Button btn_startpt;
    APIInterface apiInterface;

    SharedPreferences sp;
    Boolean ptIdExist;
    Boolean ptRoomExist;
    Activity activity;

    TextView tv_username;
    TextView tv_summary;
    ImageView iv_mainpic;
    TextView tv_comment;
    TextView tv_ptnum;
    int studentId;




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
        btn_startpt = view.findViewById(R.id.btn_start);
        btn_startpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).goPT();
            }
        });


        Context context = getActivity();
        sp = context.getSharedPreferences("coffit",Context.MODE_PRIVATE);
        tv_summary.setText(sp.getString("summary",""));
        tv_username.setText(sp.getString("trainer_name",""));



        //받아올 때 pt_id 초기 저장
        //일단 지금은 무조건 지우고 새로 만들자. 다만 나중에는 수정해줘야함
        //임시 아이디 1번으로 테스트
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("pt_id");
        editor.remove("pt_room");
        editor.putInt("student_id",1);
        editor.commit();

        studentId = sp.getInt("student_id",0);
        ptIdExist = !(sp.getInt("pt_id",0)==0);
        ptRoomExist = !(sp.getString("pt_room","").equals(""));
//        Log.d("TAG", "ptIdExist : " + ptIdExist + "ptRoomExist : "+ptRoomExist);
//        Log.d("TAG","before get, pt_id : "+ sp.getInt("pt_id",0)+" ptroom : " + sp.getString("pt_room",""));
        String pic_url = sp.getString("trainer_pic","");
        if(!pic_url.equals("")){
            Picasso.get().load(pic_url).into(iv_mainpic);
        }

        //통신
        retrofit_home();
        return view;
    }




    public void retrofit_home(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<HomeResponse> call = apiInterface.getPT(studentId);
        call.enqueue(new Callback<HomeResponse>(){
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response){
                Log.d("TAG", "apiInterface callback onResponse");
                HomeResponse hr = response.body();
                PT pt = hr.getPt();

//                받아온걸로 데이터 세팅
                PTComment comment = hr.getPtComment();
                List<Schedule> schedules = hr.getSchedules();
                String info = pt.getRestNum()+ "회 완료 " +"(전체 "+ pt.getTotalNum()+"회 중)";
                tv_ptnum.setText(info);

                if(!ptRoomExist){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("pt_id",pt.getId());
                    editor.putString("pt_room",pt.getPtRoom());
                    editor.commit();
//                    Log.d("TAG","after get, pt_id : "+ sp.getInt("pt_id",0)+" ptroom : " + sp.getString("pt_room",""));

                }

                if(comment !=null){
                    tv_comment.setText(comment.getComment());
                }

//                int size = hrs.size();
//                if(size > 0) {
//                    HomeResponse hr = hrs.get(size - 1);
//
//
//                }
//                else {
//                    Toast.makeText(activity, "PT가 없습니다",Toast.LENGTH_SHORT).show();
//                    Log.d("TAG","HomeResponse : onResponse PT 데이터 존재 안함");
//                }

            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
            }
        });
    }

}
