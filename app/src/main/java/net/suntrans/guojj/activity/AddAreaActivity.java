package net.suntrans.guojj.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.guojj.R;
import net.suntrans.guojj.api.RetrofitHelper;
import net.suntrans.guojj.bean.AreaEntity;
import net.suntrans.guojj.bean.SampleResult;
import net.suntrans.guojj.rx.BaseSubscriber;
import net.suntrans.guojj.utils.ActivityUtils;
import net.suntrans.guojj.utils.UiUtils;
import net.suntrans.guojj.views.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/16.
 */

public class AddAreaActivity extends BasedActivity implements DialogInterface.OnDismissListener {

    private Spinner spinner;
    private List<AreaEntity.AreaFloor> floor;
    private List<String> floorNames;
    private ArrayAdapter adapter;
    private TextView name;
    private LoadingDialog dialog;
    private Subscription subscribe;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area2);
        initToolBar();
        spinner = (Spinner) findViewById(R.id.spinner);
        floorNames = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, R.id.name, floorNames);
        name = (TextView) findViewById(R.id.name);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long ids) {
                if (position == 0) {
                    id = null;
                    return;
                }
                id = floor.get(position - 1).id + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_title_add_area);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        RetrofitHelper.getApi().getHomeHouse()
                .compose(this.<AreaEntity>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AreaEntity>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        e.printStackTrace();
//                        UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(AreaEntity result) {
                        if (result.code == 200) {
                            floor = result.data.lists;
                            floorNames.clear();
                            floorNames.add(getString(R.string.tx_choose_floor));
                            for (AreaEntity.AreaFloor e :
                                    result.data.lists) {
                                floorNames.add(e.name);
                            }
                            adapter.notifyDataSetChanged();
                        } else if (result.code == 401) {
                            ActivityUtils.showLoginOutDialogFragmentToActivity(getSupportFragmentManager(), "Alert");
                        }else {
                            UiUtils.showToast(result.msg);
                        }

                    }
                });
    }

    public void addArea(View view) {
        String name1 = name.getText().toString();
        if (TextUtils.isEmpty(name1)) {
            UiUtils.showToast(getString(R.string.tips_please_enter_name));
            return;
        }

        if (TextUtils.isEmpty(id)) {
            UiUtils.showToast(getString(R.string.tips_please_choose_floor));
            return;
        }

        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText(getString(R.string.tips_please_waiting));
            dialog.setOnDismissListener(this);
        }
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("name", name1);
        map.put("show_sort", "1");
        map.put("house_id", id);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
//            System.out.println(key + ":" + value);
        }

        subscribe = RetrofitHelper.getApi().addArea(map)
                .compose(this.<SampleResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<SampleResult>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        super.onError(e);
                    }

                    @Override
                    public void onNext(SampleResult addResult) {
                        dialog.dismiss();
                        if (addResult.getCode() == 200) {
                            UiUtils.showToast(getString(R.string.tips_add_success));
                            finish();
                        } else {
                            UiUtils.showToast(addResult.getMsg());
                        }

                    }
                });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        subscribe.unsubscribe();
    }
}
