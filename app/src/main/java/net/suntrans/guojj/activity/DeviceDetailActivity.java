package net.suntrans.guojj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.guojj.R;
import net.suntrans.guojj.adapter.DefaultDecoration;
import net.suntrans.guojj.api.RetrofitHelper;
import net.suntrans.guojj.bean.DeviceDetailResult;
import net.suntrans.guojj.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/8.
 * Des:
 */

public class DeviceDetailActivity extends BasedActivity {

    private String dev_id;

    private List<DeviceDetailResult.DataInfo> datas;
    private Myadapter myadapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));

        datas = new ArrayList<>();
        dev_id = getIntent().getStringExtra("id");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        myadapter = new Myadapter(R.layout.item_device_detail, datas);
        recyclerView.addItemDecoration(new DefaultDecoration());
        recyclerView.setAdapter(myadapter);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        myadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(DeviceDetailActivity.this, ChannelEditActivity.class);
                intent.putExtra("id", datas.get(position).id);
                intent.putExtra("title", datas.get(position).name);
                intent.putExtra("channel_type", datas.get(position).channel_type);
                startActivity(intent);
            }
        });
    }

    class Myadapter extends BaseQuickAdapter<DeviceDetailResult.DataInfo, BaseViewHolder> {

        public Myadapter(int layoutResId, @Nullable List<DeviceDetailResult.DataInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DeviceDetailResult.DataInfo item) {
            helper.setText(R.id.name, item.name == null ? "--" : item.name)
                    .setText(R.id.des, item.datapoint_name == null ? "--" : item.datapoint_name);
            ImageView imageView = helper.getView(R.id.image);
            if ("1".equals(item.channel_type)) {
                imageView.setImageResource(R.drawable.ic_light);
            } else {
                imageView.setImageResource(R.drawable.ic_socket);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        if (dev_id == null)
            return;
        RetrofitHelper.getApi()
                .getDeviceDetail(dev_id)
                .compose(this.<DeviceDetailResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<DeviceDetailResult>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(DeviceDetailResult deviceDetailResult) {
                        super.onNext(deviceDetailResult);
                        refreshLayout.setRefreshing(false);
                        datas.clear();
                        datas.addAll(deviceDetailResult.data);
                        myadapter.notifyDataSetChanged();
                    }
                });
    }
}
