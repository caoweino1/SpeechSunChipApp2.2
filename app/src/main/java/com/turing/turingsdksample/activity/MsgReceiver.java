package com.turing.turingsdksample.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.turing.turingsdksample.ability.Base;

/**
 * Created by liwan on 2018/6/21.
 */

public class MsgReceiver extends BroadcastReceiver {
    String TAG = "MsgReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
       try{
           if(intent.getAction().equals("com.qi.returnnotfountname")){
               Log.d("airiche","dot'n call no name");
               String name = intent.getStringExtra("other_name");
               Base.getInstance().readTtsStr("找不到联系人" + name);
               MainApplication.wakeUpService.resumeRecord();
           }
           if(intent.getAction().equals("com.sunchip.stoprecoder")){
               Log.d("airiche","stoprecoder");
               MainApplication.wakeUpService.pauseRecord();
           }
           if(intent.getAction().equals("com.sunchip.startrecoder")){
               Log.d("airiche","startrecoder");
               MainApplication.wakeUpService.resumeRecord();
           }
       }catch (Exception e){
           Log.d(TAG,"MsgReceiver error");
       }
    }
}
