package com.turing.turingsdksample.music;

import android.media.MediaPlayer;

/**
 * @author：licheng@uzoo.com
 */

public interface MusicListListener {


    /**
     * 第几首歌曲，已经准备好了
     *
     * @param position 位置
     * @param mp       对象
     **/
    public void onMusicPrepared(int position, MediaPlayer mp);


    /**
     * 播放第几首歌的音乐时出现了问题
     *
     * @param position 位置
     * @param code     原因：MusicMeidaErrorCode
     **/
    public void onMusicMediaError(int position, int code);

    /**
     * 第几首歌曲， 音乐播放完成
     *
     * @param position 第几首歌
     * @param mp       播放器对象
     **/
    public void onMusicCompletion(int position, MediaPlayer mp);

    /**
     * 资源列表的错误
     *
     * @param code 当资源列表的出错时，回调MusicErrorCode
     **/
    public void onMusicDataSourceError(int code);


}
