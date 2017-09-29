package net.suntrans.ebuilding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.RespondBody;
import net.suntrans.ebuilding.bean.SceneTimeResult;
import net.suntrans.ebuilding.rx.BaseSubscriber;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.SwitchButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/9/26.
 */

public class SceneTimingActivity extends BasedActivity {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private String scene_id;
    private SceneTimeAdater adapter;
    private TextView tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_timeing);
        initToolBar();
        init();
    }


    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_title_dingshi);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        datas = new ArrayList<>();
        scene_id = getIntent().getStringExtra("scene_id");
//        System.out.println("scene_id为:"+scene_id);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new SceneTimeAdater();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTimeer(scene_id);
            }
        });
        tips = (TextView) findViewById(R.id.tips);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tianjia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(SceneTimingActivity.this, AddSceneTimeActivity.class);
            intent.putExtra("scene_id", scene_id);
            intent.putExtra("commitType", "add");
            if (datas != null && datas.size() != 0)
                intent.putExtra("data", datas.get(0));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTimeer(scene_id);
    }

    private void getTimeer(String scene_id) {
        addSubscription(RetrofitHelper.getApi().getSceneTiming(scene_id)
                , new BaseSubscriber<RespondBody<SceneTimeResult>>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        if (refreshLayout != null)
                            refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(RespondBody<SceneTimeResult> result) {
                        super.onNext(result);
                        if (refreshLayout != null)
                            refreshLayout.setRefreshing(false);
                        if (result.data == null) {
                            tips.setVisibility(View.VISIBLE);
                            return;
                        }
                        tips.setVisibility(View.GONE);
                        datas.clear();
                        datas.add(result.data);
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    private List<SceneTimeResult> datas;

    private class SceneTimeAdater extends RecyclerView.Adapter<SceneTimeAdater.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(SceneTimingActivity.this).inflate(R.layout.item_timeing, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setData(position);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {


            TextView time;
            TextView type;
            SwitchButton switchButton;

            public ViewHolder(View itemView) {
                super(itemView);
                time = (TextView) itemView.findViewById(R.id.time);
                type = (TextView) itemView.findViewById(R.id.type);
                switchButton = (SwitchButton) itemView.findViewById(R.id.checkbox);
                switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setTimmer(scene_id, datas.get(getAdapterPosition()).status.equals("1") ? "0" : "1"
                                , datas.get(getAdapterPosition()).timer, datas.get(getAdapterPosition()).type);
                    }
                });
            }

            public void setData(int postion) {
                time.setText(datas.get(postion).timer);
                type.setText(datas.get(postion).type.equals("0") ? getString(R.string.scene_time_workday) : getString(R.string.scene_time_everyday));
                LogUtil.i(datas.get(postion).status);
                switchButton.setCheckedImmediately(datas.get(postion).status.equals("1"));

            }
        }
    }

    private void setTimmer(final String scene_id, String status, String timer, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("scene_id", scene_id);
        map.put("status", status);
        map.put("timer", timer);
        map.put("type", type);
        addSubscription(RetrofitHelper.getApi().setSceneTiming(map)
                , new BaseSubscriber<RespondBody>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(RespondBody respondBody) {
                        super.onNext(respondBody);
                        UiUtils.showToast(respondBody.msg);
                        getTimeer(scene_id);
//                        if (respondBody.code == 200) {
//
//                        }
                    }
                });
    }

}
