package com.newblack.coffit.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Fields implements Serializable {
    @SerializedName("key")
    private String key;
    @SerializedName("bucket")
    private String bucket;
    @SerializedName("X-Amz-Algorithm")
    private String xAmzAlgorithm;
    @SerializedName("X-Amz-Credential")
    private String xAmzCredential;
    @SerializedName("X-Amz-Date")
    private String xAmzDate;
    @SerializedName("Policy")
    private String policy;
    @SerializedName("X-Amz-Signature")
    private String xAmzSignature;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getXAmzAlgorithm() {
        return xAmzAlgorithm;
    }

    public void setXAmzAlgorithm(String xAmzAlgorithm) {
        this.xAmzAlgorithm = xAmzAlgorithm;
    }

    public String getXAmzCredential() {
        return xAmzCredential;
    }

    public void setXAmzCredential(String xAmzCredential) {
        this.xAmzCredential = xAmzCredential;
    }

    public String getXAmzDate() {
        return xAmzDate;
    }

    public void setXAmzDate(String xAmzDate) {
        this.xAmzDate = xAmzDate;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getXAmzSignature() {
        return xAmzSignature;
    }

    public void setXAmzSignature(String xAmzSignature) {
        this.xAmzSignature = xAmzSignature;
    }

}

