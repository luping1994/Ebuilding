package net.suntrans.ebuilding.activity;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Looney on 2017/1/5.
 */
public class BasedActivity extends RxAppCompatActivity implements SlidingPaneLayout.PanelSlideListener {

    public final static List<BasedActivity> mlist = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        initSlideBackClose();

        super.onCreate(savedInstanceState);
        synchronized (mlist) {
            mlist.add(this);
        }

    }

    @Override
    protected void onDestroy() {
        synchronized (mlist) {
            mlist.remove(this);
        }
        super.onDestroy();
    }

    public void killAll() {
        List<BasedActivity> copy;
        synchronized (mlist) {
            copy = new LinkedList<BasedActivity>(mlist);
            for (BasedActivity a :
                    copy) {
                a.finish();
            }
        }
    }


    @Override
    public void finish() {
        super.finish();
    }


    private void initSlideBackClose() {
        if (isSupportSwipeBack()) {
            SlidingPaneLayout slidingPaneLayout = new SlidingPaneLayout(this);
            // 通过反射改变mOverhangSize的值为0，
            // 这个mOverhangSize值为菜单到右边屏幕的最短距离，
            // 默认是32dp，现在给它改成0
            try {
                Field overhangSize = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
                overhangSize.setAccessible(true);
                overhangSize.set(slidingPaneLayout, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            slidingPaneLayout.setPanelSlideListener(this);
            slidingPaneLayout.setSliderFadeColor(getResources()
                    .getColor(android.R.color.transparent));

            // 左侧的透明视图
            View leftView = new View(this);
            leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            slidingPaneLayout.addView(leftView, 0);

            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();


            // 右侧的内容视图
            ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
            decorChild.setBackgroundColor(getResources()
                    .getColor(android.R.color.white));
            decorView.removeView(decorChild);
            decorView.addView(slidingPaneLayout);

            // 为 SlidingPaneLayout 添加内容视图
            slidingPaneLayout.addView(decorChild, 1);
        }
    }

    protected boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(View panel) {
        finish();
    }

    @Override
    public void onPanelClosed(View panel) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter, android.support.v7.appcompat.R.anim.abc_popup_exit);
        }
        return super.onOptionsItemSelected(item);
    }
}
