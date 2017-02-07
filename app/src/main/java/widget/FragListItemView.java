package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by flavorhythm on 1/4/17.
 */

public class FragListItemView extends EchoSuperView {
    private Drawable mIcon;
    private Paint mIconPaint;

    private Point mTextOrigin, mSubtextOrigin;
    private CharSequence mText, mSubtext;
    private StaticLayout mTextLayout, mSubtextLayout;
    private TextPaint mTextPaint, mSubtextPaint;

    private boolean mHasSubtext;

    public FragListItemView(Context context) {
        super(context);
    }

    public FragListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FragListItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setContent(int iconRes, String text, @Nullable String subtext) {
        mText = text;
        mSubtext = subtext;
        mIcon = ContextCompat.getDrawable(getContext(), iconRes);

        updateView(subtext != null);
        invalidate();
    }

    private void updateView(boolean hasNewSubtext) {
        if(hasNewSubtext) {
            if(mHasSubtext) updateContentBounds();
            else {
                mHasSubtext = true;
                requestLayout();
            }
        } else {
            if(!mHasSubtext) updateContentBounds();
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
        mIconPaint = new Paint();

        mText = "";
        mTextOrigin = new Point(0, 0);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(TEXT_SIZE);

        mSubtext = "";
        mSubtextOrigin = new Point(0, 0);
        mSubtextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSubtextPaint.setTextSize(SUBTEXT_SIZE);

        updateContentBounds();
    }

    @Override
    public void draw(Canvas canvas) {
        if(mIcon != null) {mIcon.draw(canvas);}
        if(mTextLayout != null) {drawText(canvas, mTextLayout, mTextOrigin);}
        if(mSubtextLayout != null) {drawText(canvas, mSubtextLayout, mSubtextOrigin);}
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void updateContentBounds() {
        final int w = SCREEN_WIDTH - getTextLeft();

        mTextLayout = getTextLayout(mText, mTextPaint, w, ALIGN_LEFT);
        mTextOrigin.set(getTextLeft(), getTextTop());

        if(mHasSubtext) {
            mSubtextLayout = getTextLayout(mSubtext, mSubtextPaint, w, ALIGN_LEFT);
            mSubtextOrigin.set(getTextLeft(), getTextTop() + mTextLayout.getHeight());
        }

        if(mIcon != null) {
            int top = (getDesiredHeight() - ICON_SIZE) / 2;
            Rect iconBounds = new Rect(
                    getIconLeft(),
                    top,
                    getIconLeft() + ICON_SIZE,
                    top + ICON_SIZE
            );
            mIcon.setBounds(iconBounds);
        }
    }

    @Override
    protected int getDesiredWidth() {return SCREEN_WIDTH;}

    @Override
    protected int getDesiredHeight() {
        int baseHeight = getTextTop() + mTextLayout.getHeight() + PADDING;

        if(mHasSubtext && mSubtextLayout != null) {return baseHeight + mSubtextLayout.getHeight();}
        else {return baseHeight;}
    }

    private int getIconLeft() {return 2 * PADDING;}
    private int getTextLeft() {return getIconLeft() + ICON_SIZE + PADDING;}
    private int getTextTop() {return PADDING;}
}