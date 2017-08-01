package net.suntrans.ebuilding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.activity.Ammeter3Activity;
import net.suntrans.ebuilding.activity.Ammeter3Activity2;
import net.suntrans.ebuilding.adapter.EnergyFragAdapter;
import net.suntrans.ebuilding.adapter.EnergyFragAdapter2;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.EnergyEntity;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/20.
 */

public class EnergyConFragment2 extends RxFragment {



    private EnergyFragAdapter2 adapter;


    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private List<EnergyEntity.EnergyData> datas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_energy2, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("能耗");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        adapter = new EnergyFragAdapter2(getContext(), datas, new EnergyFragAdapter2.OnitemClickListener() {
            @Override
            public void onItemClik(int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Ammeter3Activity2.class);
                intent.putExtra("sno",datas.get(position).sno);
                intent.putExtra("name",datas.get(position).name);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
//        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();
//        new CardSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        RetrofitHelper.getApi().getEnergyIndex()
                .compose(this.<EnergyEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EnergyEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(EnergyEntity energyEntity) {
                        datas.clear();
                        datas.addAll(energyEntity.data.lists);
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

}
