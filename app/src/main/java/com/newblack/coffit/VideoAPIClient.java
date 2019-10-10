package com.newblack.coffit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoAPIClient {
    public static OkHttpClient client;
    private static Retrofit retrofit = null;
    private static String BASE_URL = "https://api.coffitnow/";

    public static Retrofit getApiService() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
//                .addInterceptor(chain -> {
//                    Request original = chain.request();
//                    Request.Builder requestBuilder = requestBuilder = original.newBuilder()
//                            .method(original.method(), original.body());
//                    Request request = requestBuilder.build();
//                    return chain.proceed(request);
//                }).build();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });


        //For logging the call on Logcat
        HttpLoggingInterceptor interceptor1 = new HttpLoggingInterceptor();
        interceptor1.setLevel(HttpLoggingInterceptor.Level.BODY);
        //세상에 이것때문에 문제였다니 ㅜㅜㅜ
//        httpClient.addInterceptor(interceptor1);
        client = httpClient.build();



        if (retrofit == null) {

            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }

        return retrofit;

    }
}
