package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Data.Student;
import com.newblack.coffit.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.myId;

public class ProfileEditActivitiy extends AppCompatActivity {
    Student student;
    TextView tv_edit_profile;
    EditText tv_name;
    EditText tv_email;
    EditText tv_age;
    EditText tv_phone;
    CircleImageView iv_profile;
    Toolbar toolbar;
    APIInterface apiInterface;
    int id;
    final int GALLERY_CODE = 3333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_activitiy);
        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("student");
        if(student==null){
            Toast.makeText(this, "잘못된 접근입니다",Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            iv_profile = findViewById(R.id.iv_profile);
            tv_name = findViewById(R.id.tv_name);
            tv_email =findViewById(R.id.tv_email);
            tv_age = findViewById(R.id.tv_age);
            tv_phone = findViewById(R.id.tv_phone);

            tv_name.setText(student.getUsername());
            tv_age.setText(student.getAge());
            tv_email.setText(student.getEmail());
            tv_phone.setText(student.getPhoneNum());
            if(!student.getPicture().equals("")){
                Picasso.get().load(student.getPicture()).into(iv_profile);
            }
        }

        id = myId;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.toolbar_save){
            retrofit_put_student(id);
        }
        return super.onOptionsItemSelected(item);
    }

    public void pickFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case GALLERY_CODE:
                    Uri selectedImage = data.getData();
                    iv_profile.setImageURI(selectedImage);
                    break;
            }
        }
    }

    public void retrofit_put_student(int id){
        JsonObject student = new JsonObject();
        student.addProperty("username", tv_name.getText().toString());
        student.addProperty("age", tv_age.getText().toString());
        student.addProperty("email", tv_email.getText().toString());
        student.addProperty("phone_number", tv_phone.getText().toString());


        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call = apiInterface.putStudent(student, id);
        call.enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response){
                if(response.isSuccessful()){
                    Log.d("TAG","retrofit put 수행을 완료했습니다.");
                }
                else{
                    Log.d("TAG","retrofit put onResponse의 response에 문제가 있습니다.");
                }
                finish();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });

    }
}
