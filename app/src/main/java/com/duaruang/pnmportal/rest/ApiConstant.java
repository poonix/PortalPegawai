package com.duaruang.pnmportal.rest;

/**
 * Created by indri on 1/30/18.
 */

public class ApiConstant {
    //public static final String BASE_URL_SSO = "http://182.23.52.249/WebService/SSO_Mobile/";
    public static final String BASE_URL_SSO = "http://182.23.52.249/WebService/SSO_Mobile/";
    public static final String BASE_URL = "http://azha.ddns.net:8080/restapi/";
    public static final String BASE_URL_MASTERDATA = "http://182.23.52.249/PNM/api/v1/master/lov_global/";
    public static final String BASE_URL_POST = "http://182.23.52.249/PNM/api/v1/";
//    public static final String BASE_URL = "http://27.50.31.76:9696/restapi/";

    public static final String USER_AUTH_SSO = "event";
    public static final String PASSWORD_AUTH_SSO = "event";

    public static final String USER_AUTH = "username";
    public static final String PASSWORD_AUTH = "password";

    //public static final String LOGIN_SSO = "http://182.23.52.249/WebService/SSO_Mobile/login.php";
    public static final String LOGIN_SSO = BASE_URL_SSO + "login.php";
    //public static final String LOGIN_API = "http://azha.ddns.net:8080/restapi/login";
    public static final String LOGIN_API = BASE_URL + "login";
    public static final String LOGOUT = "logout";
    public static final String FIREBASE_ID = "firebase";

    public static final int PAGE_LIMIT_SIZE = 10;


}
