package com.turing.turingsdksample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.turing.iot.MQTTInitListener;
import com.turing.iot.MQTTManager;
import com.turing.iot.MQTTReceiverListener;
import com.turing.iot.bean.Topic;
import com.turing.turingsdksample.R;
import com.turing.turingsdksample.callback.CategoryOperateCallback;

public class IOTFragment extends Fragment implements View.OnClickListener, CategoryOperateCallback {
    private static final String TAG = IOTFragment.class.getSimpleName();
    public static final String deviceType = "gh_ca69fb525566";
    public static final String deviceId = "aiAA847303504c32";
    public static final String apikey = "1d465391260749458b65c27b020207f1";
    public static final String deivceName = "HelloKetty";
    private Topic mTopic;
    //    public static final String deviceType = "gh_133118144c31";
//    public static final String deviceId = "personalHardware1";
//    public static final String deivceName = "AirKiss3_DEVICE_NAME";
    private TextView mContentTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iot, null);
        view.findViewById(R.id.btn_start_airkiss).setOnClickListener(this);
        view.findViewById(R.id.btn_stop_airkiss).setOnClickListener(this);
        view.findViewById(R.id.btn_air_init).setOnClickListener(this);
        view.findViewById(R.id.btn_mqtt_init).setOnClickListener(this);
        view.findViewById(R.id.btn_mqtt_connect).setOnClickListener(this);
        view.findViewById(R.id.btn_mqtt_send).setOnClickListener(this);
        view.findViewById(R.id.btn_mqtt_disconnect).setOnClickListener(this);
        mContentTv = (TextView) view.findViewById(R.id.tv_content);
        MQTTManager.getInstance().registerReceiveListener(mqttReceiverListener);
        return view;
    }

    private MQTTReceiverListener mqttReceiverListener = new MQTTReceiverListener() {
        @Override
        public void onReceive(final String json) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(getActivity(), "json=" + json, Toast.LENGTH_SHORT).show();
                }
            });
            Log.e(TAG, "onReceive: " + json);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_airkiss:
                //AirKissManager.getInstance().start();
                break;
            case R.id.btn_stop_airkiss:
                //AirKissManager.getInstance().stop();
                break;
            case R.id.btn_air_init:
//                //AirKissManager.getInstance().setAirKissListener(new AirKissListener() {
//                    @Override
//                public void onReceive(final String jsonResult) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mContentTv.setText(jsonResult);
//                            }
//                        });
//                        Log.e(TAG, "onReceive: ");
//                    }
//                });
//                AirKissManager.getInstance().init(getContext(), deviceType, deviceId, deivceName, AirKiss3.AK3_SERVICE_WECHAT_MUSIC | AirKiss3.AK3_SERVICE_WECHAT_FILE,
//                        (short) 15601);
                break;
            case R.id.btn_mqtt_init:
                //MQTTManager.getInstance().init(getActivity(), apikey, deviceId, mqttInitListener);
                break;

            case R.id.btn_mqtt_connect:
                if (mTopic == null) {
                    //Toast.makeText(getActivity(), getString(R.string.param_not_null), Toast.LENGTH_SHORT).show();
                    return;
                }
//                MQTTManager.getInstance().connect(mTopic, "tulingtest", "3jJxWAUS", mqttConnectListener);
                break;
            case R.id.btn_mqtt_send:
//                MQTTManager.getInstance().send("hello world");
                break;
            case R.id.btn_mqtt_disconnect:
                MQTTManager.getInstance().disconnected();
                break;
            default:
                break;
        }
    }


    private MQTTInitListener mqttInitListener = new MQTTInitListener() {
        @Override
        public void onSccess(Topic topic) {
            Log.e(TAG, "onSccess: " + topic
                    .toString());
            mTopic = topic;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getActivity(), "MQTT初始化成功.", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onFailed(int errorCode, String errorMsg) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getActivity(), "MQTT初始化失败.", Toast.LENGTH_SHORT).show();
                }
            });

            Log.e(TAG, "onFailed: errorCode=" + errorMsg + "    errorMsg=" + errorMsg);
        }
    };

    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public void operateAll() {

    }

    @Override
    public void operateTextForTTS(String str) {

    }

    @Override
    public void operateOnlyASR() {

    }

    @Override
    public void operateOnlyTTS(String str) {

    }
}
