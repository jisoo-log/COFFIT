package com.newblack.coffit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Adapter.ChatRoomAdapter;
import com.newblack.coffit.Data.ChatRoom;
import com.newblack.coffit.Data.Message;
import com.newblack.coffit.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.myId;

public class ChatRoomActivity extends AppCompatActivity {
    Activity activity;
    RecyclerView mRecyclerView;
    ChatRoomAdapter adapter;
    List<ChatRoom> roomList;
    Toolbar toolbar;
    APIInterface apiInterface;
    int student_id;

    //선생용 임시
    String pic = "https://coffit.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%AE%E1%84%8C%E1%85%B5+1.jpg";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        activity = this;
        mRecyclerView = findViewById(R.id.rv_room);
        adapter = new ChatRoomAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("채팅방");
        student_id = myId;

        adapter.setOnItemClickListener(new ChatRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(activity, ChatActivity.class );
                ChatRoom curRoom = roomList.get(position);
                intent.putExtra("room_id",curRoom.getId());
                intent.putExtra("trainer_name",curRoom.getTrainer_name());
                intent.putExtra("trainer_id",curRoom.getTrainer_id());
                intent.putExtra("trainer_pic",curRoom.getTrainer_pic());
                intent.putExtra("from","room");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO : change it to real data
        retrofit_getRoom(student_id);
    }

    public List<ChatRoom> test_data(){
        List<ChatRoom> rooms = new ArrayList<>();

        Date date = new Date();
        Message msg = new Message.Builder()
                .message("마지막 메시지")
                .time(date)
                .type("student")
                .username("신민욱")
                .build();
        Log.d("TAG", "test : " + msg.getMessage());
        ChatRoom chatRoom = new ChatRoom.Builder()
                .id(1)
                .time(msg.getTime())
                .newMsg(3)
                .title("수지")
                .message(msg.getMessage())
                .pic(pic)
                .build();
        rooms.add(chatRoom);
        Log.d("TAG","size : " + rooms.size());
        return rooms;
    }


    public void retrofit_getRoom(int student_id){
        //TODO : 속한 채팅방을 전부 불러옴
        Log.d("TAG", "retrofit_GetRoom");
        roomList = new ArrayList<>();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<ChatRoom>> call = apiInterface.getRoomList(student_id);
        call.enqueue(new Callback<List<ChatRoom>>(){
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response){
                Log.d("TAG", "apiInterface callback onResponse");
                List<ChatRoom> rooms = response.body();

                if(rooms!=null) {
                    roomList.addAll(rooms);
                    adapter.setRoomList(roomList);
                }
                else{
                    Toast.makeText(activity, "방이 존재하지 않습니다", Toast.LENGTH_LONG).show();
                    roomList.addAll(test_data());
                    adapter.setRoomList(roomList);
                }
            }

            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "통신 실패");
            }
        });
    }
}
