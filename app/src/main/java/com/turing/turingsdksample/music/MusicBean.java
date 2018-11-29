package com.turing.turingsdksample.music;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * @author：licheng@uzoo.com
 */

public class MusicBean implements Parcelable {
    private String urlPath;//音乐的路径
    private int style = MusicResStyle.RESSTYLE__URL_PATH;//音乐的数据来源的类型，有资源文件R.raw中的，还有网络或者本地中的
    private int raw;//音乐在资源文件raw中的名字
    private int error = MusicStatus.MUSIC_RES_STATUS_RIGHT;//表示当前资源是否有问题，如果有问题就不会播放了，默认的是正确的

    public MusicBean() {
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }


    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.urlPath);
        dest.writeInt(this.style);
        dest.writeInt(this.raw);
        dest.writeInt(this.error);
    }

    protected MusicBean(Parcel in) {
        this.urlPath = in.readString();
        this.style = in.readInt();
        this.raw = in.readInt();
        this.error = in.readInt();
    }

    public static final Creator<MusicBean> CREATOR = new Creator<MusicBean>() {
        @Override
        public MusicBean createFromParcel(Parcel source) {
            return new MusicBean(source);
        }

        @Override
        public MusicBean[] newArray(int size) {
            return new MusicBean[size];
        }
    };
}
