package widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.echo_usa.echo.R;

import util.MetricCalc;

/**
 * Created by zyuki on 9/2/2016.
 */
public class EchoLogoView extends View {
    public static final int SHOW_ANIM = 0;
    public static final int HIDE_ANIM = 1;

    private static final int TEXT_SIZE = 18;
    private static final float VISIBLE = 1f;
    private static final float GONE = 0f;

    private Drawable echoLogo;
    private CharSequence upperText, lowerText;
    private StaticLayout upperTextLayout, lowerTextLayout;
    private Point upperTextOrigin, lowerTextOrigin;
    private TextPaint upperTextPaint, lowerTextPaint;

    public EchoLogoView(Context context) {
        super(context);

        init(null);
    }

    public EchoLogoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public EchoLogoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EchoLogoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private static ValueAnimator getAlphaAnimation(final EchoLogoView v) {
        ValueAnimator va = ValueAnimator.ofFloat(VISIBLE, GONE);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();

                v.setAlpha(value);
                v.invalidate();
            }
        });
        return va;
    }

    private void init(AttributeSet attrs) {
        //TODO: create Show Text parameter for xml
        if(attrs != null) {
            
        }

        setSaveEnabled(true);

        echoLogo = ContextCompat.getDrawable(getContext(), R.drawable.echo_logo);
        upperText = getResources().getString(R.string.no_unit_selected);
        lowerText = getResources().getString(R.string.pick_unit);

        upperTextOrigin = new Point(0,0);
        lowerTextOrigin = new Point(0,0);

        upperTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        lowerTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        upperTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.no_model_text));
        lowerTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.no_model_text));

        upperTextPaint.setTextSize(MetricCalc.dpToPxById(getContext(), R.dimen.content_title_size));
        lowerTextPaint.setTextSize(MetricCalc.dpToPxById(getContext(), R.dimen.content_title_size));

        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_model_bg));

        updateContentBounds();
    }

    public boolean isVisible() {return getAlpha() == VISIBLE;}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isVisible();
    }

    public void startAnimation(int animType) {
        switch(animType) {
            case SHOW_ANIM: getAlphaAnimation(this).reverse(); break;
            case HIDE_ANIM: getAlphaAnimation(this).start(); break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int resolvedWidth = View.resolveSize(getDesiredWidth(), widthMeasureSpec);
        int resolvedHeight = View.resolveSize(getDesiredHeight(), heightMeasureSpec);

        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(getAlpha() == GONE) return;

        if(upperTextLayout != null) {
            canvas.save();
            canvas.translate(upperTextOrigin.x, upperTextOrigin.y);

            upperTextLayout.draw(canvas);
            canvas.restore();
        }

        if(echoLogo != null) echoLogo.draw(canvas);

        if(lowerTextLayout != null) {
            canvas.save();
            canvas.translate(lowerTextOrigin.x, lowerTextOrigin.y);

            lowerTextLayout.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            updateContentBounds();
        }
    }

    private void updateContentBounds() {
        if(upperText == null) upperText = "";
        if(lowerText == null) lowerText = "";

        float upperTextWidth = echoLogo.getIntrinsicWidth();
        float lowerTextWidth = echoLogo.getIntrinsicWidth();

        upperTextLayout = new StaticLayout(
                upperText, upperTextPaint, (int)upperTextWidth, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true
        );

        lowerTextLayout = new StaticLayout(
                lowerText, lowerTextPaint, (int) lowerTextWidth, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true
        );

        int left = (getWidth() - getDesiredWidth()) / 2;
        int top = (getHeight() - getDesiredHeight()) / 2;

        if(upperTextLayout != null) {
            upperTextOrigin.set(left, top);
            top += upperTextLayout.getHeight();
        }

        if(echoLogo != null) {
            int right = left + echoLogo.getIntrinsicWidth();
            int bottom = top + echoLogo.getIntrinsicHeight();
            echoLogo.setBounds(left, top, right, bottom);
            top += echoLogo.getIntrinsicHeight();
        }

        if(upperTextLayout != null) {
            lowerTextOrigin.set(left, top);
        }
    }

    private int getDesiredWidth() {
        if(echoLogo == null) return 0;
        else return echoLogo.getIntrinsicWidth();
    }

    private int getDesiredHeight() {
        int upperTextHeight;
        if(upperTextLayout == null) upperTextHeight = 0;
        else upperTextHeight = upperTextLayout.getHeight();

        int echoLogoHeight;
        if(echoLogo == null) echoLogoHeight = 0;
        else echoLogoHeight = echoLogo.getIntrinsicHeight();

        int lowerTextHeight;
        if(lowerTextLayout == null) lowerTextHeight = 0;
        else lowerTextHeight = lowerTextLayout.getHeight();

        return upperTextHeight + echoLogoHeight + lowerTextHeight;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.savedAlpha = getAlpha();

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        setAlpha(savedState.savedAlpha);
        invalidate();
    }

    private static class SavedState extends BaseSavedState {
        float savedAlpha;

        public SavedState(Parcel source) {
            super(source);
            savedAlpha = source.readFloat();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(savedAlpha);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
