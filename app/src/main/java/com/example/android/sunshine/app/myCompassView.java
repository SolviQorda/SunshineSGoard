package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * TODO: document your custom view class.
 */
public class myCompassView extends View {

    private float direction;

    public myCompassView(Context context) {
        super(context);
    }
    public myCompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public myCompassView(Context context, AttributeSet attrs, int defaultStyle){
        super(context, attrs, defaultStyle);
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec){
        int hSpecMode = MeasureSpec.getMode(hMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(hMeasureSpec);

        if(hSpecMode == MeasureSpec.EXACTLY){
            int myHeight = hSpecSize;
        } else if (hMeasureSpec == MeasureSpec.AT_MOST) {
            //wrap content
        }

        int wSpecMode = MeasureSpec.getMode(wMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(wMeasureSpec);

        if(wSpecMode == MeasureSpec.EXACTLY) {
            int myWidth = wSpecSize;
        } else if(wSpecMode == MeasureSpec.AT_MOST) {
            //wrap content
        }

        setMeasuredDimension(wSpecSize, hSpecSize);
    }

    @Override
    protected void onDraw(Canvas canvas){

        int h = getMeasuredHeight();
        int w = getMeasuredWidth();
        int r;
        if (w > h)
        {
            r = h/2;
        } else {
            r = w/2;
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);
        paint.setColor(getResources().getColor(R.color.light_pink));

        canvas.drawCircle(w/2, h/2, r, paint);

        paint.setColor(getResources().getColor(R.color.sunshine_dark_blue));
        canvas.drawLine(
                    w/2,
                    h/2,
                (float) (w/2 + r * Math.sin(-direction)),
                (float) (h/2 - r * Math.cos(-direction)),
                paint);


    }

    public void update(float dir)
    {


        direction = dir * ((float) Math.PI / 180);

        invalidate();
    }

    public void setAccessibilityManager() {
        AccessibilityManager mAccessibilityManager = (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (mAccessibilityManager.isEnabled()) {
            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent ev) {

        // TODO: get this to take the actual direction.
        ev.getText().add("West");
        return true;
    }


}