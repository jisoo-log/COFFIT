package com.newblack.coffit.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.newblack.coffit.APIClient;
import com.newblack.coffit.APIInterface;
import com.newblack.coffit.Activity.MainActivity;
import com.newblack.coffit.Data.Student;
import com.newblack.coffit.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newblack.coffit.Activity.MainActivity.myId;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    CircleImageView iv_profile;
//    Button btn_pick;
//    Button btn_send;
//
    TextView tv_edit_profile;
    TextView tv_name;
    TextView tv_email;
    TextView tv_age;
    TextView tv_phone;
    int id;
    APIInterface apiInterface;
    Student student;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        iv_profile = view.findViewById(R.id.iv_profile);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_age = view.findViewById(R.id.tv_age);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_edit_profile = view.findViewById(R.id.tv_edit_profile);
        tv_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : need to check the validation
                ((MainActivity)getActivity()).editProfile(student);
            }
        });

        id = myId;
        retrofit_profile(id);
        return view;
    }


    public void retrofit_profile(int id){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<Student> call = apiInterface.getStudent(id);
        call.enqueue(new Callback<Student>(){
            @Override
            public void onResponse(Call<Student> call, Response<Student> response){
                Log.d("TAG", "apiInterface callback onResponse");
                student = response.body();
                if(student != null){
                    tv_name.setText(student.getUsername());
                    tv_age.setText(String.valueOf(student.getAge()));
                    tv_phone.setText(student.getPhoneNum());
                    tv_email.setText(student.getEmail());
                    Picasso.get().load(student.getPicture()).into(iv_profile);
                }
                else {
                    Log.d("TAG","잘못된 접근입니다.");
                }

            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Log.d("TAG", "통신 실패");
            }
        });

    }

}
