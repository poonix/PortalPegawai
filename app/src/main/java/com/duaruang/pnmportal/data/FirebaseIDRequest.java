package com.duaruang.pnmportal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by indri on 1/30/18.
 */

public class FirebaseIDRequest {

        /*
        {
	        "fcm" :
	        [{
		        "id_fcm" : "asdIoasdnmkmsd9909asdjkjkasd",
		        "idsdm"  : "1DCB599E947F9842B0C4EC6C61C1BCFF"
	        }]
        }
         */


    public FirebaseIDRequest(int idUserGroup,String idFcm) {
        setIdFcm(idFcm);
        setIdUserGroup(idUserGroup);
    }



        @SerializedName("fcm")
        @Expose
        private String idFcm;
        @SerializedName("idusergroup")
        @Expose
        private int idUserGroup;

        public String getIdFcm() {
            return idFcm;
        }

        public void setIdFcm(String idFcm) {
            this.idFcm = idFcm;
        }

        public int getIdUserGroup() {
            return idUserGroup;
        }

        public void setIdUserGroup(int idUserGroup) {
            this.idUserGroup = idUserGroup;
        }

}
