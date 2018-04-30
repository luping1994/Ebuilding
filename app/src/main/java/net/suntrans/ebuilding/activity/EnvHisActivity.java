package net.suntrans.ebuilding.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;


import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.HisEntity;
import net.suntrans.ebuilding.databinding.ActivityEnergyMoniBinding;
import net.suntrans.ebuilding.fragment.TimeChartFragment;
import net.suntrans.ebuilding.rx.BaseSubscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class EnvHisActivity extends BasedActivity implements TimeChartFragment.OnFragmentInteractionListener{
    private ActivityEnergyMoniBinding binding;
    private String field;
    private String house_id;
    private TimeChartFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_energy_moni);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String unit = getIntent().getStringExtra("unit");
        String name = getIntent().getStringExtra("name");
        TextView txTitle = (TextView) findViewById(R.id.title);
        txTitle.setText(name+"历史记录");
        fragment = TimeChartFragment.newInstance(unit,name);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
        field = getIntent().getStringExtra("field");
        house_id = getIntent().getStringExtra("house_id");

    }

    //    getEnvHis
    private void getDatas(String startTime, String endTime) {
        Map<String, String> map = new HashMap<>();
        map.put("id", house_id);
        map.put("field",field);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        addSubscription(RetrofitHelper.getApi().getEnvHis(map), new BaseSubscriber<HisEntity>(this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
            }

            @Override
            public void onNext(HisEntity hisEntity) {
                super.onNext(hisEntity);
                try {
                    fragment.setData(hisEntity);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }


    @Override
    public void getData(String startTime, String endTime) {
        getDatas(startTime,endTime);
    }
}
