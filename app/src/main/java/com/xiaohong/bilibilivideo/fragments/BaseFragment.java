package com.xiaohong.bilibilivideo.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by XIAOHONG.
 * on 2016/10/9.
 * and SuperApp
 */

/**
 *  首页的Fragment 父类
 *  定义通用的属性和行为
 *
 */
public abstract class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    public  abstract String getFragmentTitle();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
