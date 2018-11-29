package com.turing.turingsdksample.music;

import android.media.MediaPlayer;

/**
 * @author：licheng@uzoo.com
 */

public interface MusicStatusListener {

    /**
     * 音乐准备完毕，即将开始播
     *
     * @param mp 对应的监听对象
     **/
    public void onMusicPrepared(MediaPlayer mp);

    /**
     * 音乐播放完成
     *
     * @param mp 对应的监听对象
     **/
    public void onMusicCompletion(MediaPlayer mp);

    /**
     * 音乐资源出错,比如文件url路径为null或者加载不到
     **/
    public void onMusicFileError();

    /**
     * 音乐MediaPlay播放过程中，出现了异常
     *
     * @param mp    对象
     * @param what  错误的标记
     * @param extra android系统的错误标记码
     **/
    public void onMusicMediaError(MediaPlayer mp, int what, int extra);

    /**
     * 如果是网络音乐，就会这个，表示正在加载的过程
     **/
    public void onLoading();
    /**
     * 停止音乐
     * **/
    public void onStopMusic();
}
