package net.suntrans.ebuilding.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.suntrans.ebuilding.fragment.AreaFragment;
import net.suntrans.ebuilding.fragment.DiningRoomFragment;
import net.suntrans.ebuilding.fragment.EnergyConFragment;
import net.suntrans.ebuilding.fragment.EnergyConFragment2;
import net.suntrans.ebuilding.fragment.PerCenFragment;

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
        fragments = new Fragment[]{fragment1, fragment2, fragment3, fragment4};
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
