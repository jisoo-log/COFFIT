package com.newblack.coffit.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.JsonObject;
import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Adapter.TagAdapter;
import com.newblack.coffit.Data.ExerciseVideo;
import com.newblack.coffit.Data.Fields;
import com.newblack.coffit.Data.Mission;
import com.newblack.coffit.FileUploadService;
import com.newblack.coffit.R;
import com.newblack.coffit.Response.MissionResponse;
import com.newblack.coffit.Response.VideoResponse;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionDetailActivity extends AppCompatActivity {
    APIInterface apiInterface;
    String uploadUrl;
    final int GALLERY_CODE = 3333;
    final int BACK_PRESSED = 99;
    final int FILE_UPLOAD = 88;
//    ImageView iv_mission;
    Activity activity;
//    Toolbar toolbar;

    RatingBar show_rating;
    Button btn_delete;
    TextView tv_feedback;
    TextView tv_mission_dt;
    TextView tv_no_video;
    LinearLayout ll_mission_list;
    LinearLayout ll_has_feedback;
    TagAdapter adapter;
    RecyclerView mRecyclerView;
    List<String> times;
    String path;
    MediaSource mediaSource;


    CalendarDay selectedDay;
    int student_id;
    int trainer_id;
    int mission_id;
    int video_id = -1;

    private PlayerView exoPlayerView;
    private SimpleExoPlayer player;

    String selected;
//    Mission mission;
    ExerciseVideo video;
    Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mission_detail);
        activity =this;
//        iv_mission = findViewById(R.id.iv_mission);


        show_rating = findViewById(R.id.show_rating);
        btn_delete = findViewById(R.id.btn_delete);
        ll_mission_list = findViewById(R.id.ll);
        ll_has_feedback = findViewById(R.id.ll_has_feedback);
        tv_feedback = findViewById(R.id.tv_feedback);
        tv_no_video = findViewById(R.id.tv_no_video);

        mRecyclerView = findViewById(R.id.rv_tag);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        adapter = new TagAdapter();
        mRecyclerView.setAdapter(adapter);

        //TODO : delete here after get real time from server
        times = new ArrayList<>();
        times.addAll(getTagText("00:02,00:04"));

        adapter.setOnItemClickListener(new TagAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String time) {
                player.setPlayWhenReady(false);
                int min = Integer.parseInt(time.split(":")[0]);
                int sec = Integer.parseInt(time.split(":")[1]);
                int position = (min*60+sec)*1000;
                Log.d("TAG","start position : "+position);
                player.seekTo(position);
                player.setPlayWhenReady(true);
            }
        });


        Intent intent = getIntent();
        selected = intent.getStringExtra("date");
        mission_id = intent.getIntExtra("mission_id",-1);
        trainer_id = intent.getIntExtra("trainer_id",-1);

        if(mission_id==-1 || trainer_id==-1 || selected==null){
            //if can't find mission_id, finish this activity
            Toast.makeText(this, "미션이 존재하지 않습니다", Toast.LENGTH_LONG).show();
            finish();
        }
        Log.d("TAG","selected day : "+selected);
        int year = Integer.parseInt(selected.split("-")[0]);
        int month = Integer.parseInt(selected.split("-")[1]);
        int day = Integer.parseInt(selected.split("-")[2]);
        Log.d("TAG", "parsed initialDay" + year + " " + month + " "+ day);
        String title = year +"년 " + month +"월 "+day +"일 운동미션";
        tv_mission_dt = findViewById(R.id.tv_mission_dt);
        tv_mission_dt.setText(title);

        exoPlayerView = findViewById(R.id.exoPlayerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrofit_getMission(mission_id);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(myLocalBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("result");
            //TODO: change event when upload end
            tv_feedback.setText("완료");
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocalBroadcastReceiver);
    }

    private void initializePlayer(String url) {
        if(url==null){
            Log.d("TAG","영상 없음");

        }
        else {
            if (player == null) {
                DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this.getApplicationContext());
                DefaultTrackSelector trackSelector = new DefaultTrackSelector();
                DefaultLoadControl loadControl = new DefaultLoadControl();

                player = ExoPlayerFactory.newSimpleInstance(
                        this.getApplicationContext(),
                        renderersFactory,
                        trackSelector,
                        loadControl);

                exoPlayerView.setPlayer(player);
            }

            Log.d("TAG", "url : " + url);
            mediaSource = buildMediaSource(Uri.parse(url));
            //prepare
            player.prepare(mediaSource, true, false);
            //if true, start automaticially
            player.setPlayWhenReady(false);
        }
    }

    //나중에 영상파일에 따라 확인 필요
    private MediaSource buildMediaSource(Uri uri) {

        String userAgent = Util.getUserAgent(this, "blackJin");

        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {

            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);

        } else if (uri.getLastPathSegment().contains("m3u8")) {

            //com.google.android.exoplayer:exoplayer-hls 확장 라이브러리를 빌드 해야 합니다.
            return new HlsMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);

        } else {

            return new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(this, userAgent))
                    .createMediaSource(uri);
        }

    }

    private void releasePlayer() {
        if (player != null) {

            exoPlayerView.setPlayer(null);
            player.release();
            player = null;

        }
    }

    public void deleteVideo(View view){
        retrofit_deleteVideo(video_id, mission_id);
    }

    public void pickFromGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(BACK_PRESSED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case GALLERY_CODE:
                    Uri selectedImage = data.getData();
                    ContentResolver cR = getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getExtensionFromMimeType(cR.getType(selectedImage));
                    Log.d("TAG", "real type ? : " + type);
//                    iv_profile.setImageURI(selectedImage);


                    try {
                        path = getFilePath(this, selectedImage);
                        Log.d("TAG", "filepath: " + path);

                    } catch (URISyntaxException e){
                        e.printStackTrace();
                        Log.d("TAG", "wrong to print uri");
                    }

                    HashMap<String, Object> video = new HashMap<>();
                    video.put("mission_id",mission_id);
                    video.put("videoFormat",type);
                    //TODO: check student_Id 오타
                    video.put("student_id",student_id);
                    video.put("trainer_id",trainer_id);

                    //go FileUploadService and upload video
                    retrofit_postvideo(video);

                    //finish app
                    Intent intent = new Intent();
                    intent.putExtra("blocked_id",mission_id);
                    setResult(FILE_UPLOAD,intent);
                    finish();
                    break;
            }
        }
    }

    public void addTextView(String text){
        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv1.setText(text);
        tv1.setBackgroundColor(getResources().getColor(android.R.color.white)); // hex color 0xAARRGGBB
        tv1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
        ll_mission_list.addView(tv1);
    }

    public void getMissionText(String text){
        if(text==null) return;
        ll_mission_list.removeAllViews();
        String[] missions = text.split(",");
        for (String s : missions){
            String bt = "\u2022 " + s;
            addTextView(bt);
        }
    }

    public List<String> getTagText(String text){
        List<String> list = new ArrayList<>();
        String[] timeList = text.split(",");
        for (String s : timeList){
            Log.d("TAG","get time : "+s);
            list.add(s);
        }
        return list;
    }

    public void setTagAdapter(ExerciseVideo video,Bitmap bitmap){
        if(video.getTime_tag()!=null) times.addAll(getTagText(video.getTime_tag()));
        else Log.d("TAG", "no timetag in video");
        if(bitmap==null) Log.d("TAG","no thumbnail of tag");
        adapter.setTimes(getTagText(video.getTime_tag()),bitmap);
    }


    public void checkFeedback(Mission m){
        if(m.getComment()!=null){
            ll_has_feedback.setVisibility(View.VISIBLE);
            tv_feedback.setText(m.getComment());
            show_rating.setRating(m.getRate());
        }
        else{
            show_rating.setVisibility(View.GONE);
            tv_feedback.setText("아직 피드백이 없습니다");
        }
    }

    public void retrofit_getMission(int mission_id){
        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("미션 여는중...");
        mProgressDialog.show();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<MissionResponse> call = apiInterface.getMission(mission_id);
        call.enqueue(new Callback<MissionResponse>(){
            @Override
            public void onResponse(Call<MissionResponse> call, Response<MissionResponse> response) {
                Log.d("TAG", "apiInterface callback onResponse");
                MissionResponse mission = response.body();
                if (mission != null) {
                    video = mission.getVideo();
                    checkFeedback(mission);
                    getMissionText(mission.getContent());

                    if(video != null){
                        exoPlayerView.setVisibility(View.VISIBLE);
                        tv_no_video.setVisibility(View.GONE);
                        btn_delete.setVisibility(View.VISIBLE);

                        video_id = video.getId();
                        //get thumbnail bitmap from video url
                        bitmap = null;
                        MediaMetadataRetriever retriever = null;
                        try {
                            retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(mission.getVideoUrl(), new HashMap<String, String>());
                            bitmap = retriever.getFrameAtTime(20000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                            adapter.setTimes(times,bitmap);
                        } catch(Exception e){
                            e.printStackTrace();
                            Log.d("TAG", "can't get thumbnail");
                        } finally{
                            if (retriever!= null){
                                retriever.release();
                            }
                        }

                        //initalize video + set thumbnail + set tag
                        initializePlayer(mission.getVideoUrl());
                        if(mission.is_converted()){
                            setTagAdapter(video,bitmap);
                        }
                    }
                    else {
                        Log.d("TAG","no video");
                        tv_no_video.setVisibility(View.VISIBLE);
                        exoPlayerView.setVisibility(View.VISIBLE);
                    }

                }
                if(mProgressDialog.isShowing()) mProgressDialog.dismiss();

            }
            @Override
            public void onFailure(Call<MissionResponse> call, Throwable t) {
                t.printStackTrace();
                tv_no_video.setVisibility(View.VISIBLE);
                tv_no_video.setText("앗 오류가 발생했어요 ><");
                Log.d("TAG", "통신 실패");
                if(mProgressDialog.isShowing()) mProgressDialog.dismiss();
            }
        });
    }

    public void retrofit_postvideo(HashMap<String, Object> video){
        //TODO : post 준비과정. 여기서 영상format을 보내줘야함
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<VideoResponse> call = apiInterface.postExerciseVideo(video);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if(response.isSuccessful()){
                    Log.d("TAG","retrofit post video 수행을 완료했습니다.");
                    uploadUrl = response.body().getUrl();
                    Fields fields = response.body().getFields();
                    Log.d("TAG", "url : "+uploadUrl);
                    Log.d("TAG","field info : "+ fields.getKey() + " " + fields.getBucket() + " " + fields.getPolicy()+
                            " " + fields.getXAmzAlgorithm() + " " + fields.getXAmzCredential() +
                            " " + fields.getXAmzDate() + " " + fields.getXAmzSignature());

                    Intent intent = new Intent(activity, FileUploadService.class);
                    Log.d("TAG", " check path before go Service : "+ path);

                    intent.putExtra("mission_id",mission_id);
                    intent.putExtra("video_id",video_id);
                    intent.putExtra("fields",fields);
                    intent.putExtra("path",path);
                    intent.putExtra("url",uploadUrl);
                    FileUploadService.enqueueWork(activity,intent);

                }
                else{
                    Log.d("TAG","retrofit post video onResponse의 response에 문제가 있습니다.");
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });

    }

    public void retrofit_deleteVideo(int videoId, int missionId){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.deleteVideo(videoId, missionId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("TAG","retrofit delete 수행을 완료했습니다.");

                //refresh after delete video
                retrofit_getMission(missionId);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG","retrofit delete 수행에 문제가 있습니다");
            }
        });
    }




    //get real file path
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.d("TAG", "content part");
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
        }
        return null;
    }

}
