package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Data.Student;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText et_login;
    CheckBox checkBox;
    SharedPreferences sp;
    APIInterface apiInterface;
    Student student;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_login);
        et_login = findViewById(R.id.et_id);
        checkBox = findViewById(R.id.checkbox);

        sp = getSharedPreferences("coffit",MODE_PRIVATE);
        if(sp.getBoolean("auto",false)){
            int id = sp.getInt("student_id",-1);
            if(id==-1) {
                Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("student_id",id);
                startActivity(intent);
                finish();
            }
        }
    }

    public void click_login(View view){
        try {
            int id = Integer.parseInt(et_login.getText().toString());
            check_student(id);
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"올바른 id를 입력해주세요",Toast.LENGTH_LONG).show();
            et_login.setText("");
        }

    }

    public void check_student(int student_id){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<Student> call = apiInterface.getStudent(student_id);
        call.enqueue(new Callback<Student>(){
            @Override
            public void onResponse(Call<Student> call, Response<Student> response){
                Log.d("TAG", "apiInterface callback onResponse");

                student = response.body();
                Log.d("TAG",student.toString());
                if(student != null) {
                    //기존에 존재하는 트레이너
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("student_id",student_id);
                    if(checkBox.isChecked()){
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("student_id",student_id);
                        editor.putBoolean("auto",true);
                        editor.commit();
                    }
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(activity,"오류 발생",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Log.d("TAG", "통신 실패");
                //새로운 학생 등록 필요
                //생각해보니 이거 id로 구분하면 안되는데 ㅋㅋㅋㅋ
                Intent intent = new Intent(activity, RegisterActivity.class);
                intent.putExtra("trainer_id",student_id);
                Toast.makeText(activity, "등록되지 않은 id로, 가입 화면으로 이동합니다", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
