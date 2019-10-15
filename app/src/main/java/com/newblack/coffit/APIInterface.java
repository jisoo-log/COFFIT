package com.newblack.coffit;

import android.app.DownloadManager;

import com.google.gson.JsonObject;
import com.newblack.coffit.Data.Mission;
import com.newblack.coffit.Data.Noti;
import com.newblack.coffit.Data.PT;
import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.Data.Student;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.Data.TrainerSchedule;
import com.newblack.coffit.Response.HomeResponse;
import com.newblack.coffit.Response.MissionResponse;
import com.newblack.coffit.Response.NotiResponse;
import com.newblack.coffit.Response.TrainerResponse;
import com.newblack.coffit.Response.TrainerScheduleResponse;
import com.newblack.coffit.Response.VideoResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {

    @GET("home")
    Call<TrainerResponse> getMain();

    @GET("trainers")
    Call<List<Trainer>> getTrainerList();

    @GET("trainers/{id}")
    Call<Trainer> getTrainer(@Path("id") int trainerId);

    @FormUrlEncoded
    @POST("pts")
    Call<PT> postPT(@FieldMap HashMap<String,Object> param);

    @GET("pts/students/{id}")
    Call<HomeResponse> getPT(@Path("id") int studentId);

    @FormUrlEncoded
    @POST("schedules")
    Call<Schedule> postSchedule(@FieldMap HashMap<String,Object> param);

    @PUT("schedules/{scheduleId}")
    Call<Schedule> putSchedule(@Body Schedule schedule, @Path("scheduleId") int scheduleId, @Query("iAm") String iAm);

    @DELETE("schedules/{scheduleId}")
    Call<ResponseBody> deleteSchedule(@Path("scheduleId") int scheduleId);

    @GET("schedules/students/{studentId}")
    Call<List<Schedule>> getSchedule(@Path("studentId") int studentId);

    @GET("schedules/trainers/{trainerId}")
    Call<TrainerScheduleResponse> getTrainerSchedule(@Path("trainerId") int trainerId);



    @GET("notifications/students/{id}")
    Call<List<NotiResponse>> getNotiList(@Path("id") int studentId);

    @GET("students/{studentId}")
    Call<Student> getStudent(@Path("studentId") int studentId);

    @Multipart
    @PUT("students/{studentId}")
    Call<ResponseBody> putStudentForm(@Part MultipartBody.Part file,
                                      @Part("username") RequestBody name,
                                      @Part("email") RequestBody email,
                                      @Part("age") RequestBody age,
                                      @Part("phone_number") RequestBody phone,
                                      @Path("studentId") int studentId);

    @GET("/missions/students/{studentId}")
    Call<List<Mission>> getMissionList(@Path("studentId") int studentId);


    @GET("/missions/{missionId}")
    Call<MissionResponse> getMission(@Path("missionId") int missionId);

    @PUT("/missions/hasVideos/{missionId}")
    Call<Void> putMission(@Path("missionId") int missionId, @Query("pastExerciseVideoId") int videoId);


    @FormUrlEncoded
    @POST("/exerciseVideos")
    Call<VideoResponse> postExerciseVideo(@FieldMap HashMap<String,Object> param);


    @Multipart
    @POST
    Completable postVideo(@Url String urlPath,
                            @Part("key") RequestBody key,
                            @Part("bucket") RequestBody bucket,
                            @Part("X-Amz-Algorithm") RequestBody algorithm,
                            @Part("X-Amz-Credential") RequestBody credential,
                            @Part("X-Amz-Date") RequestBody date,
                            @Part("Policy") RequestBody policy,
                            @Part("X-Amz-Signature") RequestBody signature,
                            @Part MultipartBody.Part file);

    @DELETE("/exerciseVideos/{exerciseVideoId}")
    Call<ResponseBody> deleteVideo(@Path("exerciseVideoId") int videoId, @Query("missionId") int mission_id);

    //TODO : 여기 마저 채우기
    //이전 비디오 아이디, 미션 아이디 -> 비디오 새로 업데이트 한 경우에 과거 비디오를 삭제하고 현재 미션에 영상을 넣을 수 있도록
    @PUT("/exerciseVideos/{excerciseVideoId}")
    Call<ResponseBody> putVideo(@Path("exerciseVideoId") int videoId, @Query("missionId") int mission_id);

    @POST("/students/{studentId}/token")
    Call<Student> postToken(@Body JsonObject param,  @Path("studentId") int student_id);

}
