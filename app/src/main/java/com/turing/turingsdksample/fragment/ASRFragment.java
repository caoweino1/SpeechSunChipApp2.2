package com.turing.turingsdksample.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.turing.asr.callback.AsrListener;
import com.turing.asr.engine.AsrManager;
import com.turing.asr.function.bean.ASRErrorMessage;
import com.turing.turingsdksample.R;
import com.turing.turingsdksample.bean.ContentBean;
import com.turing.turingsdksample.callback.CategoryOperateCallback;
import com.turing.turingsdksample.ui.BottomLayout;
import com.turing.turingsdksample.ui.ContentLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：licheng@uzoo.com
 */

public class ASRFragment extends Fragment implements CategoryOperateCallback {
    private ContentLayout userLayout;
    private BottomLayout bottomLayout;
    private ArrayList<ContentBean> userlist;
    private RelativeLayout rel_status;
    private TextView tv_status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_asr, null);
        //用户的userId
        userLayout = (ContentLayout) view.findViewById(R.id.userlayout);
        userLayout.setCategory(getString(R.string.user));
        userlist = userLayout.getListsData();
        userlist.add(new ContentBean());
        //底部
        bottomLayout = (BottomLayout) view.findViewById(R.id.base_bottom);
        bottomLayout.setCallback(this);
        bottomLayout.onlyASRUI();
        //状态
        rel_status = (RelativeLayout) view.findViewById(R.id.rel_status);
        tv_status = (TextView) view.findViewById(R.id.tv_status);

        return view;
    }

    /**
     * 用户自己的
     **/
    private void operateUserCallback(final String str) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userlist.get(0).setText(str);
                userLayout.setAdapter();
            }
        });
    }

    private AsrListener asrListener = new AsrListener() {

        @Override
        public void onResults(List<String> list) {
            if (list != null && list.size() > 0) {
                operateUserCallback(list.get(0));
                setEndASRLayout();
            }

        }

        @Override
        public void onStartRecord() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setStartASRLayout();
                }
            });
        }

        @Override
        public void onEndOfRecord() {

        }

        @Override
        public void onError(ASRErrorMessage asrErrorMessage) {

        }



        @Override
        public void onVolumeChange(int i) {

        }
    };

    @Override
    public String getFragmentTag() {
        return getString(R.string.text_asr);
    }

    @Override
    public void operateAll() {

    }

    @Override
    public void operateTextForTTS(String str) {

    }

    @Override
    public void operateOnlyASR() {
        if (isASRing()) {
           // AsrManager.getInstance().startAsr(asrListener);
        } else {
            AsrManager.getInstance().stop();
        }

    }

    @Override
    public void operateOnlyTTS(String str) {

    }


    /**
     * 状态
     **/
    private void setStatusView(int flag) {
        rel_status.setVisibility(flag);
    }


    /**
     * 当开始ASR时
     **/
    private void setStartASRLayout() {
        setStatusView(View.VISIBLE);
        tv_status.setText(R.string.asring);
        bottomLayout.setTextBtnASR(getString(R.string.click_asr_stop));
    }

    /**
     * 当asr结束
     **/
    private void setEndASRLayout() {
        setStatusView(View.GONE);
        bottomLayout.setTextBtnASR(getString(R.string.click_asr_start));
    }


    /**
     * 判断当前是不是asring
     **/
    private boolean isASRing() {
        boolean b = false;
        if (getString(R.string.click_asr_start).equals(bottomLayout.getTextBtnASR())) {
            b = true;
        }
        return b;
    }

    /**
     * 当解析出错时
     */
    private void onErrorCallback(final String str) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                bottomLayout.setTextBtnASR(getString(R.string.click_asr_start));
                setStatusView(View.GONE);
            }
        });
    }
}
