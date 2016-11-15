package com.xiaohong.bilibilivideo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaohong.bilibilivideo.R;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends BaseFragment {

    private IjkMediaPlayer mPlayer;

    public LiveFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "直播";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_live, container, false);

        return ret;
    }


}
