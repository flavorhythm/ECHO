package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.echo_usa.echo.R;

import util.MetricCalc;

/**
 * Created by ZYuki on 9/15/2016.
 */
public class ModelListView extends ListView {
    final int SCREEN_WIDTH = MetricCalc.getScreenWidth();
    final int SCREEN_HEIGHT = MetricCalc.getScreenHeight() - MetricCalc.getStatusBarHeight();
    final int LIST_HEIGHT = MetricCalc.dpToPxByVal(getContext().getResources().getInteger(R.integer.model_list_height));
    final int LIST_TOP = SCREEN_HEIGHT - LIST_HEIGHT;

    private int translateY = 0;

    private GestureDetectorCompat mDetector;

    public ModelListView(Context context) {
        this(context, null);
    }

    public ModelListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ModelListView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ModelListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mDetector = new GestureDetectorCompat(getContext(), new CustomGestureDetector());

        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_list));

        updateContentBounds();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            updateContentBounds();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean returned = super.dispatchTouchEvent(ev);
//        Log.v("dispatch touch", String.valueOf(super.dispatchTouchEvent(ev)));
        Log.d("touch", "list dispatch " + String.valueOf(returned));
        return returned;
//        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean returned = super.onTouchEvent(ev);
        Log.d("touch", "list onTouch " + String.valueOf(returned));
//        mDetector.onTouchEvent(ev);
//        boolean test = mDetector.onTouchEvent(ev);
//        Log.v("list touched", String.valueOf(test));
        return returned;
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

    public void receiveAnimationValue(int value) {
        translateY = value / 3;
//        scrollListBy(-translateY);
//        smoothScrollByOffset(-translateY);
//        smoothScrollToPosition();
//        Log.v("translate: ", String.valueOf(translateY));

        updateContentBounds();
        invalidate();
    }

    private void updateContentBounds() {
        setTop(LIST_TOP - translateY);
        setBottom(SCREEN_HEIGHT);


        //if(adapter != null) Log.v("count: ", String.valueOf(adapter.getCount()));
        //Log.v("height: ", String.valueOf(getHeight()));
//        setBottom(LIST_TOP + LIST_HEIGHT - translateY);
    }

    private int getDesiredWith() {return SCREEN_WIDTH;}
    private int getDesiredHeight() {return SCREEN_HEIGHT - LIST_TOP;}

    private class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        boolean mFromDispatch = true;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            translateY += (distanceY / 3);

            if(translateY < 0) {translateY = 0;}
            else if(translateY > 350) {translateY = 350;}
            else {
                updateContentBounds();
                invalidate();
            }

            return true;

//            return super.onScroll(e1, e2, distanceX, - distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, - velocityY);
        }
    }
}
