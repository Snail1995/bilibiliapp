package com.xiaohong.bilibilivideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaohong.bilibilivideo.activitys.MenuActivity;
import com.xiaohong.bilibilivideo.adapter.CommonFragmentAdapter;
import com.xiaohong.bilibilivideo.fragments.BaseFragment;
import com.xiaohong.bilibilivideo.fragments.CateGoryFragment;
import com.xiaohong.bilibilivideo.fragments.DiscoveryFragment;
import com.xiaohong.bilibilivideo.fragments.FollowFragment;
import com.xiaohong.bilibilivideo.fragments.LiveFragment;
import com.xiaohong.bilibilivideo.fragments.MovieFragment;
import com.xiaohong.bilibilivideo.fragments.RecommendFragment;
import com.xiaohong.bilibilivideo.iamges.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ViewPager mPager;
    private TabLayout mTabLayout;

    // 做退出判定时使用
    private long mTime = 0;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        init();

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            // 设置状态栏透明, 但之后好像不占位置了,需要toolbar进行margin
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 设置Toolbar透明好像没有用
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
       /* Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(
                   View.SYSTEM_UI_FLAG_FULLSCREEN|
                           View.SYSTEM_UI_FLAG_LAYOUT_STABLE
           );
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }*/

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
//            actionBar.setIcon(R.mipmap.ic_launcher);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_notice_bar);
//            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        mNavigationView.setNavigationItemSelectedListener(this);
        // 去掉侧滑菜单的滚动条, 因为滚动条是在侧滑菜单的NavigationMenuView里面
        if (mNavigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) mNavigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }

      /*  // 实现三条杠的动画效果
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                        R.string.open_string, R.string.close_string);
        actionBarDrawerToggle.syncState();
        // 设置策划监听
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);*/

        if (mPager != null) {
            List<BaseFragment> mFragments = new ArrayList<>();
            mFragments.add(new LiveFragment());
            mFragments.add(new RecommendFragment());
            mFragments.add(new MovieFragment());
            mFragments.add(new CateGoryFragment());
            mFragments.add(new FollowFragment());
            mFragments.add(new DiscoveryFragment());

            CommonFragmentAdapter adapter = new CommonFragmentAdapter(getSupportFragmentManager(),
                    mFragments);

            mPager.setAdapter(adapter);
            mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
            mTabLayout.setupWithViewPager(mPager);
            // 默认选中推荐
            mPager.setCurrentItem(1);
        }

        ImageView navagationBar1 = (ImageView) findViewById(R.id.navigation_tool_bar_1);
        ImageView navagationBar2 = (ImageView) findViewById(R.id.navigation_tool_bar_2);
        TextView navagationBar3 = (TextView) findViewById(R.id.navigation_tool_bar_3);
        navagationBar1.setOnClickListener(this);
        navagationBar2.setOnClickListener(this);
        navagationBar3.setOnClickListener(this);

//        int num = 1 / 0;
       /* TelephonyManager manager =
                (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // IMEI 唯一标识
        String deviceId = manager.getDeviceId();
        // 可以在线程意外终止的情况下,进行处理,接口回调
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                //TODO: 异常情况下的处理
            }
        });*/
    }


    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        // 解决侧滑菜单的
        mNavigationView = ((NavigationView) findViewById(R.id.main_header_navigationView));
        mPager = (ViewPager) findViewById(R.id.main_pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // 打开 DrawerLayout
//                mDrawerLayout.openDrawer(GravityCompat.START, true);
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigation_tool_bar_1:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.navigation_tool_bar_2:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.navigation_tool_bar_3:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            default:
                break;
        }
    }

    /**
     * setNavigationItemSelectedListener()
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_homepage:
                // 设置菜单
                // TODO: 需要切换当前主界面为Home界面
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                // 在退出时,需要将侧滑菜单收回
                mDrawerLayout.closeDrawers();
                break;
            case R.id.action_main_offline:
                // TODO: 打开新的Activity
                break;
            case R.id.action_main_fav:
                // TODO: 打开新的Activity
                break;
            default:
                break;
        }
        // TODO: 考虑哪些是替换当前界面, 哪些是新开界面
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START, true);
        } else {
            if (System.currentTimeMillis() - mTime <= 3000) {
                finish();
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mTime = System.currentTimeMillis();
            }
        }
    }


}
