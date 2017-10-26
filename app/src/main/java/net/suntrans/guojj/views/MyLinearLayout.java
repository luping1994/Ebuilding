package net.suntrans.guojj.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Looney on 2017/10/26.
 * Des:
 */

public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View view1 = getChildAt(0);
        View view2 = getChildAt(1);
        View view3 = getChildAt(2);
        int view1MeasuredWidth = view1.getMeasuredWidth();
        int view3MeasuredWidth = view3.getMeasuredWidth();

        int widthMode = MeasureSpec.makeMeasureSpec(view1MeasuredWidth > view3MeasuredWidth ? view1MeasuredWidth : view3MeasuredWidth,
                MeasureSpec.EXACTLY);
        int heightMode = MeasureSpec.makeMeasureSpec(view2.getMeasuredHeight(),
                MeasureSpec.EXACTLY);
        measureChild(view2, widthMode, heightMode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
