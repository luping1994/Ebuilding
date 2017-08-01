package net.suntrans.ebuilding.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.bean.Ameter3;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.OffsetDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/7/31.
 */

public class AmmeterParameterActivity extends BasedActivity {
    private String sno;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private Map<String, String> dictionary = null;
    private Myadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        initData();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new Myadapter(this);
        recyclerView.setAdapter(adapter);
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.addItemDecoration(new OffsetDecoration(UiUtils.dip2px(2)));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void initData() {
        sno = getIntent().getStringExtra("sno");

        dictionary = new HashMap<>();
        dictionary.put("VolA", "A相电压");
        dictionary.put("VolAU", "V");
        dictionary.put("updated_at", "更新时间");
        dictionary.put("updated_atU", "");
        dictionary.put("VolB", "B相电压");
        dictionary.put("VolBU", "V");
        dictionary.put("VolC", "C相电压");
        dictionary.put("VolCU", "V");
        dictionary.put("IA", "A相电流");
        dictionary.put("IAU", "A");
        dictionary.put("IB", "B相电流");
        dictionary.put("IBU", "A");
        dictionary.put("IC", "C相电流");
        dictionary.put("ICU", "A");
        dictionary.put("ActivePower", "有功功率");
        dictionary.put("ActivePowerU", "W");
        dictionary.put("ActivePowerA", "A相有功功率");
        dictionary.put("ActivePowerAU", "W");
        dictionary.put("ActivePowerB", "B相有功功率");
        dictionary.put("ActivePowerBU", "W");
        dictionary.put("ActivePowerC", "C相有功功率");
        dictionary.put("ActivePowerCU", "W");
        dictionary.put("ReactivePower", "无功功率");
        dictionary.put("ReactivePowerU", "W");
        dictionary.put("ReactivePowerA", "A相无功功率");
        dictionary.put("ReactivePowerAU", "W");
        dictionary.put("ReactivePowerB", "B相无功功率");
        dictionary.put("ReactivePowerBU", "W");
        dictionary.put("ReactivePowerC", "C相无功功率");
        dictionary.put("ReactivePowerCU", "W");
        dictionary.put("PowerFactor", "功率因素");
        dictionary.put("PowerFactorU", "");
        dictionary.put("PowerFactorA", "A相功率因素");
        dictionary.put("PowerFactorAU", "");
        dictionary.put("PowerFactorB", "B相功率因素");
        dictionary.put("PowerFactorBU", "");
        dictionary.put("PowerFactorC", "C相功率因素");
        dictionary.put("PowerFactorCU", "");
        dictionary.put("EletricityValue", "用电量");
        dictionary.put("EletricityValueU", "kW*h");
        dictionary.put("EletricitySharp", "尖");
        dictionary.put("EletricitySharpU", "");
        dictionary.put("EletricityPeak", "峰");
        dictionary.put("EletricityPeakU", "");
        dictionary.put("EletricityFlat", "平");
        dictionary.put("EletricityFlatU", "");
        dictionary.put("EletricityValley", "");
        dictionary.put("EletricityValleyU", "");
    }

    private void getData() {
        for (int i=0;i<6;i++){
            Ameter3 ameter3 = new Ameter3();
            ameter3.name = "VolC";
            ameter3.nameCH="A相电压";
            ameter3.unit="V";
            ameter3.value="100";
            datas.add(ameter3);
        }
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private List<Ameter3> datas = new ArrayList<>();
    class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        private Activity mActivity;

        public Myadapter(Activity mActivity) {
            this.mActivity = mActivity;
        }

        @Override

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.item_ameter3, parent, false));
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
            private TextView name;
            private TextView value;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                value = (TextView) itemView.findViewById(R.id.value);
                itemView.findViewById(R.id.dianliull).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, HistroyDataActivity.class);
                        intent.putExtra("sno", sno);
                        intent.putExtra("vtype", "3");
                        intent.putExtra("name",getIntent().getStringExtra("name") );
                        intent.putExtra("data_type", datas.get(getAdapterPosition()).name);
                        intent.putExtra("shuoming", dictionary.get(datas.get(getAdapterPosition()).name));
                        startActivity(intent);
                    }
                });
            }

            public void setData(int position) {
                name.setText(datas.get(position).nameCH);
                value.setText(datas.get(position).value+datas.get(position).unit);
            }
        }
    }

}
