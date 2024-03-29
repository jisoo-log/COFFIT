package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;
import com.newblack.coffit.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Schedule implements Serializable {
    @SerializedName("id")
    private int id;

    //state
    //  0 : 요청
    //  1 : 확정
    //  2 : 확정 뒤 변경 요청중
    //  3 : 거절 -> 삭제
    //  4 : 끝난 schedule
    @SerializedName("state")
    private int state;
    @SerializedName("is_trainer")
    private boolean is_trainer;
    @SerializedName("past_schedule_id")
    private int pastId;
    @SerializedName("trainer_schedule_id")
    private int ts_id;

    @SerializedName("date")
    private Date date;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;

    @SerializedName("memo")
    private String memo;
    @SerializedName("pt_id")
    private int PT_id;
    @SerializedName("trainer_id")
    private int trainerId;
    @SerializedName("student_id")
    private int studentId;

    private int count;

    //테스트용
//    @SerializedName("createdAt")
//    public Date createdAt;

    public int getTs_id() {
        return ts_id;
    }

    public int getPastId() {
        return pastId;
    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount(){
        return count;
    }


    public String getStartEndTime(int start) {
        Date date = this.getDate();
        String newDate = DateUtils.fromServerTime(date);
        int hour = DateUtils.getValueFromDate(newDate,DateUtils.HOUR);
        int min = DateUtils.getValueFromDate(newDate,DateUtils.MINUTE);
        String hour_ = DateUtils.timeNumberToString(hour);
        String min_ = DateUtils.timeNumberToString(min);
        if(start==0){
            //get start time
            return hour_+":"+min_;
        }
        else{
            //get end time
            if(min==0){
                return hour_+":30";
            }
            else{
                hour_ = DateUtils.timeNumberToString(hour+1);
                return hour_+":00";
            }
        }

    }


    public String getMemo() {
        return memo;
    }

    public int getPTId() {
        return PT_id;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public int getStudentId() {
        return studentId;
    }

    public List<String> stateText(){
        //아예 상황에 따라 리스트로 박아버리자.
        // index 0 : ScheduleActivity, index 1 : Schedule
        List<String> list = new ArrayList<>();
        switch (state){
            case 0:
                //요청인데, 트레이너쪽에서 요청을 받은 경우
                if(is_trainer){
                    list.add("확정 필요");
                    if(pastId==-1){
                        //새로운 요청
                        list.add("PT가 가능하시다면 승인을 눌러주세요");
                    }
                    else{
                        //기존 것에서 변경 요청
                        list.add("PT가 가능하시다면 승인을 눌러주세요\n거절하시면 기존 스케줄로 PT가 진행됩니다");
                    }
                }
                else {
                    list.add("확정 대기중");
                    list.add("트레이너의 확정을 기다리고 있습니다.");
                }
                break;
            case 1:
                    list.add("확정");
                    list.add("확정된 스케줄입니다\n일정 변경/삭제는 PT 하루 전까지만 가능합니다");
                break;
            case 2:
                    list.add("변경 중");
                    list.add("기존에 확정된 스케줄입니다\n변경이 확정되면 일정이 삭제됩니다");
                break;
            case 3:
                break;
            case 4:
                    list.add("PT 완료");
                    list.add("PT 수업을 진행했습니다");
                break;
        }
        return list;
    }



}
