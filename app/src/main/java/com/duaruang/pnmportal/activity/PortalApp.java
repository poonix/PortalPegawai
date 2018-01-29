package com.duaruang.pnmportal.activity;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import io.fabric.sdk.android.Fabric;

public class PortalApp extends Application{
    private static PortalApp singleton = new PortalApp();

    static PortalApp getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        if (!Fresco.hasBeenInitialized())
            Fresco.initialize(this);
        singleton = this;
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
//    }
}
