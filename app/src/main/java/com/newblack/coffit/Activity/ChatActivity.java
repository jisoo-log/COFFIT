package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Adapter.MessageAdapter;
import com.newblack.coffit.ChatApplication;
import com.newblack.coffit.Data.Message;
import com.newblack.coffit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.myId;
import static com.newblack.coffit.Activity.MainActivity.myName;
import static com.newblack.coffit.Activity.MainActivity.myPic;

public class ChatActivity extends AppCompatActivity {
    private Socket mSocket;
    Button btn_send;
    EditText et_write;
    Toolbar toolbar;

    int student_id;
    String student_name;
    String student_pic;
    int trainer_id;
    String trainer_pic;
    int room_id;
    String trainer_name;
    RecyclerView mRecyclerView;
    MessageAdapter adapter;
    List<Message> messageList;
    SharedPreferences sp;
    APIInterface apiInterface;
    boolean firstConnection = false;
    String fileName;


    //선생용 임시
    String pic = "https://coffit.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%AE%E1%84%8C%E1%85%B5+1.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        student_id = myId;
        student_name = myName;
        student_pic = myPic;
        messageList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);

        trainer_id = intent.getIntExtra("trainer_id",-1);
        trainer_pic = intent.getStringExtra("trainer_pic");
        trainer_name = intent.getStringExtra("trainer_name");
        toolbar.setTitle(trainer_name);

        if(trainer_id==-1 || trainer_name==null){
            Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_LONG).show();
            finish();
        }


        Log.d("TAG","상대방 확인 - id : "+trainer_id + " name : "+ trainer_name + " pic : "+trainer_pic);
        String from = intent.getStringExtra("from");
        if("room".equals(from)) {
            room_id = intent.getIntExtra("room_id", -1);
//            trainer_name = intent.getStringExtra("trainer_name");
            if (room_id == -1) {
                Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_LONG).show();
                finish();
            }
            else retrofit_getMessage(room_id);
            Log.d("TAG","room id 존재 : "+room_id);
        }
        else{
            //첫 대화일 가능성이 있음
            sp = getSharedPreferences("coffit",MODE_PRIVATE);
            fileName = student_id + "_" + trainer_id;
            room_id = sp.getInt(fileName,-1);
            if(room_id==-1){
                //첫 대화
                Log.d("TAG","첫 대화");
                firstConnection = true;
            }
            else{
                //대화 존재
                Log.d("TAG","1:1로 진입했으나 대화 존재");
                retrofit_getMessage(room_id);
            }
        }


        ChatApplication chat = (ChatApplication)getApplication();
        mSocket = chat.getmSocket().connect();
//        if (mSocket.connected()){
            if(!firstConnection){
                //기존 연결 존재
                try {
                    JSONObject data = new JSONObject();
                    data.put("chatting_room_id", room_id);
                    mSocket.emit("joinRoom", data);
                    Log.d("TAG","join room 완료");
                }catch(JSONException e){
                    e.printStackTrace();
                    Log.d("TAG","join room 중 JSON 문제");
                }
            }
            Log.d("TAG","socket is connected");
//        }
//        else{
//            Log.d("TAG","no socket connected");
//        }

        //첫 연결에 대한 응답, 여기서 roomid를 얻어야 함
        mSocket.on("firstConnection", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        Log.d("TAG", " show data : "+ data);
                        try {
//                            String id = (String) data.get("id");
//                            room_id = Integer.parseInt(id);
                            room_id = (int) data.get("id");
                            Log.d("TAG", "만들어진 room id : "+room_id);
                        } catch (Exception e){
                            Log.d("TAG","firstConnection의 응답에 문제가 있습니다");
                            e.printStackTrace();
                        }
                        btn_send.setEnabled(true);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(fileName,room_id);
                        editor.commit();
                        firstConnection=false;
                    }
                });
            }
        });

        mSocket.on("sendMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        Log.d("TAG", " show data : "+ data);
                        Message message;
                        try {
                            String m = (String) data.get("message");
                            Date date = new Date();
                            String dd = (String) data.get("timestamp");
                            String ty = (String) data.get("type");
                            if("student".equals(ty)) return;
                            //TODO : check date type later
//                            SimpleDateFormat format = new SimpleDateFormat("EEE MMM HH:mm:ss z yyyy");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try{
                                date = format.parse(dd);
                            } catch(ParseException e){
                                e.printStackTrace();
                            }

                            message = new Message.Builder()
                                    .message(m)
                                    .time(date)
                                    .type("trainer")
                                    .username(trainer_name)
                                    .pic(trainer_pic)
                                    .build();
                            adapter.addItems(message);
                            mRecyclerView.smoothScrollToPosition(adapter.getItemCount());
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        mRecyclerView = findViewById(R.id.rv_chat);
        adapter = new MessageAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        et_write = findViewById(R.id.et_write);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject message = new JSONObject();
                try {
                    String body = et_write.getText().toString();
                    if(body.equals("")) return;
                    message.put("message", body);
                    message.put("trainer_id", trainer_id);
                    message.put("student_id", student_id);
                    message.put("type", "student");

                    if(firstConnection){
                        //첫 요청의 경우
                        JSONObject room = new JSONObject();

                        room.put("student_name",student_name);
                        room.put("student_id",student_id);
                        room.put("student_picture",student_pic);

                        room.put("trainer_name",trainer_name);
                        room.put("trainer_id",trainer_id);
                        room.put("trainer_picture",trainer_pic);

                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("room",room);
                        jsonObject.put("message",message);

                        mSocket.emit("firstConnection", jsonObject);
                        btn_send.setEnabled(false);
                    }
                    else{
                        //첫 요청이 아닌 경우는 room_id를 같이 보내줌
                        message.put("chatting_room_id", room_id);
                        mSocket.emit("sendMessage", message);
                    }

                    et_write.setText(null);

                    // 본인의 메시지는 서버에서 전달받지 않고, 바로 생성한다.
                    Date date = new Date();
                    Message m = new Message.Builder()
                            .message(body)
                            .type("student")
                            .time(date)
                            .build();
                    adapter.addItems(m);
                    mRecyclerView.smoothScrollToPosition(adapter.getItemCount());

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("chatting_room_id", room_id);
        } catch (JSONException e){
            e.printStackTrace();
        }
        mSocket.emit("closeRoom",jsonObject);
        mSocket.disconnect();
        Log.d("TAG","close room 완료");
    }

    public List<Message> test_data(){
        List<Message> list = new ArrayList<>();
        Date date = new Date();
        Message message = new Message.Builder()
                .message("메시지입니다 ㅎㅎㅎㅎㅎㅎ")
                .username("수지 트레이너")
                .type("trainer")
                .pic(pic)
                .time(date)
                .build();
        list.add(message);
        return list;
    }

    public void retrofit_getMessage(int room_id){
        //TODO : get all msg in this room from the server
        Log.d("TAG", "retrofit_getMessage");
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Message>> call = apiInterface.getMessage(room_id);
        call.enqueue(new Callback<List<Message>>(){
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                List<Message> messages = response.body();

                if(messages!=null) {
                    messageList.addAll(messages);
                    adapter.setMessages(messageList);
                }
                else{
                    Log.d("TAG","getMessage의 onresponse가 비어있습니다");
                    messageList.addAll(test_data());
                    adapter.setMessages(messageList);
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
            }
        });
    }


}
