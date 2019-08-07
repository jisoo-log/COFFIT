package com.newblack.coffit.Data;

import java.util.Date;

public class Noti {
    private String fromname;
    private String content;
    private Date time;
    private String state; //어떤 종류의 노티인지 저장할 필요 존재
    private boolean read;
    private String makerId;

    public String getFromname() {
        return fromname;
    }

    public String getContent() {
        return content;
    }

    public Date getTime() {
        return time;
    }

    public String getState() {
        return state;
    }

    public boolean isRead() {
        return read;
    }
}
