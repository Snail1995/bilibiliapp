package com.xiaohong.bilibilivideo;

import android.app.Application;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;

/**
 * Created by XIAOHONG.
 * on 2016/10/10.
 * and BilibiliSuperApp
 */

/**
 * 全局初始化
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 1. 初始化EventBus对象实例

        // 默认的
//        EventBus aDefault = EventBus.getDefault();

        // 自定义EventBus
        EventBusBuilder builder = EventBus.builder();
        builder.installDefaultEventBus();
    }

}
