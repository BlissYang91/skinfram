package com.skin.ytf.skinframwork;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author YTF
 */
public class SkinActivity extends Activity {

    SkinFactory skinFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManger.getInstance().setContext(this);
        skinFactory=new SkinFactory();
//        setContentView(R.layout.activity_skin);
//        添加布局监听器
        LayoutInflaterCompat.setFactory(getLayoutInflater(),skinFactory );
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void changeSkin(){
        skinFactory.apply();
    }
}
