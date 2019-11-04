package com.newblack.coffit.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Activity.MainActivity;
import com.newblack.coffit.Data.PT;
import com.newblack.coffit.Data.PTComment;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.R;
import com.newblack.coffit.Response.HomeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.myId;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    Button btn_schedule;
    Button btn_startpt;
    Button btn_exercise;
    APIInterface apiInterface;

//    SharedPreferences sp;
    Boolean ptIdExist;
    Boolean ptRoomExist;
    Activity activity;

    TextView tv_username;
    TextView tv_summary;
    ImageView iv_mainpic;
    TextView tv_comment;
    TextView tv_ptnum;
    int student_id;
    int trainer_id;
    int pt_id;
    String ptRoom;

    MainActivity main;



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
        trainer_id=0;
        student_id = myId;

        btn_exercise = view.findViewById(R.id.btn_exercise);
        btn_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).goMission();
            }
        });

        btn_schedule = view.findViewById(R.id.btn_schedule);
        btn_schedule.setOnClickListener(new View.OnClickListener(){
            //일정 관리로 넘어감

            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).goSchedule(trainer_id,pt_id);
            }
        });
        btn_startpt = view.findViewById(R.id.btn_start);
        btn_startpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String ptRoom = sp.getString("pt_room","");
                ((MainActivity)getActivity()).goPT(ptRoom);
            }
        });


        Context context = getActivity();
//        sp = context.getSharedPreferences("coffit",Context.MODE_PRIVATE);
//        tv_summary.setText(sp.getString("summary",""));
//        tv_username.setText(sp.getString("trainer_name",""));



        //받아올 때 pt_id 초기 저장
        //일단 지금은 무조건 지우고 새로 만들자. 다만 나중에는 수정해줘야함
        //임시 아이디 1번으로 테스트


//        student_id = sp.getInt("student_id",0);
//        ptIdExist = !(sp.getInt("pt_id",0)==0);
//        ptRoomExist = !(sp.getString("pt_room","").equals(""));
//        Log.d("TAG", "ptIdExist : " + ptIdExist + "ptRoomExist : "+ptRoomExist);
//        Log.d("TAG","before get, pt_id : "+ sp.getInt("pt_id",0)+" ptroom : " + sp.getString("pt_room",""));
//        String pic_url = sp.getString("trainer_pic","");
//        if(!pic_url.equals("")){
//            Picasso.get().load(pic_url).into(iv_mainpic);
//        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        retrofit_home();
    }

    public void retrofit_home(){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<HomeResponse> call = apiInterface.getPT(student_id);
        call.enqueue(new Callback<HomeResponse>(){
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response){
                Log.d("TAG", "apiInterface callback onResponse");
                HomeResponse hr = response.body();
                PT pt = hr.getPt();
                if(pt!=null){


                    PTComment comment = hr.getPtComment();
                    String info = (pt.getTotalNum()- pt.getRestNum())+ "회 완료 " +"(전체 "+ pt.getTotalNum()+"회 중)";
                    tv_ptnum.setText(info);
                    ptRoom = pt.getPtRoom();
                    pt_id = pt.getId();

//                    if(!ptRoomExist){
//                        SharedPreferences.Editor editor = sp.edit();
//                        editor.putInt("pt_id",pt.getId());
//                        editor.putString("pt_room",pt.getPtRoom());
//                        editor.commit();
//                    Log.d("TAG","after get, pt_id : "+ sp.getInt("pt_id",0)+" ptroom : " + sp.getString("pt_room",""));

//                    }

                    if(comment !=null){
                        tv_comment.setText(comment.getComment());
                    }
                    retrofit_detail(pt.getTrainerId());
                }
                else{
                    ((MainActivity)getActivity()).PTing = false;
                    ((MainActivity)getActivity()).setFrag(5);
                }
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
                ((MainActivity)getActivity()).PTing = false;
                ((MainActivity)getActivity()).setFrag(5);

            }
        });
    }

    public void retrofit_detail(int trainerId){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<Trainer> call = apiInterface.getTrainer(trainerId);
        call.enqueue(new Callback<Trainer>(){
            @Override
            public void onResponse(Call<Trainer> call, Response<Trainer> response){
                Log.d("TAG", "apiInterface callback onResponse");

                //트레이너 가지고 화면 세팅
                Trainer trainer = response.body();
                if(trainer != null) {
                    trainer_id = trainer.getId();
                    tv_summary.setText(trainer.getSummary());
                    tv_username.setText(trainer.getUsername());
                    Picasso.get().load(trainer.getPictureURL()).into(iv_mainpic);
                    List<Trainer.ExtraPic> pics = trainer.getExtraPics();

                }

            }

            @Override
            public void onFailure(Call<Trainer> call, Throwable t) {
                Log.d("TAG", "통신 실패");

            }
        });
    }

}
