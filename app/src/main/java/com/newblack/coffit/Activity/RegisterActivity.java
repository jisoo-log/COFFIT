package com.newblack.coffit.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.R;

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

public class RegisterActivity extends AppCompatActivity {
    TextView tv_apply;
    EditText et_name;
    EditText et_phone;
    EditText et_age;
    EditText et_email;
    CircleImageView iv_profile;
    final int GALLERY_CODE = 3333;
    String image_path;
    APIInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tv_apply = findViewById(R.id.tv_apply);
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_age = findViewById(R.id.et_age);
        et_email = findViewById(R.id.et_email);
        iv_profile = findViewById(R.id.iv_profile);

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
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    cursor.close();
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

    public void apply(View view){
        String name = et_name.getText().toString();
        String summary = et_email.getText().toString();
        String phone = et_phone.getText().toString();
        int age;
        if(name.equals("") || summary.equals("") || phone.equals("")) {
            Toast.makeText(this,"빈 칸을 모두 채워주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            age = Integer.parseInt(et_age.getText().toString());
        } catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this,"가격은 숫자값으로 입력해주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        if(image_path == null){
            Toast.makeText(this,"기본 이미지 등록이 필요합니다",Toast.LENGTH_SHORT).show();
        }
        else{
            retrofit_apply(image_path);
        }

    }

    public void retrofit_apply(String filePath){
        File file = new File(filePath);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("profilePicture", file.getName(), fileReqBody);
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"),
                et_name.getText().toString());
        RequestBody age = RequestBody.create(MediaType.parse("multipart/form-data"),
                et_age.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"),
                et_email.getText().toString());
        RequestBody phone = RequestBody.create(MediaType.parse("multipart/form-data"),
                et_phone.getText().toString());
        RequestBody gender = RequestBody.create(MediaType.parse("multipart/form-data"),
                "남성");


        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.postStudent(part, name, email, age, phone, gender);
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
        if (DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
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
