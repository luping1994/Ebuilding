package net.suntrans.ebuilding.fragment.area;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.activity.SceneDetailActivity;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.AreaDetailEntity;
import net.suntrans.ebuilding.bean.ControlEntity;
import net.suntrans.ebuilding.bean.DeviceEntity;
import net.suntrans.ebuilding.bean.SampleResult;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.LoadingDialog;
import net.suntrans.ebuilding.views.SwitchButton;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.suntrans.ebuilding.R.id.recyclerView;

/**
 * Created by Looney on 2017/7/21.
 */

public class AreaDeailFragment extends RxFragment {
    private  final String TAG = this.getClass().getSimpleName();

    private Observable<AreaDetailEntity> getDataObv;
    private SwipeRefreshLayout refreshLayout;
    private String house_id;
    private TextView tips;
    private RecyclerView recyclerView;
    private DevicesAdapter adapter;
    private String channelType;

    private LoadingDialog dialog;
    public static final AreaDeailFragment newInstance(String channel_type, String id) {
        AreaDeailFragment fragment = new AreaDeailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("channel_type",channel_type);
        bundle.putString("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area_detail,container,false);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        adapter = new DevicesAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        house_id = getArguments().getString("id");
        channelType = getArguments().getString("channel_type");
        tips = (TextView) view.findViewById(R.id.tips);
        LogUtil.i("房间id" + house_id);
    }


    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    public void getData() {

        if (getDataObv == null) {
            getDataObv = RetrofitHelper.getApi().getRoomChannel(house_id)
                    .compose(this.<AreaDetailEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

        getDataObv.subscribe(new Subscriber<AreaDetailEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof HttpException) {
                    if (e.getMessage() != null) {
                        if (e.getMessage().equals("HTTP 401 Unauthorized")) {
                            UiUtils.showToast("您的身份信息已失效,请重新登录");
                        }
                    } else {
                        UiUtils.showToast("服务器错误");
                    }
                }
                if (e instanceof SocketTimeoutException){
                    UiUtils.showToast("连接超时");
                }
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onNext(AreaDetailEntity result) {
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
                if (result != null) {
                    datas.clear();

                    for (int i= 0;i<result.data.lists.size();i++){
                        if (result.data.lists.get(i).channel_type.equals(channelType)){
                            datas.add(result.data.lists.get(i));
                        }

                    }
                    adapter.notifyDataSetChanged();

                }
                if (datas.size() == 0) {
                    tips.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tips.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private String[] items = {"移除设备"};

    class DevicesAdapter extends RecyclerView.Adapter {
        String[] colors = new String[]{"#f99e5b", "#d3e4ad", "#94c9d6"};

        public DevicesAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(inflater.inflate(R.layout.item_area, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).setData(position);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView area;
            TextView name;
            RelativeLayout root;
            SwitchButton checkbox;

            public ViewHolder(View itemView) {
                super(itemView);
                root = (RelativeLayout) itemView.findViewById(R.id.root);
                name = (TextView) itemView.findViewById(R.id.name);
                checkbox = (SwitchButton) itemView.findViewById(R.id.checkbox);
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        sendCmd(getAdapterPosition());
                    }
                });
                root.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        showDeleteDialog(datas.get(getAdapterPosition()).id);

                                        break;
                                    case 1:

                                        break;
                                }
                            }
                        });
                        builder.create().show();
                        return true;
                    }
                });
            }

            public void setData(int position) {
                name.setText(datas.get(position).name);
                checkbox.setCheckedImmediately(datas.get(position).status.equals("1") ? true : false);
//                checkbox.setChecked();
//                area.setText("暂无数据");
            }


        }
    }

    List<DeviceEntity.ChannelInfo> datas = new ArrayList<>();
    private Observable<ControlEntity> conOb;

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

            }

            @Override
            public void onNext(ControlEntity data) {
                dialog.dismiss();

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


    private void showDeleteDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogUtil.i(id);
                delete(id);
            }

        });
        builder.setNegativeButton(R.string.qvxiao, null);
        builder.setMessage("是否将该设备从该区域中移除?")
                .create().show();
    }

    private void delete(String id) {
        RetrofitHelper.getApi().deleteAreaChannel(id)
                .compose(this.<SampleResult>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SampleResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SampleResult result) {
                        if (result.getCode()==200){
                            UiUtils.showToast("删除成功");
                            getData();
                        }else {
                            UiUtils.showToast("删除失败");
                        }
                    }
                });
    }
}
