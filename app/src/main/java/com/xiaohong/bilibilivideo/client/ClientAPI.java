package com.xiaohong.bilibilivideo.client;

/**
 * Created by XIAOHONG.
 * on 2016/10/9.
 * and SuperApp
 */

import android.util.Log;

import com.google.gson.Gson;
import com.xiaohong.bilibilivideo.activitys.VideoDetailActivity;
import com.xiaohong.bilibilivideo.client.impl.OkHttpUtilmpl;
import com.xiaohong.bilibilivideo.model.PlayUrl;
import com.xiaohong.bilibilivideo.model.RecommendItem;
import com.xiaohong.bilibilivideo.model.VideoDetail;

import org.apache.commons.codec.digest.DigestUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * 所有的接口请求, 处理地址 参数
 */
public class ClientAPI {

    private static AbstractHttpUtil sHttpUtil;
    public static final String APP_KEY = "1d8b6e7d45233436";
    private static final String APP_SECRET = "FamGR6I1OwSutCZ2CelYQTJznz42c7sBbJcC0YxGXToV8j14uk1+3VvTFEbyBZeW";

    public static final String PLAYURL_APP_KEY = "4ebafd7c4951b366";
    public static final String PLAYURL_APP_SECRET = "8cb98205e9b2ad3669aad0fce12a4c13";

    static {
        // TODO: 创建特定的网络类库的支持
        sHttpUtil = new OkHttpUtilmpl();
    }

    private ClientAPI() {
    }

    /**
     * @param cid
     * @param quality
     * @param videoType
     * @see VideoDetailActivity#startVideoPlay()
     * 异步加载视频播放的详情信息
     * http://interface.bilibili.com/playurl?appkey=4ebafd7c4951b366&build=420000&channel=bili&cid=10648434&mobi_app=android&otype=json&
     * platform=android&quality=1&screen=xxdhpi&ts=1476242378860&type=mp4&sign=9a873d0b2557cd268490175cf7fb4ed0
     */
    public static void getPlayUrlAsync(String cid, int quality, String videoType) {
        String url = "http://interface.bilibili.com/playurl";
        TreeMap<String, String> params = new TreeMap<>();
        params.put("cid", cid);
        params.put("quality", Integer.toString(quality));
        params.put("otype", "json");
        params.put("type", videoType);
        url = appendParamsWithSign(url, params, PLAYURL_APP_KEY, PLAYURL_APP_SECRET);

        Log.d("VDL", "getPlayUrlAsync: " + url);
        sHttpUtil.doGetDataAsync(url, new HttpCallback() {
            @Override
            public void onSuccess(String url, int code, byte[] data) {
                if (code == 200) {
                    try {
                        String str = new String(data, "UTF-8");
                        JSONObject json = new JSONObject(str);
                        Gson gson = new Gson();
                        PlayUrl playUrl = gson.fromJson(str, PlayUrl.class);
                        /**
                         * @see VideoDetailActivity#onVideoPlayEvent(PlayUrl)                         */
                        EventBus.getDefault().post(playUrl);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            }

            @Override
            public void onFail() {

            }
        });

    }

    private static String appendParamsWithSign(String url,
                                               TreeMap<String, String> params,
                                               String appKey,
                                               String appSecret
    ) {
        String ret = url;
        if (url != null && params != null && appKey != null && appSecret != null) {
            params.put("appkey", appKey);
            params.put("build", "420000");
            params.put("channel", "bili");
            params.put("mobi_app", "android");
            params.put("platform", "android");
            // TODO: 需要机型适配
            params.put("screen", "xxdhpi");
            params.put("ts", Long.toString(System.currentTimeMillis()));

            String sign = sign(params, appSecret);
            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            Set<String> keySet = params.keySet();
            try {
                for (String key : keySet) {
                    sb.append(key).append("=")
                            .append(URLEncoder.encode(params.get(key), "UTF-8"))
                            .append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
            }
            if (sb.length() > 0) {
                sb.append("sign=").append(sign);
            }
            ret = sb.toString();
        }
        return ret;
    }

    /**
     * 获取首页推荐列表
     *
     * @return JSONObject
     */
    public static JSONObject getRecommendList() {
        String url = "http://app.bilibili.com/x/show/old?appkey=1d8b6e7d45233436";
        return sHttpUtil.doGet(url);
    }

    /**
     *  异步获取数据, 首页推荐的数据
     */
    public static void getRecommendListAsync() {
        String url = "http://app.bilibili.com/x/show/old?appkey=1d8b6e7d45233436";
        sHttpUtil.doGetDataAsync(url, new HttpCallback() {
            @Override
            public void onSuccess(String url, int code, byte[] data) {
                if (code == 200 && data != null) {
                    try {
                        String str = new String(data, "UTF-8");
                        JSONObject json = new JSONObject(str);
                        // 解析数据
                        code = json.getInt("code");
                        List<RecommendItem> items = new ArrayList<>();
                        if (code == 0) {
                            JSONArray arrayResult = json.getJSONArray("result");
                            int length = arrayResult.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject obj = arrayResult.getJSONObject(i);
                                RecommendItem item = RecommendItem.createFromJson(obj);
                                items.add(item);
                            }
                            // TODO: 把数据 发送给上层 UI 现在是子线程
                            /**
                             *  @see com.xiaohong.bilibilisuperapp.fragments.RecommendFragment#onEvent
                             */
                            Log.d("TAG", "onSuccess: " + json.toString());
                            System.out.println(json.toString());
                            EventBus.getDefault().post(items);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            }

            @Override
            public void onFail() {
                Log.d("TAG", "onFail: 数据请求错误");
            }
        });
    }

    /**
     * @param aid
     * @see VideoDetailActivity#onResume()
     * https://app.bilibili.com/x/view?aid=6623628&appkey=1d8b6e7d45233436&build=420000&channel=bili&mobi_app=android&
     * platform=android&screen=xxdhpi&ts=1476113271000&sign=92279e9e96a99970d7e9b70a4a4e8e02
     */
    public static void getVideoDetail(String aid) {
        String url = "https://app.bilibili.com/x/view";
        //TODO:进行参数数字签名,访问网络,获取数据
        TreeMap<String, String> params = new TreeMap<>();
        params.put("aid", aid);
        Log.d("VD", "getVideoDetail: " + url);
        url = appendParamsWithSign(url, params, PLAYURL_APP_KEY, PLAYURL_APP_SECRET);
        sHttpUtil.doGetDataAsync(url, new HttpCallback() {
            @Override
            public void onSuccess(String url, int code, byte[] data) {
                if (code == 200) {
                    try {
                        Log.d("VD", "url = : " + url);
                        String str = new String(data, "UTF-8");
                        JSONObject json = new JSONObject(str);
                        // json.toString(4); 带缩进打印
                        Log.d("VD", "result = : " + json.toString());
                        code = json.getInt("code");
                        if (code == 0) {
                            JSONObject jsonData = json.getJSONObject("data");
                            Gson gson = new Gson();
                            VideoDetail videoDetail = gson.fromJson(jsonData.toString(), VideoDetail.class);
                            /**
                             * @see VideoDetailActivity#onReceiveDetailEvent(VideoDetail)
                             */
                            EventBus.getDefault().post(videoDetail);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    /**
     * 拼一个完整的 GET 请求地址
     *
     * @return
     */
    private static String appendParamsWithSign(String url, TreeMap<String, String> params) {
        String ret = null;
        ret = appendParamsWithSign(url, params, APP_KEY, APP_SECRET);
        return ret;
    }

    /**
     * 计算sign数值,进行数字签名
     *
     * @param params
     * @param appSecret
     * @return
     */
    private static String sign(TreeMap<String, String> params, String appSecret) {
        String ret = null;
        if (params != null && appSecret != null) {
            StringBuilder sb = new StringBuilder();
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                try {
                    // key=value&key=value
                    sb.append(key)
                            .append("=")
                            .append(URLEncoder.encode(params.get(key), "UTF-8"))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } finally {
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(appSecret);
            String s = sb.toString();
            ret = DigestUtils.md5Hex(s);
        }
        return ret;
    }
}
