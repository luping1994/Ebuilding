package net.suntrans.ebuilding.fragment.din;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.adapter.AddSenceDevGrpAdapter;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.AreaEntity;
import net.suntrans.ebuilding.rx.BaseSubscriber;
import net.suntrans.ebuilding.utils.ActivityUtils;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/16.
 */

public class AddSceneChannelFragment extends RxFragment implements StateView.OnRetryClickListener {
    private ExpandableListView listView;
    private AddSenceDevGrpAdapter adapter;
    private StateView stateView;
    private List<AreaEntity.AreaFloor> datas = new ArrayList<>();

    public List<AreaEntity.AreaFloor> getDatas() {
        return datas;
    }

    public void setDatas(List<AreaEntity.AreaFloor> datas) {
        this.datas = datas;
    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addchannel, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        stateView = StateView.inject(view.findViewById(R.id.content), false);
        stateView.setOnRetryClickListener(this);
        listView = (ExpandableListView) view.findViewById(R.id.expandListView);
        listView.setGroupIndicator(null);
        adapter = new AddSenceDevGrpAdapter(datas, getContext());
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    private void getData() {
        stateView.showLoading();
        RetrofitHelper.getApi().getHomeHouse()
                .compose(this.<AreaEntity>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AreaEntity>(getActivity()) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
//                        UiUtils.showToast(e.getMessage());
                        stateView.showRetry();
                    }

                    @Override
                    public void onNext(AreaEntity areaEntity) {
                        if (areaEntity.code == 200) {
                            datas.clear();
                            datas.addAll(areaEntity.data.lists);
                            adapter.notifyDataSetChanged();
                            stateView.showContent();
                        } else if (areaEntity.code == 401) {
                            ActivityUtils.showLoginOutDialogFragmentToActivity(getChildFragmentManager(), "Alert");
                        } else {
                            UiUtils.showToast(areaEntity.msg);
                        }


                    }
                });
    }

    @Override
    public void onRetryClick() {
        getData();
    }
}
