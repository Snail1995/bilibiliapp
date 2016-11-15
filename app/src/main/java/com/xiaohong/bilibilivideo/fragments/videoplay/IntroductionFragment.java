package com.xiaohong.bilibilivideo.fragments.videoplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaohong.bilibilivideo.view.MyListView;
import com.xiaohong.bilibilivideo.R;
import com.xiaohong.bilibilivideo.adapter.VideoIntroAdapter;
import com.xiaohong.bilibilivideo.fragments.BaseFragment;
import com.xiaohong.bilibilivideo.iamges.CircularImageView;
import com.xiaohong.bilibilivideo.model.VideoDetail;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroductionFragment extends BaseFragment {

    public static final String VIDEO_DETAIL = "video_detail";
    private TextView mDescTxt;
    private TextView mTitleTxt;
    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mUserTime;
    private MyListView mIntroList;
    private List<VideoDetail.Relates> mRelatesList;
    private VideoIntroAdapter mAdapter;
    private TextView mShareTxt;
    private TextView mCoinTxt;
    private TextView mFavoriteTxt;
    private TextView mDownTxt;



    public IntroductionFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public String getFragmentTitle() {
        return "简介";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_video_introduction, container, false);
        mDescTxt = (TextView) ret.findViewById(R.id.video_introduction_desc);
        mTitleTxt = (TextView) ret.findViewById(R.id.video_introduction_title);
        mShareTxt = (TextView) ret.findViewById(R.id.video_introduction_share);
        mCoinTxt = (TextView) ret.findViewById(R.id.video_introduction_coin);
        mFavoriteTxt = (TextView) ret.findViewById(R.id.video_introduction_favorite);
        mDownTxt = (TextView) ret.findViewById(R.id.video_introduction_down);
        mUserImage = (ImageView) ret.findViewById(R.id.video_introduction_user_image);
        mUserName = (TextView) ret.findViewById(R.id.video_introduction_user_name);
        mUserTime = (TextView) ret.findViewById(R.id.video_introduction_user_time);
        mIntroList = (MyListView) ret.findViewById(R.id.video_introduction_list);
        mRelatesList = new ArrayList<>();

        mAdapter = new VideoIntroAdapter(mRelatesList, getContext());

        mIntroList.setAdapter(mAdapter);
        return ret;
    }

    private void bindView(VideoDetail video) {
        mTitleTxt.setText(video.getTitle());
        mDescTxt.setText(video.getDesc());
        mCoinTxt.setText(Long.toString(video.getStateMy().getCoin()));
        mShareTxt.setText(Long.toString(video.getStateMy().getShare()));
        mFavoriteTxt.setText(Long.toString(video.getStateMy().getFavorite()));
        Picasso.with(getContext())
                .load(video.getOwner().getFace())
                .placeholder(R.mipmap.ic_erro)
                .into(mUserImage);
        mUserName.setText(video.getOwner().getName());
        mUserTime.setText(Long.toString(video.getOwner().getMid()));
       /* List<String> tags = video_color_sel_txt.getTags();
        if (tags != null) {
            for (String tag : tags) {

                TextView textView = new TextView(getContext());
                textView.setText(tag);
                textView.setBackgroundColor(Color.WHITE);

            }
        }*/

        mRelatesList.clear();
        mRelatesList.addAll(video.getRelates());
//
//        ViewGroup.LayoutParams params = mIntroList.getLayoutParams();
//        params.height = params.height + (mIntroList.getDividerHeight() * ( mAdapter.getCount() - 1));
//        mIntroList.setLayoutParams(params);

        for (VideoDetail.Relates relates : mRelatesList) {
            System.out.println("AFG" + relates.getPic());
        }
        mAdapter.notifyDataSetChanged();
        mIntroList.setNestedScrollingEnabled(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    ///////////////////////////////////////////////////////////////////////////
    // EventBus
    ///////////////////////////////////////////////////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoDetailEvent2(VideoDetail videoDetail) {
        // 一定要注意: 一定要在请求完数据之后再进行为view绑定数值
        bindView(videoDetail);
    }
}
