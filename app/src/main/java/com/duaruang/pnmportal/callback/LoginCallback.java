package com.duaruang.pnmportal.callback;

import com.duaruang.pnmportal.data.Pegawai;

/**
 * Created by indri on 1/30/18.
 */

public interface LoginCallback {

    void onLoginSSOSuccess(Pegawai userModel);

    void onLoginSSOFailed(Throwable t);

    void onLoginProcessCompleted();

    void onLoginProcessNotCompleted(Throwable t);
}