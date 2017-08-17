package net.suntrans.ebuilding.fragment;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.App;
import net.suntrans.ebuilding.MainActivity;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.activity.AddSceneActivity;
import net.suntrans.ebuilding.adapter.DiningPagerAdapter;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.SampleResult;
import net.suntrans.ebuilding.bean.UserInfo;
import net.suntrans.ebuilding.fragment.din.ChangeNameDialogFragment;
import net.suntrans.ebuilding.fragment.din.SceneFragment;
import net.suntrans.ebuilding.fragment.din.UpLoadImageFragment;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.StatusBarCompat;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.design.widget.TabLayout.GRAVITY_FILL;
import static android.support.design.widget.TabLayout.MODE_FIXED;
import static net.suntrans.ebuilding.R.id.banner;
import static net.suntrans.ebuilding.R.id.tabLayout;

/**
 * Created by Looney on 2017/7/20.
 */

public class DiningRoomFragment extends RxFragment implements View.OnClickListener, ChangeNameDialogFragment.ChangeNameListener, UpLoadImageFragment.onUpLoadListener {

    private ImageView menu;
    private ImageView banner;
    private String[] items2 = { "更换背景"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dining, container, false);
        setHasOptionsMenu(true);
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
        initView(view);
    }

    private void initView(View view) {

//        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
//        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
//        tabLayout.setTabMode(MODE_FIXED);
//        tabLayout.setTabGravity(GRAVITY_FILL);
        SceneFragment fragment1 = new SceneFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.content, fragment1).commit();
//        viewPager.setAdapter();
//        tabLayout.setupWithViewPager(viewPager);
        menu = (ImageView) view.findViewById(R.id.menu);
        menu.setOnClickListener(this);
//        banner = (ImageView) view.findViewById(R.id.banner);
//        banner.setOnClickListener(this);
        getInfo();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu) {
            showPopupMenu();
        }
        if (v.getId() == R.id.banner) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setItems(items2, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            showBottomSheet();
                            break;
                    }
                }
            });
            builder.create().show();
        }
    }


    private PopupWindow mPopupWindow;

    private void showPopupMenu() {
        if (mPopupWindow == null) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_dining_menu, null);
            RelativeLayout ll = (RelativeLayout) view.findViewById(R.id.content);
            ViewCompat.setElevation(ll, 20);
            view.findViewById(R.id.name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), AddSceneActivity.class));
                    mPopupWindow.dismiss();
                }
            });
            mPopupWindow = new PopupWindow(getContext());
            mPopupWindow.setContentView(view);
            mPopupWindow.setHeight(UiUtils.dip2px(60));
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
            mPopupWindow.showAtLocation(menu, Gravity.NO_GRAVITY, width, UiUtils.dip2px(24));
//            mPopupWindow.showAsDropDown(menu);
//            setBackgroundAlpha(1f, 0.75f, 240);
        }

    }

    private void setBackgroundAlpha(float from, float to, int duration) {
        final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                getActivity().getWindow().setAttributes(lp);
            }
        });
        animator.start();
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
                                App.getSharedPreferences().edit().putString("user_id", info.data.id).commit();
                                Glide.with(getContext())
                                        .load(info.data.img_url.get(0))
                                        .placeholder(R.drawable.banner_xiawucha)
                                        .crossFade()
                                        .override(UiUtils.getDisplaySize(getContext())[0], UiUtils.dip2px(217))
                                        .into(banner);
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
            fragment2 = ChangeNameDialogFragment.newInstance("");
            fragment2.setCancelable(true);
            fragment2.setListener(this);
        }
        fragment2.show(getChildFragmentManager(), "ChangeNameDialogFragment");
    }

    UpLoadImageFragment fragment;

    private void showBottomSheet() {
        fragment = (UpLoadImageFragment) getChildFragmentManager().findFragmentByTag("bottomSheetDialog");
        if (fragment == null) {
            fragment = UpLoadImageFragment.newInstance("1");
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
            map.put("img_url", path);
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


}

