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
import android.view.View;

import com.echo_usa.echo.R;

import util.MetricCalcs;

/**
 * Created by zyuki on 9/2/2016.
 */
public class EmptyModelView extends View {
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

    private ValueAnimator animator;
    private float alphaVal = VISIBLE;

    public EmptyModelView(Context context) {this(context, null);}
    public EmptyModelView(Context context, AttributeSet attrs) {this(context, attrs, 0);}
    public EmptyModelView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyModelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        //TODO: create Show Text parameter for xml

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

        upperTextPaint.setTextSize(MetricCalcs.dpToPixels(TEXT_SIZE));
        lowerTextPaint.setTextSize(MetricCalcs.dpToPixels(TEXT_SIZE));

        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_model_bg));

        animator = ValueAnimator.ofFloat(VISIBLE, GONE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alphaVal = (float)animation.getAnimatedValue();
                invalidate();
            }
        });

        updateContentBounds();
    }

    public boolean isVisible() {return alphaVal == VISIBLE;}

    public void startAnimation(int animType) {
        switch(animType) {
            case SHOW_ANIM: animator.reverse(); break;
            case HIDE_ANIM: animator.start(); break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = View.resolveSize(getDesiredWidth(), widthMeasureSpec);
        int height = View.resolveSize(getDesiredHeight(), heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setAlpha(alphaVal);

        if(alphaVal == GONE) return;

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
        savedState.savedAlpha = alphaVal;

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        alphaVal = savedState.savedAlpha;
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
