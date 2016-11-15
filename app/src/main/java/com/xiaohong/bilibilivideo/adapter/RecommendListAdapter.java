package com.xiaohong.bilibilivideo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaohong.bilibilivideo.R;
import com.xiaohong.bilibilivideo.model.RecommendBodyItem;
import com.xiaohong.bilibilivideo.model.RecommendItem;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by XIAOHONG.
 * on 2016/10/10.
 * and BilibiliSuperApp
 */

/**
 * 推荐界面列表适配器
 */
public class RecommendListAdapter extends RecyclerView.Adapter {

    public static final int TYPE_RECOMMEND = 1;
    public static final int TYPE_LIVE = 2;
    public static final int TYPE_BANGUMI = 3;
    public static final int TYPE_REGION = 4;
    public static final int TYPE_WEB_LINK = 5;
    public static final int TYPE_HUDONG = 6;
    public static final int GL_PIC = 7;


    private Context mContext;
    private List<RecommendItem> mItems;
    private static  int sCount;
    private static  int sPosition;

    public RecommendListAdapter(Context context, List<RecommendItem> items) {
        mContext = context;
        this.mItems = items;
    }

    /*
     * 1.RecycleView Adapter  以ViewHolder 为视图复用的标准
     * 2.RecycleView 不提供onItemClickListener 接口
     * 3.Adapter 同BaseAdapter , 都提供多布局的支持
     */

    @Override
    public int getItemViewType(int position) {
        int ret = 0;
        RecommendItem item = mItems.get(position);
        String type = item.getType();
        switch (type) {
            case "recommend":
                ret = 1;
                break;
            case "live":
                ret = 2;
                break;
            case "bangumi_2":
                ret = 3;
                break;
            case "region":
                ret = 4;
                break;
            case "weblink":
                ret = 5;
                break;
            case "activity":
                ret = 6;
                break;
            case "gl_pic":
                ret = 7;
                break;
            default:
                ret = 0;
                break;
        }
        return ret;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder ret = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = null;

        // 通过type,加载不同的布局
        switch (viewType) {
            case TYPE_RECOMMEND:
                // TODO: 推荐布局
                itemView = inflater.inflate(R.layout.item_recommend_recommend, parent, false);
                ret = new RecommendViewViewHolder(itemView);
                break;
            case TYPE_LIVE:
                // TODO: 直播布局
                itemView = inflater.inflate(R.layout.item_recommend_live, parent, false);
                ret = new LiveViewViewHolder(itemView);
                break;
            case TYPE_REGION:
                // TODO: 动画 音乐 舞蹈 游戏 鬼畜 科技 生活 时尚 娱乐 电视剧 电影
                itemView = inflater.inflate(R.layout.item_recommend_region, parent, false);
                ret = new RegionViewViewHolder(itemView);
                break;
            case TYPE_WEB_LINK:
                // TODO: 话题区, 仅对应于一个网页打开
                itemView = inflater.inflate(R.layout.item_recommend_webview, parent, false);
                ret = new WebLinkViewViewHolder(itemView);
                break;
            case TYPE_BANGUMI:
                // TODO:番剧布局
            case TYPE_HUDONG:
                // TODO: 活动中心
            default:
                // TODO: 还有个为type=""的情况,就是一webView
                itemView = inflater.inflate(R.layout.item_recommend_simple, parent, false);
                ret = new SimpleViewHolder(itemView);
                break;
        }
        return ret;
    }

    /**
     * 把数据绑定到 ViewHolder 指向的View中
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecommendItem recommendItem = mItems.get(position);
        if (holder instanceof SimpleViewHolder) {
            SimpleViewHolder simpleViewHolder = (SimpleViewHolder) holder;
            simpleViewHolder.bindView(recommendItem);
        }
        sPosition = position;
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (mItems != null) {
            ret = mItems.size();
        }
        sCount = ret;
        return ret;
    }

    public static int  getCount() {
       return sCount;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    private static class SimpleViewHolder extends RecyclerView.ViewHolder {
        /**
         * 通用的View缓存功能
         */
        private SparseArrayCompat<View> mViews;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArrayCompat<>();
        }

        View getChildView(int rid) {
            View ret = null;
            ret = mViews.get(rid);
            if (ret == null) {
                ret = itemView.findViewById(rid);
                if (ret != null) {
                    mViews.put(rid, ret);
                }
            }
            return ret;
        }

        /**
         * 动态获取View的方法,
         * 利用反射, 获取视图
         *
         * @param name
         * @return
         */
        View getChildView(String name) {
            View ret = null;
            int id = 0;
            if (name != null) {
                // 使用反射动态获取多个控件的id
                Class aClass = R.id.class;
                try {
                    Field field = aClass.getDeclaredField(name);
                    field.setAccessible(true);
                    // 获取类成员的数值
                    id = field.getInt(aClass);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (id != 0) {
                ret = getChildView(id);
            }
            return ret;
        }

        /**
         * 用于绑定数据到视图
         *
         * @param recommendItem
         */
        void bindView(RecommendItem recommendItem) {
            TextView txt = (TextView) getChildView(R.id.item_recommend_txt);
            txt.setText(recommendItem.getType());
        }


    }

    private static class RecommendViewViewHolder extends SimpleViewHolder implements View.OnClickListener {

        public RecommendViewViewHolder(View itemView) {
            super(itemView);
            for (int i = 0; i < 4; i++) {
                View view = getChildView("item_commend_card_view_" + i);

                if (view != null) {
                    // 每一个Body内部视频点击事件
                    view.setOnClickListener(this);
                }
            }

        }

        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag != null) {
                if (tag instanceof RecommendBodyItem) {
                    RecommendBodyItem bodyItem = (RecommendBodyItem) tag;
                    // EventBus 传递对象给 Fragment , 有Fragment来跳转到详情
                    /**
                     * @see com.xiaohong.bilibilisuperapp.fragments.RecommendFragment#onRecommendBodyClickListenerEvent(RecommendBodyItem)
                     */
                    EventBus.getDefault().post(bodyItem);
                }
            }
        }

        @Override
        void bindView(RecommendItem recommendItem) {
            TextView txtTitle = ((TextView) getChildView(R.id.item_recommend_head_title));
            if (txtTitle != null) {
                txtTitle.setText(recommendItem.getHeadTitle());
            }

            //---------------------------
            // 条目 , 默认四个
            List<RecommendBodyItem> body = recommendItem.getBody();
            if (body.size() >= 4) {
                // 使用反射动态获取多个控件ID
                for (int i = 0; i < body.size(); i++) {
                    View view = getChildView("item_commend_card_view_" + i);
                    ImageView imageView = (ImageView) getChildView("item_commend_body_icon_" + i);
                    RecommendBodyItem bodyItem = body.get(i);
                    view.setTag(bodyItem);
                    String cover = bodyItem.getCover();
                    if (cover != null) {
                        // TODO: 显示图片
                        Context context = imageView.getContext();
                        Picasso.with(context)
                                .load(cover)
                                .config(Bitmap.Config.RGB_565)
                                .placeholder(R.drawable.ic_image_previous)
                                .noFade() // 滑动的效果
                                .resize(520, 350)
                                .into(imageView);
                    }


                    TextView txtBodyTitle = (TextView) getChildView("item_commend_body_title_" + i);
                    if (txtBodyTitle != null) {
                        String title = bodyItem.getTitle();
                        if (title != null) {
                            txtBodyTitle.setText(title);
                        }
                    }

                    TextView txtBodyCount = (TextView) getChildView("item_commend_body_count_" + i);
                    if (txtBodyTitle != null) {
                        String play = bodyItem.getPlay();
                        if (play != null) {
                            txtBodyCount.setText(play);
                        }
                    }

                    TextView txtBodyDanMuKu = (TextView) getChildView("item_commend_body_danmaku_" + i);
                    if (txtBodyTitle != null) {
                        String danMaKu = bodyItem.getDanMaKu();
                        if (danMaKu != null) {
                            txtBodyDanMuKu.setText(danMaKu);
                        }
                    }
                }
            } else {
                // TODO: 代码动态添加布局

            }
        }

    }

    private static class LiveViewViewHolder extends SimpleViewHolder implements View.OnClickListener {

        public LiveViewViewHolder(View itemView) {
            super(itemView);
            for (int i = 0; i < 4; i++) {
                View view = getChildView("item_commend_card_view_live_" + i);
                if (view != null) {
                    view.setOnClickListener(this);
                }
            }
            TextView txtMore = (TextView) getChildView("item_recommend_live_bottom_search_more");
            ImageView refresh = (ImageView) getChildView("item_recommend_live_bottom_refresh");
            if (txtMore != null) {
                txtMore.setOnClickListener(this);
            }
            if (refresh != null) {
                refresh.setOnClickListener(this);
            }
        }
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag != null) {
                if (tag instanceof RecommendBodyItem) {
                    RecommendBodyItem bodyItem = (RecommendBodyItem) tag;
                    // EventBus 传递对象给 Fragment , 有Fragment来跳转到详情
                    /**
                     * @see com.xiaohong.bilibilisuperapp.fragments.RecommendFragment#onRecommendBodyClickListenerEvent(RecommendBodyItem)
                     */
                    EventBus.getDefault().post(bodyItem);
                } else if (tag instanceof TextView) {
                    String str = "more";
//                    EventBus.getDefault().post(str);
                } else if (tag instanceof ImageView) {
                    String str = "refresh";
//                    EventBus.getDefault().post(str);
                }
            }
        }

        @Override
        void bindView(RecommendItem recommendItem) {
            TextView onLiveTitle = (TextView) getChildView(R.id.item_recommend_live_on_live);
            onLiveTitle.setText(recommendItem.getHeadTitle());
            TextView moreLiveCount = (TextView) getChildView(R.id.item_recommend_live_more_live);
            moreLiveCount.setText(Long.toString(recommendItem.getHeadCount()));

            // 每个卡片的设置
            int size = recommendItem.getBody().size();
            if (size >= 4) {
                for (int i = 0; i < size; i++) {
                    View view = getChildView("item_commend_card_view_live_" + i);
                    RecommendBodyItem bodyItem = recommendItem.getBody().get(i);
                    String cover = bodyItem.getCover();
                    ImageView imageView = (ImageView) getChildView("item_commend_body_live_icon_" + i);
                    view.setTag(bodyItem);
                    Context context = imageView.getContext();
                    Picasso.with(context)
                            .load(cover)
                            .config(Bitmap.Config.RGB_565)
                            .resize(520, 350)
                            .placeholder(R.drawable.ic_image_previous)
                            .error(R.drawable.img_tips_error_banner_tv)
                            .into(imageView);

                    TextView txtBodyLiveTitle = (TextView) getChildView("item_commend_body_live_title_" + i);
                    if (txtBodyLiveTitle != null) {
                        String title = bodyItem.getTitle();
                        String area = bodyItem.getArea();
                        if (title != null) {
                            txtBodyLiveTitle.setText("#" + area + "# " + title);
                        }
                    }

                    TextView txtBodyLiveUp = (TextView) getChildView("item_commend_body_live_up_" + i);
                    if (txtBodyLiveUp != null) {
                        String up = bodyItem.getUp();
                        if (up != null) {
                            txtBodyLiveUp.setText(up);
                        }
                    }

                    TextView txtBodyLiveOnline = (TextView) getChildView("item_commend_body_live_online_" + i);
                    if (txtBodyLiveOnline != null) {
                        long online = bodyItem.getOnline();
                        txtBodyLiveOnline.setText(Long.toString(online));
                    }
                }
            }

            // 查看更多
            TextView txtMore = (TextView) getChildView("item_recommend_live_bottom_search_more");
            // TODO: 添加点击事件
            txtMore.setTag("Livemore");

            // 点击刷新
            ImageView refresh = (ImageView) getChildView("item_recommend_live_bottom_refresh");
            // TODO: 添加点击事件,进行刷新
            refresh.setTag("Liverefresh");

        }

    }

    private static class Bangumi2ViewViewHolder extends SimpleViewHolder {

        public Bangumi2ViewViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class RegionViewViewHolder extends SimpleViewHolder implements View.OnClickListener {

        public RegionViewViewHolder(View itemView) {
            super(itemView);
            for (int i = 0; i < 4; i++) {
                View view = getChildView("item_commend_card_view_region_" + i);
                if (view != null) {
                    view.setOnClickListener(this);
                }
            }
        }
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag instanceof RecommendBodyItem) {
                RecommendBodyItem bodyItem = (RecommendBodyItem) tag;
                /**
                 * @see com.xiaohong.bilibilisuperapp.fragments.RecommendFragment#onRecommendBodyClickListenerEvent(RecommendBodyItem)
                 */
                EventBus.getDefault().post(bodyItem);
            }

        }

        @Override
        void bindView(RecommendItem recommendItem) {

            TextView onRegionRegion = (TextView) getChildView(R.id.item_recommend_region_region);
            onRegionRegion.setText(recommendItem.getHeadTitle());

            // 每个卡片的设置
            int size = recommendItem.getBody().size();
            if (size >= 4) {
                for (int i = 0; i < size; i++) {
                    View view = getChildView("item_commend_card_view_region_" + i);
                    RecommendBodyItem bodyItem = recommendItem.getBody().get(i);
                    String cover = bodyItem.getCover();
                    ImageView imageView = (ImageView) getChildView("item_commend_body_region_icon_" + i);
                    view.setTag(bodyItem);
                    Context context = imageView.getContext();
                    Picasso.with(context)
                            .load(cover)
                            .config(Bitmap.Config.RGB_565)
                            .resize(520, 350)
                            .placeholder(R.drawable.ic_image_previous)
                            .error(R.drawable.img_tips_error_banner_tv)
                            .into(imageView);

                    TextView txtBodyLiveTitle = (TextView) getChildView("item_commend_body_region_title_" + i);
                    if (txtBodyLiveTitle != null) {
                        String title = bodyItem.getTitle();
                        if (title != null) {
                            txtBodyLiveTitle.setText( title);
                        }
                    }

                    TextView txtBodyRegionPlay = (TextView) getChildView("item_commend_body_region_play_" + i);
                    if (txtBodyRegionPlay != null) {
                        String play = bodyItem.getPlay();
                        txtBodyRegionPlay.setText(play);
                    }

                    TextView txtBodyRegionDanmeku = (TextView) getChildView("item_commend_body_region_danmaku_" + i);
                    if (txtBodyRegionDanmeku != null) {
                        String danMaKu = bodyItem.getDanMaKu();
                        txtBodyRegionDanmeku.setText(danMaKu);
                    }
                }
            }

            // 查看更多
            TextView txtMore = (TextView) getChildView("item_recommend_region_bottom_search_more");
            // TODO: 添加点击事件
            txtMore.setTag("Regionmore");

            // 点击刷新
            ImageView refresh = (ImageView) getChildView("item_recommend_region_bottom_refresh");
            // TODO: 添加点击事件,进行刷新
            refresh.setTag("Regionrefresh");

        }


    }

    private static class WebLinkViewViewHolder extends SimpleViewHolder implements View.OnClickListener {

        public WebLinkViewViewHolder(View itemView) {
            super(itemView);
            ImageView webViewIma = (ImageView) getChildView("item_commend_body_webview_img");
            if (webViewIma != null) {
                webViewIma.setOnClickListener(this);
            }
        }
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag instanceof RecommendBodyItem) {
                RecommendBodyItem bodyItem = (RecommendBodyItem) tag;
                /**
                 * @see com.xiaohong.bilibilisuperapp.fragments.RecommendFragment#onRecommendBodyClickListenerEvent(RecommendBodyItem)
                 */
                EventBus.getDefault().post(bodyItem);
            }
        }

        @Override
        void bindView(RecommendItem recommendItem) {
//            TextView search = (TextView) getChildView("item_commend_body_webview_search");
            ImageView webViewImage = (ImageView) getChildView("item_commend_body_webview_img");
            RecommendBodyItem item = recommendItem.getBody().get(0);
            Context context = webViewImage.getContext();
            webViewImage.setTag(item);
            if (webViewImage != null) {
                String cover = item.getCover();
                Picasso.with(context)
                        .load(cover)
                        .config(Bitmap.Config.RGB_565)
                        .resize(
                                item.getWidth(),
                                item.getHeight()
                        )
                        .placeholder(R.drawable.patriotism_dialog_webview_error)
                        .into(webViewImage);
            }
           /* if (sPosition >= sCount - 1 ) {
                Log.d("TAGGGGG", "bindView: " + Integer.toString(sPosition) + "   " + Integer.toString(sCount));
                System.out.println("TAGGGGG22" +  "bindView: " + Integer.toString(sPosition) + "   " + Integer.toString(sCount));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(webViewImage.getLayoutParams());
                params.setMargins(0, 0, 0, 30);
                webViewImage.setLayoutParams(params);
            }
            Log.d("TAGGGGG", "bindView: " + Integer.toString(sPosition) + "   " + Integer.toString(sCount));
            System.out.println("TAGGGGG" +  "bindView: " + Integer.toString(sPosition) + "   " + Integer.toString(sCount));*/

        }

        public static void setMargins (View v, int l, int t, int r, int b) {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                p.setMargins(l, t, r, b);
                v.requestLayout();
            }
        }
    }

    private static class HuodongViewViewHolder extends SimpleViewHolder {
        public HuodongViewViewHolder(View itemView) {
            super(itemView);
        }
    }


}
