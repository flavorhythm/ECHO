package widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;

import com.echo_usa.echo.R;

import data.navigation.Model;

/**
 * Created by ZYuki on 6/30/2016.
 */
//TODO: extend EchoView
public class ModelDrawerView extends EchoSuperView {
    private static final float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));
    private static final float INFLEXION = 0.35f;
    private static final int V_UNIT_SCALE = 1000;
    private static final float PPI = Resources.getSystem().getDisplayMetrics().density * 160.0f;
    private static final float PHYSICAL_COEFF = SensorManager.GRAVITY_EARTH // g (m/s^2)
            * 39.37f // inch/meter
            * PPI
            * 0.84f; // look and feel tuning;

    private int mTextHeight;
    private Paint mImageBgPaint;
    private Paint mDisplayedPaint;
    private Paint mEnqueuedPaint;
    private Paint mTitleBgPaint;
    private TextPaint mDisplayedTextPaint;
    private TextPaint mEnqueuedTextPaint;

    private Point mTitleOrigin;
    private Point mImageOrigin;
//    private Point mEnqueuedOrigin;

    private CharSequence mDisplayedName;
    private StaticLayout mDisplayedLayout;

    private CharSequence mEnqueuedName;
    private StaticLayout mEnqueuedLayout;

    private Rect mBgBounds;
    private Rect mTextBgBounds;
    private Rect mTopShadowBounds;
    private Rect mBtmShadowBounds;
    private Rect mImgMask;

    private Bitmap mDisplayedBitmap, mEnqueuedBitmap;
    private GradientDrawable mDrawerShadow, mTopShadow, mBtmShadow;
    private VectorDrawableCompat mDrawerArrow;

    private Callback mCallback;

    private VelocityTracker mVelocityTracker;
    private float mPrevX, mPrevY;
    private boolean mIsDragging = false;
    private FlingRunnable mFlingRunnable;
    private Model mDisplayedModel;

    public ModelDrawerView(Context context) {
        super(context);
    }

    public ModelDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ModelDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ModelDrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);

        if(mDisplayedModel != null) {
            savedState.savedRes = mDisplayedModel.getImgResource();
            savedState.savedName = mDisplayedModel.getModelName();
            savedState.savedSerial = mDisplayedModel.getSerialNum();

            return savedState;
        }

        return null;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());

        mDisplayedModel = new Model(
                savedState.savedRes,
                savedState.savedName,
                savedState.savedSerial,
                0
        );

        updateModel();
    }

    @Override
    protected void initialize(AttributeSet attrs) {
        if(attrs != null) {}
        setSaveEnabled(true);

        mVelocityTracker = VelocityTracker.obtain();
        mFlingRunnable = new FlingRunnable(this);

        mDrawerArrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_vector_arrow_up, null);

        mImageBgPaint = getPaintObj(getContext(), ALPHA_VISIBLE, R.color.white);
        mTitleBgPaint = getPaintObj(getContext(), ALPHA_VISIBLE, R.color.bg_garage);
        mDisplayedPaint = getPaintObj(getContext(), ALPHA_VISIBLE, NO_COLOR);
        mEnqueuedPaint = getPaintObj(getContext(), ALPHA_GONE, NO_COLOR);

        mDisplayedTextPaint = getTextPaintObj(getContext(), R.dimen.model_name_size, R.color.echo_orange);
        mEnqueuedTextPaint = getTextPaintObj(getContext(), R.dimen.model_name_size, R.color.echo_orange);

        mDrawerShadow = (GradientDrawable)ContextCompat.getDrawable(getContext(), R.drawable.bg_model_info_lower_fade);
        mBtmShadow = (GradientDrawable)ContextCompat.getDrawable(getContext(), R.drawable.bg_model_info_upper_fade);
        mTopShadow = (GradientDrawable)ContextCompat.getDrawable(getContext(), R.drawable.bg_model_info_lower_fade);

        mTitleOrigin = new Point(0, 0);
        mTextBgBounds = new Rect(0, getTitleBarTop(), SCREEN_WIDTH, getTitleBarBtm());

        Rect shadowBounds = new Rect(0, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT + SHADOW_HEIGHT);
        mDrawerShadow.setBounds(shadowBounds);

        mBtmShadowBounds = new Rect(0, SCREEN_HEIGHT - SHADOW_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        mBtmShadow.setBounds(mBtmShadowBounds);

        mTopShadowBounds = new Rect(0, AB_HEIGHT * 2, SCREEN_WIDTH, AB_HEIGHT * 2 + SHADOW_HEIGHT);
        mTopShadow.setBounds(mTopShadowBounds);

        mImageOrigin = new Point(getImageLeft(), getImageTop());

        mBgBounds = new Rect(0, getBgTop(), SCREEN_WIDTH, SCREEN_HEIGHT);
        mImgMask = new Rect(0, getBgTop(), SCREEN_WIDTH, SCREEN_HEIGHT);

        Rect arrowBounds = new Rect(
                (SCREEN_WIDTH - mDrawerArrow.getIntrinsicWidth()) / 2,
                SCREEN_HEIGHT - PADDING - mDrawerArrow.getIntrinsicHeight(),
                (SCREEN_WIDTH + mDrawerArrow.getIntrinsicWidth()) / 2,
                SCREEN_HEIGHT - PADDING
        );
        mDrawerArrow.setBounds(arrowBounds);
    }

    public Model getDisplayedModel() {return mDisplayedModel;}

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(l != oldl || t != oldt) {
            updateContentBounds();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        x = clampScroll(x, 0, 0);
        y = clampScroll(y, 0, DRAWER_OPEN_SIZE);

        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mIsDragging = false;
                mVelocityTracker.clear();

                mPrevX = event.getX();
                mPrevY = event.getY();

                if((SCREEN_HEIGHT - event.getY()) > getScrollY()) {
                    mFlingRunnable.abortIfFlingPersists();
                    return true;
                } else return false;
            case MotionEvent.ACTION_MOVE:
                final float xPos = event.getX();
                final float yPos = event.getY();
                float xDelta = mPrevX - xPos;
                float yDelta = mPrevY - yPos;

                int slop = getTouchSlop();
                boolean touchSlopCondition = Math.abs(xDelta) > slop || Math.abs(yDelta) > slop;
                if(!mIsDragging && touchSlopCondition) {mIsDragging = true;}
                if(mIsDragging) {
                    scrollBy((int)xDelta, (int)yDelta);

                    mPrevX = xPos;
                    mPrevY = yPos;
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsDragging = false;
                mVelocityTracker.computeCurrentVelocity(V_UNIT_SCALE, getFlingVelocity(DRAWER_OPEN_SIZE));
                int velocityY = (int)mVelocityTracker.getYVelocity();

                mFlingRunnable.flingY(
                        getScrollY(),
                        adjustVelocity(velocityY)
                );
                break;
            case MotionEvent.ACTION_CANCEL:
                mIsDragging = false;
                mFlingRunnable.abortIfFlingPersists();
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mDrawerShadow != null) mDrawerShadow.draw(canvas);
        if(mBtmShadow != null) mBtmShadow.draw(canvas);

        if(mImgMask != null) {
            canvas.save();
            canvas.clipRect(mImgMask, Region.Op.INTERSECT);
            int x = mImageOrigin != null ? mImageOrigin.x : 0;
            int y = mImageOrigin != null ? mImageOrigin.y : 0;

            if(mBgBounds != null) canvas.drawRect(mBgBounds, mImageBgPaint);
            if(mDisplayedBitmap != null) canvas.drawBitmap(mDisplayedBitmap, x, y, mDisplayedPaint);
            if(mEnqueuedBitmap != null) canvas.drawBitmap(mEnqueuedBitmap, x, y, mEnqueuedPaint);

            canvas.restore();
        }

        if(mTopShadow != null) mTopShadow.draw(canvas);
        if(mTextBgBounds != null) canvas.drawRect(mTextBgBounds, mTitleBgPaint);

        if(mTitleOrigin != null) {
            canvas.save();
            canvas.translate(mTitleOrigin.x, mTitleOrigin.y);

            if(mDisplayedLayout != null) mDisplayedLayout.draw(canvas);
            if(mEnqueuedLayout != null) mEnqueuedLayout.draw(canvas);

            canvas.restore();
        }

        if(mDrawerArrow != null) {
            canvas.save();
            canvas.rotate(
                    getDegreeFromScrollY(),
                    mDrawerArrow.getBounds().centerX(),
                    mDrawerArrow.getBounds().centerY()
            );
            mDrawerArrow.draw(canvas);
            canvas.restore();
        }
    }

    public boolean updateModel(Model model) {
        //No need to check for model == null since navEndListener in MainActivity already does so
        if(mDisplayedModel == null) {
            mDisplayedModel = model;
            updateModel();

            return true;
        }

        if(!model.getSerialNum().equals(mDisplayedModel.getSerialNum())) {
            mDisplayedModel = model;

            mFlingRunnable.raiseNewModelFlag();
            mFlingRunnable.flingY(getScrollY(), -getFlingVelocity(getScrollY()));

            return true;
        }

        return false;
    }

    public void setCallback(Callback callback) {mCallback = callback;}

    //TODO: must change along with the way view is updated
    private static ValueAnimator getModelChangeAnim(final ModelDrawerView v) {
        final int w = v.SCREEN_WIDTH;

        ValueAnimator va = ValueAnimator.ofInt(ALPHA_VISIBLE, ALPHA_GONE);
        ValueAnimator.AnimatorUpdateListener alphaListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int alpha = (int)animation.getAnimatedValue();

                if(v.mDisplayedName != null) {
                    v.mDisplayedTextPaint.setAlpha(alpha);
                    Log.d("update", "animationUpdate() displayed a: " + v.mDisplayedTextPaint.getAlpha());
                    v.mDisplayedLayout = getTextLayout(
                            v.mDisplayedName, v.mDisplayedTextPaint, w, ALIGN_CENTER
                    );
                }

                if(v.mEnqueuedName != null) {
                    v.mEnqueuedTextPaint.setAlpha(ALPHA_VISIBLE - alpha);
                    Log.d("update", "animationUpdate() enqueued a: " + v.mEnqueuedTextPaint.getAlpha());
                    v.mEnqueuedLayout = getTextLayout(
                            v.mEnqueuedName, v.mEnqueuedTextPaint, w, ALIGN_CENTER
                    );
                }

                v.mDisplayedPaint.setAlpha(alpha);
                v.mEnqueuedPaint.setAlpha(ALPHA_VISIBLE - alpha);
                v.updateContentBounds();
                v.invalidate();
            }
        };
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.mDisplayedName = v.mEnqueuedName;
                v.mEnqueuedName = null;
                v.mEnqueuedLayout = null;

                if(v.mDisplayedName != null) {
                    v.mDisplayedTextPaint.setAlpha(ALPHA_VISIBLE);
                    v.mDisplayedLayout = getTextLayout(
                            v.mDisplayedName, v.mDisplayedTextPaint, w, ALIGN_CENTER
                    );
                }

                v.mDisplayedBitmap = v.mEnqueuedBitmap;
                v.mEnqueuedBitmap = null;
                v.mDisplayedPaint.setAlpha(ALPHA_VISIBLE);
                v.mEnqueuedPaint.setAlpha(ALPHA_GONE);

                v.updateContentBounds();
                v.invalidate();
            }
        });

        va.addUpdateListener(alphaListener);
        va.setInterpolator(new DecelerateInterpolator());
        return va;
    }

    //TODO: change method to improve rotation animation
    private float getDegreeFromScrollY() {
        final float pos = getScrollY();
        final float max = DRAWER_OPEN_SIZE;
        final float posP = pos / max;

        return (posP) * 180;
    }

    private int getFlingVelocity(double d) {
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        final double l = (decelMinusOne * Math.log(d / (getDefaultFriction() * PHYSICAL_COEFF))) / DECELERATION_RATE;
        return (int)Math.abs((getDefaultFriction() * PHYSICAL_COEFF * Math.exp(l))/INFLEXION);
    }

    private int adjustVelocity(int v) {
        final double pos = getScrollY();
        final double max = DRAWER_OPEN_SIZE;
        final double posP = pos / max;

        if(Math.abs(v) < getMinFling()) {
            return posP > 0.5 ? getFlingVelocity(max - pos) : -getFlingVelocity(pos);
        } else {return v < 0 ? getFlingVelocity(max - pos) : -getFlingVelocity(pos);}
    }

    private void updateModel() {
        Model m = mDisplayedModel;
        if(m != null) {
            mEnqueuedBitmap = BitmapFactory.decodeResource(getContext().getResources(), m.getImgResource());
            mEnqueuedName = m.getModelName();

            mEnqueuedLayout = getTextLayout(mEnqueuedName, mEnqueuedTextPaint, SCREEN_WIDTH, ALIGN_CENTER);
            mTextHeight = mEnqueuedLayout.getHeight();

            getModelChangeAnim(this).start();
        }
    }

    @Override
    protected void updateContentBounds() {
        if(mCallback != null) mCallback.passAnimationValue(DRAWER_OPEN_SIZE - getScrollY());

        if(mTextBgBounds != null) {
            mTextBgBounds.top = getTitleBarTop();
            mTextBgBounds.bottom = getTitleBarBtm();
        }

        if(mTitleOrigin != null) {
            int halfHeight = (int)(mTextHeight / 2 + 0.5f);
            mTitleOrigin.y = AB_HEIGHT + halfHeight + getScrollY();
        }

        if(mTopShadow != null) {
            mTopShadow.setBounds(
                    mTopShadowBounds.left,
                    mTopShadowBounds.top + getScrollY(),
                    mTopShadowBounds.right,
                    mTopShadowBounds.bottom + getScrollY()
            );
        }

        int y = getImageTop() + (int)(getScrollY() * 0.5 + 0.5f);
        if(mImageOrigin != null) mImageOrigin.y = y;

        if(mImgMask != null) {
            mImgMask.top = getBgTop();
        }

        if(mBtmShadow != null) {
            mBtmShadow.setBounds(
                    mBtmShadowBounds.left,
                    mBtmShadowBounds.top + getScrollY(),
                    mBtmShadowBounds.right,
                    mBtmShadowBounds.bottom + getScrollY()
            );
        }
    }

    /**Private method that returns desired width of this view**/
    @Override
    protected int getDesiredWidth() {return SCREEN_WIDTH;}
    /**Private method that returns desired height of this view**/
    @Override
    protected int getDesiredHeight() {return SCREEN_HEIGHT;}

    private int getTouchSlop() {return ViewConfiguration.get(getContext()).getScaledTouchSlop();}
    private int getMinFling() {return ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();}
    private float getDefaultFriction() {return ViewConfiguration.getScrollFriction();}
    private float getCustomFriction() {return getDefaultFriction() * 0.66f;}

    private int getImageLeft() {return (int)((SCREEN_WIDTH - DRAWER_IMAGE_HEIGHT) / 2 + 0.5f);}
    private int getImageTop() {return SCREEN_HEIGHT - DRAWER_IMAGE_WIDTH;}

    private int getTitleBarTop() {return AB_HEIGHT + getScrollY();}
    private int getTitleBarBtm() {return getTitleBarTop() + AB_HEIGHT;}

    private int getBgTop() {return getTitleBarBtm();}

    public interface Callback {
        void passMotionEvent(MotionEvent event);
        void passAnimationValue(int inverseValue);
    }

    static class SavedState extends BaseSavedState {
        int savedRes;
        String savedName;
        String savedSerial;

        SavedState(Parcel source) {
            super(source);

            savedRes = source.readInt();
            savedName = source.readString();
            savedSerial = source.readString();
        }

        SavedState(Parcelable superState) {super(superState);}

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            savedRes = out.readInt();
            savedName = out.readString();
            savedSerial = out.readString();
        }

        static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel source) {return new SavedState(source);}

                    @Override
                    public SavedState[] newArray(int size) {return new SavedState[size];}
                };
    }

    /**Runnable class to assist with scrolling. Since this is a view and not a viewgroup,
     * View.computeScroll is ignored. Instead, scrolling is computed within this runnable class**/
    class FlingRunnable implements Runnable {
        private final View mView;
        private final OverScroller mScroller;
        private int mPrevY = 0;
        private boolean newModelFlag = false;

        FlingRunnable(View v) {
            mScroller = new OverScroller(getContext());
            mView = v;
        }

        void flingY(int posY, int vY) {
            mScroller.fling(
                    0, posY,
                    0, vY,
                    0, 0,
                    0, DRAWER_OPEN_SIZE
            );

            mPrevY = posY;
            postInView();
        }

        public void run() {
            if(mScroller.computeScrollOffset()) {
                int currY = mScroller.getCurrY();

                if (currY != mPrevY) {
                    getView().scrollBy(0, currY - mPrevY);
                    mPrevY = currY;
                }

                postInView();
            } else {
                if(newModelFlag) {
                    updateModel();
                    mScroller.setFriction(getDefaultFriction());
                    newModelFlag = false;
                }
            }
        }

        void postInView() {getView().post(this);}
        View getView() {return mView;}
        boolean isFlinging() {return !mScroller.isFinished();}
        void abortIfFlingPersists() {if(isFlinging()) mScroller.abortAnimation();}

        void raiseNewModelFlag() {
            mScroller.setFriction(getCustomFriction());
            newModelFlag = true;
        }
    }
}
