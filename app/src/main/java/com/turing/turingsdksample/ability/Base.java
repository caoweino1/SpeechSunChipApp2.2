package com.turing.turingsdksample.ability;

import com.turing.asr.callback.AsrListener;
import com.turing.asr.callback.LexiconUpdateListener;
import com.turing.asr.engine.AsrManager;
import com.turing.asr.function.bean.ASRErrorMessage;
import com.turing.semantic.SemanticManager;
import com.turing.semantic.listener.OnHttpRequestListener;
import com.turing.tts.TTSListener;
import com.turing.tts.TTSManager;
import com.turing.turingsdksample.util.AiricheForceHandle;
import com.turing.turingsdksample.util.OSDataTransformUtil;

import java.util.List;

/**
 * @author：licheng@uzoo.com
 */

public class Base {

    private AsrListener AsrListener;
    private TTSListener TTSListener;
    private OnHttpRequestListener mSemanticListener;
    private boolean isDopost = false;//是否进行请求
    public boolean isTTs = false;//是否tts

    private Base(){}

    private static class SingletonHolder{
        private static final Base INSTANCE = new Base();
    }

    public static Base getInstance(){
        return SingletonHolder.INSTANCE;
    }

    protected void setAsrListener(AsrListener AsrListener) {
        this.AsrListener = AsrListener;
    }

    protected void setTTSListener(TTSListener TTSListener) {
        this.TTSListener = TTSListener;
    }

    protected void setSemanticListener(OnHttpRequestListener semanticListener) {
        this.mSemanticListener = semanticListener;
    }

    protected OnHttpRequestListener getSemanticListener() {
        return mSemanticListener;
    }

    protected AsrListener getAsrListener() {
        return AsrListener;
    }

    protected TTSListener getTTSListener() {
        return TTSListener;
    }

    protected AsrListener asrListener = new AsrListener() {
        @Override
        public void onResults(List<String> list) {
            if (AsrListener != null) {
                AsrListener.onResults(list);
            }
            if (isDopost) {
                if (list != null && list.size() != 0) {
                    doPost(list.get(0));
                } else {
                    doPost("");
                }
            }
        }

        @Override
        public void onStartRecord() {
            if (AsrListener != null) {
                AsrListener.onStartRecord();
            }
        }

        @Override
        public void onEndOfRecord() {
            if (AsrListener != null) {
                AsrListener.onEndOfRecord();
            }
        }

        @Override
        public void onError(ASRErrorMessage errorMessage) {
            if (AsrListener != null) {
                AsrListener.onError(errorMessage);
            }
        }

        @Override
        public void onVolumeChange(int i) {
            if (AsrListener != null) {
                AsrListener.onVolumeChange(i);
            }
        }
    };
    protected OnHttpRequestListener semanticListener = new OnHttpRequestListener() {

        @Override
        public void onSuccess(String s) {
            if (mSemanticListener != null) {
                mSemanticListener.onSuccess(s);
            }
            if (isTTs) {
                readTts(s);
            }
        }

        @Override
        public void onError(int i, String s) {
            if (mSemanticListener != null) {
                mSemanticListener.onError(i, s);
            }
        }

        @Override
        public void onCancel() {

        }

    };
    protected TTSListener ttsCallBack = new TTSListener() {

        @Override
        public void onSpeakBegin(String s) {
            if (TTSListener != null) {
                TTSListener.onSpeakBegin(s);
            }
        }

        @Override
        public void onSpeakPaused() {
            if (TTSListener != null) {
                TTSListener.onSpeakPaused();
            }
        }

        @Override
        public void onSpeakResumed() {
            if (TTSListener != null) {
                TTSListener.onSpeakResumed();
            }
        }

        @Override
        public void onSpeakCompleted() {
            if (TTSListener != null) {
                TTSListener.onSpeakCompleted();
            }
        }

        @Override
        public void onSpeakFailed() {

        }


    };

    /**
     * 录音
     **/
    protected void record() {
        //AsrManager.getInstance().startAsr(asrListener);

    }

    /**
     * 更新用户自定义识别词库
     */
    protected void updateLexicon(List<String> list, LexiconUpdateListener lexiconUpdateListener) {
        //AsrManager.getInstance().updateLexicon(list, lexiconUpdateListener);
    }

    /**
     * 语义
     **/
    public void doPost(String content) {
        if(AiricheForceHandle.getInstance().istextDispose(content)){
            return;
        }
        SemanticManager.getInstance().requestSemantic(content, semanticListener);
    }

    /**
     * 读
     **/
    public void readTts(String str) {
        try {
            String tts = (String) OSDataTransformUtil.getResultBean(str).getValues().getText();
            int code = OSDataTransformUtil.getIntent(str).getCode();
            /*** 音乐 + */
            if (code == 200101 || code == 200201 ) {
                return;
            }

            TTSManager.getInstance().startTTS(tts, ttsCallBack);
        } catch (Exception e) {

        }
    }


    /**
     * 读
     **/
    public void readTtsStr(String str) {
        try {
            TTSManager.getInstance().startTTS(str, ttsCallBack);
        } catch (Exception e) {

        }
    }

    /**
     * 用于设置
     **/
    protected void setActionBo(boolean isdo, boolean isTt) {
        this.isDopost = isdo;
        this.isTTs = isTt;
    }

    /**
     * asr+语义+tts
     **/
    public void getAll(AsrListener asrClientListener, OnHttpRequestListener semanticClientListener, TTSListener ttsClientListener) {
        setAsrListener(asrClientListener);
        setSemanticListener(semanticClientListener);
        setTTSListener(ttsClientListener);
        setActionBo(true, true);
        record();
    }
}
