package net.suntrans.ebuilding.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.suntrans.ebuilding.fragment.AreaFragment;
import net.suntrans.ebuilding.fragment.DiningRoomFragment;
import net.suntrans.ebuilding.fragment.EnergyConFragment;
import net.suntrans.ebuilding.fragment.PerCenFragment;
import net.suntrans.ebuilding.fragment.din.EnvFragment;
import net.suntrans.ebuilding.fragment.din.LightFragment;
import net.suntrans.ebuilding.fragment.din.SceneFragment;

import rx.Observable;

/**
 * Created by Looney on 2017/7/20.
 */

public class DiningPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments;
    private final String[] titles = new String[]{"场景","照明","环境"};

    public DiningPagerAdapter(FragmentManager fm) {
        super(fm);
        SceneFragment fragment1 = new SceneFragment();
        LightFragment fragment2 = new LightFragment();
        EnvFragment fragment3 = new EnvFragment();
        fragments = new Fragment[]{fragment1, fragment2, fragment3};
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
