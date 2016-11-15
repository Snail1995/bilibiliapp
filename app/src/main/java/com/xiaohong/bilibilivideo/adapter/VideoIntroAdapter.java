package com.xiaohong.bilibilivideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaohong.bilibilivideo.R;
import com.xiaohong.bilibilivideo.model.VideoDetail;

import java.util.List;

/**
 * Created by XIAOHONG.
 * on 2016/10/14.
 * and BilibiliSuperApp
 */

public class VideoIntroAdapter extends BaseAdapter {
    public static List<VideoDetail.Relates> mRelates;
    public static Context mContext;
    private LayoutInflater mInflater;

    public VideoIntroAdapter(List<VideoDetail.Relates> relates, Context context) {
        mRelates = relates;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mRelates.size();
    }

    @Override
    public Object getItem(int position) {
        return mRelates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        if (convertView == null) {
            ret = mInflater.inflate(R.layout.item_video_detail_intro, parent, false);
        } else {
            ret = convertView;
        }
        Object tag = ret.getTag();
        if (tag != null) {
            ((ViewHolder) tag).bindView(position);
        }
        ret.setTag(new ViewHolder(ret));

        return ret;
    }

    private static class ViewHolder {

        private final ImageView mImage;
        private final TextView mTitle;
        private final TextView mName;
        private final TextView mPlay;
        private final TextView mDanmaku;

        public ViewHolder(View itemView) {
            mImage = ((ImageView) itemView.findViewById(R.id.video_introduction_item_image));
            mTitle = ((TextView) itemView.findViewById(R.id.video_introduction_item_title));
            mName = ((TextView) itemView.findViewById(R.id.video_introduction_item_name));
            mPlay = ((TextView) itemView.findViewById(R.id.video_introduction_item_sum));
            mDanmaku = ((TextView) itemView.findViewById(R.id.video_introduction_item_danmaku));
        }


        public void bindView(int position) {
            VideoDetail.Relates relates = mRelates.get(position);
            Picasso.with(mContext)
                    .load(relates.getPic())
                    .resize(500, 300)
                    .into(mImage);
            mTitle.setText(relates.getTitle());
            mName.setText(relates.getOwneMy().getName());
            mPlay.setText(Long.toString(relates.getStat().getView()));
            mDanmaku.setText(Long.toString(relates.getStat().getDanmeku()));
        }
    }
}
