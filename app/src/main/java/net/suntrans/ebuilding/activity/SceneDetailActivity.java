package net.suntrans.ebuilding.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.ControlEntity;
import net.suntrans.ebuilding.bean.SampleResult;
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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.suntrans.ebuilding.utils.UiUtils.getContext;

/**
 * Created by Looney on 2017/7/21.
 */

public class SceneDetailActivity extends BasedActivity implements View.OnClickListener, DialogInterface.OnDismissListener {
    private RecyclerView recyclerView;
    private String title;
    private String imgurl;
    private String id;
    private List<SceneChannelResult.SceneChannel> datas = new ArrayList<>();
    private LoadingDialog dialog;
    private MyAdapter adapter1;
    private Observable<ControlEntity> conOb;
    private TextView tips;
    private ImageView banner;
    private Subscription subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_detail);
        tips = (TextView) findViewById(R.id.tips);

        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");


        findViewById(R.id.menu).setOnClickListener(this);
        TextView txTitle = (TextView) findViewById(R.id.title);
        txTitle.setText(title);
        imgurl = getIntent().getStringExtra("imgurl");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter1 = new MyAdapter(R.layout.item_scene_detail, datas);
        recyclerView.setAdapter(adapter1);

        banner = (ImageView) findViewById(R.id.banner);
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu) {
            showPopupMenu();
        }
    }



    class MyAdapter extends BaseQuickAdapter<SceneChannelResult.SceneChannel, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<SceneChannelResult.SceneChannel> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneChannelResult.SceneChannel item) {
            helper.setText(R.id.name, item.name);
            AppCompatCheckBox compatCheckBox = helper.getView(R.id.checkbox);
            compatCheckBox.setChecked(item.cmd.equals("1") ? true : false);
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
//                        System.out.println(result.data.total);
                        datas.clear();
                        datas.addAll(result.data.lists);
                        adapter1.notifyDataSetChanged();
                        Glide.with(SceneDetailActivity.this)
                                .load(result.data.img_banner)
                                .placeholder(R.drawable.banner_xiawucha)
                                .centerCrop()
                                .into(banner);
                        LogUtil.i("场景动作的数量：" + datas.size());
                        if (datas.size() != 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            tips.setVisibility(View.GONE);

                        } else {
                            tips.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }


    private boolean canExcute = true;

    public void excute(View view) {

        if (dialog == null) {
            dialog = new LoadingDialog(this, R.style.loading_dialog);
            dialog.setCancelable(false);
        }
        dialog.setWaitText("请稍后");
        dialog.show();
        if (!canExcute) {
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
                canExcute = true;
                dialog.dismiss();
                if (e instanceof HttpException) {
                    if (e.getMessage() != null) {
                        if (e.getMessage().equals("HTTP 401 Unauthorized")) {
                            UiUtils.showToast("您的登录状态已失效,请重新登录");
                        } else {
                            UiUtils.showToast("服务器错误");

                        }
                    } else {
                        UiUtils.showToast("服务器错误");
                    }
                }
                if (e instanceof SocketTimeoutException) {
                    UiUtils.showToast("连接超时");
                }
            }

            @Override
            public void onNext(ControlEntity data) {
                canExcute = true;
                dialog.dismiss();

                if (data.code == 200) {
                    UiUtils.showToast("成功!");
                } else {
                    UiUtils.showToast("执行失败,请稍后再试");

                }
            }
        });
        canExcute = false;


    }


    private PopupWindow mPopupWindow;

    private void showPopupMenu() {
        if (mPopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_scenedetail_menu, null);
            view.findViewById(R.id.addChannel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    startActivity(new Intent(SceneDetailActivity.this,AddSceneChannelActivity.class));
                }
            });
            view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    AlertDialog.Builder alertDialog = UiUtils.getAlertDialog(SceneDetailActivity.this, "是否删除该场景?");
                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteScene(id);
                        }
                    }).setNegativeButton("取消",null).create().show();
                }
            });
            mPopupWindow = new PopupWindow(getContext());
            mPopupWindow.setContentView(view);
            mPopupWindow.setHeight(UiUtils.dip2px(120));
            mPopupWindow.setWidth(UiUtils.dip2px(155));
            mPopupWindow.setAnimationStyle(R.style.TRM_ANIM_STYLE);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
//                    setBackgroundAlpha(0.75f, 1f, 300);
                }
            });
        }

        if (!mPopupWindow.isShowing()) {
            int width = UiUtils.getDisplaySize(getContext())[0];
            mPopupWindow.showAtLocation(recyclerView, Gravity.NO_GRAVITY, width, UiUtils.dip2px(24));
//            mPopupWindow.showAsDropDown(menu);
//            setBackgroundAlpha(1f, 0.75f, 240);
        }

    }

    private void deleteScene(String id) {
        if (dialog==null){
            dialog = new LoadingDialog(this);
            dialog.setWaitText("请稍后");
            dialog.setOnDismissListener(this);
        }
        dialog.show();
        subscribe = RetrofitHelper.getApi().deleteScene(id)
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
                        UiUtils.showToast("连接服务器失败");

                    }

                    @Override
                    public void onNext(SampleResult addResult) {
                        dialog.dismiss();
                        if (addResult.getCode()==200){
                          new AlertDialog.Builder(SceneDetailActivity.this)
                                  .setMessage("删除成功")
                                  .setCancelable(false)
                                  .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int which) {
                                          finish();
                                      }
                                  }).create().show();
                        }else {
                            UiUtils.showToast("删除失败");
                        }

                    }
                });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        subscribe.unsubscribe();
    }
}
