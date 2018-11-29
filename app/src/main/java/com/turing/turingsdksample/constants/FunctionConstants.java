package com.turing.turingsdksample.constants;

import java.util.HashMap;

/**
 * @author：licheng@uzoo.com
 */

public class FunctionConstants {
    public static HashMap<Integer, String> codeMap;

    static {
        codeMap = new HashMap();
        codeMap.put(100000, "互动聊天");
        codeMap.put(200205, "知识问答-十万个为什么");
        codeMap.put(201401, "知识问答-天气预报");
        codeMap.put(200702, "知识问答-时间查询");
        codeMap.put(201501, "知识问答-计算");
        codeMap.put(200401, "诗词");
        codeMap.put(200203, "英语学习-中英互译");
        codeMap.put(200301, "声音演示-动物叫声");
        codeMap.put(200303, "声音演示-乐器的声音");
        codeMap.put(200302, "声音演示-大自然的声音");
        codeMap.put(200201, "内容互动-故事点播、随机播放");
        codeMap.put(200101, "内容互动-儿歌点播、随机点播");
        codeMap.put(201002, "播放切换、停止");
        codeMap.put(900301, "播放过程、非播放过程--音量增加、音量减少");
        codeMap.put(900102, "播放过程、非播放--屏幕亮度减小、屏幕亮度增加");
        codeMap.put(900101, "设备操作-休眠设置");
        codeMap.put(900201, "设备操作-电量查询");
        codeMap.put(300101, "运动控制");
        codeMap.put(200501, "拍照");
        codeMap.put(200701, "跳舞");
        codeMap.put(200710, "闹钟");
    }
    /**
     * 音乐的功能
     **/
    public final static int MUSIC_FUNCTION = 200101;
    /**
     * 聊天的功能
     * **/
    public final static int CHAT_FUNCTION = 100000;
}
