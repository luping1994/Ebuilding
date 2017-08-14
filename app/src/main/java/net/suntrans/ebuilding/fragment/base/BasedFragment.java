package net.suntrans.ebuilding.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.ebuilding.R;
import net.suntrans.stateview.StateView;

/**
 * Created by Administrator on 2017/8/14.
 */

public abstract class BasedFragment extends LazyLoadFragment implements StateView.OnRetryClickListener {
    protected StateView stateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        if (view != null) {
            stateView = StateView.inject(view.findViewById(R.id.content), false);
            stateView.setOnRetryClickListener(this);
        }
        initView(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onRetryClick() {

    }

    public abstract int getLayoutRes();

    protected void initView(View view) {

    }
}
