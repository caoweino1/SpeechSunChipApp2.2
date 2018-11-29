package com.turing.turingsdksample.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.turing.turingsdksample.R;
import com.turing.turingsdksample.activity.MainApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by liwan on 2018/5/8.
 */

public class CommonUtil {
    public static CommonUtil commonUtil;
    SoundPool sp;
    int soundID_01;
    int soundID_0;
    int soundID_1;
    int soundID_2;
    int soundID_3;
    int soundID_4;
    int soundID_5;
    int soundID_6;
    int soundID_7;
    int soundID_8;
    //int soundID_9;
    int last;
    public static CommonUtil getInstance(){
        if(commonUtil == null){
            commonUtil = new CommonUtil();
        }
        return commonUtil;
    }
    public CommonUtil(){
        sp=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        // soundID_01=sp.load(MainApplication.context,R.raw.tts, 1);
        soundID_01=sp.load(MainApplication.context,R.raw.welcome, 1);
        soundID_0=sp.load(MainApplication.context,R.raw.brake, 1);
        soundID_1=sp.load(MainApplication.context,R.raw.forword, 1);
        soundID_2=sp.load(MainApplication.context,R.raw.backword, 1);
        soundID_3=sp.load(MainApplication.context,R.raw.left, 1);
        soundID_4=sp.load(MainApplication.context,R.raw.right, 1);
        soundID_5=sp.load(MainApplication.context,R.raw.photo, 1);
        soundID_6=sp.load(MainApplication.context,R.raw.big, 1);
        soundID_7=sp.load(MainApplication.context,R.raw.small, 1);
        soundID_8=sp.load(MainApplication.context,R.raw.dance, 1);
        //soundID_9=sp.load(MainApplication.context,R.raw.song, 1);
        last = soundID_01;
    }

    public void startTTS(int id){
        sp.stop(last);
        if(id == -1){
            last = soundID_01;
            sp.play(soundID_01, 0.8f, 0.8f,1, 0, 1.0f);
        }
        if(id == 0){
            last = soundID_0;
            sp.play(soundID_0, 0.8f, 0.8f,1, 0, 1.0f);
        }
        if(id == 1){
            last = soundID_1;
            sp.play(soundID_1, 0.8f, 0.8f,1, 0, 1.0f);
        }
        if(id == 2){
            last = soundID_2;
            sp.play(soundID_2, 0.8f, 0.8f,1, 0, 1.0f);
        }
        if(id == 3){
            last = soundID_3;
            sp.play(soundID_3, 0.8f, 0.8f,1, 0, 1.0f);
        }
        if(id == 4){
            last = soundID_4;
            sp.play(soundID_4, 0.8f, 0.8f,1, 0, 1.0f);
        }
        if(id == 5){
            last = soundID_5;
            sp.play(soundID_5, 0.8f, 0.8f,1, 0, 1.0f);
        }
        if(id == 6){
            last = soundID_6;
            sp.play(soundID_6, 0.8f, 0.8f,1, 0, 1.0f);
        }
        if(id == 7){
            last = soundID_7;
            sp.play(soundID_7, 0.8f, 0.8f,1, 0, 1.0f);
        }
        if(id == 8){
            last = soundID_8;
            sp.play(soundID_8, 0.8f, 0.8f,1, 0, 1.0f);
        }
//        if(id == 9){
//            last = soundID_9;
//            sp.play(soundID_9, 0.8f, 0.8f,1, 0, 1.0f);
//        }


    }

    public boolean isFileExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /**
     * 拷贝asset的文件到内存卡
     * @param context
     * @param oldPath
     * @param newPath
     *
     * CopyAssets(this,"res",Environment.getExternalStorageDirectory()+"/resche");
     */
    public static void copyAssets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);
            if (fileNames.length > 0) {
                File file = new File(newPath);
                file.mkdirs();
                for (String fileName : fileNames) {
                    copyAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {

                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
