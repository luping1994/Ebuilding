package net.suntrans.ebuilding.activity;

import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.ControlEntity;
import net.suntrans.ebuilding.bean.SceneChannelResult;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.LoadingDialog;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/21.
 */

public class SceneDetailActivity extends BasedActivity {
    private RecyclerView recyclerView;
    private String title;
    private String imgurl;
    private String id;
    private List<SceneChannelResult.SceneChannel> datas = new ArrayList<>();
    private LoadingDialog dialog;
    private MyAdapter adapter1;
    private Observable<ControlEntity> conOb;
    private TextView tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_detail);
        tips = (TextView) findViewById(R.id.tips);

        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");

        TextView txTitle = (TextView) findViewById(R.id.title);
        txTitle.setText(title);
        imgurl = getIntent().getStringExtra("imgurl");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new MyAdapter(R.layout.item_scene_detail, datas);
        recyclerView.setAdapter(adapter1);
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class MyAdapter extends BaseQuickAdapter<SceneChannelResult.SceneChannel, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<SceneChannelResult.SceneChannel> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneChannelResult.SceneChannel item) {
            helper.setText(R.id.name, item.name);
            AppCompatCheckBox compatCheckBox = helper.getView(R.id.checkbox);
            compatCheckBox.setChecked(item.status.equals("1") ? true : false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();

    }

    private void getData() {
        RetrofitHelper.getApi().getSceneChannel(id)
                .compose(this.<SceneChannelResult>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SceneChannelResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SceneChannelResult result) {
                        System.out.println(result.data.total);
                        datas.clear();
                        datas.addAll(result.data.lists);
                        adapter1.notifyDataSetChanged();
                        LogUtil.i("场景动作的数量：" + datas.size());
                        if (datas.size()!=0){
                            recyclerView.setVisibility(View.VISIBLE);
                            tips.setVisibility(View.GONE);

                        }else {
                            tips.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }


    private boolean canExcute = true;
    public void excute(View view) {

        if (dialog ==null){
            dialog = new LoadingDialog(this,R.style.loading_dialog);
            dialog.setCancelable(false);
        }
        dialog.setWaitText("请稍后");
        dialog.show();
        if (!canExcute){
            UiUtils.showToast("请稍后");
            return;

        }
        if (conOb == null)
            conOb = RetrofitHelper.getApi().switchScene(id)
                    .compose(this.<ControlEntity>bindUntilEvent(ActivityEvent.DESTROY))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());

        conOb.subscribe(new Subscriber<ControlEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                canExcute =true;
                dialog.dismiss();
                if (e instanceof HttpException) {
                    if (e.getMessage() != null) {
                            if (e.getMessage().equals("HTTP 401 Unauthorized")) {
                                UiUtils.showToast("您的登录状态已失效,请重新登录");
                            }else {
                                UiUtils.showToast("服务器错误");

                            }
                    } else {
                        UiUtils.showToast("服务器错误");
                    }
                }
                if (e instanceof SocketTimeoutException){
                    UiUtils.showToast("连接超时");
                }
            }

            @Override
            public void onNext(ControlEntity data) {
                canExcute =true;
                dialog.dismiss();

                if (data.code==200){
                    UiUtils.showToast("成功!");
                }else {
                    UiUtils.showToast("执行失败,请稍后再试");

                }
            }
        });
        canExcute =false;



    }
}
