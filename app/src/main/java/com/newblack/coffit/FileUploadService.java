package com.newblack.coffit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.newblack.coffit.Activity.CountingRequestBody;
import com.newblack.coffit.Data.Fields;

import java.io.File;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadService extends JobIntentService {
    private static final String TAG = "FileUploadService";
    Disposable mDisposable;
    int video_id;
    int mission_id;
    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 102;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        /**
         * Download/Upload of file
         * The system or framework is already holding a wake lock for us at this point
         */

        // get file file here
        String mFilePath = intent.getStringExtra("path");
        String url = intent.getStringExtra("url");
        Fields fields = (Fields)intent.getSerializableExtra("fields");
        video_id = intent.getIntExtra("video_id",-1);
        mission_id = intent.getIntExtra("mission_id",-1);
        Log.d("TAG","확인 url : "+url+" key : "+fields.getKey());
        if(mFilePath==null){
            Log.e(TAG, "onHandleWork: Invalid file URI");
            return;
        }
        else if(url==null){
            Log.e(TAG, "onHandleWork: Invalid url");
            return;
        }
        else if(mission_id==-1){
            Log.e(TAG, "onHandleWork: Invalid misison id");
            return;
        }


        APIInterface apiService = VideoAPIClient.getApiService().create(APIInterface.class);

        RequestBody key = createRequestBodyFromText(fields.getKey());
        RequestBody bucket = createRequestBodyFromText(fields.getBucket());
        RequestBody algorithm = createRequestBodyFromText(fields.getXAmzAlgorithm());
        RequestBody credential = createRequestBodyFromText(fields.getXAmzCredential());
        RequestBody date = createRequestBodyFromText(fields.getXAmzDate());
        RequestBody policy = createRequestBodyFromText(fields.getPolicy());
        RequestBody signature = createRequestBodyFromText(fields.getXAmzSignature());

        Log.d("TAG","field info : "+ fields.getKey() + " " + fields.getBucket() + " " + fields.getPolicy()+
                " " + fields.getXAmzAlgorithm() + " " + fields.getXAmzCredential() +
                " " + fields.getXAmzDate() + " " + fields.getXAmzSignature());


        Flowable<Double> fileObservable = Flowable.create(emitter -> {
            apiService.postVideo(url,key,bucket,algorithm,credential,date,policy,signature,createMultipartBody(mFilePath, emitter)).blockingGet();
            emitter.onComplete();
        }, BackpressureStrategy.LATEST);

        mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(progress -> onProgress(progress), throwable -> onErrors(throwable), () -> onSuccess());
    }



    private void onErrors(Throwable throwable) {

        Log.d("TAG","error : "+throwable.getMessage());
        Log.d("TAG","error : "+throwable.toString());
//        sendBroadcastMeaasge("Error in file upload " + throwable.getMessage());
        Log.e(TAG, "onErrors: ", throwable);
    }


    private void onProgress(Double progress) {
        //TODO : use this if we need show progress
//        sendBroadcastMeaasge("Uploading in progress... " + (int) (100 * progress));
        Log.i(TAG, "onProgress: " + progress);
    }

    //TODO: need to add listener to know file upload finished
    private void onSuccess() {
        sendBroadcastMeaasge("File uploading successful ");

        //put after upload success!
        retrofit_putMission(mission_id, video_id);
        Log.i(TAG, "onSuccess: File Uploaded");

    }

    public void sendBroadcastMeaasge(String message) {
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("result", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private RequestBody createRequestBodyFromFile(File file, String mimeType) {
        return RequestBody.create(MediaType.parse(mimeType), file);
    }

    private RequestBody createRequestBodyFromText(String mText) {
        return RequestBody.create(MediaType.parse("text/plain"), mText);
    }


    /**
     * return multi part body in format of FlowableEmitter
     *
     * @param filePath
     * @param emitter
     * @return
     */
    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);
        return MultipartBody.Part.createFormData("file", file.getName(), createCountingRequestBody(file, "video/*", emitter));
    }

    private RequestBody createCountingRequestBody(File file, String mimeType, FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBodyFromFile(file, mimeType);
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) -> {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }


    public void retrofit_putMission(int missionId, int exVideoId){
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Void> call = apiInterface.putMission(missionId, exVideoId);
        call.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("TAG","Success : change has_video in mission & delete ex video");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG","Failure : problem with put mission");
            }
        });
    }
}