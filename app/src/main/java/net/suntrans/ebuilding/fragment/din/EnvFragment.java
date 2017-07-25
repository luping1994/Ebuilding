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
import android.webkit.WebView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.activity.EnvDetailActivity;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.LinkageResult;
import net.suntrans.ebuilding.bean.SceneEntity;
import net.suntrans.ebuilding.bean.SensusEntity;
import net.suntrans.ebuilding.utils.UiUtils;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/20.
 */

public class EnvFragment extends RxFragment {

    private RecyclerView recyclerView;
    private List<SensusEntity.SixInfo> datas;
    private EnvAdapter adapter;
    private Observable<SensusEntity> getDataOb;
    private SwipeRefreshLayout refreshLayout;
    private TextView tips;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_env, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tips = (TextView) view.findViewById(R.id.tips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();

            }
        });
        adapter = new EnvAdapter(R.layout.item_env, datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), EnvDetailActivity.class);
//                intent.putExtra("info",datas.get(position).sub);
                startActivity(intent);
            }
        });


    }


    private class EnvAdapter extends BaseQuickAdapter<SensusEntity.SixInfo, BaseViewHolder> {

        public EnvAdapter(@LayoutRes int layoutResId, @Nullable List<SensusEntity.SixInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SensusEntity.SixInfo item) {
            helper.setText(R.id.name, item.name)
                    .setText(R.id.isOnline, item.is_online.equals("1") ? "在线" : "不在线")
                    .setText(R.id.wendu, item.sub.getWendu() + "℃")
                    .setText(R.id.shidu, item.sub.getShidu() + "%")
                    .setText(R.id.pm25, item.sub.getPm25())
                    .setText(R.id.guanzhao, item.sub.guanzhaoEva);
        }
    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    private void getData() {
        if (getDataOb == null)
            getDataOb = RetrofitHelper.getApi().getHomeSceneNew()
                    .compose(this.<SensusEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        getDataOb.subscribe(new Subscriber<SensusEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ConnectException) {
                    UiUtils.showToast("网络连接失败");
                    if (refreshLayout != null)
                        refreshLayout.setRefreshing(false);
                }
                e.printStackTrace();
            }

            @Override
            public void onNext(SensusEntity data) {

                if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
                if (data.code == 200) {

                    for (SensusEntity.SixInfo ds : data.data.lists) {
                        ds.sub.setEva();
                    }

                    datas.clear();
                    datas.addAll(data.data.lists);
                    adapter.notifyDataSetChanged();
                } else {
                    UiUtils.showToast("暂无信息");
                }

                if (datas.size() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    tips.setVisibility(View.GONE);

                } else {
                    tips.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }

            }
        });
    }


}
