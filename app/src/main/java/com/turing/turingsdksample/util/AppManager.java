package com.turing.turingsdksample.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.turing.tts.TTSListener;
import com.turing.tts.TTSManager;
import com.turing.turingsdksample.ability.Base;
import com.turing.turingsdksample.activity.Blackboard;
import com.turing.turingsdksample.activity.MainApplication;
import com.turing.turingsdksample.bean.MainItemBean;
import com.turing.turingsdksample.constants.Constants;

import java.io.File;
import java.util.List;

/**
 * Created by liwan on 2018/7/13.
 */

public class AppManager implements TTSListener {
    private static boolean isDefault = true;
    private List<MainItemBean> mListData;
    private volatile static AppManager appManger;
    private int currentApp = 0;

    public static AppManager getInstance() {
        if (appManger == null) {
            synchronized (AppManager.class) {
                if (appManger == null) {
                    appManger = new AppManager();
                }
            }
        }
        return appManger;
    }

    private AppManager() {
        addLauncherIamger();
    }

    //加载机器里的json文件
    private void addLauncherIamger() {
        File file = new File(FileManager.DEFAULT__LAUNCHERXMLPATH);
        if (file.exists()) {
            String data = ReadAssetsUtils.getdefaultData(MainApplication.context, FileManager.DEFAULT__LAUNCHERXMLPATH + "/defaults.json");
            Log.e("cao", "path" + FileManager.DEFAULT__LAUNCHERXMLPATH + "data==" + data);
            mListData = JsonUtils.fromJson(data, new TypeToken<List<MainItemBean>>() {

            });
            isDefault = true;
        } else {
//                String data = ReadAssetsUtils.getData(MainActivity.this, "defaults.json");
//                mListData= JsonUtils.fromJson(data, new TypeToken<List<MainItemBean>>() {});
            isDefault = false;
        }
    }

    public void handAPP(int bnfId, boolean isViewShow) {
        if (isViewShow) {
            if (bnfId == 21) {
                // 打开多元智能 21


                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开多元智能");
                }
                /**没有网络TTS无法播音 所以只能直接开启**/
                if (!isNetworkConnected(MainApplication.context)) {
                    openAppExt("wyt[@]0");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.xq.flash")) {
                    TTSManager.getInstance().startTTS("好的，马上打开多元智能", this);
                    currentApp = 21;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }
            if (bnfId == 22) {
                // 打开亲子互动 22
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开亲子互动");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openAppExt("wyt[@]13");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.xq.flash")) {
                    TTSManager.getInstance().startTTS("好的，马上打开亲子互动", this);
                    currentApp = 22;
                    // wyt[@]13
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }

            }

            if (bnfId == 23) {
                // 打开视觉空间 23
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开视觉空间");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openAppExt("wyt[@]7");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.xq.flash")) {
                    TTSManager.getInstance().startTTS("好的，马上打开视觉空间", this);
                    currentApp = 23;
                    // wyt[@]13
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }

            }

            if (bnfId == 24) {

                //打开习惯养成 24
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开习惯养成");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openAppExt("wyt[@]10");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.xq.flash")) {
                    TTSManager.getInstance().startTTS("好的，马上打开习惯养成", this);
                    currentApp = 24;
                    // wyt[@]10
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }

            }

            if (bnfId == 25) {

                //打开自然科学 25
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开自然科学");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openAppExt("wyt[@]8");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.xq.flash")) {
                    TTSManager.getInstance().startTTS("好的，马上打开自然科学", this);
                    currentApp = 25;
                    // wyt[@]8
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }

            }

            if (bnfId == 26) {
                //     打开语言启蒙 26
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开语言启蒙");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openAppExt("wyt[@]6");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.xq.flash")) {
                    TTSManager.getInstance().startTTS("好的，马上打开语言启蒙", this);
                    currentApp = 26;
                    // wyt[@]6
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }

            }

            if (bnfId == 27) {

                //     打开快乐英语 27
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开快乐英语");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openAppExt2("wyt[@][1]6");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.xq.flash")) {
                    TTSManager.getInstance().startTTS("好的，马上打开快乐英语", this);
                    currentApp = 27;
                    // wyt[@][1]6
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }
            if (bnfId == 28) {
                //     打开数字逻辑 28
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开数字逻辑");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openAppExt("wyt[@]9");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.xq.flash")) {
                    TTSManager.getInstance().startTTS("好的，马上打开数字逻辑", this);
                    currentApp = 28;
                    // wyt[@][1]9
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 29) {

                //     打开故事王国 29
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开故事王国");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openAppExt("wyt[@]3");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.xq.flash")) {
                    TTSManager.getInstance().startTTS("好的，马上打开故事王国", this);
                    currentApp = 29;
                    // wyt[@][1]3
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }
            if (bnfId == 30) {

                //     打开传统国学 30

                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开传统国学");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openAppExt("wyt[@]2");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.xq.flash")) {
                    TTSManager.getInstance().startTTS("好的，马上打开传统国学", this);
                    currentApp = 30;
                    // wyt[@][1]2
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }
            if (bnfId == 31) {
                //     打开酷我K歌 31
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开酷我K歌");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("cn.kuwo.sing.tv", "cn.kuwo.sing.tv.activity.EntryActivity");
                    return;
                }
                if (checkApkExist(MainApplication.context, "cn.kuwo.sing.tv")) {
//                    "pkgname": "cn.kuwo.sing.tv",
//                            "clsname": "cn.kuwo.sing.tv.activity.EntryActivity"
                    TTSManager.getInstance().startTTS("好的，马上打开酷我K歌", this);
                    currentApp = 31;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 32) {
                //     打开微信对讲 32
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开微信对讲");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.tencent.mm")) {
//                    3.微信对讲 "pkgname": "com.tencent.mm",
//                            "clsname": "com.tencent.mm.ui.LauncherUI"
                    TTSManager.getInstance().startTTS("好的，马上打开微信对讲", this);
                    currentApp = 32;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 33) {
                //      打开本地K歌 33
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开本地K歌");
                }

                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.sunchip.luqiyalauncher", "com.example.android_launcher_lqy.Ui.CommonGridViewFragment"));
                    intent.putExtra("audio_type", 1);
                    intent.putExtra("audio_path", "本地K歌");
                    intent.putExtra("bj", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(1).getIcon());
                    intent.putExtra("isDefault", isDefault);
                    intent.putExtra("gridViewleft", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(0).getGridViewleft());
                    intent.putExtra("gridViewright", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(0).getGridViewright());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainApplication.context.startActivity(intent);
                } catch (Exception e) {
                    Log.e("cao", "e==" + e);
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }

            }
            if (bnfId == 34) {
                //           打开画一画 34
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开画一画");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.mobileapps.apps.LearnToDraw", "air.com.zy.huihuamianfei.AppEntry");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.mobileapps.apps.LearnToDraw")) {
                    //6.画一画
//                    "pkgname": "com.mobileapps.apps.LearnToDraw",
//                            "clsname": "air.com.zy.huihuamianfei.AppEntry"
                    TTSManager.getInstance().startTTS("好的，马上打开画一画", this);
                    currentApp = 34;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 35) {
                //            打开考拉FM 35
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开考拉FM");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.edog.car", "com.kaolafm.auto.home.MainActivity");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.edog.car")) {
//                    "pkgname": "com.edog.car",
//                            "clsname": "com.kaolafm.auto.home.MainActivity"
                    TTSManager.getInstance().startTTS("好的，马上打开考拉FM", this);
                    currentApp = 35;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 36) {
                //                 打开爱奇艺少儿 36
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开爱奇艺少儿");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.qiyi.video.child", "com.qiyi.video.child.WelcomeActivity");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.qiyi.video.child")) {

//                    "pkgname": "com.qiyi.video.child",
//                            "clsname": "com.qiyi.video.child.WelcomeActivity"
                    TTSManager.getInstance().startTTS("好的，马上打开爱奇艺少儿", this);
                    currentApp = 36;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 37) {
                //                 打开爱奇艺追剧 37
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开爱奇艺追剧");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.qiyi.video.pad", "org.qiyi.android.video.MainActivity");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.qiyi.video.pad")) {
//                    pkgname": "com.qiyi.video.pad",
//                    "clsname": "org.qiyi.android.video.MainActivity"
                    TTSManager.getInstance().startTTS("好的，马上打开爱奇艺追剧", this);
                    currentApp = 37;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }
            if (bnfId == 38) {
                //                    打开小小企鹅 38
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开小小企鹅");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.tencent.qqlivekid", "com.tencent.qqlivekid.activity.WelcomeActivity");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.tencent.qqlivekid")) {
//                    "pkgname": "com.tencent.qqlivekid",
//                            "clsname": "com.tencent.qqlivekid.activity.WelcomeActivity"
                    TTSManager.getInstance().startTTS("好的，马上打开小小企鹅", this);
                    currentApp = 38;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 39) {
                //                   打开宝宝巴士 39
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开宝宝巴士");
                }
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.sunchip.luqiyalauncher", "com.example.android_launcher_lqy.Ui.GridViewInsideXML"));
                    intent.putExtra("audio_path", "宝宝巴士");
                    intent.putExtra("audio_type", 1);
                    intent.putExtra(Constants.AUDIO_TAG, 404);
                    intent.putExtra("bj", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(1).getIcon());
                    intent.putExtra("isDefault", 1);
                    intent.putExtra("gridViewleft", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(0).getGridViewleft());
                    intent.putExtra("gridViewright", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(0).getGridViewright());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainApplication.context.startActivity(intent);
                } catch (Exception e) {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 40) {
                //          打开口袋故事 40
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开口袋故事");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.appshare.android.ilisten.tv", "com.appshare.android.ilisten.tv.SplashActivity");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.appshare.android.ilisten.tv")) {
//                    "pkgname": "com.appshare.android.ilisten.tv",
//                            "clsname": "com.appshare.android.ilisten.tv.SplashActivity"
                    TTSManager.getInstance().startTTS("好的，马上打开口袋故事", this);
                    currentApp = 40;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 41) {
                //      打开3D动物认知 41
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开3D动物认知");
                }
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.sunchip.luqiyalauncher", "com.example.android_launcher_lqy.Ui.GridViewInsideXML"));
                    intent.putExtra(Constants.AUDIO_PATH, "3D动物");
                    intent.putExtra(Constants.AUDIO_TYPE, Constants.TYPE_LOCAL_LIST);
                    intent.putExtra(Constants.AUDIO_TAG, 305);
                    intent.putExtra("bj", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(1).getIcon());
                    intent.putExtra("isDefault", 1);
                    intent.putExtra("gridViewleft", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(0).getGridViewleft());
                    intent.putExtra("gridViewright", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(0).getGridViewright());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainApplication.context.startActivity(intent);
                } catch (Exception e) {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 42) {
                //            打开智能机器人 42
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开智能机器人");
                }
                Base.getInstance().readTtsStr("小主人，我已经打开了哦");
            }

            if (bnfId == 43) {
                //         打开音量 43

                AudioManager mAudioManager = (AudioManager) MainApplication.context.getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
                        AudioManager.FLAG_SHOW_UI);
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开音量");
                }
            }

            if (bnfId == 44) {
                //          打开设置 44
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开设置");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.example.settingdemo", "com.example.settingdemo.MainActivity");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.example.settingdemo")) {
                    //        "com.example.settingdemo",						"com.example.settingdemo.MainActivity"
                    TTSManager.getInstance().startTTS("好的，马上打开设置", this);
                    currentApp = 44;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 45) {
                //        打开WIFI 45
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开WIFI");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.ikan.nscreen.box.settings", "com.ikan.nscreen.box.settings.WifiSettings");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.ikan.nscreen.box.settings")) {
                    //        com.ikan.nscreen.box.settings
                    // com.ikan.nscreen.box.settings.WifiSettings
                    TTSManager.getInstance().startTTS("好的，马上打开WIFI", this);
                    currentApp = 45;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 46) {
                //         打开小学课程 46
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开小学课程");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.wyt.zxp.xx", "com.wyt.gelingpad.lauchermain.MainActivity");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.wyt.zxp.xx")) {
//                    16.小学课程
//
//                    "com.wyt.zxp.xx",							"com.wyt.gelingpad.lauchermain.MainActivity"
                    TTSManager.getInstance().startTTS("好的，马上打开小学课程", this);
                    currentApp = 46;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

            if (bnfId == 47) {
                //        打开一键清理 47
                if (Blackboard.getInstance().allFragment != null) {
                    Blackboard.getInstance().allFragment.sendText("打开一键清理");
                }
                if (!isNetworkConnected(MainApplication.context)) {
                    openApp("com.sunchip.luqiyalauncher", "com.sunchip.luqiyalauncher.Ui.CleanMemoryActivity");
                    return;
                }
                if (checkApkExist(MainApplication.context, "com.sunchip.luqiyalauncher")) {

                    TTSManager.getInstance().startTTS("好的，马上打开一键清理", this);
                    currentApp = 47;
                } else {
                    Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了对应软件");
                }
            }

//            打开早教益智 48
//            打开学习园地 49
//            打开娱乐互动 50
//            打开在线乐园 51


        } else {

        }
    }

    private void openApp(String name, String mainclass) {
        Log.e("cao", "name==" + name + "mainclass==" + mainclass);

//        if (name.equals("com.tencent.mm")||name.equals("com.cx")||name.equals("com.tencent.deviceapp")){
//            MainApplication.wakeUpService.pauseRecord();
//        }
        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName(name, mainclass);
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainApplication.context.startActivity(intent);
            //进入第三方apk把语音apk本身结束
            System.exit(0);
        } catch (Exception e) {
        }
    }

    private void openAppExt2(String ext) {
//        快乐英语  wyt[@][1]6

        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.wyt.xq.flash", "com.wyt.xq.flash.yinYuActivity");
            intent.setComponent(cn);
            intent.putExtra("type", ext);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            MainApplication.context.startActivity(intent);
            System.exit(0);

        } catch (Exception e) {
            Log.e("cao", "e:" + e);
        }
    }

    private void openAppExt(String ext) {

//        1.多元智能  app_package wyt[@]0
//        亲子互动  wyt[@]13
//        视觉空间  wyt[@]7
//        习惯养成  wyt[@]10
//        自然科学  wyt[@]8
//        语言启蒙  wyt[@]6
//        快乐英语  wyt[@][1]6
//        数字逻辑  wyt[@]9
//        故事王国  wyt[@]3
//        传统国学  wyt[@]2

        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.wyt.xq.flash", "com.wyt.xq.flash.MainActivity");
            intent.setComponent(cn);
            intent.putExtra("type", ext);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            MainApplication.context.startActivity(intent);
            Log.e("cao", "ext:" + ext);
            System.exit(0);
        } catch (Exception e) {
            Log.e("cao", "e:" + e);
        }
    }

    /**
     * 判断应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    public void onSpeakBegin(String s) {

    }

    @Override
    public void onSpeakPaused() {

    }

    @Override
    public void onSpeakResumed() {

    }

    @Override
    public void onSpeakCompleted() {
        if (currentApp == 21) {
            openAppExt("wyt[@]0");
        }
        if (currentApp == 22) {
            openAppExt("wyt[@]13");
        }
        if (currentApp == 23) {
            openAppExt("wyt[@]7");
        }
        if (currentApp == 24) {
            openAppExt("wyt[@]10");
        }
        if (currentApp == 25) {
            openAppExt("wyt[@]8");
        }
        if (currentApp == 26) {
            openAppExt("wyt[@]6");
        }
        if (currentApp == 27) {
            openAppExt2("wyt[@][1]6");
        }
        if (currentApp == 28) {
            openAppExt("wyt[@]9");
        }
        if (currentApp == 29) {
            openAppExt("wyt[@]3");
        }
        if (currentApp == 30) {
            openAppExt("wyt[@]2");
        }
        if (currentApp == 31) {
//            .酷我K歌 "pkgname": "cn.kuwo.sing.tv",
//                    "clsname": "cn.kuwo.sing.tv.activity.EntryActivity"
            openApp("cn.kuwo.sing.tv", "cn.kuwo.sing.tv.activity.EntryActivity");
        }
        if (currentApp == 32) {
//            3.微信对讲 "pkgname": "com.tencent.mm",
//                    "clsname": "com.tencent.mm.ui.LauncherUI"
            openApp("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        }
        if (currentApp == 33) {
//            本地K歌
            //openApp("com.sunchip.luqiyalauncher","com.example.android_launcher_lqy.Ui.CommonGridViewFragment");

            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.sunchip.luqiyalauncher", "com.example.android_launcher_lqy.Ui.CommonGridViewFragment"));
            intent.putExtra("audio_type", 1);
            intent.putExtra("audio_path", "本地K歌");
            intent.putExtra("bj", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(1).getIcon());
            intent.putExtra("isDefault", isDefault);
            intent.putExtra("gridViewleft", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(0).getGridViewleft());
            intent.putExtra("gridViewright", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(0).getGridViewright());
            MainApplication.context.startActivity(intent);
        }
        if (currentApp == 34) {
//            "pkgname": "com.mobileapps.apps.LearnToDraw",
//                    "clsname": "air.com.zy.huihuamianfei.AppEntry"
            openApp("com.mobileapps.apps.LearnToDraw", "air.com.zy.huihuamianfei.AppEntry");
        }
        if (currentApp == 35) {
//            "pkgname": "com.edog.car",
//            "clsname": "com.kaolafm.auto.home.MainActivity"
            openApp("com.edog.car", "com.kaolafm.auto.home.MainActivity");
        }

        if (currentApp == 36) {
            //爱奇艺少儿
//            "pkgname": "com.qiyi.video.child",
//                    "clsname": "com.qiyi.video.child.WelcomeActivity"
            openApp("com.qiyi.video.child", "com.qiyi.video.child.WelcomeActivity");
        }

        if (currentApp == 37) {
            //爱奇艺追剧
//            pkgname": "com.qiyi.video.pad",
//            "clsname": "org.qiyi.android.video.MainActivity"
            openApp("com.qiyi.video.pad", "org.qiyi.android.video.MainActivity");
        }

        if (currentApp == 38) {
//            9.小小企鹅
//            "pkgname": "com.tencent.qqlivekid",
//                    "clsname": "com.tencent.qqlivekid.activity.WelcomeActivity"
            openApp("com.tencent.qqlivekid", "com.tencent.qqlivekid.activity.WelcomeActivity");
        }

        if (currentApp == 39) {
//            9.宝宝巴士
//            "pkgname": "com.tencent.qqlivekid",
//                    "clsname": "com.tencent.qqlivekid.activity.WelcomeActivity"
            openApp("com.tencent.qqlivekid", "com.tencent.qqlivekid.activity.WelcomeActivity");
        }

        if (currentApp == 40) {
            //智能机器人
//            "pkgname": "com.appshare.android.ilisten.tv",
//                    "clsname": "com.appshare.android.ilisten.tv.SplashActivity"
            openApp("com.appshare.android.ilisten.tv", "com.appshare.android.ilisten.tv.SplashActivity");
        }

        if (currentApp == 41) {
            // openApp("com.appshare.android.ilisten.tv","com.appshare.android.ilisten.tv.SplashActivity")
        }

        if (currentApp == 44) {
            //设置
            //  "com.example.settingdemo",						"com.example.settingdemo.MainActivity"
            openApp("com.example.settingdemo", "com.example.settingdemo.MainActivity");
        }

        if (currentApp == 45) {
            //打开wifi
            openApp("com.ikan.nscreen.box.settings", "com.ikan.nscreen.box.settings.WifiSettings");
        }


        if (currentApp == 46) {

//            16.小学课程
//
//            "com.wyt.zxp.xx",							"com.wyt.gelingpad.lauchermain.MainActivity"
            openApp("com.wyt.zxp.xx", "com.wyt.gelingpad.lauchermain.MainActivity");
        }

        if (currentApp == 47) {
            //17.一键清理
            //  "com.example.settingdemo",						"com.example.settingdemo.MainActivity"
            //Intent intent=new Intent
//            intent = new Intent("com.sunchip.luqiyalauncher", "com.sunchip.luqiyalauncher.Ui.CleanMemoryActivity");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            Log.e("cao", "currentApp==" + currentApp);
            openApp("com.sunchip.luqiyalauncher", "com.example.android_launcher_lqy.Ui.CleanMemoryActivity");
        }

    }

    @Override
    public void onSpeakFailed() {

    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
