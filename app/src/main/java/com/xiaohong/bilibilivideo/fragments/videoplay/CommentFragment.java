package com.xiaohong.bilibilivideo.fragments.videoplay;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaohong.bilibilivideo.R;
import com.xiaohong.bilibilivideo.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends BaseFragment {


    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "评论";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_comment, container, false);
    }

}
