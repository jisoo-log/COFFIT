package com.newblack.coffit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

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


    //date -> calendarday

    //calendarday -> date


}