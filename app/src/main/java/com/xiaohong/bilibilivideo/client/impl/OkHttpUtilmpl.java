package com.xiaohong.bilibilivideo.client.impl;

import com.xiaohong.bilibilivideo.client.AbstractHttpUtil;
import com.xiaohong.bilibilivideo.client.HttpCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XIAOHONG.
 * on 2016/10/10.
 * and BilibiliSuperApp
 */

public class OkHttpUtilmpl extends AbstractHttpUtil {


    private final OkHttpClient mOkHttpClient;

    public OkHttpUtilmpl() {
        mOkHttpClient = new OkHttpClient();
    }

    @Override
    public byte[] doGetData(String url) {

        byte[] ret = new byte[0];
        if (url != null) {
            Request.Builder builder = new Request.Builder();
            builder.url(url)
                    .get()
                    .addHeader(
                            "User-Agent",
                            "BiLiBiLi WP Client/4.20.0 (orz@loli.my)");

            Request request = builder.build();
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                // 缺点: Response 获取操作, 会直接读取网络数据并且关闭
                int code = response.code();
                if (code == 200) {
                    ret = response.body().bytes();
                    // 在读取完数据到末尾之后,不能再进行读取
                    response.close();
                    response = null;
                    request = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public void doGetDataAsync(final String url, final HttpCallback callback) {
        if (url != null) {
            Request.Builder builder = new Request.Builder();
            builder.url(url).get()
                    .addHeader("User-Agent",
                            "BiLiBiLi WP Client/4.20.0 (orz@loli.my)");
            Request request = builder.build();
            mOkHttpClient.newCall(request).enqueue(
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            int code = response.code();
                            byte[] data = response.body().bytes();
                            HttpUrl httpUrl = call.request().url();
                            if (callback != null) {
                                callback.onSuccess(httpUrl.toString(), code, data);
                            }
                        }
                    }
            );
        }
    }
}
