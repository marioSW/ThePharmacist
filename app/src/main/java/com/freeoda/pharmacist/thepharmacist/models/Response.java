package com.freeoda.pharmacist.thepharmacist.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lakna on 3/21/2016.
 */
public class Response extends BaseModel {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    String message;
}
