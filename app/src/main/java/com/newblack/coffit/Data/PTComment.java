package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

public class PTComment{
    @SerializedName("id")
    private int id;
    @SerializedName("comment")
    private String comment;
    @SerializedName("pt_id")
    private int PT_id;

    public String getComment() {
        return comment;
    }

    public int getPT_id() {
        return PT_id;
    }

    public int getId() {
        return id;
    }
}
