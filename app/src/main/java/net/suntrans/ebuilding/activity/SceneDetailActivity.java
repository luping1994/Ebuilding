package net.suntrans.ebuilding.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.ControlEntity;
import net.suntrans.ebuilding.bean.SampleResult;
import net.suntrans.ebuilding.bean.SceneChannelResult;
import net.suntrans.ebuilding.bean.SceneEdit;
import net.suntrans.ebuilding.fragment.din.ChangSceneNameDialogFragment;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.LoadingDialog;
import net.suntrans.ebuilding.fragment.din.UpLoadImageFragment;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static java.lang.System.in;
import static java.lang.System.load;
import static net.suntrans.ebuilding.utils.UiUtils.getContext;

/**
 * Created by Looney on 2017/7/21.
 */

public class SceneDetailActivity extends BasedActivity implements View.OnClickListener, DialogInterface.OnDismissListener, ChangSceneNameDialogFragment.ChangeNameListener, UpLoadImageFragment.onUpLoadListener {
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
    private String[] items = {"移除设备", "更改操作"};
    private String[] items2 = {"更改名称", "更换背景"};
    private List<String> con;
    private TextView txTitle;
    private String img_url1;
    private UpLoadImageFragment fragment;
    private ChangSceneNameDialogFragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_detail);
        tips = (TextView) findViewById(R.id.tips);

        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");

        con = new ArrayList<>();
        con.add(getString(R.string.channel_choose_close));
        con.add(getString(R.string.channel_choose_open));
        findViewById(R.id.menu).setOnClickListener(this);
        txTitle = (TextView) findViewById(R.id.title);
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
        Glide.with(this)
                .load(imgurl)
                .crossFade()
                .override(UiUtils.getDisplaySize(this)[0], UiUtils.dip2px(217))
                .into(banner);

        banner.setOnClickListener(this);
        adapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SceneDetailActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                showDeleteDialog(datas.get(position).id);

                                break;
                            case 1:
                                OptionsPickerView pickerView1 = null;
                                if (pickerView1 == null) {
                                    pickerView1 = new OptionsPickerView.Builder(SceneDetailActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                                        @Override
                                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                                            setSceneChannel(position, options1);
                                        }
                                    })
                                            .setTitleText(getString(R.string.choose_action))
                                            .build();
                                    pickerView1.setPicker(con);
                                }
                                pickerView1.show();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    private void setSceneChannel(int position, int options1) {
        String id = datas.get(position).id;
        String cmd = options1 == 0 ? "0" : "1";
        RetrofitHelper.getApi().setChannel(id, cmd)
                .compose(this.<SampleResult>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SampleResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SampleResult result) {
                        if (result.getCode() == 200) {
                            UiUtils.showToast("修改成功");
                            getData();
                        } else {
                            UiUtils.showToast("服务器错误修改失败");
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu) {
            showPopupMenu();
        }
        if (v.getId() == R.id.banner) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SceneDetailActivity.this);
            builder.setItems(items2, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            showChangedNameDialog();
                            break;
                        case 1:
                            showBottomSheet();
                            break;
                    }
                }
            });
            builder.create().show();
        }
    }

    private void showBottomSheet() {
        fragment = (UpLoadImageFragment) getSupportFragmentManager().findFragmentByTag("bottomSheetDialog");
        if (fragment == null) {
            fragment = UpLoadImageFragment.newInstance("1");
//            Dialog dialog = fragment.getDialog();
//            View view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_sheet, null);
//            dialog.setContentView(view);
            fragment.setCancelable(true);
            fragment.setLoadListener(this);
        }
        fragment.show(getSupportFragmentManager(), "bottomSheetDialog");

    }

    private void showChangedNameDialog() {
        fragment2 = (ChangSceneNameDialogFragment) getSupportFragmentManager().findFragmentByTag("ChangSceneNameDialogFragment");
        if (fragment2 == null) {
            fragment2 = ChangSceneNameDialogFragment.newInstance("更改场景名称");
            fragment2.setCancelable(true);
            fragment2.setListener(this);
        }
        fragment2.show(getSupportFragmentManager(), "ChangSceneNameDialogFragment");
    }

    @Override
    public void changeName(String name, String nameEn) {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("请稍后");
        }
        dialog.show();
        upDate(name, null, nameEn);
    }

    @Override
    public void uploadImageSuccess(String path) {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("请稍后");
        }
        LogUtil.i("path=" + path);
        LogUtil.i("path=");
        dialog.show();
        upDate(null, path, null);
    }

    private void upDate(String name, String path, String nameEn) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        if (!TextUtils.isEmpty(name)) {
            map.put("name", name);
        }
        if (!TextUtils.isEmpty(path)) {
            map.put("img_url", path);
        }
        if (!TextUtils.isEmpty(nameEn)) {
            map.put("name_en", nameEn);
        }
        addSubscription(RetrofitHelper.getApi().updateScene(map), new Subscriber<SampleResult>() {
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
            public void onNext(SampleResult result) {
                dialog.dismiss();
                if (result.getCode() == 200) {
                    UiUtils.showToast("更新成功");
                    getSceneInfo(id);
                } else {
                    UiUtils.showToast(result.getMsg());
                }
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
            compatCheckBox.setChecked(item.cmd.equals("1") ? true : false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        getSceneInfo(id);
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
                        System.out.println(result.code);

                        if (result.code == 500) {
                            new AlertDialog.Builder(SceneDetailActivity.this)
                                    .setMessage("该场景已经被删除")
                                    .setCancelable(false)
                                    .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).create().show();
                            return;
                        }
                        if (result.code == 200) {
                            datas.clear();
                            datas.addAll(result.data.lists);
                            adapter1.notifyDataSetChanged();
                            LogUtil.i("场景动作的数量：" + datas.size());
                            if (datas.size() != 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                tips.setVisibility(View.GONE);

                            } else {
                                tips.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                            }
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
                UiUtils.showToast(data.msg);
                if (data.code == 200) {
                } else if (data.code == 500) {
                    finish();
                } else if (data.code == 101) {

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
                    Intent intent = new Intent(SceneDetailActivity.this, AddSceneChannelActivity.class);
                    intent.putExtra("scene_id", id);
                    startActivity(intent);
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
                    }).setNegativeButton("取消", null).create().show();
                }
            });
            mPopupWindow = new PopupWindow(view, UiUtils.dip2px(120), UiUtils.dip2px(155));
            mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
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
        if (dialog == null) {
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
                        if (addResult.getCode() == 200) {
                            new AlertDialog.Builder(SceneDetailActivity.this)
                                    .setMessage("删除成功")
                                    .setCancelable(false)
                                    .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).create().show();
                        } else {
                            UiUtils.showToast("删除失败");
                        }

                    }
                });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        subscribe.unsubscribe();
    }

    private void showDeleteDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogUtil.i(id);
                delete(id);
            }

        });
        builder.setNegativeButton(R.string.qvxiao, null);
        builder.setMessage("是否将该设备从场景中移除?")
                .create().show();
    }

    private void delete(String id) {
        RetrofitHelper.getApi().deleteChannel(id)
                .compose(this.<SampleResult>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SampleResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SampleResult result) {
                        if (result.getCode() == 200) {
                            UiUtils.showToast("删除成功");
                            getData();
                        } else {
                            UiUtils.showToast("删除失败");
                        }
                    }
                });
    }


    private void getSceneInfo(String id) {
        RetrofitHelper.getApi().getSceneInfo(id)
                .compose(this.<SceneEdit>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SceneEdit>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SceneEdit info) {
                        txTitle.setText(info.getData().getRow().get(0).getName());
                        img_url1 = info.getData().getRow().get(0).getImg_url();
                        System.out.println(img_url1);
                        Glide.with(SceneDetailActivity.this)
                                .load(img_url1)
                                .override(UiUtils.getDisplaySize(SceneDetailActivity.this)[0], UiUtils.dip2px(217))
                                .into(banner);
                    }
                });
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
