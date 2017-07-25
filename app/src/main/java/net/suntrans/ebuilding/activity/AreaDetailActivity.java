package net.suntrans.ebuilding.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.fragment.area.AreaDeailFragment;

import static android.support.design.widget.TabLayout.GRAVITY_FILL;
import static android.support.design.widget.TabLayout.MODE_FIXED;

/**
 * Created by Looney on 2017/5/2.
 */

public class AreaDetailActivity extends BasedActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomdetail);

        initView();
    }


    private void initView() {

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("name"));

        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.setTabGravity(GRAVITY_FILL);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class PagerAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;

        private final String[] title = new String[]{"照明", "插座"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            AreaDeailFragment fragment =  AreaDeailFragment.newInstance("1",getIntent().getStringExtra("id"));
            AreaDeailFragment fragment2 =  AreaDeailFragment.newInstance("2",getIntent().getStringExtra("id"));
            fragments =new Fragment[]{fragment,fragment2};
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
