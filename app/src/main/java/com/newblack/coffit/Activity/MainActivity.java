package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonObject;
import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Data.Student;
import com.newblack.coffit.Fragment.AccountFragment;
import com.newblack.coffit.Fragment.HomeFragment;
import com.newblack.coffit.Fragment.HomeNewFragment;
import com.newblack.coffit.R;
import com.newblack.coffit.Fragment.TrainerFragment;
import com.newblack.coffit.Fragment.TrainerListFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;
    Activity activity;
    Toolbar toolbar;
    FragmentManager fm;
    FragmentTransaction ft;
    SharedPreferences sp;
    public static boolean PTing;
    //TODO : need to get id when ppl login
    public static int myId = 1;
    APIInterface apiInterface;

    private TrainerFragment trainerFragment;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private TrainerListFragment trainerListFragment;
    private HomeNewFragment homeNewFragment;

    static final int H_FRAGMENT = 1;
    static final int T_FRAGMENT = 2;
    static final int A_FRAGMENT = 3;
    static final int TL_FRAGMENT = 4;
    static final int HN_FRAGMENT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNav = findViewById(R.id.bottomNav);

        PTing = true;

        //프래그먼트 정의
        trainerFragment = new TrainerFragment();
        homeFragment = new HomeFragment();
        accountFragment = new AccountFragment();
        trainerListFragment = new TrainerListFragment();
        homeNewFragment = new HomeNewFragment();

        activity = this;
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>(){
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("TAG", "token : "+token);
//                        Toast.makeText(activity,token,Toast.LENGTH_LONG).show();
                        retrofit_token(token,myId);
                    }
                });




        //결제 여부에 따라 변경 필요!
        //일단 지금은 선생 이름이 있으면 결제한 것으로 판단.. -> 나중에 문제가 생길까?
        sp = getSharedPreferences("coffit",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
//        editor.clear();
////        editor.putBoolean("hasPT",true);
//        editor.commit();
////        editor.remove("pt_id");
////        editor.remove("pt_room");
////
//        editor.putInt("student_id",1);

//        hasPT =sp.getBoolean("hasPT",false);
//        String trainer_name = sp.getString("trainer_name","none");

        //TODO : set initial fragment
        setFrag(H_FRAGMENT);
//        if(!hasPT){
//            PTing = false;
//            setFrag(T_FRAGMENT);
//        }
//        else{
//            PTing = true;
//            setFrag(H_FRAGMENT);
//        }
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.bmenu_home:
                        if(PTing){
                            setFrag(H_FRAGMENT);
                        }
                        else {
                            setFrag(HN_FRAGMENT);
                        }
                        break;
                    case R.id.bmenu_trainer:
                        setFrag(T_FRAGMENT);
                        break;
                    case R.id.bmenu_account:
                        setFrag(A_FRAGMENT);
                        break;
                }
                return true;
            }
        });




    }

    //뒤로가기 할때 종료되지 않도록
    public interface onKeyBackPressedListener{
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener){
        mOnKeyBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if(mOnKeyBackPressedListener != null){
            setFrag(T_FRAGMENT);
        }
        else{
            super.onBackPressed();
        }
    }

    //찾기와 노티 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_search:
                Toast.makeText(this,"노티 버튼",Toast.LENGTH_SHORT).show();
                //for time test -> end!
//                Intent intent2 = new Intent(this,TimeControlActivity.class);
//                startActivity(intent2);
                break;
            case R.id.menu_alarm:
                Intent intent = new Intent(this,NotiActivity.class);
                startActivity(intent);
                //Toast.makeText(this,"노티 버튼",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //메뉴 부분 끝


    //프래그먼트 전환
    public void setFrag(int n){
        Log.d("TAG","check pt : "+PTing);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch(n){
            case H_FRAGMENT:
                ft.replace(R.id.mainFrame,homeFragment);
                ft.commit();
                break;
            case T_FRAGMENT:
                ft.replace(R.id.mainFrame,trainerFragment);
                ft.commit();
                break;
            case A_FRAGMENT:
                ft.replace(R.id.mainFrame,accountFragment);
                ft.commit();
                break;
            case TL_FRAGMENT:
                ft.replace(R.id.mainFrame,trainerListFragment);
                ft.commit();
                break;
            case HN_FRAGMENT:
                ft.replace(R.id.mainFrame,homeNewFragment);
                ft.commit();
                break;

        }


    }

    public void goTrainerDetail(int id){
        Intent intent = new Intent(this, TrainerDetailActivity.class);
        intent.putExtra("trainerId",id);
        startActivity(intent);
    }

    public void goSchedule(){
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }

    public void goMission(){
        Intent intent = new Intent(this, MissionActivity.class);
        startActivity(intent);
    }

    public void goPT(String ptRoom){
//        Toast.makeText(this, "PT 시작!!",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,com.example.webrtc.android.ConnectActivity.class);
//        room_number = 1323232323;
        Log.d("TAG", "room number : "+ptRoom);
        intent.putExtra("room_number",ptRoom);
        startActivity(intent);
    }

    public void editProfile(Student student){
        Intent intent = new Intent(this, ProfileEditActivitiy.class);
        intent.putExtra("student",student);
        startActivity(intent);
    }

    public void retrofit_token(String token, int student_id){
        JsonObject object = new JsonObject();
        object.addProperty("fcm_token",token);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiInterface.postToken(object,student_id).enqueue(new Callback<Student>(){
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                Log.d("TAG","post token success");
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Log.d("TAG","post token failed");
            }
        });
    }
}
