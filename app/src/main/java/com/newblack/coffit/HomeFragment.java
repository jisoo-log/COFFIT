package com.newblack.coffit;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    Button btn_schedule;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btn_schedule = view.findViewById(R.id.btn_schedule);
        btn_schedule.setOnClickListener(new View.OnClickListener(){
            //일정 관리로 넘어감
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).goSchedule();
            }
        });
        return view;
    }


    public void startPT(View view){

    }
}
