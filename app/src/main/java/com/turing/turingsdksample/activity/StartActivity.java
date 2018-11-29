package com.turing.turingsdksample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.turing.turingsdksample.R;


/**
 * 闪屏页面
 *
 * @author：licheng@uzoo.com
 */

public class StartActivity extends Activity {
    private boolean isStartMained = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startLoginActivity();
            }
        }, 2000);
    }

    /**
     * 进入主页面
     */
    private void startLoginActivity() {
        if (!isStartMained) {
            isStartMained = true;
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
