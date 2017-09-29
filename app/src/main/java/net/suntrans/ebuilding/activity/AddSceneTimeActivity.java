package net.suntrans.ebuilding.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.RespondBody;
import net.suntrans.ebuilding.bean.SceneTimeResult;
import net.suntrans.ebuilding.rx.BaseSubscriber;
import net.suntrans.ebuilding.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2017/9/26.
 */

public class AddSceneTimeActivity extends BasedActivity implements View.OnClickListener {

    private String commitType;
    private TimePicker picker;
    private TextView typeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_timeing_set);
        initToolBar();
        init();

    }


    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("设置定时");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        findViewById(R.id.chooseType).setOnClickListener(this);
        typeName = (TextView) findViewById(R.id.typeName);
        picker = (TimePicker) findViewById(R.id.timePicker);
        picker.setIs24HourView(true);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        timer = format.format(new Date());

        scene_id = getIntent().getStringExtra("scene_id");
        commitType = getIntent().getStringExtra("commitType");
        SceneTimeResult result = getIntent().getParcelableExtra("data");
        if (result != null) {
            if (result.type != null) {
                typeName.setText(result.type.equals("1") ? "每天执行" : "周一至周五");
                currentIndex = result.type.equals("0") ? 0 : 1;
                type = result.type;
            }
            if (result.timer != null) {
                picker.setCurrentHour(Integer.valueOf(result.timer.split(":")[0]));
                picker.setCurrentMinute(Integer.valueOf(result.timer.split(":")[1]));
            }
            if (result.status != null) {
                status = result.status;
            }

        }


        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timer = UiUtils.pad(hourOfDay) + ":" + UiUtils.pad(minute);
//                System.out.println(timer);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tijiao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.commit) {
            if (commitType != null) {
                setTimmer(scene_id, status, timer, type);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private String scene_id;
    private String status = "1";
    private String timer;
    private String type = "0";

    private void setTimmer(String scene_id, String status, String timer, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("scene_id", scene_id);
        map.put("status", status);
        map.put("timer", timer);
        map.put("type", type);
//        for (Map.Entry<String,String> entry:map.entrySet()){
//            System.out.println(entry.getKey()+":"+entry.getValue());
//        }
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
                        if (respondBody.code == 200) {
                            finish();
                        }
                    }
                });
    }


    private String[] items = {"周一至周五", "每天"};
    private int currentIndex = 0;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.chooseType) {
            new AlertDialog.Builder(this)
                    .setSingleChoiceItems(items, currentIndex, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentIndex = which;
                            if (which == 0)
                                type = "0";
                            else
                                type = "1";
                            typeName.setText(type.equals("0") ? "周一至周五" : "每天");
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }
}
