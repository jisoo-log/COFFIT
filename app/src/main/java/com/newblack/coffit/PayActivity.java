package com.newblack.coffit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PayActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tv_count;
    int ptNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("PT 결제");
        tv_count = findViewById(R.id.tv_count);
        ptNum=4;
        tv_count.setText(String.valueOf(ptNum));

    }

    public void clickPay(View view){
        //결제화면 나중에 붙이기
    }

    public void plus(View view){
        ptNum++;
        tv_count.setText(String.valueOf(ptNum));
    }

    public void minus(View view){
        ptNum--;
        tv_count.setText(String.valueOf(ptNum));
    }
}
