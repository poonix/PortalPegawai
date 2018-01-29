package com.duaruang.pnmportal.config;

/**
 * Created by way on 8/15/2017.
 */

public class Config {
    //URL Login
    public static final String KEY_USERNAME = "user";
    public static final String KEY_PASSWORD = "pass";
    public static final String KEY_APPCODE = "event";

    //URL Portal Pegawai
    public static final String KEY_URL_LOGIN="http://103.76.17.197/WebService/SSO_Mobile/login.php";
    public static final String KEY_URL_NEWS_GENERAL="http://27.50.31.76:9495/hrapi/news/getNewsGeneral";
    public static final String KEY_URL_NEWS_PRIVATE="http://27.50.31.76:9495/hrapi/news/getNewsPrivate?idgroup=";
    public static final String KEY_URL_GET_POSITION="http://27.50.31.76:9495/hrapi/privilege/getUsergroup?position=";
    public static final String KEY_URL_FAQ="http://27.50.31.76:9495/hrapi/faq/getFaq";
    public static final String KEY_URL_HOMEPAGE_BANNER="http://27.50.31.76:9495/hrapi/content/getHomepageBanner";
    public static final String KEY_URL_UPDATE_PASSWORD="http://103.76.17.197/PNM/api/v1/sso/update_password"; //post
    public static final String KEY_URL_FORGOT_PASSWORD="http://103.76.17.197/PNM/api/v1/sso/lupa_password"; //post

    //EMS
    public static final String KEY_URL_EVENT_GENERAL="http://27.50.31.76:9495/hrapi/event/getEventGeneral";
//  public static final String KEY_URL_EVENT_EMS="http://27.50.31.76:9495/eventapi/event/getEvent?idsdm=B0BA88CF0E61B42E50B3BCF968579979";
    public static final String KEY_URL_EVENT_EMS="http://27.50.31.76:9495/eventapi/event/getEvent?idsdm=";
    public static final String KEY_URL_EVENT_MATERIAL="http://27.50.31.76:9495/eventapi/event/getEventMaterial?idevent=";
    public static final String KEY_URL_EVENT_LOCATION="http://27.50.31.76:9495/eventapi/event/getEventLocation?idevent=";
//  public static final String KEY_URL_GET_FEEDBACK="http://27.50.31.76:9495/eventapi/event/getEventFeedback?idevent=NR0B5ITBPZ1480957200&idsdm=F4F650806EEF5CE474F8C25FD202F32B";
    public static final String KEY_URL_GET_FEEDBACK="http://27.50.31.76:9495/eventapi/event/getEventFeedback?idevent=";
    public static final String KEY_URL_GET_ATTENDANCE="http://27.50.31.76:9495/eventapi/event/getEventAttendance?idevent=";
    public static final String KEY_URL_GET_RUNDOWN="http://27.50.31.76:9495/eventapi/event/getEventRundown?idevent=";
    public static final String KEY_URL_POST_FEEDBACK="http://27.50.31.76:9495/eventapi/event/postEventFeedback"; //post
    public static final String KEY_URL_POST_ATTENDANCE="http://27.50.31.76:9495/eventapi/event/postEventAttendance"; //post
    public static final String KEY_URL_POST_FCM="http://27.50.31.76:9495/hrapi/notification/postFirebase";

    //Url content
    public static final String KEY_URL_ASSET_CONTENT="http://27.50.31.76:9495/cms-hr/assets/content/";
    public static final String KEY_URL_ASSET_EVENT="http://27.50.31.76:9495/cms-hr/assets/images/events/";
    public static final String KEY_URL_ASSET_NEWS="http://27.50.31.76:9495/cms-hr/assets/images/news/";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "Berhasil Login";
    public static final String DATA_FOUND = "data found.";
    public static final String SUCCESS = "success";
    public static final String MESSAGE_SUCCESS_UPDATE_PASSWORD = "Berhasil Update Password SSO.";
    public static final String MESSAGE_SUCCESS_RESET_PASSWORD="Password sudah dikirim ke email Anda.";
    public static final String MESSAGE_SUCCESS_FCM="success";

    public static final String KEY_URL_DOWNLOAD_MATERIAL="http://27.50.31.76:9495/event/assets/attachments/materi/";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "PNMPortal";

    //This would be used to store the email of current logged in user
//    public static final String IDSDM_SHARED_PREF = "idsdm";
//    public static final String USERNAME_SHARED_PREF = "username";
//    public static final String EMAIL_SHARED_PREF = "email";
//    public static final String NAMA_SHARED_PREF = "nama";
//    public static final String NIK_SHARED_PREF = "nik";
//    public static final String POSISI_NAMA_SHARED_PREF = "posisi_nama";
//    public static final String LOKASI_KERJA_SHARED_PREF = "lokasi_kerja";
//
//    public static final String UNIT_SHARED_PREF = "unit";
//    public static final String KODE_UNIT_SHARED_PREF = "kode_unit";
//    public static final String CABANG_SHARED_PREF = "cabang";
//    public static final String KODE_CABANG_SHARED_PREF = "kode_cabang";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    public static final String KEY_GOOGLE_MAPS_API="AIzaSyDV1hzcwLffneTWNQO55Kbm6htD2CSkxkc";

}
