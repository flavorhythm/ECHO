package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.echo_usa.echo.R;

import util.MetricCalc;

/**
 * Created by flavorhythm on 1/5/17.
 */
public abstract class EchoSuperView extends View {
    static final Layout.Alignment ALIGN_LEFT = Layout.Alignment.ALIGN_NORMAL;
    static final Layout.Alignment ALIGN_CENTER = Layout.Alignment.ALIGN_CENTER;

    static final int NO_COLOR = -1;

    static final int ALPHA_GONE = 0;
    static final int ALPHA_VISIBLE = 255;

    final int AB_HEIGHT = MetricCalc.getActionBarSize(getContext());
    final int SCREEN_WIDTH = MetricCalc.getScreenWidth();
    final int SCREEN_HEIGHT = MetricCalc.getScreenHeight() - MetricCalc.getStatusBarHeight();
    final int PADDING = MetricCalc.dpToPxById(getContext(), R.dimen.standard_padding);

    final int CARD_WIDTH = MetricCalc.getScreenWidth() - (2 * PADDING);

    final int DIVIDER_WIDTH = MetricCalc.dpToPxById(getContext(), R.dimen.divider_line_width);
    final int DIVIDER_HEIGHT = MetricCalc.dpToPxById(getContext(), R.dimen.divider_line_height);

    final int TEXT_SIZE = MetricCalc.dpToPxById(getContext(), R.dimen.divider_title_size);
    final int SUBTEXT_SIZE = MetricCalc.dpToPxById(getContext(), R.dimen.divider_subtitle_size);

    final int ICON_SIZE = MetricCalc.dpToPxById(getContext(), R.dimen.icon_size);

    final int ROUND_IMG_SIZE = MetricCalc.dpToPxById(getContext(), R.dimen.circular_image_size);

    final int CARD_HEIGHT = MetricCalc.dpToPxById(getContext(), R.dimen.card_image_height);

    final int INFO_C_FOOTER_SIZE = MetricCalc.dpToPxById(getContext(), R.dimen.content_footer_size);
    final int INFO_C_IMG_WIDTH = MetricCalc.dpToPxById(getContext(), R.dimen.card_image_width);

    final int HEADER_HEIGHT = MetricCalc.getDrawerHeaderHeight();

    final int SNACKBAR_SIZE = MetricCalc.dpToPxById(getContext(), R.dimen.snackbar_size);
    final int SNACKBAR_TEXT_X = MetricCalc.dpToPxById(getContext(), R.dimen.snackbar_text_x);
    final int SNACKBAR_TEXT_Y = MetricCalc.dpToPxById(getContext(), R.dimen.snackbar_text_y);

    final int SNACKBAR_DURATION = getResources().getInteger(R.integer.snackbar_duration);

    final int DRAWER_IMAGE_WIDTH = MetricCalc.dpToPxById(getContext(), R.dimen.model_image_height);
    final int DRAWER_IMAGE_HEIGHT = MetricCalc.dpToPxById(getContext(), R.dimen.model_image_width);

    final int DRAWER_OPEN_SIZE = MetricCalc.dpToPxById(getContext(), R.dimen.model_drawer_size);

    final int SHADOW_HEIGHT = MetricCalc.dpToPxById(getContext(), R.dimen.model_image_shadow_size);

    public EchoSuperView(Context context) {
        super(context);
        initialize(null);
    }

    public EchoSuperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public EchoSuperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EchoSuperView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int desiredWidth = View.resolveSize(getDesiredWidth(), widthMeasureSpec);
        final int desiredHeight = View.resolveSize(getDesiredHeight(), heightMeasureSpec);

        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            updateContentBounds();
        }
    }

    protected static StaticLayout getTextLayout(CharSequence c, TextPaint p, int w, Layout.Alignment a) {
        if(c != null && p != null && a != null) {return new StaticLayout(c, p, w, a, 1f, 0f, true);}
        else return null;
    }

    protected static Paint getPaintObj(Context context, int alpha, int colorRes) {
        Paint p = new Paint();
        p.setAlpha(alpha);

        if(colorRes > 0) p.setColor(ContextCompat.getColor(context, colorRes));
        return p;
    }

    protected static TextPaint getTextPaintObj(Context context, int textSizeRes, int colorRes) {
        TextPaint tp = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        tp.setTextSize(MetricCalc.dpToPxById(context, textSizeRes));

        if(colorRes > 0) tp.setColor(ContextCompat.getColor(context, colorRes));
        return tp;
    }

    protected final void drawText(Canvas c, StaticLayout sl, Point p) {
        c.save();
        c.translate(p.x, p.y);

        sl.draw(c);
        c.restore();
    }

    protected static int clampScroll(int scroll, int min, int max) {
        scroll = scroll < min ? min : scroll;
        scroll = scroll > max ? max : scroll;

        return scroll;
    }

    protected abstract void initialize(AttributeSet attrs);
    protected abstract void updateContentBounds();

    protected abstract int getDesiredWidth();
    protected abstract int getDesiredHeight();
}
