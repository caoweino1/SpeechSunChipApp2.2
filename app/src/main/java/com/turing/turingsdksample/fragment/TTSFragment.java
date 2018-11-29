package com.turing.turingsdksample.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turing.tts.TTSListener;
import com.turing.tts.TTSManager;
import com.turing.turingsdksample.R;
import com.turing.turingsdksample.activity.BaseFragmentActivity;
import com.turing.turingsdksample.bean.ContentBean;
import com.turing.turingsdksample.callback.CategoryOperateCallback;
import com.turing.turingsdksample.ui.BottomLayout;
import com.turing.turingsdksample.ui.ContentLayout;

import java.util.ArrayList;

/**
 * @author：licheng@uzoo.com
 */

public class TTSFragment extends Fragment implements CategoryOperateCallback {
    private ContentLayout robotLayout;
    private BottomLayout bottomLayout;
    private ArrayList<ContentBean> robotlist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tts, null);
        //机器人
        robotLayout = (ContentLayout) view.findViewById(R.id.userlayout);
        robotLayout.setCategory(getString(R.string.robot));
        robotLayout.setStartTextShow(getString(R.string.tts_start));
        robotLayout.setEndTextShow(getString(R.string.tts_stop));
        robotLayout.setStartViewShow(View.VISIBLE);
        robotLayout.setEndViewShow(View.VISIBLE);
        robotlist = robotLayout.getListsData();
        robotlist.add(new ContentBean());
        //底部
        bottomLayout = (BottomLayout) view.findViewById(R.id.base_bottom);
        bottomLayout.setCallback(this);
        bottomLayout.onlyTTSUI();
        return view;
    }

    /**
     * 用户自己的
     **/
    private void operateUserCallback(final String str) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                robotlist.get(0).setText(str);
                robotLayout.setAdapter();
                bottomLayout.clearInputDate();
            }
        });
    }

    private TTSListener itsCallback = new TTSListener() {

        @Override
        public void onSpeakBegin(String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakCompleted() {

        }

        @Override
        public void onSpeakFailed() {

        }


    };


    @Override
    public String getFragmentTag() {
        return getString(R.string.text_tts);
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
        operateUserCallback(str);
        if(((BaseFragmentActivity)getActivity()).isTTSInit){
            TTSManager.getInstance().startTTS(str, itsCallback);
        }
    }
}
