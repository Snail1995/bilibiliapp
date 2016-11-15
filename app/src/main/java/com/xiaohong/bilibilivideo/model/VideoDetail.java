package com.xiaohong.bilibilivideo.model;

/**
 * Created by XIAOHONG.
 * on 2016/10/12.
 * and BilibiliSuperApp
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 视频详情数据
 */
public class VideoDetail implements Parcelable {
    @SerializedName("aid")
    private long aid;
    @SerializedName("title")
    private String title;
    @SerializedName("desc")
    private String desc;
    @SerializedName("owner")
    private OwnerMy owner;
    @SerializedName("pages")
    private List<Page> mPages;
    @SerializedName("stat")
    private StateMy mStateMy;
    @SerializedName("relates")
    private List<Relates> mRelates;
    @SerializedName("tags")
    private List<String> mTags;

    protected VideoDetail(Parcel in) {
        aid = in.readLong();
        title = in.readString();
        desc = in.readString();
        mTags = in.createStringArrayList();
    }

    public static final Creator<VideoDetail> CREATOR = new Creator<VideoDetail>() {
        @Override
        public VideoDetail createFromParcel(Parcel in) {
            return new VideoDetail(in);
        }

        @Override
        public VideoDetail[] newArray(int size) {
            return new VideoDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(aid);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeStringList(mTags);
    }


    public static class OwnerMy {
        @SerializedName("face")
        private String face;
        @SerializedName("name")
        private String name;
        @SerializedName("mid")
        private long mid;

        public long getMid() {
            return mid;
        }

        public String getFace() {
            return face;
        }

        public String getName() {
            return name;
        }
    }

    public static class Relates {
        @SerializedName("pic")
        private String pic;
        @SerializedName("title")
        private String title;
        @SerializedName("owner")
        private OwnerMy owneMy;
        @SerializedName("stat")
        private StateMy stat;

        public String getPic() {
            return pic;
        }

        public String getTitle() {
            return title;
        }

        public OwnerMy getOwneMy() {
            return owneMy;
        }

        public StateMy getStat() {
            return stat;
        }
    }
    public static class StateMy {
        @SerializedName("favorite")
        private long favorite;
        @SerializedName("coin")
        private long coin;
        @SerializedName("share")
        private long share;
        @SerializedName("danmaku")
        private long danmeku;
        @SerializedName("view")
        private long view;
        @SerializedName("reply")
        private long reply;

        public long getReply() {
            return reply;
        }

        public long getFavorite() {
            return favorite;
        }

        public long getCoin() {
            return coin;
        }

        public long getShare() {
            return share;
        }

        public long getDanmeku() {
            return danmeku;
        }

        public long getView() {
            return view;
        }
    }
    public static class Page {
        @SerializedName("cid")
        private long cid;
        @SerializedName("part")
        private String part;
        @SerializedName("vid")
        private String vid;

        public long getCid() {
            return cid;
        }

        public String getPart() {
            return part;
        }

        public String getVid() {
            return vid;
        }
    }

    public long getAid() {
        return aid;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public List<Page> getPages() {
        return mPages;
    }

    public OwnerMy getOwner() {
        return owner;
    }

    public List<Relates> getRelates() {
        return mRelates;
    }

    public List<String> getTags() {
        return mTags;
    }

    public StateMy getStateMy() {
        return mStateMy;
    }
}
