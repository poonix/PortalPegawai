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

    @SerializedName("fcm")
    @Expose
    private List<DataFCM> fcmList;

    public FirebaseIDRequest(String idSdm, String idFcm) {
        buildRequest(idSdm, idFcm);
    }

    private void buildRequest(String idSdm, String idFcm) {
        DataFCM fcm = new DataFCM();
        fcm.setIdsdm(idSdm);
        fcm.setIdFcm(idFcm);
        fcmList = new ArrayList<>();
        fcmList.add(fcm);
    }

    class DataFCM {
        @SerializedName("id_fcm")
        @Expose
        private String idFcm;
        @SerializedName("idsdm")
        @Expose
        private String idsdm;

        public String getIdFcm() {
            return idFcm;
        }

        public void setIdFcm(String idFcm) {
            this.idFcm = idFcm;
        }

        public String getIdsdm() {
            return idsdm;
        }

        public void setIdsdm(String idsdm) {
            this.idsdm = idsdm;
        }
    }
}
