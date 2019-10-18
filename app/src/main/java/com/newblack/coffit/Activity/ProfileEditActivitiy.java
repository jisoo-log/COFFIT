package com.newblack.coffit.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import java.io.File;
import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    int student_id;
    final int GALLERY_CODE = 3333;
    String image_path;

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
            tv_age.setText(String.valueOf(student.getAge()));
            tv_email.setText(student.getEmail());
            tv_phone.setText(student.getPhoneNum());
            if(!student.getPicture().equals("")){
                Picasso.get().load(student.getPicture()).into(iv_profile);
            }
        }

        student_id = myId;
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
//            retrofit_put_student(id);
            retrofit_putStudent(student_id,image_path);
        }
        return super.onOptionsItemSelected(item);
    }

    public void pickFromGallery(View view){
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

                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
//                    iv_profile.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                    for(String s : filePathColumn){
                        Log.d("TAG","file path column : " + s);
                    }

                    try {
                        image_path = getFilePath(this, selectedImage);
                        Log.d("TAG", "filepath: " + image_path);
                    } catch (URISyntaxException e){
                        e.printStackTrace();
                        Log.d("TAG", "wrong to print uri");
                    }

                    break;

            }
        }
    }


    public void retrofit_putStudent(int id, String filePath){
        Log.d("TAG"," id : "+id);
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("profilePicture", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"),
                tv_name.getText().toString());
        RequestBody age = RequestBody.create(MediaType.parse("multipart/form-data"),
                tv_age.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"),
                tv_email.getText().toString());
        RequestBody phone = RequestBody.create(MediaType.parse("multipart/form-data"),
                tv_phone.getText().toString());


        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.putStudentForm(part, name, email, age, phone, id);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                if(response.isSuccessful()){
                    Log.d("TAG","retrofit put 수행을 완료했습니다.");
                }
                else{
                    Log.d("TAG","retrofit put onResponse의 response에 문제가 있습니다.");
                }
                finish();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
            }
        });
    }


    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
