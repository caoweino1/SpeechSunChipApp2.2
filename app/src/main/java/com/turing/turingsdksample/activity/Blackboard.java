package com.turing.turingsdksample.activity;

import android.os.Handler;
import android.util.Log;

import com.turing.asr.callback.AsrListener;
import com.turing.turingsdksample.fragment.AllFragment;

/**
 * Created by liwan on 2018/6/21.
 * 记录应用保存的数据
 */

public class Blackboard {
    private Handler mNormalHandler;
    private static Blackboard blackboard;
    /**
     * 控制在界面里面的逻辑
     */
    public AllFragment allFragment;
    /**
     * asr结果回调通知
     */
    public AsrListener asrListener;
    /**
     * 是否給asr送音频
     */
    public boolean isAsrWork = false;
    /**
     * 唤醒以后1.5S内的asr消息清除掉。没有办法解决唤醒的音频被图灵误识别
     */
    public boolean isIgnoreResult = false;

    public static Blackboard getInstance() {
        if (blackboard == null) {
            blackboard = new Blackboard();
        }
        return blackboard;
    }

    private Blackboard() {
        mNormalHandler = new Handler();
    }

    /**
     * 2.5以内的ASR消息都忽略掉
     */
    public void setIgnoreResult() {
        mNormalHandler.removeCallbacks(r);
        isIgnoreResult = true;
        mNormalHandler.postDelayed(r, 3500);
    }


    Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                isIgnoreResult = false;
            } catch (Exception e) {
                Log.d("airiche", " isIgnoreResult" + e.toString());
            }
        }
    };

    /**
     * 清除
     */
    public void clean() {
        allFragment = null;
    }

}
