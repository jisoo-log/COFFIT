package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.newblack.coffit.Fragment.AccountFragment;
import com.newblack.coffit.Fragment.HomeFragment;
import com.newblack.coffit.R;
import com.newblack.coffit.Fragment.TrainerFragment;
import com.newblack.coffit.Fragment.TrainerListFragment;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;
    Toolbar toolbar;
    FragmentManager fm;
    FragmentTransaction ft;

    private TrainerFragment trainerFragment;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private TrainerListFragment trainerListFragment;

    final int H_FRAGMENT = 1;
    final int T_FRAGMENT = 2;
    final int A_FRAGMENT = 3;
    final int TL_FRAGMENT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNav = findViewById(R.id.bottomNav);

        //프래그먼트 정의
        trainerFragment = new TrainerFragment();
        homeFragment = new HomeFragment();
        accountFragment = new AccountFragment();
        trainerListFragment = new TrainerListFragment();


        //결제 여부에 따라 변경 필요!
        setFrag(T_FRAGMENT);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.bmenu_home:
                        setFrag(H_FRAGMENT);
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
                Toast.makeText(this,"찾기 버튼",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_alarm:
                Toast.makeText(this,"노티 버튼",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //메뉴 부분 끝


    //프래그먼트 전환
    public void setFrag(int n){
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
                Log.d("TAG", "setFrag TL_FRAGMENT");
                ft.replace(R.id.mainFrame,trainerListFragment);
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
}
