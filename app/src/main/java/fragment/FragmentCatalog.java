package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.echo_usa.echo.R;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.List;

//import data.card_content.CardCat;
import adapter.CardAdapter;
import data.card_content.CardContent;
import util.FragSpec;
import util.ImageLoaderTask;
import util.MetricCalc;
import widget.ScrollingHeaderView;

/**
 * Created by zyuki on 9/16/2016.
 *
 * potential implementation: have "item class" list of items. Each one opens a list of units
 * (in new activity)? User can click on unit to open further details (instead, in new activity here?)
 *
 * Content mentioned above can be stored in server. Server would require to contain images of units
 * and its respective data. "item class" data is stored within app (maybe even on server and as JSON
 * objects?).
 */
public class FragmentCatalog extends FragmentBase implements ObservableScrollViewCallbacks {
    private static int currentScroll = 0;

    private ObservableRecyclerView mRecycler;
    private ScrollingHeaderView mRecyclerBg;

    public static FragmentCatalog newInstance() {
        return new FragmentCatalog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int layoutRes = R.layout.frag_recycler_header;
        View fragView = inflater.inflate(layoutRes, container, false);

        mRecycler = (ObservableRecyclerView)fragView.findViewById(R.id.recycler_header_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setHasFixedSize(false);
        mRecycler.addOnScrollListener(
                getToolbar().getScrollListener(currentScroll, getToolbarAlphaThreshold(Threshold.W_HEADER))
        );

        mRecyclerBg = (ScrollingHeaderView)fragView.findViewById(R.id.recycler_header_bg);

//        final View headerView = new View(getContext());
//        headerView.setLayoutParams(new FrameLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                MetricCalc.getDrawerHeaderHeight()
//        ));

        List<CardContent> content = getDataAccess().getCards(FragSpec.CATALOG);
        mRecycler.setAdapter(new CardAdapter(
                getPaddingView(getContext(), MetricCalc.getDrawerHeaderHeight()),
                CardContent.TYPE_CATALOG,
                content)
        );
        mRecycler.setScrollViewCallbacks(this);

        if(cancelPotentialWork(R.drawable.drawer_header, mRecyclerBg.getLoaderTask())) {
            mRecyclerBg.setLoaderTask(new ImageLoaderTask(this, mRecyclerBg));
            ImageLoaderTask task = mRecyclerBg.getLoaderTask();
            AsyncTaskCompat.executeParallel(task, R.drawable.drawer_header);
        }

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        currentScroll = scrollY;

        getToolbar().setRawY(currentScroll);
        mRecyclerBg.scrollTo(0, currentScroll);
    }

    @Override public void onDownMotionEvent() {}
    @Override public void onUpOrCancelMotionEvent(ScrollState scrollState) {}
}