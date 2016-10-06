package fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.echo_usa.echo.DataAccessApplication;
import com.echo_usa.echo.R;

import data.DataAccessObject;
import util.MetricCalcs;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentBase extends Fragment {
    protected static Callback callback;
    protected static DataAccessObject dataAccess;

    private static Toolbar mToolbar;

    @Override
    public void onAttach(Context context) {
        Log.v("FragmentBase", "onAttach");
        super.onAttach(context);

        callback = (Callback)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("FragmentBase", "onCreate");
        super.onCreate(savedInstanceState);

        dataAccess = ((DataAccessApplication)getActivity().getApplication()).getDataAccessObject();
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        Log.v("FragmentBase", "onViewCreated: " + fragmentView.toString());
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.v("FragmentBase", "onResume: " + getContext().toString());
        Log.v("FragmentBase", "onResume: is equals? " + String.valueOf(callback.equals(getContext())));
        //mToolbar.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.app_bar_solid));

//        if(mToolbar != null) {
//            mToolbar.setBackground(
//                    ContextCompat.getDrawable(getContext(), R.drawable.app_bar_solid)
//            );
//        }
        super.onResume();

        if(callback != null && !callback.equals(getContext())) callback = (Callback)getContext();
        Log.v("FragmentBase", "onResume: is now? " + String.valueOf(callback.equals(getContext())));
    }

    @Override
    public void onPause() {
        //TODO: still not working
        super.onPause();
    }

    public void setToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
    }

    public Toolbar getToolbar() throws NullPointerException {
        if(mToolbar != null) return mToolbar;
        else throw new NullPointerException("toolbar is null");
    }

    public RecyclerView.OnScrollListener getListenerWithThreshold(final int currentScroll, final int threshold) {
        return new RecyclerView.OnScrollListener() {
            final int adjThreshold = threshold >= MetricCalcs.getActionBarSize(getContext()) ?
                    threshold - MetricCalcs.getActionBarSize(getContext()) : 0;
            int verticalOffset = currentScroll;
            int deltaY = 0;

            private boolean thresholdCondition() {return adjThreshold > verticalOffset;}

            private int getAlpha(Double offsetDouble, Double thresholdDouble) {
                final int maxAlpha = 255;

                if(adjThreshold != 0) {
                    double percentage = (offsetDouble / thresholdDouble) < 1f ?
                            offsetDouble / thresholdDouble : 1f;
                    return (int)Math.floor(maxAlpha * percentage);
                } else return maxAlpha;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                try {
                    Toolbar toolbar = getToolbar();

                    if(thresholdCondition()) {
                        showToolbarAnim();
                        return;
                    }

                    if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if(deltaY > 0) {
                            if(verticalOffset <= toolbar.getTranslationY()) showToolbarAnim();
                        } else if(deltaY < 0){
                            boolean condition = toolbar.getTranslationY() < toolbar.getHeight() * -0.6;

                            if(condition) hideToolbarAnim();
                            else showToolbarAnim();
                        } else showToolbarAnim();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                verticalOffset += dy;
                deltaY = dy;

                try {
                    Toolbar toolbar = getToolbar();
                    //Must be declared here or weird threshold setting bug arises
                    Drawable toolbarBg = ContextCompat.getDrawable(getContext(), R.drawable.app_bar_solid);
                    toolbarBg.setAlpha(getAlpha((double)verticalOffset, (double)adjThreshold));

                    toolbar.setBackground(toolbarBg);

                    if(thresholdCondition()) return;

                    int toolbarOffsetY = (int)(dy - toolbar.getTranslationY());
                    toolbar.animate().cancel();

                    if(deltaY > 0) {
                        if(toolbarOffsetY < toolbar.getHeight()) toolbar.setTranslationY(-toolbarOffsetY);
                        else toolbar.setTranslationY(-toolbar.getHeight());

                    } else if(deltaY < 0) {
                        if (toolbarOffsetY < 0) toolbar.setTranslationY(0);
                        else toolbar.setTranslationY(-toolbarOffsetY);

                    } else toolbar.setTranslationY(-toolbarOffsetY);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void showToolbarAnim() {
        mToolbar.animate()
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
        mToolbar.animate()
                .translationY(-mToolbar.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                }).start();
    }

    public interface Callback {
        View.OnClickListener getCardListnener();
        void scrollToolbar(int scrollY, int actionbarSize, int vertThreshold);
        void setToolbar(Toolbar toolbar);
        void closeDrawer(int gravity);
        void setGarageBtnVisibility(boolean visible);
    }
}
