package net.suntrans.ebuilding.fragment.din;

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
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.ControlEntity;
import net.suntrans.ebuilding.bean.DeviceEntity;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.LoadingDialog;
import net.suntrans.ebuilding.views.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/20.
 */

public class LightFragment extends RxFragment {

    private RecyclerView recyclerView;
    private List<DeviceEntity.ChannelInfo> datas;
    private Observable<ControlEntity> conOb;
    private LoadingDialog dialog;
    private LightAdapter adapter;
    private Observable<DeviceEntity> getDataObj;
    private SwipeRefreshLayout refreshLayout;
    private TextView tips;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        tips = (TextView) view.findViewById(R.id.tips);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LightAdapter();
        recyclerView.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }
    private class LightAdapter extends RecyclerView.Adapter<LightAdapter.ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_light,parent,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setData(position);
        }

        @Override
        public int getItemCount() {
            return datas==null?0:datas.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView name;
            SwitchButton button;
            public void setData(int position){
                button.setCheckedImmediately(datas.get(position).status.equals("1")?true:false);
                name.setText(datas.get(position).name);
            }
            public ViewHolder(View itemView) {
                super(itemView);
                button = (SwitchButton) itemView.findViewById(R.id.checkbox);
                name = (TextView) itemView.findViewById(R.id.name);
                button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        sendCmd(getAdapterPosition());
                    }
                });
            }
        }
    }



    private void sendCmd(int position) {
        if (position==-1){
            UiUtils.showToast("请不要频繁操作！");
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(getActivity(), R.style.loading_dialog);
            dialog.setCancelable(false);

        }
        dialog.setWaitText("请稍后");
        dialog.show();

        conOb = null;
        conOb = RetrofitHelper.getApi().switchChannel(datas.get(position).id, datas.get(position).datapoint,
                datas.get(position).din, datas.get(position).status.equals("1") ? "0" : "1")
                .compose(this.<ControlEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        String order = datas.get(position).status.equals("1") ? "关" : "开";
        LogUtil.i("发出命令:" + order);
        conOb.subscribe(new Subscriber<ControlEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                UiUtils.showToast("服务器错误");
                dialog.dismiss();

                adapter.notifyDataSetChanged();
                if (refreshLayout!=null){
                    refreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onNext(ControlEntity data) {
                dialog.dismiss();
                if (refreshLayout!=null){
                    refreshLayout.setRefreshing(false);
                }
                if (data.code == 200) {
                    LogUtil.i(data.data.toString());
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).id.equals(data.data.id)) {
                            datas.get(i).status = String.valueOf(data.data.status);
                        }
                    }
                } else {
                    UiUtils.showToast("服务器错误");

                }

                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();

    }

    public void getData() {
        if (getDataObj == null) {
            getDataObj = RetrofitHelper.getApi().getAllDevice()
                    .compose(this.<DeviceEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

        getDataObj.subscribe(new Subscriber<DeviceEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onNext(DeviceEntity result) {
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
                if (result != null) {
                    datas.clear();
                    datas.addAll(result.data.lists);
                    adapter.notifyDataSetChanged();
                }
                if (datas.size()!=0){
                    recyclerView.setVisibility(View.VISIBLE);
                    tips.setVisibility(View.GONE);

                }else {
                    tips.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

}
