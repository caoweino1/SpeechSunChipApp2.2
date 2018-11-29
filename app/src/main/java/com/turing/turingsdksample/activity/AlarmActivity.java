package com.turing.turingsdksample.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;

import com.turing.turingsdksample.R;

public class AlarmActivity extends Activity {
    private MediaPlayer mPlayer2;
    private long clickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        SpeakTest("clock.mp3");

    }

/*闹钟apk*/


    public void SpeakTest(String PlayerFile) {
        mPlayer2 = new MediaPlayer();
        try {
            AssetFileDescriptor fd = getAssets().openFd(PlayerFile);
            mPlayer2.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
                    fd.getDeclaredLength());
            mPlayer2.prepare();
            mPlayer2.start();
            mPlayer2.setLooping(true);
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - clickTime < 2 * 1000) {
                mPlayer2.stop();
                Intent intent = new Intent(AlarmActivity.this, MainFragmentActivity.class);
                startActivity(intent);
                // finish();
            } else {
                clickTime = System.currentTimeMillis();
            }
        }


        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onPause() {
        mPlayer2.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
