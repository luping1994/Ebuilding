package net.suntrans.ebuilding.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.App;
import net.suntrans.ebuilding.MainActivity;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.activity.AboutActivity;
import net.suntrans.ebuilding.activity.ChangePassActivity;
import net.suntrans.ebuilding.activity.DeviceManagerActivity;
import net.suntrans.ebuilding.activity.LoginActivity;
import net.suntrans.ebuilding.activity.QuestionActivity;
import net.suntrans.ebuilding.activity.YichangActivity;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.SampleResult;
import net.suntrans.ebuilding.bean.UserInfo;
import net.suntrans.ebuilding.bean.YichangEntity;
import net.suntrans.ebuilding.fragment.base.BasedFragment;
import net.suntrans.ebuilding.fragment.base.LazyLoadFragment;
import net.suntrans.ebuilding.fragment.din.ChangeNameDialogFragment;
import net.suntrans.ebuilding.fragment.din.UpLoadImageFragment;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.StatusBarCompat;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.GlideRoundTransform;
import net.suntrans.ebuilding.views.LoadingDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.attr.y;
import static net.suntrans.ebuilding.utils.UiUtils.getContext;

/**
 * Created by Looney on 2017/7/20.
 */

public class PerCenFragment extends LazyLoadFragment implements View.OnClickListener, ChangeNameDialogFragment.ChangeNameListener, UpLoadImageFragment.onUpLoadListener {
    TextView name;
    private ImageView avatar;
    private TextView bagde;
    private RequestManager glideRequest;
    private List<YichangEntity.DataBean.ListsBean> lists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_percen, container, false);
        View statusBar = view.findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarHeight = StatusBarCompat.getStatusBarHeight(getContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            params.height = statusBarHeight;
            statusBar.setLayoutParams(params);
            statusBar.setVisibility(View.VISIBLE);
        } else {
            statusBar.setVisibility(View.GONE);

        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setListener(view);
        avatar = (ImageView) view.findViewById(R.id.img);
        bagde = (TextView) view.findViewById(R.id.bagde);
        glideRequest = Glide.with(this);

    }

    private void setListener(View view) {
        view.findViewById(R.id.RLAbout).setOnClickListener(this);
        view.findViewById(R.id.RLDevice).setOnClickListener(this);
//        view.findViewById(R.id.RLHelp).setOnClickListener(this);
        view.findViewById(R.id.RLModify).setOnClickListener(this);
        view.findViewById(R.id.RLQues).setOnClickListener(this);
        view.findViewById(R.id.loginOut).setOnClickListener(this);
        view.findViewById(R.id.titleHeader).setOnClickListener(this);
        view.findViewById(R.id.RLtishi).setOnClickListener(this);

        name = (TextView) view.findViewById(R.id.name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RLQues:
                startActivity(new Intent(getActivity(), QuestionActivity.class));

                break;
            case R.id.RLModify:
                startActivity(new Intent(getActivity(), ChangePassActivity.class));

                break;
            case R.id.RLDevice:
                startActivity(new Intent(getActivity(), DeviceManagerActivity.class));
                break;
//            case R.id.RLHelp:
//                break;
            case R.id.RLAbout:
                startActivity(new Intent(getActivity(), AboutActivity.class));

                break;
            case R.id.RLtishi:
                startActivity(new Intent(getActivity(), YichangActivity.class));

                break;
            case R.id.loginOut:
                new AlertDialog.Builder(getContext())
                        .setMessage("是否退出当前账号?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                App.getSharedPreferences().edit().clear().commit();
                                ((MainActivity) getActivity()).killAll();
                                UiUtils.showToast("已退出当前账号");
                                startActivity(new Intent(getActivity(), LoginActivity.class));

                            }
                        }).setNegativeButton("取消", null).create().show();
                break;
            case R.id.titleHeader:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                break;
        }
    }

    private String[] items2 = {"更改名称", "更换头像"};

    @Override
    public void onResume() {
        super.onResume();
        getInfo();
        getBadgeCount();
    }

    private void checkBadge() {
//        int yichangCount = App.getSharedPreferences().getInt("yichangCount", 0);
//        if (lists==null){
//            bagde.setVisibility(View.INVISIBLE);
//
//        }else {
//            if (lists.size()!=yichangCount){
//                bagde.setVisibility(View.VISIBLE);
//            }else {
//                bagde.setVisibility(View.INVISIBLE);
//
//            }
//        }

    }

    private void getInfo() {
        RetrofitHelper.getApi()
                .getUserInfo()
                .compose(this.<UserInfo>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserInfo info) {

                        if (info != null) {
                            if (info.code == 200) {
                                name.setText(info.data.nickname);
                                App.getSharedPreferences().edit().putString("user_id", info.data.id)
                                        .putString("nikename", info.data.nickname)
                                        .putString("touxiang", info.data.avatar_url)
                                        .commit();
                                LogUtil.i("http://tit.suntrans-cloud.com" + info.data.avatar_url);
                                glideRequest
                                        .load("http://tit.suntrans-cloud.com" + info.data.avatar_url)
                                        .transform(new GlideRoundTransform(getActivity(),UiUtils.dip2px(16)))
                                        .override(UiUtils.dip2px(33), UiUtils.dip2px(33))
                                        .placeholder(R.drawable.user_white)
                                        .into(avatar);
                            } else {

                            }
                        } else {
                        }
                    }
                });
    }


    ChangeNameDialogFragment fragment2;

    private void showChangedNameDialog() {
        fragment2 = (ChangeNameDialogFragment) getChildFragmentManager().findFragmentByTag("ChangeNameDialogFragment");
        if (fragment2 == null) {
            fragment2 = ChangeNameDialogFragment.newInstance("更改昵称");
            fragment2.setCancelable(true);
            fragment2.setListener(this);
        }
        fragment2.show(getChildFragmentManager(), "ChangeNameDialogFragment");
    }

    UpLoadImageFragment fragment;

    private void showBottomSheet() {
        fragment = (UpLoadImageFragment) getChildFragmentManager().findFragmentByTag("bottomSheetDialog");
        if (fragment == null) {
            fragment = UpLoadImageFragment.newInstance("2");
            fragment.setCancelable(true);
            fragment.setLoadListener(this);
        }
        fragment.show(getChildFragmentManager(), "bottomSheetDialog");

    }

    @Override
    public void changeName(String name) {
        upDate(name, null);
    }

    @Override
    public void uploadImageSuccess(String path) {
        upDate(null, path);
    }

    private LoadingDialog dialog;

    private void upDate(String name, String path) {
        if (dialog == null) {
            dialog = new LoadingDialog(getContext());
            dialog.setWaitText("请稍后");
        }
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(name)) {
            map.put("nickname", name);
        }
        if (!TextUtils.isEmpty(path)) {
            map.put("avatar_url", path);
        }
        LogUtil.i("percenfragment:" + path);
        ((MainActivity) getActivity()).addSubscription(RetrofitHelper.getApi().updateProfile(map), new Subscriber<SampleResult>() {
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
                    getInfo();
                } else {
                    UiUtils.showToast(result.getMsg());
                }
            }
        });
    }

    private void getBadgeCount() {
        ((MainActivity) getActivity()).   addSubscription(RetrofitHelper.getApi().getYichang(), new Subscriber<YichangEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(YichangEntity o) {
                if (o.getCode()==200){
                    lists = o.getData().getLists();
                    int yichangCount = App.getSharedPreferences().getInt("yichangCount", 0);
//                    if (lists.size()!=yichangCount){
//                        bagde.setVisibility(View.VISIBLE);
//                    }else {
//                        bagde.setVisibility(View.INVISIBLE);
//                    }
                    App.getSharedPreferences().edit().putInt("yichangCount",lists.size()).commit();
                }else {
                }
            }
        });
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        checkBadge();
    }
}
