package net.suntrans.ebuilding.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.fragment.din.EnvFragment;

/**
 * Created by Administrator on 2017/8/14.
 */

public class EnvHomeFragment extends RxFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_env_home,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        EnvFragment fragment = new EnvFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.content,fragment).commit();
    }
}
