package com.turing.turingsdksample.util;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.turing.turingsdksample.R;
import com.turing.turingsdksample.ability.Base;
import com.turing.turingsdksample.activity.Blackboard;
import com.turing.turingsdksample.activity.MainApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

/**
 * Created by liwan on 2018/7/5.
 * 动作控制
 */

public class ActionManager {
    BufferedWriter WorkUpWriter;
    AudioManager mAudioManager;
    HandlerThread handlerThread = new HandlerThread("ActionManager");
    Handler handler;
    public static MediaPlayer mPlayer2;
    MediaPlayer mMediaPlayer;
    int currentMusicId = 0;

    /***JVM保证线程安全*/
    private static class SingletonHolder {
        private static ActionManager instance = new ActionManager();
    }

    //本地的一些命令控制
    private ActionManager() {
        System.out.println("Singleton has loaded");
        mAudioManager = (AudioManager) MainApplication.context.getSystemService(Context.AUDIO_SERVICE);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                sendCmdToMachine(msg.what);
            }
        };
    }

    public static ActionManager getInstance() {
        return SingletonHolder.instance;
    }


    public void controlMachine(int cmdID, boolean isViewShow) {
        stopMusic();
        if (isViewShow) {
            if (Blackboard.getInstance().allFragment != null) {
                /**asr的结果是否出现异常 这里显示没有*/
                Blackboard.getInstance().allFragment.isASR = false;
            }
        }

        if (cmdID == 4) {
            sendAction(1, isViewShow, "给我向前走", "好的，我要向前走啦");
        }
        if (cmdID == 5) {
            sendAction(2, isViewShow, "给我向后走", "好的，我要向后走啦");
        }
        if (cmdID == 6) {
            sendAction(3, isViewShow, "给我向左转", "好的，我要向左转啦");
        }
        if (cmdID == 7) {
            sendAction(4, isViewShow, "给我向右转", "好的，我要向右转啦");
        }
        if (cmdID == 8) {
            sendAction(5, isViewShow, "给我跳个舞", "好的，请欣赏我的舞蹈");
        }
        if (cmdID == 9) {
            if (isViewShow) {
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("给我拍照片");
                }
                Base.getInstance().readTtsStr("好的，要拍照片啦");
            } else {
                CommonUtil.getInstance().startTTS(5);
            }
            openCapture();
        }
        if (cmdID == 10) {
            if (isViewShow) {
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("音量大一点");
                }
                Base.getInstance().readTtsStr("好的，帮音量调大一点");
            } else {
                CommonUtil.getInstance().startTTS(6);
            }
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
                    AudioManager.FX_FOCUS_NAVIGATION_UP);
            int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //得到听筒模式的最大值
            current += 4;
            if (current >= max) current = max;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_SHOW_UI);
        }
        if (cmdID == 11) {
            if (isViewShow) {
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("音量小一点");
                }
                Base.getInstance().readTtsStr("好的，帮音量调小一点");
            } else {
                CommonUtil.getInstance().startTTS(7);
            }
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
                    AudioManager.FX_FOCUS_NAVIGATION_UP);
            int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            current -= 4;
            if (current <= 0) current = 0;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_SHOW_UI);

        }
        if (cmdID == 12) {
            sendAction(0, isViewShow, "给我停下来", "好的，不动就不动，我做木头人");
        }
        if (cmdID == 13) {
            if (isViewShow) {
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("给我唱首歌");
                }
                Base.getInstance().readTtsStr("好的，请欣赏我的歌曲");
            } else {
                // CommonUtil.getInstance().startTTS(7);
            }
            palyMusic();
        }
        if (cmdID >= 20) {
            AppManager.getInstance().handAPP(cmdID, isViewShow);
        }
    }

    private void openCapture() {
        handler.post(new Runnable() {
            @Override
            public void run() {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, 0);
//                cameraIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                MainApplication.context.startActivity(cameraIntent);
                Intent intent = new Intent(Intent.ACTION_MAIN);

                ComponentName comp = new ComponentName(
                        "com.android.camera2",
                        "com.android.camera.CameraLauncher");
                intent.setComponent(comp);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainApplication.context.startActivity(intent);

            }
        });
    }


    private void sendAction(int cmdIDCX, boolean isViewShow, String asktip, String answertip) {
        if (isViewShow && AppManager.getInstance().isNetworkConnected(MainApplication.context)) {
            if (Blackboard.getInstance().allFragment != null) {
                Blackboard.getInstance().allFragment.sendText(asktip);
            }
            Base.getInstance().readTtsStr(answertip);
        } else {
            if (cmdIDCX == 0) {
                CommonUtil.getInstance().startTTS(0);
            }
            if (cmdIDCX == 1) {
                CommonUtil.getInstance().startTTS(1);
            }
            if (cmdIDCX == 2) {
                CommonUtil.getInstance().startTTS(2);
            }
            if (cmdIDCX == 3) {
                CommonUtil.getInstance().startTTS(3);
            }
            if (cmdIDCX == 4) {
                CommonUtil.getInstance().startTTS(4);
            }
            if (cmdIDCX == 5) {
                CommonUtil.getInstance().startTTS(8);
            }

        }
        handler.sendEmptyMessage(cmdIDCX);
        //sendCmdToMachine(cmdIDCX);
    }

    public void sendControlHandler(int cmdIDCX) {
        handler.sendEmptyMessage(cmdIDCX);
    }

    /**
     * ACITON_BRAKE    = 0,
     * ACITON_FORWARD  = 1,
     * ACITON_BACKWARD = 2,
     * ACITON_LEFT     = 3,
     * ACITON_RIGHT    = 4,
     * ACITON_DANCE    = 5,      //跳舞
     * 动作耗时较少，没有必要单独在非UI执行。所以这个方法，不用担心，调用的线程是UI还是非UI
     */
    public void sendCmdToMachine(int id) {
        Log.i("airiche", "sendCmdToMachine" + id);
        /**跳舞时播放歌曲,退出apk停止播放歌曲*/
        if (id == 5) {
            SpeakTest("song.mp4");
        } else if (id == 0) {
            if (mPlayer2 != null) {
                mPlayer2.stop();
            }
        }
        try {
            WorkUpWriter = new BufferedWriter(new FileWriter("/sys/class/action/mx612e/workup"));
        } catch (Exception e) {
            Log.i("airiche", "mWorkUpDecectNode error!!");
        }
        try {
            WorkUpWriter.write(id + "");
            WorkUpWriter.flush();
        } catch (Exception e) {
            Log.i("airiche", "w.write /sys/class/wakeup/attr/wakeup node error!!");
        }

        try {
            WorkUpWriter.close();
        } catch (Exception e) {

        }
    }

    public void SpeakTest(String PlayerFile) {

        mPlayer2 = new MediaPlayer();
        try {
            AssetFileDescriptor fd = MainApplication.context.getAssets().openFd(PlayerFile);
            mPlayer2.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
                    fd.getDeclaredLength());
            mPlayer2.prepare();
            mPlayer2.start();
            //设置播放的音频文件的声音大小
            mPlayer2.setVolume(0.4f, 0.4f);
//				Handler handler = new Handler();
//		        handler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						mPlayer2.stop();
//					}
//		        }, timer);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void stopMusic() {

        if (mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.reset();
                mMediaPlayer.release();
            } catch (Exception e) {

            }
        }
    }

    public void palyMusic() {
        stopMusic();
        Random rand = new Random();
        currentMusicId = rand.nextInt(11);
        if (currentMusicId == 0) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_zero);
        }
        if (currentMusicId == 1) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_one);
        }
        if (currentMusicId == 2) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_two);
        }
        if (currentMusicId == 3) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_three);
        }
        if (currentMusicId == 4) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_four);
        }
        if (currentMusicId == 5) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_five);
        }
        if (currentMusicId == 6) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_six);
        }
        if (currentMusicId == 7) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_seven);
        }
        if (currentMusicId == 8) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_eight);
        }
        if (currentMusicId == 9) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_night);
        }
        if (currentMusicId == 10) {
            mMediaPlayer = MediaPlayer.create(MainApplication.context, R.raw.music_ten);
        }

        mMediaPlayer.start();
    }
}
