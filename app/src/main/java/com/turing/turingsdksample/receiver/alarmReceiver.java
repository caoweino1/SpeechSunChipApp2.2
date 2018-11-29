package com.turing.turingsdksample.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.turing.turingsdksample.activity.AlarmActivity;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class alarmReceiver extends BroadcastReceiver {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.sunchip.alarm")) {
            mContext = context;
            Log.e("cao", "time out");
            Intent alarm_intent = new Intent(context, AlarmActivity.class);
            alarm_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarm_intent);

        }
    }
}
