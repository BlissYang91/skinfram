package com.skin.ytf.skinframwork;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 皮肤资源管理器
 * 用于加载皮肤，找到就加载，找不到就加载默认layout
 */
public class SkinManger {
    private Resources resources;
    private static final SkinManger ourInstance = new SkinManger();
    private Context context;
    private String skinPackge;

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public static SkinManger getInstance() {
        return ourInstance;
    }

    private SkinManger() {
    }
//加载apk中的资源
    public void loadSkin() {
        String path=context.getDir("skin",
                Context.MODE_PRIVATE).getAbsolutePath()+"/skin.apk";
//        拿到外置皮肤apk的包名
        PackageManager packageManager=context.getPackageManager();
        skinPackge=packageManager.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES).packageName;
        File file=new File(path);
        if (!file.exists()){
            return;
        }
        try {
            AssetManager assetManager=AssetManager.class.newInstance();//系统隐藏api（@hide 通过反射调用）
//            系统隐藏方法，同样通过反射强行调用
            Method addAssetPath=assetManager.getClass().getMethod("addAssetPath",String.class);
            addAssetPath.invoke(assetManager,path);
            resources=new Resources(assetManager,context.getResources().getDisplayMetrics(),context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getColor(int id) {
        if (resources==null){
            return context.getResources().getColor(id);
        }
        String resType=context.getResources().getResourceTypeName(id);
        String resName=context.getResources().getResourceEntryName(id);
        int skinId=resources.getIdentifier(resName,resType,skinPackge);
        if (skinId==0){
            return context.getResources().getColor(id);
        }

        return resources.getColor(skinId);

    }

    public Drawable getDrawable(int id) {
        if (resources==null){
            return ContextCompat.getDrawable(context,id);
        }
        String resType=context.getResources().getResourceTypeName(id);
        String resName=context.getResources().getResourceEntryName(id);
        int skinId=resources.getIdentifier(resName,resType,skinPackge);
        if (skinId==0){
            return ContextCompat.getDrawable(context,id);
        }

        return resources.getDrawable(skinId);

    }
}
