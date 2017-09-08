package net.suntrans.ebuilding;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.update.PgyUpdateManager;

import net.suntrans.ebuilding.activity.BasedActivity;
import net.suntrans.ebuilding.activity.ChangePassActivity;
import net.suntrans.ebuilding.adapter.MainPagerAdapter;
import net.suntrans.ebuilding.fragment.AreaFragment;
import net.suntrans.ebuilding.fragment.DiningRoomFragment;
import net.suntrans.ebuilding.fragment.EnergyConFragment2;
import net.suntrans.ebuilding.fragment.EnvHomeFragment;
import net.suntrans.ebuilding.fragment.PerCenFragment;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.IViewPager;

import static android.support.design.widget.TabLayout.GRAVITY_FILL;
import static android.support.design.widget.TabLayout.MODE_FIXED;
import static net.suntrans.ebuilding.BuildConfig.DEBUG;

import android.content.Context;

public class MainActivity extends BasedActivity {

    private final int[] TAB_TITLES = new int[]{R.string.nav_tit, R.string.nav_area, R.string.nav_env, R.string.nav_power, R.string.nav_user};
    private final int[] TAB_IMGS = new int[]{R.drawable.select_home, R.drawable.select_area, R.drawable.select_env, R.drawable.select_power, R.drawable.select_user};
    private TabLayout tabLayout;

    private DiningRoomFragment fragment1;
    private AreaFragment fragment2;
    private EnergyConFragment2 fragment3;
    private PerCenFragment fragment4;
    private EnvHomeFragment fragment5;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.i("绑定服务成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        Intent intent = new Intent();
        intent.setClass(this, MyService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        if (!DEBUG)
            PgyUpdateManager.register(this, "net.suntrans.ebuilding.fileProvider");
        init();
    }

    private Fragment[] fragments;

    private void init() {
//        fragment1 = (DiningRoomFragment) getSupportFragmentManager().findFragmentByTag("0");
//        fragment2 = (AreaFragment) getSupportFragmentManager().findFragmentByTag("1");
//        fragment3 = (EnergyConFragment2) getSupportFragmentManager().findFragmentByTag("2");
//        fragment4 = (PerCenFragment) getSupportFragmentManager().findFragmentByTag("3");
//        fragment5 = (EnvHomeFragment) getSupportFragmentManager().findFragmentByTag("4");

        if (fragment1 == null)
            fragment1 = new DiningRoomFragment();

        if (fragment2 == null)
            fragment2 = new AreaFragment();

        if (fragment3 == null)
            fragment3 = new EnergyConFragment2();

        if (fragment4 == null)
            fragment4 = new PerCenFragment();

        if (fragment5 == null)
            fragment5 = new EnvHomeFragment();

        fragments = new Fragment[]{fragment1, fragment2, fragment5, fragment3, fragment4};
        changFragment(0, "0");

        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.setTabGravity(GRAVITY_FILL);
        setTabs(tabLayout, this.getLayoutInflater(), TAB_TITLES, TAB_IMGS);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println(tab.getPosition() + "");
                changFragment(tab.getPosition(), tab.getPosition() + "");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * @description: 设置添加Tab
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.tab, null);
            tab.setCustomView(view);

            TextView tvTitle = (TextView) view.findViewById(R.id.tv_tab);
            ImageView imgTab = (ImageView) view.findViewById(R.id.img_tab);

            tvTitle.setText(tabTitlees[i]);
            imgTab.setImageResource(tabImgs[i]);

            tabLayout.addTab(tab);

        }
    }

    @Override
    protected void onDestroy() {
        if (!DEBUG)
            PgyUpdateManager.unregister();

        if (connection != null)
            unbindService(connection);
        super.onDestroy();
    }


    private long[] mHits = new long[2];

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
//                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                Toast.makeText(this.getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    int currentIndex = 0;

    private void changFragment(int index, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[currentIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.content, fragments[index]);
        }
        transaction.show(fragments[index]).commit();
        currentIndex = index;
    }


}
