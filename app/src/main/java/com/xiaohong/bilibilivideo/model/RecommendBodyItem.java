package com.xiaohong.bilibilivideo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XIAOHONG.
 * on 2016/10/10.
 * and BilibiliSuperApp
 */

public class RecommendBodyItem implements Parcelable {

    /*
    * 必须部分
     */
    // title可以为null
    @SerializedName("title")
    private String title;
    @SerializedName("style")
    private String style;
    @SerializedName("cover")
    private String cover;
    @SerializedName("param")
    private String param;
    @SerializedName("goto")
    private String _goto;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;

    /*
     * 可选部分
     */

    // 推荐部分
    @SerializedName("play")
    private String play;
    @SerializedName("danmaku")
    private String danmaku;

    // 直播部分
    @SerializedName("up_face")
    private String up_face;
    @SerializedName("up")
    private String up;
    @SerializedName("online")
    private long online;
    @SerializedName("area")
    private String area;
    @SerializedName("area_id")
    private long area_id;

    // 番剧部分
    @SerializedName("desc1")
    private String desc1; // 更新到第几集
    @SerializedName("status")
    private int status;


    // 区域
    // 和推荐部分相同

    protected RecommendBodyItem(Parcel in) {
        title = in.readString();
        style = in.readString();
        cover = in.readString();
        param = in.readString();
        _goto = in.readString();
        width = in.readInt();
        height = in.readInt();
        play = in.readString();
        danmaku = in.readString();
        up_face = in.readString();
        up = in.readString();
        online = in.readLong();
        area = in.readString();
        area_id = in.readLong();
        desc1 = in.readString();
        status = in.readInt();
    }

    public static final Creator<RecommendBodyItem> CREATOR = new Creator<RecommendBodyItem>() {
        @Override
        public RecommendBodyItem createFromParcel(Parcel in) {
            return new RecommendBodyItem(in);
        }

        @Override
        public RecommendBodyItem[] newArray(int size) {
            return new RecommendBodyItem[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(style);
        dest.writeString(cover);
        dest.writeString(param);
        dest.writeString(_goto);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(play);
        dest.writeString(danmaku);
        dest.writeString(up_face);
        dest.writeString(up);
        dest.writeLong(online);
        dest.writeString(area);
        dest.writeLong(area_id);
        dest.writeString(desc1);
        dest.writeInt(status);
    }

    public String getTitle() {
        return title;
    }

    public String getStyle() {
        return style;
    }

    public String getCover() {
        return cover;
    }

    public String getParam() {
        return param;
    }

    public String getGoto() {
        return _goto;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getPlay() {
        return play;
    }

    public String getDanMaKu() {
        return danmaku;
    }

    public String getUpFace() {
        return up_face;
    }

    public String getUp() {
        return up;
    }

    public long getOnline() {
        return online;
    }

    public String getArea() {
        return area;
    }

    public long getAreaId() {
        return area_id;
    }

    public String getDesc1() {
        return desc1;
    }

    public int getStatus() {
        return status;
    }


}
