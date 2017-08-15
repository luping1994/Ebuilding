package com.zaaach.toprightmenu;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Bro0cL on 2016/12/26.
 */
public class TopRightMenu {
    private Activity mContext;
    private PopupWindow mPopupWindow;
    private View content;

    private static final int DEFAULT_HEIGHT = 480;
    private int popHeight ;
    private int popWidth ;
    private boolean showIcon = true;
    private boolean dimBackground = true;
    private boolean needAnimationStyle = true;

    private static final int DEFAULT_ANIM_STYLE = R.style.TRM_ANIM_STYLE;
    private int animationStyle;

    private float alpha = 0.75f;

    public TopRightMenu(Activity context,int resId) {
        this.mContext = context;
        init(resId);
    }

    private void init(int resId) {
        content = LayoutInflater.from(mContext).inflate(resId, null);
        popHeight = content.getHeight();
        popWidth = content.getWidth();
    }

    private PopupWindow getPopupWindow(){
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setContentView(content);
        mPopupWindow.setHeight(popHeight);
        mPopupWindow.setWidth(popWidth);
        if (needAnimationStyle){
            mPopupWindow.setAnimationStyle(animationStyle <= 0 ? DEFAULT_ANIM_STYLE : animationStyle);
        }

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (dimBackground) {
                    setBackgroundAlpha(alpha, 1f, 300);
                }
            }
        });

        return mPopupWindow;
    }


    /**
     * 添加单个菜单
     * @param item
     * @return
     */
    public TopRightMenu addMenuItem(MenuItem item){
        return this;
    }

    /**
     * 添加多个菜单
     * @param list
     * @return
     */
    public TopRightMenu addMenuList(List<MenuItem> list){
        return this;
    }

    /**
     * 是否让背景变暗
     * @param b
     * @return
     */
    public TopRightMenu dimBackground(boolean b){
        this.dimBackground = b;
        return this;
    }

    /**
     * 否是需要动画
     * @param need
     * @return
     */
    public TopRightMenu needAnimationStyle(boolean need){
        this.needAnimationStyle = need;
        return this;
    }

    /**
     * 设置动画
     * @param style
     * @return
     */
    public TopRightMenu setAnimationStyle(int style){
        this.animationStyle = style;
        return this;
    }

    public TopRightMenu setOnMenuItemClickListener(OnMenuItemClickListener listener){
        return this;
    }

    public TopRightMenu showAsDropDown(View anchor){
        showAsDropDown(anchor, 0, 0);
        return this;
    }

    public TopRightMenu showAsDropDown(View anchor, int xoff, int yoff){
        if (mPopupWindow == null){
            getPopupWindow();
        }
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(anchor, xoff, yoff);
            if (dimBackground){
                setBackgroundAlpha(1f, alpha, 240);
            }
        }
        return this;
    }

    private void setBackgroundAlpha(float from, float to, int duration) {
        final WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                mContext.getWindow().setAttributes(lp);
            }
        });
        animator.start();
    }

    public void dismiss(){
        if (mPopupWindow != null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }

    public interface OnMenuItemClickListener{
        void onMenuItemClick(int position);
    }
}
