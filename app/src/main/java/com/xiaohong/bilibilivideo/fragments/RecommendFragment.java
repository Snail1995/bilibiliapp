package com.xiaohong.bilibilivideo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaohong.bilibilivideo.R;
import com.xiaohong.bilibilivideo.activitys.LiveVideoActivity;
import com.xiaohong.bilibilivideo.activitys.VideoDetailActivity;
import com.xiaohong.bilibilivideo.activitys.WebViewActivity;
import com.xiaohong.bilibilivideo.adapter.RecommendListAdapter;
import com.xiaohong.bilibilivideo.client.ClientAPI;
import com.xiaohong.bilibilivideo.model.RecommendBodyItem;
import com.xiaohong.bilibilivideo.model.RecommendItem;
import com.xiaohong.bilibilivideo.presenters.RecommendPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class RecommendFragment extends BaseFragment implements IRecommendView, SwipeRefreshLayout.OnRefreshListener {

    // 可以将其初始化的内容放置onCreateView中
    private RecommendPresenter mRecommendPresenter;

    private List<RecommendItem> mItems;
    private RecommendListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLm;


    public RecommendFragment() {
    }

    @Override
    public String getFragmentTitle() {
        return "推荐";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItems = new ArrayList<>();
        mAdapter = new RecommendListAdapter(getContext(), mItems);


        // 注册EventBus事件 订阅者
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View ret = inflater.inflate(R.layout.fragment_recommend, container, false);
        RecyclerView recyclerView =
                ((RecyclerView) ret.findViewById(R.id.recommend_list));
        mSwipeRefreshLayout = ((SwipeRefreshLayout) ret.findViewById(R.id.recommend_refresh_layout));
        mSwipeRefreshLayout.setRefreshing(true);
        if (recyclerView != null) {
            // 布局管理器, 对Item进行排版

           /* if (mLm != null) {
                mLm.removeAllViews();
                mSwipeRefreshLayout.setRefreshing(false);
            }
            mLm = new LinearLayoutManager(
                    getContext(),
                    LinearLayoutManager.VERTICAL,
                    false);*/
            mLm = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL,
                    false);

            recyclerView.setLayoutManager(mLm);
            // TODO: 创建和设置 RecycleView的Adapter
            recyclerView.setAdapter(mAdapter);
        }


        //------------------------------
        // 下拉刷新
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        // 异步联网, 同时会收到EventBus发来的通知, 并且在onEvent方法中执行
        ClientAPI.getRecommendListAsync();
        return ret;
    }

    //--------------------------------------
    // MVP
    @Override
    public void setTypeText(String type) {
    }


    @Override
    public void onRefresh() {
        ClientAPI.getRecommendListAsync();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    ///////////////////////////////////////////////////////////////////////////
    // EventBus
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 没有使用的方法在进行混淆时删除掉
     * 用注解, 代表方法 , 可能会阻止删除
     * EventBus 当事件发送过来的时候, 调用
     * 根据threadMode 来设置这个方法在哪个线程中执行
     * 方法名字随便
     * 方法的修饰符必须是public, 否则EventBus在发布消息之后,再本包外不能访问这个方法
     *
     * @param items
     * @see ClientAPI#getRecommendListAsync()
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(List<RecommendItem> items) {
        if (items != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            // 收到联网获得的数据之后
            mItems.clear();
            mItems.addAll(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param item
     * @see RecommendListAdapter.RecommendViewViewHolder#onClick(View)
     */
    @Subscribe
    public void onRecommendBodyClickListenerEvent(RecommendBodyItem item) {
        // 根据goto来进行显示不同的界面内容
        String aGoto = item.getGoto();

        // av 视频详情  推荐区域
        // live 直播
        // weblink 网页  weblink activity
        // banguimi_list 番剧

        Intent intent = null;
        switch (aGoto) {
            case "av":
                intent = new Intent(getContext(), VideoDetailActivity.class);
                intent.putExtra("goto", item.getGoto());
                intent.putExtra("param", item.getParam());
                intent.putExtra(VideoDetailActivity.EXTRA_VIDEO_ITEM, item);
                break;
            case "live":
                intent = new Intent(getContext(), LiveVideoActivity.class);
                intent.putExtra("goto", item.getGoto());
                intent.putExtra("param", item.getParam());
                intent.putExtra(LiveVideoActivity.LIVE_VIDEO, item);
                break;
            case "weblink":
                intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("goto", item.getGoto());
                intent.putExtra("param", item.getParam());
                intent.putExtra(WebViewActivity.WEB_VIEW, item);
                break;
            case "banguimi_list":
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

}
