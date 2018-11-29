package com.turing.turingsdksample.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.thundersoft.airichelibrary.ServiceResultListener;
import com.thundersoft.airichelibrary.WakeUpService;
import com.turing.asr.engine.AsrManager;
import com.turing.tts.TTSListener;
import com.turing.tts.TTSManager;
import com.turing.turingsdksample.R;
import com.turing.turingsdksample.music.TuringMusic;
import com.turing.turingsdksample.util.ActionManager;
import com.turing.turingsdksample.util.AppManager;
import com.turing.turingsdksample.util.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

/**
 * @author：licheng@uzoo.com
 */
public class MainApplication extends Application {
    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;
    public static Context context;
    public static Handler appHandler;

    public static WakeUpService wakeUpService;


    public static MediaPlayer mediaPlayer;

    public static MediaPlayer mediaPlayer1;

    public static boolean isBackStopRecord=false;

    public static Handler uiHandler;

    public static Handler workHandler;

    public HandlerThread handlerThread;

    public static boolean updataDictionarySuccess = false;

    /**语音对话的界面是否在显示*/
    public static boolean isViewshow = false;

    /**图灵语音引擎是否启动好了*/
    public static boolean isSREngReady = false;
    /**打印日志控制*/
    int readCount = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("airiche","onCreate");
        context=this;
        appHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        /**在application中创建属于UI lopper*/
        uiHandler = new Handler();
        Blackboard.getInstance();
        CommonUtil.getInstance();

        /**建立工作线程*/
        handlerThread = new HandlerThread("workThread");
        handlerThread.start();
        workHandler = new Handler(handlerThread.getLooper());


        /**第一次安装的时候是否资源拷贝过唤醒资源*/
                Boolean isInstallLoadRes =  getSharedPreferences("appShared", MODE_PRIVATE).getBoolean("isInstallLoadRes", false);
        SharedPreferences.Editor editor = getSharedPreferences("appShared", MODE_PRIVATE).edit();
        if(!CommonUtil.getInstance().isFileExists(Environment.getExternalStorageDirectory().getPath()+"/resche") || !isInstallLoadRes){
            Log.d("airiche","res on");
            CommonUtil.getInstance().copyAssets(MainApplication.context,"res",Environment.getExternalStorageDirectory()+"/resche");
            Log.d("airiche","res over");
            editor.putBoolean("isInstallLoadRes",true);
            editor.commit();
        }

        /**
         * 所有唤醒封装在这里
         */
        wakeUpService =  WakeUpService.getInstance(this, Environment.getExternalStorageDirectory().getPath() + "/resche/one", new ServiceResultListener() {
            @Override
            public void onRecordData(byte[] mBuffer, byte[] music, int count) {

                /**这里传递录音，当前录音互斥，暂不使用*/
                if(isSREngReady&&isViewshow&&Blackboard.getInstance().isAsrWork&&Blackboard.getInstance().asrListener!=null){
                    AsrManager.getInstance().startAsr(Blackboard.getInstance().asrListener,mBuffer);
                    readCount++;
                    if(readCount== 100) {
                        //不要日志打的太频繁
                        readCount= 0;
                        Log.d("airiche","onRecordData" + isSREngReady +  isViewshow);
                    }
                }else{
                    readCount++;
                    if(readCount== 100) {
                        //不要日志打的太频繁
                        readCount= 0;
                        Log.d("airiche","isSREngReady = " + isSREngReady + "isViewshow = " +isViewshow + "isAsrWork" + Blackboard.getInstance().isAsrWork);
                    }
                }
            }

            @Override
            public void onMVWWakeup(int nMvwScene, int nMvwId, int nMvwScore, String str) {
                Log.d("airiche","onMVWWakeup,view = " + isViewshow);
                if(isViewshow){
                    Blackboard.getInstance().isAsrWork = false;
                    Blackboard.getInstance().setIgnoreResult();
                    TuringMusic.getInstance().stopMusic();
                    try {
                        TTSManager.getInstance().stopTTS();
                    }catch (Exception e){

                    }
                    if(MainApplication.mediaPlayer!=null)MainApplication.mediaPlayer.stop();
                    if(MainApplication.mediaPlayer1!=null)MainApplication.mediaPlayer1.stop();
                        /**在呢的提示语也要500ms*/
                        if( Blackboard.getInstance().allFragment!=null){
                            if(isSREngReady){
                                byte newByte[] = getMutePcm();
                                AsrManager.getInstance().startAsr(Blackboard.getInstance().asrListener,newByte);
                            }
                            if(nMvwId<=3){
                                //CommonUtil.getInstance().startTTS(-1);
                                ActionManager.getInstance().sendControlHandler(0);
                                if (!AppManager.getInstance().isNetworkConnected(MainApplication.context)) {
                                    CommonUtil.getInstance().startTTS(-1);
                                    return;
                                }
                                uiHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
//                                        byte newByte[] = getMutePcm();
//                                        AsrManager.getInstance().startAsr(Blackboard.getInstance().asrListener,newByte);
//                                        Blackboard.getInstance().allFragment.wake();
                                        TTSManager.getInstance().startTTS("在的", new TTSListener() {
                                            @Override
                                            public void onSpeakBegin(String s) {

                                            }

                                            @Override
                                            public void onSpeakPaused() {

                                            }

                                            @Override
                                            public void onSpeakResumed() {

                                            }

                                            @Override
                                            public void onSpeakCompleted() {
                                                Blackboard.getInstance().allFragment.wake();
                                            }

                                            @Override
                                            public void onSpeakFailed() {

                                            }
                                        });
                                    }
                                },5);
                            }else{
                                ActionManager.getInstance().controlMachine(nMvwId,true);
                            }
                        }
                }else{
                   if(nMvwId<=3){
                       ActionManager.getInstance().sendControlHandler(0);
                       Intent intent = new Intent(Intent.ACTION_MAIN);
                       intent.addCategory(Intent.CATEGORY_LAUNCHER);
                       PackageManager packageManager = getPackageManager();
                       intent = packageManager.getLaunchIntentForPackage("com.turing.turingsdksample");
                       startActivity(intent);
                   }else{
                       ActionManager.getInstance().controlMachine(nMvwId,false);
                   }
                }

            }

            @Override
            public void onError(int nErrorCode, byte[] str) {
                Log.d("airiche","onError");
                /*获取不到录音其，这里尝试关闭图灵获取*/
                if(nErrorCode == 100){
                    try {
                        AsrManager.getInstance().cancel();
                        AsrManager.getInstance().stop();
                    }catch (Exception e){

                    }

                }
            }
        });
        //wakeUpService.setLog(true);
    }


    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        Log.e("cao" ,"初始化");

        if (mSerialPort == null) {
            //设置端口
            mSerialPort = new SerialPort(new File("/dev/ttyS2"), 115200, 0);
            //	mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }
    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }


    /***
     * 获取静音片段 防止sr时候，没有vad前端点
     *
     * @return
     */
    public byte[] getMutePcm() {
        InputStream inputStream = null;
        byte[] reader = null;
        try {
            inputStream = context.getResources()
                    .openRawResource(R.raw.mute);
            reader = new byte[inputStream.available()];
            while (inputStream.read(reader) != -1) {
            }
        } catch (IOException e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return reader;
    }

}
