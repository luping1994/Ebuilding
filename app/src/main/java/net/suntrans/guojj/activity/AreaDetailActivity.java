package net.suntrans.guojj.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.guojj.R;
import net.suntrans.guojj.api.RetrofitHelper;
import net.suntrans.guojj.bean.SampleResult;
import net.suntrans.guojj.fragment.area.AreaDeailFragment;
import net.suntrans.guojj.rx.BaseSubscriber;
import net.suntrans.guojj.utils.UiUtils;
import net.suntrans.guojj.views.LoadingDialog;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.design.widget.TabLayout.GRAVITY_FILL;
import static android.support.design.widget.TabLayout.MODE_FIXED;
import static net.suntrans.guojj.utils.UiUtils.getContext;

/**
 * Created by Looney on 2017/5/2.
 */

public class AreaDetailActivity extends BasedActivity implements View.OnClickListener, DialogInterface.OnDismissListener {


    private ViewPager viewPager;
    private String id;
    private Subscription subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomdetail);

        initView();
    }


    private void initView() {

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("name"));

        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.setTabGravity(GRAVITY_FILL);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        findViewById(R.id.menu).setOnClickListener(this);

        id = getIntent().getStringExtra("id");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu) {
            showPopupMenu();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        subscribe.unsubscribe();
    }

    class PagerAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;

        private final String[] title = new String[]{"照明", "插座"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            AreaDeailFragment fragment =  AreaDeailFragment.newInstance("1",getIntent().getStringExtra("id"));
            AreaDeailFragment fragment2 =  AreaDeailFragment.newInstance("2",getIntent().getStringExtra("id"));
            fragments =new Fragment[]{fragment,fragment2};
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }


    private PopupWindow mPopupWindow;

    private void showPopupMenu() {
        if (mPopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_areadetail_menu, null);
            view.findViewById(R.id.addChannel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    Intent intent = new Intent(AreaDetailActivity.this, AddAreaChannelActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            });
            view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    AlertDialog.Builder alertDialog = UiUtils.getAlertDialog(AreaDetailActivity.this, "是否删除该区域?");
                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteArea(id);
                        }
                    }).setNegativeButton("取消", null).create().show();
                }
            });
            mPopupWindow = new PopupWindow(getContext());
            mPopupWindow.setContentView(view);
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
            int offset = UiUtils.dip2px(38);
            mPopupWindow.showAtLocation(viewPager, Gravity.NO_GRAVITY, width - (int) getResources().getDimension(R.dimen.pouopwindon_offset), offset);
//            mPopupWindow.showAsDropDown(menu);
//            setBackgroundAlpha(1f, 0.75f, 240);
        }

    }

    private LoadingDialog dialog;


    private void deleteArea(String id) {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("请稍后");
            dialog.setOnDismissListener(this);
        }
        dialog.show();
        subscribe = RetrofitHelper.getApi().deleteArea(id)
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
                        e.printStackTrace();
                        super.onError(e);

                    }

                    @Override
                    public void onNext(SampleResult addResult) {
                        dialog.dismiss();
                        if (addResult.getCode() == 200) {
                            new AlertDialog.Builder(AreaDetailActivity.this)
                                    .setMessage("删除成功")
                                    .setCancelable(false)
                                    .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).create().show();
                        } else {
                            UiUtils.showToast(addResult.getMsg());
                        }

                    }
                });
    }

}


