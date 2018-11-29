package com.turing.turingsdksample.ability;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class Msg {
    public static final int TYPE_RECEIVED = 0;//表示这是一条收到的消息
    public static final int TYPE_SENT = 1;//表示这是一条发出的消息
    private String content;
    private int type;

    /**
     * content表示消息的内容，type表示消息的类型。其中消息类型有以上两个值可选。
     * @param content
     * @param type
     */
    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public Msg(String content) {
        this.content = content;
    }


    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }
    public String getContent() {
        return content;
    }
}