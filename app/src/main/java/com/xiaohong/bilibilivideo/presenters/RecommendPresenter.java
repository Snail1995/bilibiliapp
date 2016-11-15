package com.xiaohong.bilibilivideo.presenters;


import com.xiaohong.bilibilivideo.fragments.IRecommendView;
import com.xiaohong.bilibilivideo.model.IRecommendItemModel;

/**
 * Created by XIAOHONG.
 * on 2016/10/10.
 * and BilibiliSuperApp
 */

public class RecommendPresenter {
    private IRecommendView mRecommendView;
    private IRecommendItemModel mModel;

    // 一般讲View 通过构造方法传过来
    public RecommendPresenter(IRecommendView recommendView) {
        mRecommendView = recommendView;
    }

    // 设置方法随意,也可以在构造方法中
    public void setModel(IRecommendItemModel model) {
        mModel = model;
    }

    public void updateTypeText() {
        if (mModel != null) {
            mRecommendView.setTypeText(mModel.getType());
        }
    }
}
