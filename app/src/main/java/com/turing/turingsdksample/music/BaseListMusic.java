package com.turing.turingsdksample.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author：licheng@uzoo.com
 */

public class BaseListMusic {

    public static BaseListMusic instance;
    protected MediaPlayer mediaPlayer;
    protected ArrayList<MusicBean> lists;
    protected int mode = MusicPlayMode.MODE_SQUENCE;//播放的模式，用于选择接下来要播放的是哪一首歌曲
    protected Context context;
    protected int position;//当前准备要播放歌曲，默认的从第一首开始
    protected MusicListListener listListener;


    protected void statrBefor() {
        if (lists != null && lists.size() > 0) {
            if (checkListError() == lists.size()) {
                handererrorListen(1, MusicResourceErrorCode.ERROR_ALL_FILE_RES_ERROR);
                return;
            }
            //判断当前的模式时什么
            if (mode == MusicPlayMode.MODE_SQUENCE) {
                //如果是顺序播放
                do {
                    position++;
                    if (lists.size() == position || lists.size() < position) {
                        //如果到了最后一首，那么就
                        position = 0;
                    }
                } while (lists.get(position).getError() == MusicStatus.MUSIC_RES_STATUS_ERROR);

            } else if (mode == MusicPlayMode.MODE_SINGLE) {
                //如果是单曲循环
            } else if (mode == MusicPlayMode.MODE_RANDOM) {
                //如果是随机播放
                Random random = new Random();

                int temp = random.nextInt(lists.size());
                if (temp == position) {
                    //如果随机的数字，与当前的相等，那么就在前一个的基础上+1，保证其与前一个不重复
                    temp++;
                }
                position = temp;
            }
            startMusic(lists.get(position));
        }
    }

    /**
     * 开始播放音乐
     *
     * @param musicBean musicBean
     **/
    protected void startMusic(MusicBean musicBean) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (MusicResStyle.RESSTYLE__URL_PATH == musicBean.getStyle()) {
            //当时url路径时
            if (musicBean.getUrlPath().startsWith("http")) {
                down(musicBean);
            } else {
                startMusic(musicBean, musicBean.getUrlPath());
            }

        } else {
            //当为资源文件时
            mediaPlayer = MediaPlayer.create(context, musicBean.getRaw());
            setBase(musicBean);
        }
    }


    private void startMusic(MusicBean musicBean, String path) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        //当时url路径时
        mediaPlayer = new MediaPlayer();
        Uri uri = Uri.parse(path);
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            mediaPlayer = null;
        }
        setBase(musicBean);
    }


    private void setBase(MusicBean musicBean) {
        //当为空时
        if (mediaPlayer == null) {
            handererrorListen(2, MusicMeidaErrorCode.ERROR_SOME_FILE_PARSE_ERROR);
            musicBean.setError(MusicStatus.MUSIC_RES_STATUS_ERROR);
            statrBefor();
            return;
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (listListener != null) {
                    listListener.onMusicPrepared(position, mp);
                }
                mediaPlayer.start();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                handererrorListen(2, MusicMeidaErrorCode.ERROR_OPERATE_ERROR);
                return false;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (listListener != null) {
                    listListener.onMusicCompletion(position, mp);
                }
                statrBefor();
            }
        });
    }


    /**
     * 设置资源错误
     **/
    protected void handererrorListen(int what, int code) {
        switch (what) {
            case 1://资源list
                if (listListener != null) {
                    listListener.onMusicDataSourceError(code);
                }
                break;
            case 2://单一file
                if (listListener != null) {
                    listListener.onMusicMediaError(position, code);
                }
                break;
            default:
                break;
        }

    }


    /**
     * 判断当前的
     *
     * @return int
     **/
    private int checkListError() {
        int temp = 0;
        for (MusicBean bean : lists) {
            if (bean.getError() == MusicStatus.MUSIC_RES_STATUS_ERROR) {
                temp++;
            }
        }
        return temp;
    }

    protected MusicHttpImpl impl;

    private void down(final MusicBean musicBean) {
        impl = new MusicHttpImpl() {
            @Override
            public void onStart(final String path) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        startMusic(musicBean, path);
                    }
                });
            }

            @Override
            public void onErrorCall(String error) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        handererrorListen(2, MusicMeidaErrorCode.ERROR_SOME_FILE_PARSE_ERROR);
                    }
                });
            }

            @Override
            public void onLoad() {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpConnectionUtil.downLoadFromUrl(musicBean.getUrlPath(), impl);
            }
        }).start();

    }
}
