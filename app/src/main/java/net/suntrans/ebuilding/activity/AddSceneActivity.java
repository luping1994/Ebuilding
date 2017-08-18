package net.suntrans.ebuilding.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.SampleResult;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.EditView;
import net.suntrans.ebuilding.views.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */

public class AddSceneActivity extends BasedActivity implements DialogInterface.OnDismissListener {

    private EditView name;
    private EditView nameEn;
    private LoadingDialog dialog;
    private Subscription subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scene);

        name = (EditView) findViewById(R.id.name);
        nameEn = (EditView) findViewById(R.id.nameEn);
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addScene(View view) {
        String name1  = name.getText().toString();
        String nameEn1 = nameEn.getText().toString();
        if (TextUtils.isEmpty(name1)){
            UiUtils.showToast("请输入场景名称");
            return;
        }
        if (TextUtils.isEmpty(nameEn1)) {
            UiUtils.showToast("请输入场景英文名称");
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
        map.put("name_en",nameEn1);
        map.put("img_banner","123");
        subscribe = RetrofitHelper.getApi().addScene(map)
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
                    }

                    @Override
                    public void onNext(SampleResult addResult) {
                        dialog.dismiss();
                        if (addResult.getCode()==200){
                            UiUtils.showToast("添加成功");
                            finish();
                        }else {
                            UiUtils.showToast("添加失败");
                        }

                    }
                });
    }

    public void changeBg(View view) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        subscribe.unsubscribe();
    }
}
