package com.xiaohong.bilibilivideo.model;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIAOHONG.
 * on 2016/10/10.
 * and BilibiliSuperApp
 */

public class RecommendItem implements IRecommendItemModel {
    private String type;
    private String headParam;
    private String headGoto;
    private String headStyle;
    private String headTitle;

    // 可选的
    private long headCount;

    private List<RecommendBodyItem> body;

    public static RecommendItem createFromJson(JSONObject json) throws JSONException {
        RecommendItem ret = null;

        if (json != null) {
            ret = new RecommendItem();
            // 异常往外抛
            ret.type = json.optString("type");

            JSONObject head = json.getJSONObject("head");
            ret.headParam = head.optString("param");
            ret.headGoto = head.optString("goto");
            ret.headStyle = head.optString("style");
            ret.headTitle = head.optString("title");

            ret.headCount = head.optLong("count");

            JSONArray body = json.getJSONArray("body");
            int size = body.length();
            if (size > 0) {
                ret.body = new ArrayList<>();
                Gson gson = new Gson();
                RecommendBodyItem bodyItem = null;
                for (int i = 0; i < size; i++) {
                    JSONObject bi = body.getJSONObject(i);
                    bodyItem = gson.fromJson(bi.toString(), RecommendBodyItem.class);
                    // TODO: 解析Body内部的内容
                    ret.body.add(bodyItem);
                }
            }
        }
        return ret;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeadParam() {
        return headParam;
    }

    public String getHeadGoto() {
        return headGoto;
    }

    public String getHeadStyle() {
        return headStyle;
    }

    public String getHeadTitle() {
        return headTitle;
    }

    public long getHeadCount() {
        return headCount;
    }

    public List<RecommendBodyItem> getBody() {
        return body;
    }
}
