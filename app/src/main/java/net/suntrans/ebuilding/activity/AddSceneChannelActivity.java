package net.suntrans.ebuilding.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.AddSceneChannelResult;
import net.suntrans.ebuilding.bean.AreaEntity;
import net.suntrans.ebuilding.fragment.din.AddSceneChannelFragment;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.LoadingDialog;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */

public class AddSceneChannelActivity extends BasedActivity implements DialogInterface.OnDismissListener {


    private AddSceneChannelFragment fragment;
    private LoadingDialog dialog;
    private Subscription subscribe;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scene_channel);
        setUpToolBar();
        init();
        id = getIntent().getStringExtra("scene_id");

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
        fragment = new AddSceneChannelFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tijiao, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.commit) {
            List<AreaEntity.AreaFloor> datas = fragment.getDatas();
            StringBuilder channel_id = new StringBuilder();
            StringBuilder cmd = new StringBuilder();
            int count = 0;
            for (int i = 0; i < datas.size(); i++) {
                for (int j = 0; j < datas.get(i).sub.size(); j++) {
                    for (int k = 0; k < datas.get(i).sub.get(j).lists.size(); k++) {
                        AreaEntity.Channel channel = datas.get(i).sub.get(j).lists.get(k);
                        if (channel.isChecked) {
                            count++;
                            channel_id.append(channel.channel_id)
                                    .append(",");
                            cmd.append("0")
                                    .append(",");
                        }
                    }
                }
            }
            if (count == 0) {
                UiUtils.showToast("您没有选择任何设备");
                return true;
            }
            String ci = channel_id.toString();
            String cd = cmd.toString();

            final String a = ci.substring(0, ci.length() - 1);
            final String b = cd.substring(0, cd.length() - 1);
            new AlertDialog.Builder(this)
                    .setMessage("是否添加" + count + "个设备")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            add(id, a, b);
                        }
                    }).setNegativeButton("取消",null).create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void add(String id, String channel_id, String cmd) {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("请稍后");
            dialog.setOnDismissListener(this);
        }
        dialog.show();
        subscribe = RetrofitHelper.getApi().addChannel(id, channel_id, cmd)
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
