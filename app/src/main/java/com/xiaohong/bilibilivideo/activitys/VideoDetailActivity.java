package com.xiaohong.bilibilivideo.activitys;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xiaohong.bilibilivideo.R;
import com.xiaohong.bilibilivideo.adapter.RecommendVideoDetailAdapter;
import com.xiaohong.bilibilivideo.client.ClientAPI;
import com.xiaohong.bilibilivideo.fragments.BaseFragment;
import com.xiaohong.bilibilivideo.fragments.videoplay.CommentFragment;
import com.xiaohong.bilibilivideo.fragments.videoplay.IntroductionFragment;
import com.xiaohong.bilibilivideo.model.PlayUrl;
import com.xiaohong.bilibilivideo.model.RecommendBodyItem;
import com.xiaohong.bilibilivideo.model.VideoDetail;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 从推荐  分区 点击进图视频的详情部分中, 视频的基本对象
 * 通常可以使用RecommendBodyItem 即可, 所以就需要将item通过intent传过来
 * 进入本Activity之后,会在onResume方法里面去请求数据
 */
public class VideoDetailActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnBufferingUpdateListener, View.OnTouchListener {

    public static final String EXTRA_VIDEO_ITEM = "video_item";
    public static final String EXTRA_ON_LIVE = "on_live";

    // 控件

    private TabLayout mTabLayout;
    private ViewPager mPager;
    private ImageView mTopImage;
    private FloatingActionButton mFab;
    // 视频播放的大控件
    private LinearLayout mMediaController;

    // 数据

    // 视频播放器的窗口,不是一个控件
    private SurfaceView mSurfaceView;
    private RecommendBodyItem mRecommendBodyItem;
    // 视屏详情的数据
    private VideoDetail mVideoDetail;
    // 简介和评论两个Fragment
    private List<BaseFragment> mFragments;
    // 视频播放器
    private IjkMediaPlayer mIjkMediaPlayer;
    private ActionBar mActionBar;
    private ImageView mImageBtnSwitch;
    private GestureDetectorCompat mCompat;
    private RecommendVideoDetailAdapter mAdapter;


    // 如果发生屏幕切换,进行播放
    private boolean hasSwitchOrientation;
    // 视频是否在播放
    private boolean isPlaying;
    // 通过Toolbar的菜单,才能控制菜单中的group是否显示
    private Menu mMenu;

    // 控制播放控制条的布局显示和隐藏
    private Handler mHandler = new Handler();

    // 控制Toolbar的变化
    private CollapsingToolbarLayoutState state;
    private CollapsingToolbarLayout mCollLayout;
    private ButtonBarLayout mPlayBtn;
    private TextView mToolTitle;
    private Toolbar mToolbar;
    private String mTitle;


    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        // 获取跳转的信息, 一定要注意成员变量初始化的顺序,否则很容易出现空指针异常
        Intent intent = getIntent();
        mRecommendBodyItem = intent.getParcelableExtra(EXTRA_VIDEO_ITEM);

        init();
        // EventBus的注册
        EventBus.getDefault().register(this);

        Toast.makeText(this, Integer.toString(getRequestedOrientation()), Toast.LENGTH_SHORT).show();
        System.out.println(Integer.toString(getRequestedOrientation()));
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            hasSwitchOrientation = true;
        }

        // 竖屏状态下
        if (!hasSwitchOrientation) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mToolbar = (Toolbar) findViewById(R.id.video_detail_tool_bar);
            if (mToolbar != null) {
                mToolbar.setTitle("");
                setSupportActionBar(mToolbar);
            }
            mActionBar = getSupportActionBar();
            if (mActionBar != null) {
                mActionBar.setDisplayHomeAsUpEnabled(true);
            }

            AppBarLayout app_bar = (AppBarLayout) findViewById(R.id.video_detail_app_bar_layout);
            mToolTitle = (TextView) findViewById(R.id.video_detail_tool_bar_txt);
            mCollLayout = (CollapsingToolbarLayout) findViewById(R.id.video_detail_coll_tool_bar_layout);
            mPlayBtn = (ButtonBarLayout) findViewById(R.id.playButton);
            mPlayBtn.setOnClickListener(this);
            mTitle = intent.getStringExtra("goto") + intent.getStringExtra("param");
            mToolTitle.setText(mTitle);
            // 对状态栏折叠的功能  展开、折叠、中间
            state = CollapsingToolbarLayoutState.EXPANDED;
            app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (verticalOffset == 0) {
                        if (state != CollapsingToolbarLayoutState.EXPANDED) {
//                            mCollLayout.setTitle("EXPANDED");//设置title为EXPANDED
                            mToolTitle.setText(mTitle);
                            state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        }
                    } else if (Math.abs(verticalOffset) + 15 >= appBarLayout.getTotalScrollRange()) {
                        if (state != CollapsingToolbarLayoutState.COLLAPSED) {
//                            mCollLayout.setTitle("");//设置title不显示
                            mToolTitle.setText("");
                            mPlayBtn.setVisibility(View.VISIBLE);//显示播放按钮
                            state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        }
                    } else {
                        if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                            if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                                mPlayBtn.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                            }
                            mToolTitle.setText(mTitle);
//                            mCollLayout.setTitle("INTERNEDIATE");//设置title为INTERNEDIATE
                            state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                        }
                    }
                }
            });
           /* // 播放界面,如果横屏转换为竖屏,进行播放
            if ((getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)) {
                // 如果mFab隐藏,就自动播放
                if (mFab.getVisibility() == View.INVISIBLE) {
                    startVideoPlay();
                }
            }*/

        }
       /* // 横屏状态下
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mTopImage.setVisibility(View.INVISIBLE);  // 初始的图片不能再显示
            mActionBar.setDisplayHomeAsUpEnabled(false); // 隐藏返回按钮
            mToolTitle.setVisibility(View.INVISIBLE);  // 隐藏标题
        }*/

     /*   // 无论横屏还是竖屏,只要isPlaying=true;就播放
        if (savedInstanceState != null) {
             startVideoPlay();
        }*/



    }

   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("play", isPlaying);
    }*/

    private void init() {
        // TODO: 控件的初始化
        mTabLayout = (TabLayout) findViewById(R.id.video_detail_tab_layout);
        mPager = (ViewPager) findViewById(R.id.video_detail_view_pager);
        mTopImage = (ImageView) findViewById(R.id.video_detail_cover);
        mFab = (FloatingActionButton) findViewById(R.id.video_detail_play_fab);
        mSurfaceView = (SurfaceView) findViewById(R.id.video_detail_play_video);
        mImageBtnSwitch = (ImageView) findViewById(R.id.video_detail_controller_orientation_switch);
        mMediaController = (LinearLayout) findViewById(R.id.video_detail_controller);



        // TODO: 控件的监听事件和触摸事件 以及 数据绑定
        // 顶部Toolbar出的ImageView
        if (mTopImage != null) {
            Picasso.with(this)
                    .load(mRecommendBodyItem.getCover())
                    .config(Bitmap.Config.RGB_565) // 如果使用RGB-565 对于png图片来说将不支持透明
                    .error(R.mipmap.ic_erro)
                    .resize(mRecommendBodyItem.getWidth(),
                            mRecommendBodyItem.getHeight())
                    .into(mTopImage);
        }

        // 悬浮的点击播放的fbc
        if (mFab != null) {
            mFab.setOnClickListener(this);
        }


        // SurfaceView 准备视频的播放
        if (mSurfaceView != null) {
            mSurfaceView.getHolder().addCallback(this);
            // 兼容包下的手势识别器, 实现点击 拖拽 的功能
            mCompat = new GestureDetectorCompat(
                    this, new VideoGestureDetector());

            // 手势可以事件里面调用进行检查, 因为他不是一个控件,没有onClickListener方法
            mSurfaceView.setOnTouchListener(this);
        }


        mIjkMediaPlayer = new IjkMediaPlayer();
        // 当播放器加载网络的视频资源时,会在内部进行网络访问
        mIjkMediaPlayer.setOnPreparedListener(this);
        //
        mIjkMediaPlayer.setOnBufferingUpdateListener(this);
        // 进行横竖屏的切换的监听事件
        mImageBtnSwitch.setOnClickListener(this);



        // ViewPager的Fragment, 横屏的时候没有ViewPager
        if (mPager != null) {
            mFragments = new ArrayList<>();
            FragmentManager manager = getSupportFragmentManager();
           /*  IntroductionFragment introductionFragment = new IntroductionFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putParcelable(IntroductionFragment.VIDEO_DETAIL,mVideoDetail);
            introductionFragment.setArguments(bundle);
            transaction.add(R.id.video_detail_view_pager,introductionFragment);
            transaction.commit();*/
            mFragments.add(new IntroductionFragment());
            mFragments.add(new CommentFragment());
            // ViewPage的的适配器
            mAdapter = new RecommendVideoDetailAdapter(manager, mFragments);
            mPager.setAdapter(mAdapter);
            // Tab和 ViewPager联动
            mTabLayout.setupWithViewPager(mPager);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    // Activity的启动模式 SingleInstance, 再次执行时,如果栈里面有activity,会直接调用此方法
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mVideoDetail = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ClientAPI.getVideoDetail(mRecommendBodyItem.getParam());
        Log.d("TAG", "onResume: =" + mRecommendBodyItem.getParam());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_tool_bar_menu, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // 打开 DrawerLayout
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                || getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            super.onBackPressed();

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * mFab.setOnClickListener(this); 的监听方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.video_detail_play_fab:
                // TODO: 做视频播放
                startVideoPlay();
                v.setVisibility(View.INVISIBLE);
                // 视频播放
                isPlaying = true;
                break;
            case R.id.video_detail_controller_orientation_switch:
                // 获取当前屏幕的方向
                int requestedOrientation = getRequestedOrientation();
                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                    // 垂直方向，那么切换成 水平方向
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.playButton:

                startVideoPlay();
                break;
            default:
                break;
        }
    }

    private void startVideoPlay() {
        if (mVideoDetail != null) {
            List<VideoDetail.Page> pages = mVideoDetail.getPages();
            // TODO: 需要支持多个Pages的视频切换功能
            if (pages != null && pages.size() > 0) {
                VideoDetail.Page page = pages.get(0);
                long cid = page.getCid();
                // 是通过EventBus进行事件的
                ClientAPI.getPlayUrlAsync(Long.toString(cid), 1, "mp4");
            }
        }
    }

    /**
     * 当SurfaceView准备好,并且能够显示的时候回调
     * 使用SurfaceHolder 可以进行绘制
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 初始化 播放器或者是照相机, 和setDataResource()一样的作用.
        mIjkMediaPlayer.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    /**
     * surfaceDestroyed 和 onDestroy
     * 在竖屏时,会先执行,onDestroy,然后再surfaceDestroyed
     * 仅限于在本次调试中,视频的横竖屏切换
     * 横屏时 会先执行surfaceDestroyed
     *
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 释放资源, 播放器停止 释放
        if (mIjkMediaPlayer.isPlaying()) {
            mIjkMediaPlayer.stop();
        }
        mIjkMediaPlayer.release();
        Log.d("TAG", "surfaceDestroyed: ");
//        mIjkMediaPlayer = null;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        Log.d("TAG", "onDestroy: ");
    }

    /**
     * 当视频准备好得时候,方法回调
     *
     * @param iMediaPlayer
     */
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {

        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.fragment_video_nested_view);

        // 使用iMediaPlayer进行播放,而不是 mIjkMediaPlayer
        // 如果有多个流媒体,就不是一个mIjkMediaPlayer的播放了,会有很多成员变量的播放
        iMediaPlayer.start();
        // 视频播放时,不响应netView滚动事件
        nestedScrollView.setNestedScrollingEnabled(false);
    }

    /**
     * 当缓存进度发生变化的时候,进行回调
     *
     * @param iMediaPlayer
     * @param progress     进度
     */
    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int progress) {
        Log.d("VDA", "buffering update: " + progress);
        System.out.println("VDA" + "VideoDetailActivity.onBufferingUpdate" + "      " + progress);
    }


    /**
     * SurfaceView 事件处理器
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mCompat.onTouchEvent(event);
    }

    private class VideoGestureDetector extends GestureDetector.SimpleOnGestureListener {
        public static final String token = "token";

        /**
         * 按下事件, 返回true, 才会处理手势
         *
         * @param e
         * @return
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /**
         * 当快速单击 抬起的时候,
         * 对视频单击的操作 点击显示或隐藏
         *
         * @param e
         * @return
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mHandler.removeCallbacksAndMessages(token);
            int vis = mMediaController.getVisibility();
            if (vis == View.INVISIBLE) {
                mMediaController.setVisibility(View.VISIBLE);
                long time = SystemClock.uptimeMillis() + 5000;
                mHandler.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        mMediaController.setVisibility(View.INVISIBLE);
                        mActionBar.setDisplayHomeAsUpEnabled(false);
                        mToolTitle.setVisibility(View.INVISIBLE);
                        // 显示菜单栏
                        mMenu.setGroupVisible(R.id.video_menu, false);
                    }
                }, token, time);
                // 音乐是否播放
                if (isPlaying) {
                    // 显示返回按钮
                    mActionBar.setDisplayHomeAsUpEnabled(true);
                    mToolTitle.setVisibility(View.VISIBLE);
                    // 显示菜单栏
                    mMenu.setGroupVisible(R.id.video_menu, true);
                }
            } else {
                mActionBar.setDisplayHomeAsUpEnabled(false); // 隐藏返回按钮
                mToolTitle.setVisibility(View.INVISIBLE);
                mMediaController.setVisibility(View.INVISIBLE);
                mMenu.setGroupVisible(R.id.video_menu, false);
            }
            return super.onSingleTapUp(e);
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // EventBus
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 接收到详情数据,需要更新UI,需要在主线程中
     *
     * @param detail
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveDetailEvent(VideoDetail detail) {
       /* if (detail != null) {
            List<VideoDetail.Page> pages = detail.getPages();
            if (pages != null && !pages.isEmpty()) {
                long cid = pages.get(0).getCid();
                Toast.makeText(this, Long.toString(cid), Toast.LENGTH_SHORT).show();
            }
        }*/
        // 详情请求回来之后变化mFab的图片
        mFab.setImageResource(R.drawable.ic_video_play);
        // 视频详情
        mVideoDetail = detail;
        // 判断是否为横屏,横屏情况下,要求自动播放视频
        if (hasSwitchOrientation) {
            startVideoPlay();
            Toast.makeText(this, "播放", Toast.LENGTH_SHORT).show();
        }
//        Toast.makeText(this, mVideoDetail.getTitle(), Toast.LENGTH_SHORT).show();

        /**
         * @see  会到简介的Fragment中
         */
        // 进行事件的回调,让Fragment进行数据的展示
    }

    /**
     * SurfaceView播放一定是在子线程中执行的.
     *
     * @param playUrl
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoPlayEvent(PlayUrl playUrl) {
        mSurfaceView.setVisibility(View.VISIBLE); // 更该界面,要在主线程中
        // 横屏状态下

        // TODO: 视频播放
        List<PlayUrl.Durl> durls = playUrl.getDurl();
        if (durls != null) {
            if (!durls.isEmpty()) {
                PlayUrl.Durl durl = durls.get(0);
                String url = durl.getUrl();
                if (url != null) {
                    try {
                        // TODO:应该接着竖屏的地方继续放映
//                        mIjkMediaPlayer.reset();
                        // 设置视频或者音频的地址,需要网络 或者io流的读取
                        mIjkMediaPlayer.setDataSource(url);
                        // 没有同步,异步子线程方式开始准备视频,准备好就回调相应接口
                        mIjkMediaPlayer.prepareAsync();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
