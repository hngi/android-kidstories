package com.project.android_kidstories.Api.Responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.android_kidstories.Model.User;

public class LoginResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private User user;

    public String getStatus() {
        return status;
    }

    public String getMethod() {
        return method;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
