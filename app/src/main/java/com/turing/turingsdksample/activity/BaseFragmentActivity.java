package com.turing.turingsdksample.activity;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.turing.asr.callback.InitialListener;
import com.turing.asr.engine.AsrManager;
import com.turing.authority.authentication.AuthenticationListener;
import com.turing.authority.authentication.SdkInitializer;
import com.turing.music.InitListener;
import com.turing.music.MusicManager;
import com.turing.tts.TTSInitListener;
import com.turing.tts.TTSManager;
import com.turing.turingsdksample.R;
import com.turing.turingsdksample.callback.SelectFragmentCallback;
import com.turing.turingsdksample.music.TuringMusic;
import com.turing.turingsdksample.receiver.NetChangeManager;
import com.turing.turingsdksample.ui.HeadLayout;
import com.turing.turingsdksample.util.ActionManager;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * @author：licheng@uzoo.com
 */

public abstract class BaseFragmentActivity extends FragmentActivity implements SelectFragmentCallback, NetChangeManager.NetChangeCallback {
    private String TAG = BaseFragmentActivity.class.getSimpleName();
    protected FragmentManager mFragMgr;
    protected HeadLayout headLayout;
    public boolean isTTSInit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        MainApplication.isViewshow = true;
        init();
        if (isConnectIsNomarl()) {
            initPremission();
        }
        Log.i(TAG, "initPremission =111=");
    }

    @Override
    /**
     * 界面显示的时候全部停止掉
     */
    protected void onStop() {
        MainApplication.isViewshow = false;
        try {
            AsrManager.getInstance().cancel();
            AsrManager.getInstance().stop();
            TuringMusic.getInstance().stopMusic();
            TTSManager.getInstance().stopTTS();
        } catch (Exception e) {

        } catch (ExceptionInInitializerError e) {

        } catch (java.lang.Throwable a) {

        }
        if (MainApplication.mediaPlayer != null) MainApplication.mediaPlayer.stop();
        if (MainApplication.mediaPlayer1 != null) MainApplication.mediaPlayer1.stop();
//        MainApplication.wakeUpService.resumeRecord();
        // MainApplication.wakeUpService.restore(true);
        if (MainApplication.isBackStopRecord) {
            MainApplication.wakeUpService.pauseRecord();
        } else {
            MainApplication.wakeUpService.resumeRecord();
        }
        MainApplication.isBackStopRecord=false;
        ActionManager.getInstance().stopMusic();
        Blackboard.getInstance().clean();
        super.onStop();
    }

    private void init() {
        mFragMgr = getSupportFragmentManager();
        //设置网络监听回调
        NetChangeManager.getInstance().add(BaseFragmentActivity.this);
    }

    private void initPremission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.RECORD_AUDIO).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                Log.i(TAG, "initPremission ==" + aBoolean);
                if (!aBoolean) {
                    toast(getString(R.string.no_promission));
                }
                initSDK();
            }
        });
    }

    private AuthenticationListener authenticationListener = new AuthenticationListener() {
        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "getInstance ==");
                    TTSManager.getInstance().init(MainApplication.context, new TTSInitListener() {
                        @Override
                        public void onSuccess() {
                            Log.i(TAG, "TTS init success");
                            isTTSInit = true;
                            getDataAndNotify();
                        }

                        @Override
                        public void onFailed(int i, String s) {
                            isTTSInit = false;
                            Log.i(TAG, "TTS init failed errorCode=" + i + "   errorMsg=" + s);
                        }
                    });
//                    AsrManager.getInstance().setOptionVAD(600000,300);
                    AsrManager.getInstance().init(MainApplication.context, new InitialListener() {

                        @Override
                        public void onInitialSuccess() {
                            MainApplication.isSREngReady = true;
                            Log.d(TAG, "ASR init success");
                        }

                        @Override
                        public void onInitialError(int errorCode, String asrErrorMessage) {
                            Log.d(TAG, "ASR init failed errorCode=" + errorCode + "   errorMsg=" + asrErrorMessage);
                        }
                    });

                    MusicManager.getInstance().init(MainApplication.context, new InitListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Music init success");
                        }

                        @Override
                        public void onFailed(int i, String s) {
                            Log.d(TAG, "ASR init failed errorCode=" + i + "   errorMsg=" + s);
                        }
                    });


                }
            });

        }

        @Override
        public void onError(int errorCode, String s) {
            Log.d(TAG, "errorCode=" + errorCode + "   errorMsg=" + s);
        }
    };

    private void initSDK() {
        SdkInitializer.init(this, authenticationListener);

    }


    /**
     * 更新用户自定义识别词库
     */
    private void updateLexicon() {

    }


    protected abstract void getDataAndNotify();

    @Override
    public void onSelectFragment(String str) {

    }

    /**
     * 右边的回调按钮
     **/
    @Override
    public void onLeftBtn() {
        finish();
    }

    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    /**
     * 有网络时的回调
     **/
    @Override
    public void onHasNet() {
        toast(getString(R.string.has_net));
    }

    /**
     * 没有网络时的回调
     **/
    @Override
    public void onNotNet() {
        toast(getString(R.string.not_net));
    }

    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
//			Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
//			Log.i(TAG, "MQTT 没有可用网络");
            Toast.makeText(this, "没有可用网络", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
