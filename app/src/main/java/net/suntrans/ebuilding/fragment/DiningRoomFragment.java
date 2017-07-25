package net.suntrans.ebuilding.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.trello.rxlifecycle.components.support.RxFragment;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.adapter.DiningPagerAdapter;

import static android.support.design.widget.TabLayout.GRAVITY_FILL;
import static android.support.design.widget.TabLayout.MODE_FIXED;

/**
 * Created by Looney on 2017/7/20.
 */

public class DiningRoomFragment extends RxFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dining, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    private void initView(View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.setTabGravity(GRAVITY_FILL);
        viewPager.setAdapter(new DiningPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }
}

