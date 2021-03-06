package net.suntrans.ebuilding.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.suntrans.ebuilding.utils.UiUtils;


/**
 * Created by Looney on 2017/9/4.
 */

public class DefaultDecoration extends RecyclerView.ItemDecoration {

    private int offsets = UiUtils.dip2px(1);
    private Paint mPaint;
    private int mOffsetsColor = Color.parseColor("#eeeeee");
    private final Rect mBounds = new Rect();
    private  final int padding = UiUtils.dip2px(0);


    public DefaultDecoration(int offsets, int mOffsetsColor) {
        this.offsets = offsets;
        this.mOffsetsColor = mOffsetsColor;
        init();
    }

    public DefaultDecoration() {
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mOffsetsColor);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = offsets;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft()+padding;
        int right = parent.getWidth() - parent.getPaddingRight()-padding;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + offsets;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
        canvas.restore();
    }
}
