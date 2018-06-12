package com.skin.ytf.skinframwork;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author YTF
 */
public class MainActivity extends SkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void jump(View view) {
        Intent intent=new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void change(View view) {
        File fileDir=this.getDir("skin", Context.MODE_PRIVATE);
        String name="skin.apk";
        String filePath=new File(fileDir,name).getAbsolutePath();
        File file=new File(filePath);
        if (file.exists()){
            file.delete();
        }
        InputStream is=null;
        FileOutputStream os=null;
        try {
            is=new FileInputStream(new File(Environment.getExternalStorageDirectory(),name));
             os=new FileOutputStream(filePath);
            int len=0;
            byte[] buffer=new byte[1024];
            while ((len=is.read(buffer))!=-1){
                os.write(buffer,0,len);
            }
            File f=new File(filePath);
            if (f.exists()){
                Toast.makeText(this,"dex overwrite",Toast.LENGTH_SHORT).show();
            }
//           开始执行换肤
            SkinManger.getInstance().loadSkin();
            changeSkin();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (os!=null){
                    os.close();
                }
               if (is!=null){
                   is.close();
               }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
