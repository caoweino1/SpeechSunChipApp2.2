package com.turing.turingsdksample.music;

import android.content.Context;
import android.media.MediaPlayer;


/**
 * @author：licheng@uzoo.com
 */

public class TuringMusic extends BaseMusic {
    private static TuringMusic instance;

    private TuringMusic() {

    }

    /**
     * 单例
     *
     * @return TuringMusic
     **/
    public static TuringMusic getInstance() {
        if (instance == null) {
            instance = new TuringMusic();
        }
        return instance;
    }

    /**
     * 开始一段音乐
     *
     * @param context  上下文
     * @param urlPath  对应的资源url路径
     * @param listener 对应的监听
     **/

    @Override
    public void startMusic(Context context, String urlPath, final MusicStatusListener listener) {
        super.startMusic(context, urlPath, listener);
    }

    /**
     * 开始一段音乐
     *
     * @param context  上下文
     * @param raw      资源文件的R值
     * @param listener 监听
     **/
    @Override
    public void startMusic(final Context context, final int raw, final MusicStatusListener listener) {
        super.startMusic(context, raw, listener);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * 暂停音乐
     **/
    @Override
    public void pauseMusic() {
        super.pauseMusic();
    }

    /**
     * 继续播放音乐
     **/
    @Override
    public void resumeMusic() {
        super.resumeMusic();
    }

    /**
     * 停止音乐
     **/
    @Override
    public void stopMusic() {
        super.stopMusic();
    }
}
