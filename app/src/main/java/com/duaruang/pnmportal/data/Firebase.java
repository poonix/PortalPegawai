package com.duaruang.pnmportal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 11/21/2017.
 */

public class Firebase {
    @SerializedName("idusergroup")
    @Expose
    private String idUserGroup;
    @SerializedName("fcm")
    @Expose
    private String fcm;

    public Firebase(String idUserGroup, String fcm){
        this.idUserGroup = idUserGroup;
        this.fcm = fcm;
    }

    public String getIdUserGroup() {
        return idUserGroup;
    }

    public void setIdUserGroup(String idUserGroup) {
        this.idUserGroup = idUserGroup;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }
}
