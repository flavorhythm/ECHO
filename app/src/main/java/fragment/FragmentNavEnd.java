package fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;

import com.echo_usa.echo.R;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import adapter.NavEndAdapter;
import util.FragSpec;
import util.MetricCalc;

/**
 * Created by zyuki on 6/7/2016.
 */
public class FragmentNavEnd extends FragmentBase implements ObservableScrollViewCallbacks {
    private View mImageView;
    private View mOverlayView;
    private View mListBackgroundView;
    private View mHeader;
    private Button addNewUnit;

    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;

    private FrameLayout.LayoutParams mHeaderLp;
    private AbsListView.LayoutParams mPaddingLp;

    private ObservableRecyclerView mRecycler;
//    private NavEndAdapter mAdapter;

    private NavEndAdapter mCatalogAdapter, mLocatorAdapter, mModelAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActionBarSize = MetricCalc.getActionBarSize(getActivity());
        mFlexibleSpaceImageHeight = MetricCalc.getDrawerHeaderHeight();

        mHeaderLp = new FrameLayout.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                mFlexibleSpaceImageHeight
        );

        mPaddingLp = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                mFlexibleSpaceImageHeight
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

        mHeader = new View(getActivity());
        mHeader.setLayoutParams(mPaddingLp);
        // This is required to disable header_base's list selector effect
        mHeader.setClickable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_nav_end;
        View fragView = inflater.inflate(layoutRes, container, false);

        mImageView = fragView.findViewById(R.id.garage_header);
        mOverlayView = fragView.findViewById(R.id.garage_overlay);

        mImageView.setLayoutParams(mHeaderLp);
        mOverlayView.setLayoutParams(mHeaderLp);

//        mRecycler = (ObservableListView) fragView.findViewById(R.id.garage_recycler);
        mRecycler = (ObservableRecyclerView)fragView.findViewById(R.id.garage_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecycler.setScrollViewCallbacks(this);

//        mRecycler.addHeaderView(mHeader);

        addNewUnit = (Button)fragView.findViewById(R.id.garage_button_addUnit);
        addNewUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentRouter.initAddUnit("");
            }
        });

        mListBackgroundView = fragView.findViewById(R.id.garage_background);

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
//        mCatalogAdapter = new NavEndAdapter(mHeader, mDataAccess.getNavList(FragSpec.CATALOG));
//        mLocatorAdapter = new NavEndAdapter(mHeader, getDataAccess().getNavList(FragSpec.LOCATOR));
        mModelAdapter = new NavEndAdapter(mHeader, getDataAccess().getNavList(FragSpec.MODEL_DOCS));
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int overlayViewHeight = mOverlayView.getHeight();
        int overlayViewWidth = mOverlayView.getWidth();

        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - overlayViewHeight;
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
    }

    public void addNewUnit(String modelName) {
        Snackbar.make(getActivity().findViewById(R.id.main_drawer_layout), "click", Snackbar.LENGTH_SHORT).show();
    }

    public boolean updateContent(FragSpec fragSpec) {
        switch(fragSpec) {
//            case CATALOG: mRecycler.setAdapter(mCatalogAdapter); break;
//            case LOCATOR: mRecycler.setAdapter(mLocatorAdapter); return true;
            case MODEL_DOCS: mRecycler.setAdapter(mModelAdapter); return true;
            default: return false;
        }
    }

    @Override public final void onDownMotionEvent() {}
    @Override public final void onUpOrCancelMotionEvent(ScrollState scrollState) {}
}
