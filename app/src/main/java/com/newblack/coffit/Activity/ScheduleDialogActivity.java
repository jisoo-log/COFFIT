package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.R;

public class ScheduleDialogActivity extends AppCompatActivity {
    Schedule schedule;
    //요청들 넣기


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        schedule = (Schedule) intent.getSerializableExtra("schedule");

    }


}
