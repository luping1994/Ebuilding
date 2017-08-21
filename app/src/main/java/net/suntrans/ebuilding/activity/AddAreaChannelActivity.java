package net.suntrans.ebuilding.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.AddSceneChannelResult;
import net.suntrans.ebuilding.bean.FreshChannelEntity;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.LoadingDialog;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/16.
 */

public class AddAreaChannelActivity extends BasedActivity {

    private RecyclerView recyclerView;
    private List<FreshChannelEntity.DataBean.ListsBean> datas;
    private MyAdapter adapter;
    private StateView stateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area_channel);
        stateView = StateView.inject(findViewById(R.id.content));
        stateView.setEmptyResource(R.layout.add_area_empty);
        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getData();
            }
        });
        setUpToolBar();
        init();
    }


    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("选择要添加的设备");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        datas = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyAdapter(R.layout.item_channel_selected, datas);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                datas.get(position).setChecked(!datas.get(position).isChecked());
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);

    }

    class MyAdapter extends BaseQuickAdapter<FreshChannelEntity.DataBean.ListsBean, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<FreshChannelEntity.DataBean.ListsBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FreshChannelEntity.DataBean.ListsBean item) {
            helper.setText(R.id.name, item.getName());
            AppCompatCheckBox compatCheckBox = helper.getView(R.id.checkbox);
            compatCheckBox.setChecked(item.isChecked() ? true : false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tijiao, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.commit) {
            int count = 0;
            StringBuilder channel = new StringBuilder();
            StringBuilder sort = new StringBuilder();
            for (FreshChannelEntity.DataBean.ListsBean s :
                    datas) {
                if (s.isChecked()) {
                    count++;
                    sort.append("1")
                            .append(",");
                    channel.append(s.getId())
                            .append(",");
                }
            }
            if (count < 1) {
                UiUtils.showToast("请选择一个设备");
                return true;
            }
            final String area_id = channel.substring(0, channel.length() - 1);
            final String show_sort = sort.substring(0, sort.length() - 1);

            new AlertDialog.Builder(this)
                    .setMessage("是否添加" + count + "个设备")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            add(area_id, getIntent().getStringExtra("id"), show_sort);
                        }
                    }).setNegativeButton("取消", null).create().show();
            return true;

        }
        return super.onOptionsItemSelected(item);

    }

    private LoadingDialog dialog;

    private void add(String channel_id, String area_id, String show_sort) {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setCancelable(false);
            dialog.setWaitText("请稍后");
        }
        dialog.show();
        LogUtil.i("区域id=" + area_id + ",channelid=" + channel_id);
        RetrofitHelper.getApi().addAreaChannel(area_id, channel_id, show_sort)
                .compose(this.<AddSceneChannelResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddSceneChannelResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(AddSceneChannelResult result) {
                        dialog.dismiss();
                        if (result.getMsg() != null) {
                            int count = 0;
                            for (String s :
                                    result.getMsg()) {
                                if (s.equals("ok"))
                                    count++;
                            }
                            UiUtils.showToast("添加" + count + "个设备成功");
                            finish();
                        } else {
                            UiUtils.showToast("添加失败");
                        }
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        showLoading();
        RetrofitHelper.getApi().getFreshChannel()
                .compose(this.<FreshChannelEntity>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FreshChannelEntity>() {


                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(e.getMessage());
                        showError();
                    }

                    @Override
                    public void onNext(FreshChannelEntity result) {
                        if (result.getCode().equals("200")) {
                            if (result.getData() != null) {
                                if (result.getData().getLists().size() != 0) {
                                    showContent();
                                    datas.clear();
                                    datas.addAll(result.getData().getLists());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    showEmpty();
                                }
                            } else {
                                showError();

                            }
                        } else {
                            showError();
                        }

                    }
                });
    }





    private void showError() {
        stateView.showRetry();
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        stateView.showLoading();
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void showEmpty() {
        stateView.showEmpty();
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void showContent() {
        stateView.showContent();
        recyclerView.setVisibility(View.VISIBLE);

    }
}
