package com.xiaohong.bilibilivideo.client;

/**
 * Created by XIAOHONG.
 * on 2016/10/10.
 * and BilibiliSuperApp
 */

/**
 * 网络请求的回调接口
 * 接口的设计原则:
 * 1. 通常一个接口要尽可能少包含方法;
 */
public interface HttpCallback {
    /**
     * 网络访问成功,返回数据
     */
    void onSuccess(String url, int code, byte[] data);

    void onFail();
}
