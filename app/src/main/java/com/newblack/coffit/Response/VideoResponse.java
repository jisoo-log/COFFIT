package com.newblack.coffit.Response;

import com.google.gson.annotations.SerializedName;
import com.newblack.coffit.Data.Fields;

public class VideoResponse {
    @SerializedName("url")
    private String url;
    @SerializedName("fields")
    private Fields fields;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }


}
