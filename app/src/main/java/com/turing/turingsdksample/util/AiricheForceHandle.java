package com.turing.turingsdksample.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.turing.semantic.entity.Behavior;
import com.turing.tts.TTSListener;
import com.turing.tts.TTSManager;
import com.turing.turingsdksample.ability.Base;
import com.turing.turingsdksample.activity.MainApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liwan on 2018/6/29.
 */

public class AiricheForceHandle {
    private static AiricheForceHandle aicicheForceHandle;
    String keyWord = "动画片";
    String name;

    public static AiricheForceHandle getInstance() {
        if (aicicheForceHandle == null) {
            aicicheForceHandle = new AiricheForceHandle();
        }
        return aicicheForceHandle;
    }

    private AiricheForceHandle() {

    }

    /**
     * 从语义入口强转语义
     *
     * @param str
     * @return
     */
    public boolean isSematicDispose(String str) {
        if (isPhontcontrol(str)) {
            return true;
        }
        return false;
    }

    /**
     * 从文本入手强转语义
     */
    public boolean istextDispose(String str) {
        if (isVideocontrol(str)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是电话操作
     *
     * @param str
     * @return
     */
    private boolean isPhontcontrol(String str) {

        Behavior.IntentInfo osIntentBean = OSDataTransformUtil.getIntent(str);
        Log.d("airiche", "IntentInfo==" + osIntentBean.toString());
        Behavior behavior = OSDataTransformUtil.getBehavior(str);

        //com.tencent.deviceapp
        if (osIntentBean.getCode() == 200802) {
            Base.getInstance().isTTs = false;
            /**
             * 不让执行播报语音的应答，改用自己的提示语
             */
            try {
                JSONObject object = new JSONObject(osIntentBean.getParameters().toString());
                name = (String) object.opt("people_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (checkApkExist(MainApplication.context, "com.tencent.deviceapp")) {


                MainApplication.uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainApplication.isBackStopRecord=true;
                        String action = "com.qi.tocallmobile";
                        Intent intent = new Intent(action);
                        intent.putExtra("other_name", name);
                        MainApplication.context.sendBroadcast(intent);
                        Log.d("airiche", "call + name =" + name);
//                       Intent intent1 ;
//                       PackageManager packageManager = MainApplication.context.getPackageManager();
//                       intent1 =packageManager.getLaunchIntentForPackage("com.tencent.deviceapp");
//                       intent1.addCategory(Intent.CATEGORY_LAUNCHER);
//                       intent1.setAction(Intent.ACTION_MAIN);
//                       MainApplication.context.startActivity(intent1);
                    }
                });
            } else {
                Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了视频电话软件");
            }
            return true;

        }


        return false;
    }

    private boolean isVideocontrol(String str) {
//        通过 Action 启动  Intent intent = new Intent(“myvst.intent.action.SearchActivity”); startActivity(intent);
//        Ps:参数设置,外部可以直接设置搜索参数,为客户拥有语⾳搜索⽽准备(版本⾼于 2.5.4 ⽣效)
//        Key = ”search_word“ Value =”中⽂字符“ For example(搜索影⽚ ⼼花路放): intent.putExtras(“search_word”,”⼼花路放“);
        if (!TextUtils.isEmpty(str)) {
            if (str.matches("^[想看|打开动画片|我想看|看动画片|动画片|播放动画片|播放动画|播放动漫|看动画片|看动画|看动漫].*")) {
                if (str.startsWith("想看")) {
                    keyWord = str.replace("想看", "");
                } else if (str.startsWith("我想看")) {
                    keyWord = str.replace("我想看", "");
                } else if (str.startsWith("看动画片")) {
                    keyWord = str.replace("看动画片", "");
                } else if (str.startsWith("动画片")) {
                    keyWord = str.replace("动画片", "");
                } else if (str.startsWith("播放动画片")) {
                    keyWord = str.replace("播放动画片", "");
                } else if (str.startsWith("播放动画")) {
                    keyWord = str.replace("播放动画", "");
                } else if (str.startsWith("播放动漫")) {
                    keyWord = str.replace("播放动漫", "");
                } else if (str.startsWith("看动画片")) {
                    keyWord = str.replace("看动画片", "");
                } else if (str.startsWith("看动画")) {
                    keyWord = str.replace("看动画", "");
                } else if (str.startsWith("看动漫")) {
                    keyWord = str.replace("看动漫", "");
                } else if (str.startsWith("打开动画片") || str.startsWith("打开视频") || str.startsWith("打开电视") || str.startsWith("打开动漫")) {
                    keyWord = "动画片";
                } else {
                    return false;
                }
                if (TextUtils.isEmpty(keyWord)) keyWord = "动画片";
                TTSManager.getInstance().startTTS("好的！马上为你播放" + keyWord, new TTSListener() {
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
                        try {
                            Intent intent = new Intent("myvst.intent.action.SearchActivity");
//                ComponentName componentName =new ComponentName("net.myvst.v2","myvst.intent.action.SearchActivity");
//                intent.setComponent(componentName);
                            // intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("search_word", keyWord);
                            MainApplication.context.startActivity(intent);
                            System.exit(0);
                        } catch (Exception e) {
                            Base.getInstance().readTtsStr("执行操作失败，请检查是否安装了视频播放软件");
                        }
                    }

                    @Override
                    public void onSpeakFailed() {

                    }
                });
                return true;
            }
        }
        return false;
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


}
