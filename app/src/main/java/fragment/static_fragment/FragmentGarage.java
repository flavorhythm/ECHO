package fragment.static_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;

import com.echo_usa.echo.R;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import adapter.GarageAdapter;
import data.Model;
import fragment.FragmentRouter;
import fragment.repacement_fragment.FragmentModelInfo;
import util.MetricCalcs;

/**
 * Created by zyuki on 6/7/2016.
 */
public class FragmentGarage extends FragmentModelInfo implements ObservableScrollViewCallbacks {
    private View mImageView;
    private View mOverlayView;
    private View mListBackgroundView;
    private View paddingView;

    //private CustomFloatingActionButton mFab;
    private Button addNewUnit;

    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;

    private FrameLayout.LayoutParams lpHeader;
    private AbsListView.LayoutParams lpPadding;

    private ObservableListView listView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActionBarSize = MetricCalcs.getActionBarSize(getActivity());
        mFlexibleSpaceImageHeight = MetricCalcs.getDrawerHeaderHeight(mActionBarSize);
//        mFlexibleSpaceShowFabOffset = mFlexibleSpaceImageHeight / 2;

        lpHeader = new FrameLayout.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                mFlexibleSpaceImageHeight
        );

        lpPadding = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                mFlexibleSpaceImageHeight
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paddingView = new View(getActivity());
        paddingView.setLayoutParams(lpPadding);
        // This is required to disable header_base's list selector effect
        paddingView.setClickable(true);

//        this.fragName = "right_drawer_garage";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_garage;
        View fragView = inflater.inflate(layoutRes, container, false);

        mImageView = fragView.findViewById(R.id.garage_header);
        mOverlayView = fragView.findViewById(R.id.garage_overlay);

        mImageView.setLayoutParams(lpHeader);
        mOverlayView.setLayoutParams(lpHeader);

        listView = (ObservableListView) fragView.findViewById(R.id.garage_recycler);
        listView.setScrollViewCallbacks(this);

        listView.addHeaderView(paddingView);

        addNewUnit = (Button)fragView.findViewById(R.id.garage_button_addUnit);
//        addNewUnit = (Button)inflater.inflate(R.layout.button_new_unit, container, false);
        addNewUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRouter.initAddUnit("");
            }
        });

        //mFab = (CustomFloatingActionButton)fragView.findViewById(R.id.garage_button_addUnit);
        // mListBackgroundView makes ListView's background except header_base view.
        mListBackgroundView = fragView.findViewById(R.id.garage_background);

//        listView.bringToFront();
//        fragView.findViewById(R.id.garage_bottom_shadow).invalidate();

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(fragmentView, savedInstanceState);

        listView.setAdapter(new GarageAdapter(getContext(), R.layout.item_garage_list, dataAccess.getModelList()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int originalPos, long id) {
                int adjustedPos = originalPos - 1;

                if(parent.getAdapter().getItem(adjustedPos) instanceof Model) {
                    Model thisModel = (Model)parent.getAdapter().getItem(adjustedPos);

                    String modelName = thisModel.getModelName();
                    Log.d("FragmentGarage", "onItemClick: " + modelName + " selected");
                    getValueChange().enqueueModelChange(thisModel);
//                callback.setModelName(modelName);
                    callback.closeDrawer(GravityCompat.END);
                }
            }
        });
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int overlayViewHeight = mOverlayView.getHeight();
        int overlayViewWidth = mOverlayView.getWidth();
        //int fabHeight = mFab.getHeight();

        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - overlayViewHeight;
        //int maxFabTranslationY = mFlexibleSpaceImageHeight - fabHeight / 2;
        /*float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - fabHeight / 2,
                mActionBarSize - fabHeight / 2,
                maxFabTranslationY);
*/
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        //mFab.scrollChange(overlayViewWidth, fabTranslationY);
    }

    public void addNewUnit(String modelName) {
        Snackbar.make(getActivity().findViewById(R.id.main_drawer_layout), "click", Snackbar.LENGTH_SHORT).show();
    }

    @Override public final void onDownMotionEvent() {}
    @Override public final void onUpOrCancelMotionEvent(ScrollState scrollState) {}
}
