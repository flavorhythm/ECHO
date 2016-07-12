package fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.echo_usa.echo.R;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import util.ScreenSize;

/**
 * Created by zyuki on 6/7/2016.
 */
public class FragmentGarage extends BaseFragment implements ObservableScrollViewCallbacks {
    public static FragmentGarage thisFragment;

    private View mImageView;
    private View mOverlayView;
    private View mListBackgroundView;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;

    private ObservableListView listView;

    public static FragmentGarage newInstance() {
        if(thisFragment == null) thisFragment = new FragmentGarage();
        return thisFragment;
    }

    @Override
    public void updateFragContent(String modelName) {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mFlexibleSpaceImageHeight = ScreenSize.getDrawerHeaderHeight(getActivity());
        mFlexibleSpaceShowFabOffset = mFlexibleSpaceImageHeight / 2;
        mActionBarSize = ScreenSize.getActionBarSize(getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.fragName = "right_drawer_garage";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_garage;
        View fragView = inflater.inflate(layoutRes, container, false);

        mImageView = fragView.findViewById(R.id.garage_header);
        mOverlayView = fragView.findViewById(R.id.garage_overlay);

        FrameLayout.LayoutParams lpHeader = new FrameLayout.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                mFlexibleSpaceImageHeight
        );
        mImageView.setLayoutParams(lpHeader);
        mOverlayView.setLayoutParams(lpHeader);

        listView = (ObservableListView)fragView.findViewById(R.id.garage_recycler);
        listView.setScrollViewCallbacks(this);

        // Set header_padding view for ListView. This is the flexible space.
        View paddingView = new View(getActivity());
        AbsListView.LayoutParams lpPadding = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                mFlexibleSpaceImageHeight
        );
        paddingView.setLayoutParams(lpPadding);

        // This is required to disable header_base's list selector effect
        paddingView.setClickable(true);

        listView.addHeaderView(paddingView);
        listView.setAdapter(dataAccess.getGarageAdapter());

        mFab = fragView.findViewById(R.id.garage_button_addUnit);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "FAB is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        // mListBackgroundView makes ListView's background except header_base view.
        mListBackgroundView = fragView.findViewById(R.id.garage_background);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String modelName = parent.getAdapter().getItem(position).toString();
                Log.d("FragmentGarage", "onItemClick: " + modelName + " selected");
                valueChange.enqueueModelNameChange(modelName);
//                callback.setModelName(modelName);
                callback.closeDrawer(GravityCompat.END);
            }
        });

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

    @Override
    public final void onDownMotionEvent() {
        // We don't use this callback in this pattern.
    }

    @Override
    public final void onUpOrCancelMotionEvent(ScrollState scrollState) {
        // We don't use this callbac
    }

}
