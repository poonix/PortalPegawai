package com.duaruang.pnmportal.preference;

import android.text.TextUtils;
import android.util.Log;


import com.duaruang.pnmportal.data.Pegawai;
import com.duaruang.pnmportal.rest.ApiConstant;
import com.google.gson.Gson;

/**
 * Created by saifullahalbasrie on 3/29/17.
 */

public class AppPreference extends BasePreference {

    private final String LOG_TAG = AppPreference.class.getSimpleName();

    public static final String API_HOST_SSO = "api_host_sso";
    public static final String API_HOST_INTERNAL = "api_host_internal";
    public static final String API_HOST_INTERNALPNM = "api_host_internal_pnm";

    public static final String API_CHECK_DEBITUR_BY_NAME = "api_check_debitur_by_name";
    public static final String API_CHECK_DEBITUR_BY_NOMOR_REKEKING = "api_check_debitur_by_nomor_rekeking";
    public static final String API_CHECK_DEBITUR_BY_IBU_KANDUNG = "api_check_debitur_by_ibu_kandung";
    public static final String API_CHECK_DEBITUR_BY_TANGGAL_LAHIR = "api_check_debitur_by_tanggal_lahir";
    public static final String API_GET_LIST_COLLECTION = "api_get_list_collection";
    public static final String API_GET_COLLECTION_DETAIL = "api_get_collection_detail";


    public static final String USER_SSO = "user_sso";
    public static final String USER_INTERNAL = "user_pnm";
    public static final String USER_TYPE = "user_type";
    public static final String FCM_ID = "user_fcm_id";
    public static final String FCM_NEED_RESEND = "fcm_id_need_resend";
    public static final String LAST_ACTIVE = "last_active_time";

    public static final String MASTER_JENIS_PROSPEK = "master_jenis_produk";
    public static final String MASTER_JENIS_USAHA = "master_jenis_usaha";
    public static final String MASTER_STATUS_DEBITUR = "master_status_debitur";
    public static final String MASTER_KODE_BIDANG_USAHA = "master_kode_bidang_sauaha";
    public static final String MASTER_KODE_USAHA = "master_kode_usaha";
    public static final String MASTER_HUB_PNM = "master_hub_pnm";
    public static final String MASTER_HUB_BANK = "master_hub_bank";
    public static final String MASTER_PROVINSI = "master_provinsi";
    public static final String MASTER_PRODUK = "master_produk";
    public static final String MASTER_PROGRAM = "master_program";
    public static final String MASTER_JENIS_AGUNAN = "master_jenis_agunan";
    public static final String MASTER_JENIS_DOKUMEN = "master_jenis_dokumen";
    public static final String MASTER_JENIS_HARTA = "master_jenis_harta";
    public static final String MASTER_GELAR = "master_gelar";
    public static final String MASTER_PENDIDIKAN_TERAKHIR = "master_pendidikan_terakhir";
    public static final String MASTER_JENIS_PEKERJAAN = "master_jenis_pekerjaan";
    public static final String MASTER_JENIS_PRODUK_USAHA = "master_jenis_produk_usaha";
    public static final String MASTER_PENGELOLAAN_KEUANGAN = "master_pengelolaan_keuangan";

    public static final String MASTER_ATAP = "master_atap";
    public static final String MASTER_DINDING = "master_dinding";
    public static final String MASTER_JENDELA = "master_jendela";
    public static final String MASTER_KUSEN = "master_kusen";
    public static final String MASTER_LANTAI = "master_lantai";
    public static final String MASTER_PINTU = "master_pintu";
    public static final String MASTER_PLAFON = "master_plafon";
    public static final String MASTER_PONDASI = "master_pondasi";

    public static final String MASTER_BATAS_WILAYAH = "master_batas_wilayah";
    public static final String MASTER_BENTUK_TANAH = "master_bentuk_tanah";
    public static final String MASTER_KONDISI_PERMUKAAN_TANAH = "master_kondisi_permukaan_tanah";
    public static final String MASTER_PENGGUNAAN_TANAH = "master_penggunaan_tanah";
    public static final String MASTER_STATUS_TANAH = "master_status_tanah";

    public static final String MASTER_JENIS_BUKTI_KEPEMILIKAN_AGUNAN = "master_jenis_bukti_kepemilikan_agunan";
    public static final String MASTER_BUKTI_KEPEMILIKAN_AGUNAN = "master_bukti_kepemilikan_agunan";
    public static final String MASTER_PENGELOLAAN_USAHA = "master_pengelolaan_usaha";
    public static final String MASTER_JALAN_DILALUI = "master_jalan_dilalui";
    public static final String MASTER_PERUNTUKAN_LOKASI = "master_peruntukan_lokasi";
    public static final String MASTER_SALURAN_AIR = "master_saluran_air";
    public static final String MASTER_SALURAN_TELEPON = "master_saluran_telepon";





    private static final Object LOCK = new Object();
    private static AppPreference sInstance;

    public static AppPreference getInstance() {
        synchronized (LOCK) {
            if (sInstance == null) {
                sInstance = new AppPreference();
            }
        }
        return sInstance;
    }

    private AppPreference() {

    }

    public void setUserLoggedIn(Pegawai userModel) {
        //userModel.setIdUserGroup(com.example.saifullah_albasrie.pnm.model.annotation.UserType.MANAGER_UNIT); //TEST MU
        set(USER_SSO, new Gson().toJson(userModel));
    }

    public Pegawai getUserSSOLoggedIn() {
        Pegawai userSSOModel = null;
        try {
            userSSOModel = new Gson().fromJson(getString(USER_SSO), Pegawai.class);
        }catch (Exception e) {
            Log.e(LOG_TAG, "getUserSSOLoggedIn : " + e.getMessage());
        }
        return userSSOModel;

    }

    public void setApiHostSso(String host) {
        set(API_HOST_SSO, host);
    }

    public String getApiHostSso() {
        String host = getString(API_HOST_SSO);
        if (TextUtils.isEmpty(host))
            host = ApiConstant.BASE_URL_SSO;

        return host;
    }


    /*End Tambahan 11 Jan 2018 -Andityo-*/

    public void setUserType(int type) {
        set(USER_TYPE, type);
    }

    public int getUserType() {
        return getInt(USER_TYPE);
    }

    public void setFcmId(String token) {
        set(FCM_ID, token);
    }

    public String getFcmId() {
        return getString(FCM_ID);
    }

    public void setFcmNeedToResend(boolean fcmIsRefresh) {
        set(FCM_NEED_RESEND, fcmIsRefresh);
    }

    public boolean getFcmNeedToResend() {
        return getBoolean(FCM_NEED_RESEND, true);
    }

    public void setLastActive(long time) {
        set(LAST_ACTIVE, time);
    }

    public long getLastActive() {
        return getLong(LAST_ACTIVE);
    }

    public void clearData() {
        set(USER_INTERNAL, null);
        set(USER_SSO, null);
        set(LAST_ACTIVE, 0);
        set(API_HOST_SSO, null);
        set(API_HOST_INTERNAL, null);

        set(API_CHECK_DEBITUR_BY_NAME, null);
        set(API_CHECK_DEBITUR_BY_IBU_KANDUNG, null);
        set(API_CHECK_DEBITUR_BY_NOMOR_REKEKING, null);
        set(API_CHECK_DEBITUR_BY_TANGGAL_LAHIR, null);
        set(API_GET_LIST_COLLECTION, null);
        set(API_GET_COLLECTION_DETAIL, null);

        removeAll();

    }
}
