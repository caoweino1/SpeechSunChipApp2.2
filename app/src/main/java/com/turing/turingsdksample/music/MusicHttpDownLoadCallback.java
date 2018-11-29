package com.turing.turingsdksample.music;

/**
 * @author：licheng@uzoo.com
 */

public interface MusicHttpDownLoadCallback {

    /**
     * 下载成功，返回路径
     *
     * @param path 返回路径
     **/
    public void onSuccess(String path);

    /**
     * 下载失败，返回原因
     *
     * @param error 错误原因
     **/
    public void onError(String error);

    /**
     * 正在加载
     **/
    public void onLoading();
}
