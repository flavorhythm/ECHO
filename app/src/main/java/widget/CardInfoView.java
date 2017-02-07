package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.echo_usa.echo.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import data.card_content.CardContent;
import data.card_content.CardInfo;
import util.ImageLoaderTask;

/**
 * Created by zyuki on 8/4/2016.
 */
public class CardInfoView extends CardContentView {
    private Bitmap mBitmap;
    private Rect mImgMask;

    private CharSequence mText, mSubtext, mFoottext;
    private StaticLayout mTextLayout, mSubtextLayout, mFoottextLayout;
    private Point mTextOrigin, mSubtextOrigin, mFoottextOrigin;
    private TextPaint mTextPaint, mSubtextPaint, mFoottextPaint;

    private boolean mIsImageLeft;

    public CardInfoView(Context context) {super(context);}

    public CardInfoView(Context context, AttributeSet attrs) {super(context, attrs);}

    public CardInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initialize(AttributeSet attrs) {
        mIsImageLeft = true;

        mText = "";
        mSubtext = "";
        mFoottext = getResources().getString(R.string.btn_learn_more);

        mTextOrigin = new Point(0, 0);
        mSubtextOrigin = new Point(0, 0);
        mFoottextOrigin = new Point(0, 0);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSubtextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mFoottextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint.setTextSize(TEXT_SIZE);
        mSubtextPaint.setTextSize(SUBTEXT_SIZE);
        mFoottextPaint.setTextSize(INFO_C_FOOTER_SIZE);

        mFoottextPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
        mFoottextPaint.setTypeface(Typeface.create(mFoottextPaint.getTypeface(), Typeface.ITALIC));

        mImgMask = new Rect(getImgMaskLeft(), getImgMaskTop(), getImgMaskRight(), getImgMaskBtm());
    }

    public void setContent(CardInfo ci) {
        mText = ci.getText();
        mSubtext = ci.getSubtext();
        mIsImageLeft = ci.leftAligned();

        updateContentBounds();
        invalidate();
    }

    @Override
    public int getRequiredImageWidth() {
        return INFO_C_IMG_WIDTH;
    }

    @Override
    public int getRequiredImageHeight() {
        return CARD_HEIGHT;
    }

    @Override
    public ImageLoaderTask.ScalingLogic getScalingLogic() {
        return ImageLoaderTask.ScalingLogic.CROP;
    }

    @Override
    public int getImageSampleSize() {
        return 1;
    }

    @Override
    public void setImage(View v, ImageLoaderTask task, Bitmap bm) {
        if(task == getLoaderTask() && v instanceof CardInfoView){
            setImage(bm);
        }
    }

    private void setImage(Bitmap cardImage) {
        mBitmap = cardImage;

        updateContentBounds();
        invalidate();
    }

    @Override
    protected void updateContentBounds() {
        int paddedText = getTextWidth() - (2 * PADDING);

        mTextLayout = getTextLayout(mText, mTextPaint, paddedText, ALIGN_LEFT);
        mSubtextLayout = getTextLayout(mSubtext, mSubtextPaint, paddedText, ALIGN_LEFT);
        mFoottextLayout = getTextLayout(mFoottext, mFoottextPaint, paddedText, ALIGN_LEFT);

        mImgMask.left = getImgMaskLeft();
        mImgMask.right = getImgMaskRight();

        mTextOrigin.set(getTextX(), getTextY());
        mSubtextOrigin.set(getTextX(), getSubtextY());
        mFoottextOrigin.set(getTextX(), getButtonY());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mTextLayout != null) {drawText(canvas, mTextLayout, mTextOrigin);}
        if(mSubtextLayout != null) {drawText(canvas, mSubtextLayout, mSubtextOrigin);}
        if(mFoottextLayout != null) {drawText(canvas, mFoottextLayout, mFoottextOrigin);}

        if(mImgMask != null) {
            canvas.save();
            canvas.clipRect(mImgMask, Region.Op.INTERSECT);

            if(mBitmap != null) {
                canvas.drawBitmap(
                        mBitmap,
                        getImgLeft(),
                        getImgTop(),
                        null
                );
            }

            canvas.restore();
        }
    }

    private int getTextWidth() {return CARD_WIDTH - INFO_C_IMG_WIDTH;}

    private int getTextX() {return mIsImageLeft ? PADDING + INFO_C_IMG_WIDTH : PADDING;}
    private int getTextY() {return PADDING;}
    private int getSubtextY() {return getTextY() + PADDING + mTextLayout.getHeight();}
    private int getButtonY() {return CARD_HEIGHT - mFoottextLayout.getHeight() - PADDING;}

    private int getImgCenter() {return (INFO_C_IMG_WIDTH - mBitmap.getWidth()) / 2;}
    private int getImgLeft() {return mIsImageLeft ? getImgCenter() : getImgCenter() + getTextWidth();}
    private int getImgTop() {return 0;}

    private int getImgMaskLeft() {return mIsImageLeft ? 0 : CARD_WIDTH - INFO_C_IMG_WIDTH;}
    private int getImgMaskTop() {return 0;}
    private int getImgMaskRight() {return getImgMaskLeft() + INFO_C_IMG_WIDTH;}
    private int getImgMaskBtm() {return CARD_HEIGHT;}
}