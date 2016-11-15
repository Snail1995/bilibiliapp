package com.xiaohong.bilibilivideo.model;

/**
 * Created by XIAOHONG.
 * on 2016/10/10.
 * and BilibiliSuperApp
 */

/**
 *  针对MVP模式
 *  把数据模型进行接口抽象,包含数据的各种操作
 */
public interface IRecommendItemModel {
    String getType();
    void setType(String type);
}
