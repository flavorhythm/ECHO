package fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.echo_usa.echo.DataAccessApplication;
import com.echo_usa.echo.R;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import adapter.CardAdapter;
import data.DataAccessObject;
import util.ScreenSize;

/**
 * Created by zyuki on 6/3/2016.
 */
public class FragmentHome extends BaseFragment implements ObservableScrollViewCallbacks {
    private static FragmentHome thisFragment;

    private View mImageView;
    private View mOverlayView;
    private View mRecyclerViewBackground;
    private int flexibleSpaceImageHeight;

    public static FragmentHome newInstance() {
        if(thisFragment == null) thisFragment = new FragmentHome();
        return thisFragment;
    }

    @Override
    public void updateFragContent(String modelName) {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        flexibleSpaceImageHeight = ScreenSize.getHomeHeaderHeight(getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.fragName = "home";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_home;
        View fragView = inflater.inflate(layoutRes, container, false);

        ObservableRecyclerView
                cardsRecycler = (ObservableRecyclerView)fragView.findViewById(R.id.home_recycler);

        cardsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        cardsRecycler.setHasFixedSize(false); //may need to change in future

        final View headerView = LayoutInflater.from(getActivity())
                .inflate(R.layout.header_base, container, false);

        mRecyclerViewBackground = fragView.findViewById(R.id.home_background);
        mImageView = fragView.findViewById(R.id.home_header);
        mOverlayView = fragView.findViewById(R.id.home_image_overlay);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                flexibleSpaceImageHeight
        );

        headerView.setLayoutParams(lp);
        mImageView.setLayoutParams(lp);
        mOverlayView.setLayoutParams(lp);

        cardsRecycler.setAdapter(
                new CardAdapter(
                        dataAccess.getCards(DataAccessObject.CARDS_FOR_HOME),
                        headerView,
                        callback.getCardListnener()
                ).setActivity(getActivity())
        );

        cardsRecycler.setScrollViewCallbacks(this);

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        int mActionBarSize = ScreenSize.getActionBarSize(getActivity());
        float flexibleRange = flexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();

        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mRecyclerViewBackground, Math.max(0, -scrollY + flexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        boolean isBeforeFlexRange = flexibleRange == ScrollUtils.getFloat(scrollY, flexibleRange - 1, flexibleRange);

        Log.d("scroll", "Still scrolling");
        if(FragmentRouter.isInstantiated()) FragmentRouter.actionBarFadeScroll(scrollY, mActionBarSize, flexibleRange);
    }

    @Override
    public final void onDownMotionEvent() {}

    @Override
    public final void onUpOrCancelMotionEvent(ScrollState scrollState) {}
}