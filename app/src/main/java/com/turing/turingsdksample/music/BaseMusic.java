package com.turing.turingsdksample.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;



import java.io.IOException;


/**
 * @author：licheng@uzoo.com
 */

public class BaseMusic {
    private final String TAG = BaseMusic.class.getSimpleName();
    protected MediaPlayer mediaPlayer;
    protected MusicStatusListener mMusicStatusListener;

    /**
     * 初始化mediaPlayer
     **/
    private void initMediaUseString() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
    }

    public void setmMusicStatusListener(MusicStatusListener mMusicStatusListener) {
        this.mMusicStatusListener = mMusicStatusListener;
    }

    /**
     * 开始一段音乐
     *
     * @param context  上下文
     * @param urlPath  对应的资源url路径
     * @param listener 对应的监听
     **/
    public void startMusic(Context context, String urlPath, final MusicStatusListener listener) {
        cancleMusicLoading();
        if (TextUtils.isEmpty(urlPath)) {
            listener.onMusicFileError();
            return;
        }
        if (urlPath.startsWith("http")) {
            mediaPlayer = null;
            down(context, urlPath, listener);
            return;
        }

        initMediaUseString();
        Uri uri = Uri.parse(urlPath);
        try {
            mediaPlayer.setDataSource(context, uri);
            if (mediaPlayer == null) {
                if (listener != null) {
                    listener.onMusicFileError();
                }
                return;
            }

            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                if (listener != null) {
                    listener.onMusicFileError();
                }
                return;
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (listener != null) {
                        listener.onMusicPrepared(mp);
                        mediaPlayer.start();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (listener != null) {
                        listener.onMusicCompletion(mp);
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (listener != null) {
                        listener.onMusicMediaError(mp, what, extra);
                    }
                    return false;
                }
            });
            setmMusicStatusListener(listener);

        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onMusicFileError();
            }
        }

    }

    private MusicHttpImpl impl;

    private void down(final Context context, final String urlPath, final MusicStatusListener listener) {
        impl = new MusicHttpImpl() {
            @Override
            public void onStart(final String path) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        startMusic(context, path, listener);
                    }
                });
            }

            @Override
            public void onErrorCall(String error) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onMusicFileError();
                        }
                    }
                });
            }

            @Override
            public void onLoad() {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onLoading();
                        }
                    }
                });
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpConnectionUtil.downLoadFromUrl(urlPath, impl);
            }
        }).start();

    }

    /**
     * 开始一段音乐
     *
     * @param context  上下文
     * @param raw      资源文件的R值
     * @param listener 监听
     **/
    public void startMusic(final Context context, final int raw, final MusicStatusListener listener) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        try {
            mediaPlayer = MediaPlayer.create(context, raw);
            if (mediaPlayer == null) {
                if (listener != null) {
                    listener.onMusicFileError();
                }
                return;
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (listener != null) {
                        listener.onMusicPrepared(mp);
                    }
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (listener != null) {
                        listener.onMusicMediaError(mp, what, extra);
                    }
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (listener != null) {
                        listener.onMusicCompletion(mp);
                    }
                }
            });
            setmMusicStatusListener(listener);
        } catch (Exception e) {
        }
    }

    /**
     * 暂停音乐
     **/
    public void pauseMusic() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 继续播放音乐
     **/
    public void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    /**
     * 停止音乐
     **/
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            if (mMusicStatusListener != null) {
                mMusicStatusListener.onStopMusic();
            }
        }
    }

    /**
     * 取消下载
     * 此方法是用来在网络歌曲下载过程中，不想播放音乐了，就要用这种方法
     * 因为音乐下载完成后，会自动开始播放，这儿相当与不要自动播放
     **/
    public void cancleMusicLoading() {
        if (impl != null) {
            impl.setReturn(false);
        }
    }
}
