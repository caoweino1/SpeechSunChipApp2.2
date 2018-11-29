package com.turing.turingsdksample.util;

import android.os.Environment;

/**
 * Created by liwan on 2018/5/8.
 */

public interface FileConstant {
    String NROrgPath = "nrorg.pcm";
    String NRResultPath = "nrresult.pcm";
    String ECOrgPath = "ecorg.pcm";
    String ECRefPath = "ecref.pcm";
    String ECResultPath = "ecresult.pcm";
    String musicPath = Environment.getExternalStorageDirectory().getPath() + "/resche/music.pcm";
    String noisePath = Environment.getExternalStorageDirectory().getPath() + "/resche/noise.pcm";
    String kiwiResPath = Environment.getExternalStorageDirectory().getPath() + "/resche/";
    String kiwiResOnePath = Environment.getExternalStorageDirectory().getPath() + "/resche/one";
    String kiwiResTwoPath = Environment.getExternalStorageDirectory().getPath() + "/resche/two";
    String kiwiResThreePath = Environment.getExternalStorageDirectory().getPath() + "/resche/three";
    String kiwiecmicPath = Environment.getExternalStorageDirectory().getPath() + "/resche/mic.pcm";
    String kiwiecorgmusicPath = Environment.getExternalStorageDirectory().getPath() + "/resche/orgmuisc.pcm";
    String kiwiecresultmusicPath = Environment.getExternalStorageDirectory().getPath() + "/resche/resultmusic.pcm";
    String kiwiecresultPath = Environment.getExternalStorageDirectory().getPath() + "/resche/result.pcm";
    String kiwiecmicName = "/resche/mic.pcm";
    String kiwiecorgmusicName = "/resche/orgmuisc.pcm";
    String kiwiecresultmusicName = "/resche/resultmusic.pcm";
    String kiwiecresultName = "/resche/result.pcm";
}