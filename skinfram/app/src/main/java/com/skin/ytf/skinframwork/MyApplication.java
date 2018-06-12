package com.skin.ytf.skinframwork;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            SkinManger.getInstance().setContext(this);
            SkinManger.getInstance().loadSkin();
        } catch (Exception e) {

        }


    }
}
