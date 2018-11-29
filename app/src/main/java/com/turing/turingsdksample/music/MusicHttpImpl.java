package com.turing.turingsdksample.music;


/**
 * @author：licheng@uzoo.com
 */

public abstract class MusicHttpImpl implements MusicHttpDownLoadCallback {

    private boolean isReturn = true;//是否还回调


    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }


    @Override
    public void onSuccess(String path) {
        if (isReturn) {
            onStart(path);
        }
    }

    @Override
    public void onError(String error) {
        if (isReturn) {
            //回调
            onErrorCall(error);
        }
    }

    @Override
    public void onLoading() {
        if (isReturn) {
            //回调
            onLoad();
        }
    }

    public abstract void onStart(String path);

    public abstract void onErrorCall(String error);

    public abstract void onLoad();
}
