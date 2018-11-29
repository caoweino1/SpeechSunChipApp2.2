package com.turing.turingsdksample.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.turing.turingsdksample.activity.MainApplication;

/**
 * Created by Administrator on 2018/7/5 0005.
 */
//控制录音的静态广播
public class ControlRecoderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.sunchip.stoprecoder")){
            Log.e("cao","com.sunchip.stoprecoder");
            MainApplication.wakeUpService.pauseRecord();
        }
        else if (intent.getAction().equals("com.sunchip.startrecoder")){
            Log.e("cao","com.sunchip.startrecoder");
            MainApplication.wakeUpService.resumeRecord();
        }

    }
}
