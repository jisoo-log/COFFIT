package com.newblack.coffit;

import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtils {
    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DATE = 2;
    public static final int HOUR = 3;
    public static final int MINUTE = 4;

    //string -> simpledateformat -> date;
    public static Date stringToDate(String dateValue){
        Date date = new Date();
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        try{
            date = myFormat.parse(dateValue);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return date;
    }


//    public static String dateToString(Date date){
//        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
//        String result = myFormat.format(date);
//        return result;
//    }

    //make date class to send to server
    public static Date forServerTime(int year, int month, int day, int hour, int min){
        String M,d,h,m;
        M = timeNumberToString(month);
        d = timeNumberToString(day);
        h = timeNumberToString(hour);
        m = timeNumberToString(min);

        String dateValue = year+M+d+h+m;
        Date date = new Date();
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMddHHmm");
        try{
            date = myFormat.parse(dateValue);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    public static String fromServerTime(Date date){
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy,MM,dd,HH,mm");
        String result = myFormat.format(date);
        return result;
    }

    // i -> 0 : year, 1 : Month , 2 : Day, 3 : Hour, 4 : Minute
    public static int getValueFromDate(String date,int i){
        return Integer.parseInt(date.split(",")[i]);
    }

    public static CalendarDay getCalendarDay(Date date){
        String newDate = fromServerTime(date);
        CalendarDay result = CalendarDay.from(
                DateUtils.getValueFromDate(newDate, YEAR),
                DateUtils.getValueFromDate(newDate, MONTH),
                DateUtils.getValueFromDate(newDate, DATE)
        );
        return result;
    }

    public static String dateObject(Date date){
        String newDate = DateUtils.fromServerTime(date);
        String year = timeNumberToString(getValueFromDate(newDate,YEAR));
        String month = timeNumberToString(getValueFromDate(newDate,MONTH));
        String day = timeNumberToString(getValueFromDate(newDate,DATE));
        return year+"-"+month+"-"+day;
    }


    //date -> calendarday

    //calendarday -> date

    public static String timeNumberToString(int time){
        String result;
        if(time < 10){
            result = "0"+time;
        }
        else{
            result = String.valueOf(time);
        }
        return result;
    }

}