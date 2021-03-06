package net.suntrans.ebuilding.fragment.din;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.activity.EnvDetailActivity;
import net.suntrans.ebuilding.activity.EnvDetailActivity2;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.SensusEntity;
import net.suntrans.ebuilding.fragment.base.BasedFragment;
import net.suntrans.ebuilding.rx.BaseSubscriber;
import net.suntrans.ebuilding.utils.UiUtils;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.suntrans.ebuilding.utils.UiUtils.getContext;

/**
 * Created by Looney on 2017/7/20.
 */

public class EnvFragment extends BasedFragment {

    private RecyclerView recyclerView;
    private List<SensusEntity.SixDetailData> datas;
    private EnvAdapter adapter;
    private Observable<SensusEntity> getDataOb;
    private SwipeRefreshLayout refreshLayout;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(1);
            }
        });
        adapter = new EnvAdapter(R.layout.item_env, datas);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.addFooterView(getFooterView());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(getActivity(), EnvDetailActivity2.class);
                intent.putExtra("id", datas.get(position).id);
                intent.putExtra("info", datas.get(position));
                intent.putExtra("name", datas.get(position).title);
                startActivity(intent);
            }
        });

        super.onViewCreated(view, savedInstanceState);

    }

    private View getFooterView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.footer, null, false);
        return view;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_env;
    }


    private class EnvAdapter extends BaseQuickAdapter<SensusEntity.SixDetailData, BaseViewHolder> {

        public EnvAdapter(@LayoutRes int layoutResId, @Nullable List<SensusEntity.SixDetailData> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SensusEntity.SixDetailData item) {
            helper.setText(R.id.name, item.title)
                    .setText(R.id.isOnline, item.is_online.equals("1") ? "" : "不在线")
                    .setText(R.id.wendu, item.getWendu() + "℃")
                    .setText(R.id.shidu, item.getShidu() + "%")
                    .setText(R.id.pm25, item.getPm25())
                    .setText(R.id.guanzhao, item.guanzhaoEva);
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        getData(0);
        super.onFragmentFirstVisible();
    }

    @Override
    public void onRetryClick() {
        getData(0);
        super.onRetryClick();
    }


    private void getData(final int refreshType) {
        if (refreshType == 0){
            stateView.showLoading();
            recyclerView.setVisibility(View.INVISIBLE);
        }
        if (getDataOb == null)
            getDataOb = RetrofitHelper.getApi().getHomeSceneNew()
                    .compose(this.<SensusEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        getDataOb.subscribe(new BaseSubscriber<SensusEntity>(getActivity()) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
                if (refreshType == 0){
                    stateView.showRetry();
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNext(SensusEntity data) {

                if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
                if (data.data.lists == null || data.data.lists.size() == 0) {
                    stateView.showEmpty();
                    return;
                }
                stateView.showContent();
                recyclerView.setVisibility(View.VISIBLE);

                if (data.code == 200) {
                    for (SensusEntity.SixDetailData ds : data.data.lists) {
                        ds.setEva();
//                        System.out.println(ds.name);
                    }
                    datas.clear();
                    datas.addAll(data.data.lists);
                    adapter.notifyDataSetChanged();
                } else {
                    UiUtils.showToast("暂无信息");
                }
            }
        });
    }


}
