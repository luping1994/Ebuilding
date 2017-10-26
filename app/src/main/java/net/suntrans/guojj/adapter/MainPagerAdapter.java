package net.suntrans.guojj.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.suntrans.guojj.fragment.AreaFragment;
import net.suntrans.guojj.fragment.DiningRoomFragment;
import net.suntrans.guojj.fragment.EnergyConFragment2;
import net.suntrans.guojj.fragment.EnvHomeFragment;
import net.suntrans.guojj.fragment.PerCenFragment;

/**
 * Created by Looney on 2017/7/20.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        DiningRoomFragment fragment1 = new DiningRoomFragment();
        AreaFragment fragment2 = new AreaFragment();
        EnergyConFragment2 fragment3 = new EnergyConFragment2();
        PerCenFragment fragment4 = new PerCenFragment();
        EnvHomeFragment fragment5 = new EnvHomeFragment();
        fragments = new Fragment[]{fragment1, fragment2,fragment5, fragment3, fragment4};
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
