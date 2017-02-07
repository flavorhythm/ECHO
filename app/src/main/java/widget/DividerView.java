package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.echo_usa.echo.R;

/**
 * Created by ZYuki on 7/19/2016.
 */
public class DividerView extends EchoSuperView {
    private Paint mDividerPaint;
    private Rect mDividerRect;

    private Point mTextOrigin, mSubtextOrigin;
    private CharSequence mText, mSubtext;
    private StaticLayout mTextLayout, mSubtextLayout;
    private TextPaint mTextPaint, mSubtextPaint;

    private boolean mHasSubtext;

    public DividerView(Context context) {
        super(context);
    }

    public DividerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DividerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DividerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setContent(String text, @Nullable String subtext) {
        mText = text;
        mSubtext = subtext;

        updateThisView(subtext != null);
        invalidate();
    }

    private void updateThisView(boolean hasNewSubtext) {
        if(hasNewSubtext) {
            if(mHasSubtext) {updateContentBounds();}
            else {
                mHasSubtext = true;
                requestLayout();
            }
        } else {
            if(!mHasSubtext) {updateContentBounds();}
            else {
                mHasSubtext = false;
                requestLayout();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void initialize(AttributeSet attrs) {
        if(attrs != null) {}

        mHasSubtext = true;
        mDividerRect = new Rect(
                getDividerLeft(),
                getDividerTop(),
                getDividerLeft() + DIVIDER_WIDTH,
                getDividerTop() + DIVIDER_HEIGHT
        );

        mDividerPaint = new Paint();
        mDividerPaint.setColor(ContextCompat.getColor(getContext(), R.color.echo_orange));

        mText = "";
        mTextOrigin = new Point(0, 0);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(TEXT_SIZE);

        if(mHasSubtext) {
            mSubtext = "";
            mSubtextOrigin = new Point(0, 0);
            mSubtextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            mSubtextPaint.setTextSize(SUBTEXT_SIZE);
        }

        updateContentBounds();
    }

    @Override
    public void draw(Canvas canvas) {
        if(mDividerRect != null) {canvas.drawRect(mDividerRect, mDividerPaint);}
        if(mTextLayout != null) {drawText(canvas, mTextLayout, mTextOrigin);}
        if(mSubtextLayout != null) {drawText(canvas, mSubtextLayout, mSubtextOrigin);}
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void updateContentBounds() {
        mTextLayout = getTextLayout(mText, mTextPaint, SCREEN_WIDTH, ALIGN_CENTER);
        mTextOrigin.set(getTextLeft(), getTextTop());

        if(mHasSubtext) {
            mSubtextLayout = getTextLayout(mSubtext, mSubtextPaint, SCREEN_WIDTH, ALIGN_CENTER);
            mSubtextOrigin.set(getTextLeft(), getTextTop() + mTextLayout.getHeight());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false; //Never receive touch events
    }

    @Override
    protected int getDesiredWidth() {return SCREEN_WIDTH;}

    @Override
    protected int getDesiredHeight() {
        int baseHeight = getDividerTop() + DIVIDER_HEIGHT + PADDING;

        if(mTextLayout != null) {baseHeight += mTextLayout.getHeight() + PADDING;}
        if(mSubtextLayout != null && mHasSubtext) {return baseHeight + mSubtextLayout.getHeight();}

        else return baseHeight;
    }

    //"variables" converted to methods due to how class and superclass are instantiated.
    //Variables in this class are not instantiated prior to running the initialize() method
    private int getDividerLeft() {return (SCREEN_WIDTH - DIVIDER_WIDTH) / 2;}
    private int getDividerTop() {return PADDING;}

    private int getTextLeft() {return 0;}
    private int getTextTop() {return getDividerTop() + DIVIDER_HEIGHT + PADDING;}
}
