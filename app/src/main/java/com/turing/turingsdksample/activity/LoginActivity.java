package com.turing.turingsdksample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.turing.turingsdksample.R;

/**
 * @author wuyihua
 * @Date 2017/9/30
 * @todo
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String KEY = "key";
    private static final String SECRET = "secret";
    private EditText apiKeyEt;
    private EditText secretEt;
    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiKeyEt = (EditText) findViewById(R.id.et_apikey);
        secretEt = (EditText) findViewById(R.id.et_secret);
        findViewById(R.id.btn_login).setOnClickListener(this);
        startMainActivity();
    }


    private void startMainActivity() {
        Intent intent = new Intent(this, MainFragmentActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {

    }
}
