package net.suntrans.ebuilding.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.AreaEntity;
import net.suntrans.ebuilding.bean.SampleResult;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.suntrans.ebuilding.R.id.nameEn;

/**
 * Created by Administrator on 2017/8/16.
 */

public class AddAreaActivity extends BasedActivity implements DialogInterface.OnDismissListener {

    private Spinner spinner;
    private List<AreaEntity.AreaFloor> floor;
    private List<String> floorNames;
    private ArrayAdapter adapter;
    private TextView name;
    private  LoadingDialog dialog;
    private Subscription subscribe;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area2);
        initToolBar();
        spinner = (Spinner) findViewById(R.id.spinner);
        floorNames = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.item_spinner,R.id.name,floorNames);
        name = (TextView) findViewById(R.id.name);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long ids) {
                if (position==0){
                    id=null;
                    return;
                }
                id = floor.get(position-1).id+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("添加区域");
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
                .subscribe(new Subscriber<AreaEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(AreaEntity result) {
                        floor = result.data.lists;
                        floorNames.clear();
                        floorNames.add("-----请选择楼层-----");
                        for (AreaEntity.AreaFloor e :
                                result.data.lists ) {
                            floorNames.add(e.name);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void addArea(View view) {
        String name1  = name.getText().toString();
        if (TextUtils.isEmpty(name1)){
            UiUtils.showToast("请输入名称");
            return;
        }

        if (TextUtils.isEmpty(id)){
            UiUtils.showToast("请选择楼层");
            return;
        }

        if (dialog==null){
            dialog = new LoadingDialog(this);
            dialog.setWaitText("请稍后");
            dialog.setOnDismissListener(this);
        }
        dialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("name",name1);
        map.put("show_sort","1");
        map.put("house_id",id);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + ":" + value);
        }

        subscribe = RetrofitHelper.getApi().addArea(map)
                .compose(this.<SampleResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SampleResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        e.printStackTrace();
                        UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SampleResult addResult) {
                        dialog.dismiss();
                        if (addResult.getCode() == 200) {
                            UiUtils.showToast("添加成功");
                            finish();
                        } else {
                            UiUtils.showToast("添加失败");
                        }

                    }
                });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        subscribe.unsubscribe();
    }
}
