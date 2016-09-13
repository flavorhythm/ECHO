package widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.echo_usa.echo.R;

import java.util.List;

import data.Card;
import util.MetricCalcs;

/**
 * Created by ZYuki on 6/30/2016.
 */
public class ModelInfoView extends View {
    private static TimeInterpolator DECELERATE = new DecelerateInterpolator();
    private static TimeInterpolator ACCEL_DECEL = new AccelerateDecelerateInterpolator();

    private static final int LEFT = 100;
    private static final int RIGHT = 200;
    private static final int MAX_TRANS_Y = 200;
    private static final int MAX_SHIFT = 5;

    private static final int IMG_HEIGHT = 250;
    private static final int IMG_WIDTH = 350;
    private static final int ON_BTM_IMG_SHDW_SIZE = 8;
    private static final int ON_TOP_IMG_SHDW_SIZE = 5;

    private int modelImageHeight = MetricCalcs.dpToPixels(IMG_HEIGHT);
    private int modelImageWidth = MetricCalcs.dpToPixels(IMG_WIDTH);

    private Paint whiteBgPaint;
    private Rect upperWhiteBgBounds, lowerWhiteBgBounds;
    private Rect onBtmImgShadowBounds, onTopImgShadowBounds;
    private Rect upperImageBounds, lowerImageBounds;
    private Rect upperImageMask, lowerImageMask;
    private Rect originalUpperImage, originalLowerImage;

    private Bitmap upperImageBitmap, lowerImageBitmap;

    private ValueAnimator moveBgDown, shiftImgIn;
    private ValueAnimator moveBgUp, shiftImgOut;

    private AnimatorSet openDrawerAnimation, closeDrawerAnimation;

    private GradientDrawable onTopImgShadow, onBtmImgShadow;

    private Callback callback;
    private DrawerState drawerState;

    private int imageShift = 0;
    private int drawerTranslateY = 0;

    int screenWidth = MetricCalcs.getScreenWidth();
    int screenHeight = MetricCalcs.getScreenHeight();
    int actionBarSize = MetricCalcs.getActionBarSize(getContext()); //TODO: check

    int onBtmImgShadowHeight = MetricCalcs.dpToPixels(ON_BTM_IMG_SHDW_SIZE);
    int onTopImgShadowHeight = MetricCalcs.dpToPixels(ON_TOP_IMG_SHDW_SIZE);

    int imageLeft = (screenWidth - modelImageWidth) / 2;
    int upperImageTop = screenHeight - actionBarSize - (2 * modelImageHeight);
    int lowerImageTop = upperImageTop + modelImageHeight;

    int onTopImgShadowTop = upperImageTop + modelImageHeight;
    int onBtmImgShadowTop = lowerImageTop - onBtmImgShadowHeight;

    //TODO: view function order8
    //1. set model image <- requires image setter, image splitter and top/bottom drawable
    //1.5 clear empty model view
    //2. open this view to show recyclerview
    //3. use the same code used in split to "move" image of unit

    //TODO: required methods
    //1. image setter, image splitter
    //2. upper/lower image animation -> open/close/toggle
    //3. swipe left/right


    public ModelInfoView(Context context) {this(context, null);}
    public ModelInfoView(Context context, AttributeSet attrs) {this(context, attrs, 0);}
    public ModelInfoView(Context context, AttributeSet attrs, int defStyleAttr) {this(context, attrs, defStyleAttr, 0);}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ModelInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        drawerState = new DrawerState();

        setModelImage(R.drawable.srm_225);

        whiteBgPaint = new Paint();
        whiteBgPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));

        onTopImgShadowBounds = new Rect(0, onTopImgShadowTop, screenWidth, onTopImgShadowTop + onTopImgShadowHeight);
        onBtmImgShadowBounds = new Rect(0, onBtmImgShadowTop, screenWidth, onBtmImgShadowTop + onBtmImgShadowHeight);

        onTopImgShadow = (GradientDrawable)ContextCompat.getDrawable(getContext(), R.drawable.bg_model_info_lower_fade);
        onTopImgShadow.setBounds(onTopImgShadowBounds);

        onBtmImgShadow = (GradientDrawable)ContextCompat.getDrawable(getContext(), R.drawable.bg_model_info_upper_fade);
        onBtmImgShadow.setBounds(onBtmImgShadowBounds);

        upperImageBounds = new Rect(imageLeft, upperImageTop, imageLeft + modelImageWidth, upperImageTop + modelImageHeight);
        lowerImageBounds = new Rect(imageLeft, lowerImageTop, imageLeft + modelImageWidth, lowerImageTop + modelImageHeight);

        upperImageMask = new Rect(imageLeft, upperImageTop, imageLeft + modelImageWidth, upperImageTop + modelImageHeight);
        lowerImageMask = new Rect(imageLeft, lowerImageTop, imageLeft + modelImageWidth, lowerImageTop + modelImageHeight);

        originalUpperImage = new Rect(imageLeft, upperImageTop, imageLeft + modelImageWidth, upperImageTop + modelImageHeight);
        originalLowerImage =  new Rect(imageLeft, lowerImageTop, imageLeft + modelImageWidth, lowerImageTop + modelImageHeight);

        upperWhiteBgBounds = new Rect(0, 0, screenWidth, upperImageTop + modelImageHeight);
        lowerWhiteBgBounds = new Rect(0, lowerImageTop, screenWidth, lowerImageTop + modelImageHeight);

        moveBgDown = ValueAnimator.ofInt(0, MetricCalcs.dpToPixels(MAX_TRANS_Y));
        shiftImgIn = ValueAnimator.ofInt(0, MetricCalcs.dpToPixels(MAX_SHIFT));

        moveBgUp = ValueAnimator.ofInt(MetricCalcs.dpToPixels(MAX_TRANS_Y), 0);
        shiftImgOut = ValueAnimator.ofInt(MetricCalcs.dpToPixels(MAX_SHIFT), 0);

        moveBgDown.setInterpolator(DECELERATE);
        moveBgUp.setInterpolator(ACCEL_DECEL);

        moveBgDown.setDuration(250L);
        shiftImgIn.setDuration(200L);
        shiftImgIn.setStartDelay(100L);

        moveBgUp.setDuration(250L);
        shiftImgOut.setDuration(200L);
        shiftImgOut.setStartDelay(100L);

        ValueAnimator.AnimatorUpdateListener translateYListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawerTranslateY = (int)animation.getAnimatedValue();

                updateContentBounds();
                invalidate();
            }
        };

        ValueAnimator.AnimatorUpdateListener imgShiftListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                imageShift = (int)animation.getAnimatedValue();

                updateContentBounds();
                invalidate();
            }
        };

        moveBgDown.addUpdateListener(translateYListener);
        moveBgUp.addUpdateListener(translateYListener);

        shiftImgIn.addUpdateListener(imgShiftListener);
        shiftImgOut.addUpdateListener(imgShiftListener);

        openDrawerAnimation = new AnimatorSet();
        openDrawerAnimation.play(moveBgDown).with(shiftImgIn);
        //openDrawerAnimation.setStartDelay(100L);
        openDrawerAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                drawerState.setDrawerStateClosed(false);
                callback.restoreListeners();
            }
        });

        closeDrawerAnimation = new AnimatorSet();
        closeDrawerAnimation.play(moveBgUp).with(shiftImgOut);
        closeDrawerAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                drawerState.setDrawerStateClosed(true);
                callback.updateRecycler();
            }
        });

        updateContentBounds();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return drawerState.isDrawerClosed();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int resolvedWidth = View.resolveSize(desiredWidth(), widthMeasureSpec);
        int resolvedHeight = View.resolveSize(desiredHeight(), heightMeasureSpec);

        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(onTopImgShadow != null) onTopImgShadow.draw(canvas);
        if(onBtmImgShadow != null) onBtmImgShadow.draw(canvas);

        if(upperWhiteBgBounds != null) canvas.drawRect(upperWhiteBgBounds, whiteBgPaint);
        if(lowerWhiteBgBounds != null) canvas.drawRect(lowerWhiteBgBounds, whiteBgPaint);

        if(upperImageBounds != null && upperImageBitmap != null) {
            if(upperImageMask != null) canvas.clipRect(upperImageMask, Region.Op.REPLACE);
            canvas.drawBitmap(upperImageBitmap, upperImageBounds.left, upperImageBounds.top, null);
        }

        if(lowerImageBounds != null && lowerImageBitmap != null) {
            if(lowerImageMask != null) canvas.clipRect(lowerImageMask, Region.Op.REPLACE);
            canvas.drawBitmap(lowerImageBitmap, lowerImageBounds.left, lowerImageBounds.top, null);
        }
    }

    private void setModelImage(int resource) {
        upperImageBitmap = ModelInfoView.splitImageBitmap(getContext(), resource)[0];
        lowerImageBitmap = ModelInfoView.splitImageBitmap(getContext(), resource)[1];
    }

    public void updateModelImage(int resource) {
        setModelImage(resource);

        updateContentBounds();
        invalidate();

        if(!drawerState.isDrawerClosed()) closeDrawer();
        else callback.updateRecycler();
    }

    public void setCallback(Callback callback) {this.callback = callback;}

    public void toggleDrawer(boolean isUpdateRequired) {
        if(isUpdateRequired) {
            if(drawerState.isDrawerClosed()) callback.updateRecycler();
            else closeDrawer();
        } else {
            toggleDrawer();
        }
    }

    private void openDrawer() {
        AnimatorSet localAnimator = openDrawerAnimation.clone();
        localAnimator.start();
        callback.animateRecyclerUp();
    }

    private void closeDrawer() {
        AnimatorSet localAnimator = closeDrawerAnimation.clone();
        localAnimator.start();
        callback.animateRecyclerDown();
    }

    private void toggleDrawer() {
        if(drawerState.isDrawerClosed()) openDrawer();
        else closeDrawer();
    }
    private void updateContentBounds() {
        if(onBtmImgShadowBounds != null) {
            onBtmImgShadowBounds.top = onBtmImgShadowTop + drawerTranslateY;
            onBtmImgShadowBounds.bottom = onBtmImgShadowTop + onBtmImgShadowHeight + drawerTranslateY;

            onBtmImgShadow.setBounds(onBtmImgShadowBounds);
        }

        if(lowerWhiteBgBounds != null) {
            lowerWhiteBgBounds.top = originalLowerImage.top + drawerTranslateY;
            lowerWhiteBgBounds.bottom = originalLowerImage.bottom + drawerTranslateY;
        }

        if(upperImageBounds != null) {
            upperImageBounds.top = originalUpperImage.top + imageShift;
            upperImageBounds.bottom = originalUpperImage.bottom + imageShift;
        }

        if(lowerImageBounds != null) {
            lowerImageBounds.top = originalLowerImage.top + drawerTranslateY - imageShift;
            lowerImageBounds.bottom = originalLowerImage.bottom + drawerTranslateY - imageShift;
        }

        if(lowerImageMask != null) {
            lowerImageMask.top = originalLowerImage.top + drawerTranslateY;
            lowerImageMask.bottom = originalLowerImage.bottom + drawerTranslateY;
        }
    }

    private int desiredWidth() {
        return MetricCalcs.getScreenWidth();
    }

    private int desiredHeight() {
        int upperBgHeight;
        if(upperWhiteBgBounds == null) upperBgHeight = 0;
        else upperBgHeight = upperWhiteBgBounds.height();

        int lowerBgHeight;
        if(lowerWhiteBgBounds == null) lowerBgHeight = 0;
        else lowerBgHeight = lowerWhiteBgBounds.height();

        return upperBgHeight + lowerBgHeight;
    }

    private static Drawable sliceImageBitmap(Context context, Bitmap bitmap, int imgOffset) {
        return new BitmapDrawable(context.getResources(), Bitmap.createBitmap(bitmap, 0, imgOffset, bitmap.getWidth(), bitmap.getHeight() - imgOffset));
    }

    private static Drawable[] splitImageDrawable(Context context) {
        Drawable[] splitImages = new Drawable[2];
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.srm_225);
        int splitYPos = bitmap.getHeight() / 2;

        splitImages[0] = new BitmapDrawable(context.getResources(), Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), splitYPos));
        splitImages[1] = new BitmapDrawable(context.getResources(), Bitmap.createBitmap(bitmap, 0, splitYPos, bitmap.getWidth(), splitYPos));

        return splitImages;
    }

    private static Bitmap[] splitImageBitmap(Context context, int drawableResource) {
        Bitmap[] bitmapArray = new Bitmap[2];
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableResource);
        int splitYPos = bitmap.getHeight() / 2;

        bitmapArray[0] = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), splitYPos);
        bitmapArray[1] = Bitmap.createBitmap(bitmap, 0, splitYPos, bitmap.getWidth(), splitYPos);

        return bitmapArray;
    }

    private Drawable[] splitImageDrawable() {
        Drawable[] splitImages = new Drawable[2];
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.srm_225);
        int splitYPos = bitmap.getHeight() / 2;

        splitImages[0] = new BitmapDrawable(getResources(), Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), splitYPos));
        splitImages[1] = new BitmapDrawable(getResources(), Bitmap.createBitmap(bitmap, 0, splitYPos, bitmap.getWidth(), splitYPos));

        return splitImages;
    }

    private Bitmap[] splitImageBitmap() {
        Bitmap[] bitmapArray = new Bitmap[2];
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.srm_225);
        int splitYPos = bitmap.getHeight() / 2;

        bitmapArray[0] = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), splitYPos);
        bitmapArray[1] = Bitmap.createBitmap(bitmap, 0, splitYPos, bitmap.getWidth(), splitYPos);

        return bitmapArray;
    }

    private void moveImageLeftAnim() {
        Drawable[] drawables = splitImageDrawable();

    }

    private void moveImageRightAnim() {

    }

    public void updateRecycler(String modelName, List<Card> cardList) {
    }

    private void swipeModel(int direction) {
        //TODO: change models when button is clicked or image is swiped
        //TODO: will close model image tray
        //TODO: fires changeModelImage method
        switch(direction) {
            case LEFT: break;
            case RIGHT: break;
        }
    }

    private class DrawerState {
        boolean drawerClosed = true;

        void setDrawerStateClosed(boolean closed) {drawerClosed = closed;}
        boolean isDrawerClosed() {return drawerClosed;}
    }

    public interface Callback {
        boolean updateRecycler();
        void nullifyListeners();
        void restoreListeners();
        void animateRecyclerUp();
        void animateRecyclerDown();
    }
}
