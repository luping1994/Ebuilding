package net.suntrans.ebuilding;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgyersdk.update.PgyUpdateManager;

import net.suntrans.ebuilding.activity.BasedActivity;
import net.suntrans.ebuilding.adapter.MainPagerAdapter;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.IViewPager;

import static android.support.design.widget.TabLayout.GRAVITY_FILL;
import static android.support.design.widget.TabLayout.MODE_FIXED;
import static net.suntrans.ebuilding.BuildConfig.DEBUG;

public class MainActivity extends BasedActivity {
    private final int[] TAB_TITLES = new int[]{R.string.nav_tit, R.string.nav_area, R.string.nav_power, R.string.nav_user};
    private final int[] TAB_IMGS = new int[]{R.drawable.select_home, R.drawable.select_area, R.drawable.select_power, R.drawable.select_user};
    private TabLayout tabLayout;
    private IViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//       LogUtil.i("mainActivity:", UiUtils.px2dip(400)+"");
       LogUtil.i("mainActivity:", UiUtils.px2dip(448)+"");
            PgyUpdateManager.register(this, "net.suntrans.ebuilding.fileProvider");
        init();
    }

    private void init() {
        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.setTabGravity(GRAVITY_FILL);
        setTabs(tabLayout, this.getLayoutInflater(), TAB_TITLES, TAB_IMGS);
        vp = (IViewPager) findViewById(R.id.content);
        vp.setOffscreenPageLimit(3);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
//        tabLayout.setupWithViewPager(vp);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition(),false);
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
        PgyUpdateManager.unregister();
        super.onDestroy();
    }
}
