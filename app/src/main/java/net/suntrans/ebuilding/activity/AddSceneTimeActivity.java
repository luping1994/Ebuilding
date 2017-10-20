package net.suntrans.ebuilding.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.RespondBody;
import net.suntrans.ebuilding.bean.SceneTimeResult;
import net.suntrans.ebuilding.rx.BaseSubscriber;
import net.suntrans.ebuilding.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/9/26.
 */

public class AddSceneTimeActivity extends BasedActivity implements View.OnClickListener {

    private String commitType;
    private String user_defined;
    private TimePicker picker;
    private TextView typeName;
    private boolean[] itemStatus = new boolean[]{false, false, false, false, false, false, false};
    ;
    private SceneTimeResult result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_timeing_set);
        initToolBar();
        init();

    }


    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getIntent().getStringExtra("commitType").equals("set"))
            toolbar.setTitle("设置定时");
        else
            toolbar.setTitle("添加定时");

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
        id = getIntent().getStringExtra("id");
        commitType = getIntent().getStringExtra("commitType");
        result = getIntent().getParcelableExtra("data");
        if (result != null) {
            if (result.type != null) {
                try {
                    if (result.user_defined != null) {
                        weeks = new ArrayList<>(Arrays.asList(result.user_defined.split(",")));
                        updateRepetText(Arrays.asList(result.user_defined.split(",")));
                    } else
                        typeName.setText("无");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (result.user_defined != null) {

                    itemStatus = new boolean[]{result.user_defined.contains("1"),
                            result.user_defined.contains("2"),
                            result.user_defined.contains("3"),
                            result.user_defined.contains("4"),
                            result.user_defined.contains("5"),
                            result.user_defined.contains("6"),
                            result.user_defined.contains("7")};
                }
            }

            if (result.timer != null) {
                picker.setCurrentHour(Integer.valueOf(result.timer.split(":")[0]));
                picker.setCurrentMinute(Integer.valueOf(result.timer.split(":")[1]));
                timer = result.timer;
            }
            if (result.status != null) {
                status = result.status;
            }

        }

        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timer = UiUtils.pad(hourOfDay) + ":" + UiUtils.pad(minute);
                System.out.println(timer);
            }
        });

        Button button = (Button) findViewById(R.id.delete);
        if (commitType.equals("add")) {
            button.setVisibility(View.INVISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);
        }


    }

    private void updateRepetText(List<String> weeks) {
        if (weeks == null || weeks.size() == 0) {
            typeName.setText("无");
            user_defined = "";
            return;
        }
        System.out.println(weeks);
        Collections.sort(weeks);
        StringBuilder user_defined1 = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (String s : weeks) {
            user_defined1.append(s)
                    .append(",");

            sb.append(getString(R.string.week));
            if (s.equals("7"))
                sb.append("日");
            else
                sb.append(s);
            sb.append(",");
        }
        user_defined = user_defined1.substring(0, user_defined1.length() - 1);
        typeName.setText(sb.substring(0, sb.length() - 1));
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
                if (commitType.equals("set"))
                    setTimmer(id, status, timer, user_defined);
                else
                    addTimer(scene_id, status, timer, user_defined);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private String scene_id;
    private String status = "1";
    private String timer;
    private String id;

    private void setTimmer(String id, String status, String timer, String user_defined) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        map.put("timer", timer);
        if (user_defined != null && user_defined.length() != 0) {
            map.put("user_defined", user_defined);
        } else {
            map.put("user_defined", "");
        }

//        map.put("type", type);
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


    private List<String> weeks = new ArrayList<>();

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.chooseType) {
            new AlertDialog.Builder(this)
                    .setMultiChoiceItems(R.array.weekItem, itemStatus, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            int index = which + 1;

                            if (isChecked) {
                                System.out.println("add:" + index);

                                weeks.add(index + "");
                            } else {
                                weeks.remove(index + "");
                                System.out.println("remove:" + index);

                            }
                        }
                    })
                    .setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            updateRepetText(weeks);
                        }
                    })
                    .setNegativeButton(R.string.qvxiao, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
        }
    }


    private void addTimer(final String id, String status, String timer, String user_defined) {
        Map<String, String> map = new HashMap<>();
        map.put("scene_id", id);
        map.put("status", "1");
        if (user_defined != null) {
            map.put("user_defined", user_defined);
        }
        if (timer == null) {
            UiUtils.showToast("请选择时间");
            return;
        }
        map.put("timer", timer);
//        map.put("type", type);
        addSubscription(RetrofitHelper.getApi().addTimmer(map)
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

    public void deleteTimer(View view) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.tips_delete_timmer)
                .setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();

                    }
                }).setNegativeButton(R.string.qvxiao, null).create().show();
    }

    private void delete() {
        addSubscription(RetrofitHelper.getApi().deleteTimmer(id)
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
}
