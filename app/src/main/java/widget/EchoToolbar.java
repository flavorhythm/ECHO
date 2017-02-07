package widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.echo_usa.echo.R;

import fragment.FragmentRouter;
import util.FragSpec;
import util.MetricCalc;

import static widget.EchoToolbar.Direction.DOWN;
import static widget.EchoToolbar.Direction.UP;

/**
 * Created by flavorhythm on 1/15/17.
 */

public class EchoToolbar extends Toolbar {
    private float sRawTranslationY = 0;

    private static boolean sEndDrawerEnabled = false;

    private Callback mCallback;

    public EchoToolbar(Context context) {
        super(context);
        initialize(null);
    }

    public EchoToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public EchoToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        if(attrs != null) {}

//        setTranslationY(-MetricCalc.getActionBarSize(getContext()));

        mCallback = (Callback)getContext();
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.app_bar_solid));
        getBackground().setAlpha(EchoSuperView.ALPHA_GONE);
    }

    @Override
    public void setTranslationY(float translationY) {
        translationY = clampTranslation(translationY, getMinY(), getMaxY());
        super.setTranslationY(translationY);
    }

    public void onPrepareFragmentMenu() {
        FragSpec displayed = FragmentRouter.getDisplayed();
        enableEndDrawer(displayed.isToolbarBtnEnabled());

        if(!displayed.hasRecycler()) {
            int toAlpha = displayed.isToolbarFilled() ? EchoSuperView.ALPHA_VISIBLE : EchoSuperView.ALPHA_GONE;
            if(toAlpha != getBackground().getAlpha()) getAlphaToolbarBgAnim(this, toAlpha).start();
        }
    }
    private void enableEndDrawer(boolean toEnable) {
        if(sEndDrawerEnabled != toEnable) {
            getAlphaToolbarBtnAnim(this, toEnable).start();
        } else enableNavEndBtn(toEnable);
    }

    public RecyclerView.OnScrollListener getScrollListener(float currentScroll, final int thresholdY) {
        setRawY(currentScroll);

        return new RecyclerView.OnScrollListener() {
            int thisGestureDeltaY = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                String state;
                switch(newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING: state = "dragging";
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING: state = "settling";
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE: state = "idle";
                        break;
                    default: state = "n/a";
                }
                Log.i("EchoToolbar", state);
                float modifier = 0f;
                //New State is idle at the end of every scroll gesture
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //If clause for when gesture fling up ends
                    if(thisGestureDeltaY > 0) modifier = -0.6f;
                        //Else clause for when gesture fling down ends
                    else if(thisGestureDeltaY < 0) modifier = -0.4f;
//                    else showToolbarAnim(); //may not need

                    if(Math.abs(getRawY()) < thresholdY) showToolbarAnim();
                    else if(getTranslationY() < getHeight() * modifier) hideToolbarAnim();
                    else showToolbarAnim();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                thisGestureDeltaY = dy;
                addToRawY(dy);

                if(thresholdY != 0) {
                    getBackground().setAlpha(
                            getAlpha((double)getRawY(), (double)thresholdY)
                    );
                    Log.v("EchoToolbar", String.valueOf(getBackground().getAlpha()));
                } else getAlphaToolbarBgAnim(EchoToolbar.this, EchoSuperView.ALPHA_VISIBLE).start();

                if(Math.abs(getRawY()) < thresholdY) return;

                animate().cancel();
                int toolbarOffsetY = (int)(dy - getTranslationY());
                if(thisGestureDeltaY > 0) {
                    toolbarOffsetY = toolbarOffsetY < getHeight() ? -toolbarOffsetY : -getHeight();
                } else if(thisGestureDeltaY < 0) {
                    toolbarOffsetY = toolbarOffsetY < 0 ? 0 : -toolbarOffsetY;
                } else toolbarOffsetY = -toolbarOffsetY;

                setTranslationY(toolbarOffsetY);
            }

            private int getAlpha(Double rawY, Double threshold) {
                double percentage = (rawY / threshold);
                if(percentage > 1f) percentage = 1f;
                else if(percentage < 0f) percentage = 0f;

                return (int)Math.floor(EchoSuperView.ALPHA_VISIBLE * percentage);
            }
        };
    }

    public void setRawY(float rawY) {sRawTranslationY = rawY;}

    private void addToRawY(float rawY) {
        sRawTranslationY += rawY;
    }

    private float getRawY() {
        return sRawTranslationY;
    }

    private static ValueAnimator getAlphaToolbarBgAnim(final EchoToolbar t, final int toAlpha) {
        ValueAnimator va = ValueAnimator.ofInt(t.getBackground().getAlpha(), toAlpha);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int)animation.getAnimatedValue();

                t.getBackground().setAlpha(i);
                t.invalidate();
            }
        });

        return va;
    }

    private static ValueAnimator getAlphaToolbarBtnAnim(final EchoToolbar t, final boolean toEnable) {
        final int alpha = toEnable ? 255 : 0;
        ValueAnimator va = ValueAnimator.ofInt(Math.abs(alpha - 255), alpha);
//        if(!toEnable) va.setStartDelay(150);
        va.setDuration(150);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int)animation.getAnimatedValue();

                t.getMenu().getItem(0).getIcon().setAlpha(i);
                t.invalidate();
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                if(toEnable) t.enableNavEndBtn(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if(!toEnable) t.enableNavEndBtn(false);
            }
        });
        return va;
    }

    private void enableNavEndBtn(boolean toEnable) {
        sEndDrawerEnabled = toEnable;

        mCallback.lockEndDrawer(!toEnable);
        getMenu().setGroupEnabled(R.id.menu_garage_group, toEnable);
        getMenu().setGroupVisible(R.id.menu_garage_group, toEnable);
    }

//    private static ValueAnimator getTextColorAnim(final EchoToolbar t, boolean toBlack) {
//        int toColor = toBlack ? R.color.black : R.color.white;
//        AnimatorSet as = new AnimatorSet();
//        as.playSequentially(getToolbarTransAnim(t, UP), getToolbarTransAnim(t, DOWN));
//        return as;
//    }

    private static ValueAnimator getToolbarTransAnim(final EchoToolbar t, Direction d) {
//        final int abHeight = MetricCalc.getActionBarSize(t.getContext());
        int start = d == UP ? 0 : -t.getHeight();

        ValueAnimator va = ValueAnimator.ofFloat(start, -t.getHeight() - start);
        va.setDuration(150);
        va.setInterpolator(new AccelerateDecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float)animation.getAnimatedValue();

                t.setTranslationY(f);
                t.invalidate();
            }
        });

        return va;
    }

    private void showToolbarAnim() {
        animate()
            .translationY(0)
            .setInterpolator(new LinearInterpolator())
            .setDuration(180)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            }).start();
    }

    private void hideToolbarAnim() {
        //((TransitionDrawable)mActionbarBg).startTransition(180);
        animate()
            .translationY(-getHeight())
            .setInterpolator(new LinearInterpolator())
            .setDuration(180)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            }).start();
    }

    private float clampTranslation(float axisVal, float min, float max) {
        axisVal = axisVal >= min ? axisVal : min;
        axisVal = axisVal <= max ? axisVal : max;

        return axisVal;
    }

    private float getMinY() {
        return -MetricCalc.getActionBarSize(getContext());
    }

    private float getMaxY() {
        return 0;
    }

    enum Direction {
        UP, DOWN
    }

    public interface Callback {
        void lockEndDrawer(boolean toLock);
    }
}
