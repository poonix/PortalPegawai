package com.duaruang.pnmportal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by indri on 1/30/18.
 */

public class LoginSSOResponse {

    @SerializedName("login")
    @Expose
    private List<Login> login = null;

    public List<Login> getLogin() {
        return login;
    }

    public void setLogin(List<Login> login) {
        this.login = login;
    }

    public boolean isSuccessResponse() {
        try {
            return getLogin().get(0).getResponse().equalsIgnoreCase("true");
        } catch (Exception e) {
            return false;
        }
    }

    public Pegawai getUserLoggedin() {
        try{
            return getLogin().get(0).getData().get(0);
        }catch (Exception e) {
            return null;
        }
    }

    public class Login {

        @SerializedName("response")
        @Expose
        private String response;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private List<Pegawai> data = null;


        public List<Pegawai> getData() {
            return data;
        }

        public void setData(List<Pegawai> data) {
            this.data = data;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
}
