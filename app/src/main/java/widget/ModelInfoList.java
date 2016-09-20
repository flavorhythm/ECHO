package widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import util.MetricCalcs;

/**
 * Created by ZYuki on 9/15/2016.
 */
public class ModelInfoList extends ListView {
    private static final int LIST_HEIGHT = 320;
    private static final int MAX_TRANS_Y = MetricCalcs.dpToPixels(20);

    private int screenWidth = MetricCalcs.getScreenWidth();
    private int screenHeight = MetricCalcs.getScreenHeight() - MetricCalcs.getStatusBarHeight();

    private int listHeight = MetricCalcs.dpToPixels(LIST_HEIGHT);
    private int listTop = screenHeight - listHeight;

    private ValueAnimator slideDownAnim, slideUpAnim;
    private int translateY = 0;

    public ModelInfoList(Context context) {
        this(context, null);
    }

    public ModelInfoList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ModelInfoList(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ModelInfoList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        updateContentBounds();
    }

    public void receiveAnimationValue(int value) {
        translateY = value / 3;

        updateContentBounds();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int resolvedWidth = View.resolveSize(getDesiredWith(), widthMeasureSpec);
        int resolvedHeight = View.resolveSize(getDesiredHeight(), heightMeasureSpec);

        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void updateContentBounds() {
        setTop(listTop - translateY);
        setBottom(listTop + listHeight - translateY);
    }

    private int getDesiredWith() {return screenWidth;}
    private int getDesiredHeight() {return listHeight;}
}
