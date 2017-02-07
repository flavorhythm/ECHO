package widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.echo_usa.echo.R;

/**
 * Created by flavorhythm on 1/20/17.
 *
 * Displays a custom Snackbar for the application
 *
 * Due to an unknown reason, the OOTB Snackbar does not animate correctly.
 * This view remedies this issue by implementing custom ValueAnimators
 */

public class EchoSnackbar extends EchoSuperView {
    private Point mTextOrigin;
    private CharSequence mText;
    private StaticLayout mTextLayout;
    private TextPaint mTextPaint;

    private Rect mSnackbarBg;
    private Paint mBgPaint;

    public EchoSnackbar(Context context) {
        super(context);
    }

    public EchoSnackbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EchoSnackbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EchoSnackbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mSnackbarBg != null) canvas.drawRect(mSnackbarBg, mBgPaint);
        if(mText != null) drawText(canvas, mTextLayout, mTextOrigin);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void initialize(AttributeSet attrs) {
        mTextOrigin = new Point(SNACKBAR_TEXT_X, getDesiredHeight() + SNACKBAR_TEXT_Y);

        mTextPaint = getTextPaintObj(getContext(), R.dimen.divider_subtitle_size, R.color.white);
        mBgPaint = getPaintObj(getContext(), ALPHA_VISIBLE, R.color.snackbar_bg);

        mSnackbarBg = new Rect(0, getDesiredHeight(), getDesiredWidth(), getDesiredHeight() * 2);
    }

    @Override
    protected void updateContentBounds() {}

    public void show(String text) {
        mText = text;
        mTextLayout = getTextLayout(mText, mTextPaint, getTextWidth(), ALIGN_LEFT);
        getUpAnimation(this).start();
    }

    @Override
    protected int getDesiredWidth() {return SCREEN_WIDTH;}

    @Override
    protected int getDesiredHeight() {return SNACKBAR_SIZE;}

    private int getTextWidth() {return getDesiredWidth() - 2 * SNACKBAR_TEXT_X;}

    private static ValueAnimator getUpAnimation(final EchoSnackbar snackbar) {
        ValueAnimator va = ValueAnimator.ofInt(0, snackbar.getDesiredHeight());
        va.setInterpolator(new FastOutSlowInInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int)animation.getAnimatedValue();

                snackbar.scrollTo(0, i);
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                getAlphaAnimation(snackbar).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                ValueAnimator va = getDownAnimation(snackbar);
                va.setStartDelay(snackbar.SNACKBAR_DURATION);
                va.start();
            }
        });

        return va;
    }

    private static ValueAnimator getDownAnimation(final EchoSnackbar snackbar) {
        ValueAnimator va = ValueAnimator.ofInt(snackbar.getDesiredHeight(), 0);
        va.setInterpolator(new FastOutSlowInInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int)animation.getAnimatedValue();

                snackbar.scrollTo(0, i);
            }
        });

        return va;
    }

    private static ValueAnimator getAlphaAnimation(final EchoSnackbar snackbar) {
        ValueAnimator va = ValueAnimator.ofInt(0, 255);
        va.setInterpolator(new LinearOutSlowInInterpolator());
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int)animation.getAnimatedValue();

                snackbar.mTextPaint.setAlpha(i);
                snackbar.invalidate();
            }
        });

        return va;
    }
}
