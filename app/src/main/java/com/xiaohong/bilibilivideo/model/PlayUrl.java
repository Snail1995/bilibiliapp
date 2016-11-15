package com.xiaohong.bilibilivideo.model;

/**
 * Created by XIAOHONG.
 * on 2016/10/12.
 * and BilibiliSuperApp
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 视频播放时能够获取到的视频网址
 *
 * @see com.xiaohong.bilibilivideo.client.ClientAPI#getPlayUrlAsync(String, int, String)
 */
public class PlayUrl {
    @SerializedName("from")
    private String from;
    @SerializedName("timelength")
    private long timeLength;
    @SerializedName("format")
    private String format; // 视频的格式 mp4 / hdmp4
    @SerializedName("durl")
    private List<Durl> durl;


    public static class Durl {
        @SerializedName("order")
        private int order;
        @SerializedName("length")
        private long length; // 和timeLength大小一样
        @SerializedName("size")
        private long size; // 视频文件长度
        @SerializedName("url")
        private String url; // 视频地址, 直接播放
        @SerializedName("backup_url")
        private List<String> backUrls; // 备用的url

        public int getOrder() {
            return order;
        }

        public long getLength() {
            return length;
        }

        public long getSize() {
            return size;
        }

        public String getUrl() {
            return url;
        }

        public List<String> getBackUrls() {
            return backUrls;
        }
    }

    public String getFrom() {
        return from;
    }

    public long getTimeLength() {
        return timeLength;
    }

    public String getFormat() {
        return format;
    }

    public List<Durl> getDurl() {
        return durl;
    }
}
