package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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

import adapter.CardAdapter;
import data.card_content.CardContent;
import util.FragSpec;
import util.ImageLoaderTask;
import util.MetricCalc;
import widget.ScrollingHeaderView;

/**
 * Created by zyuki on 6/3/2016.
 */
public class FragmentHome extends FragmentBase implements ObservableScrollViewCallbacks/*, ImageLoaderTask.Callback*/ {
    private static int currentScroll = 0;

    private ObservableRecyclerView mRecycler;
    private ScrollingHeaderView mRecyclerBg;

    public static FragmentHome newInstance() {
        Log.v("FragmentHome", "newInstance");
        return new FragmentHome();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final int layoutRes = R.layout.frag_recycler_header;
        View fragView = inflater.inflate(layoutRes, container, false);

        mRecycler = (ObservableRecyclerView)fragView.findViewById(R.id.recycler_header_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setHasFixedSize(false); //may need to change in future
        mRecycler.addOnScrollListener(
                getToolbar().getScrollListener(currentScroll, getToolbarAlphaThreshold(Threshold.W_HEADER))
        );

        mRecyclerBg = (ScrollingHeaderView)fragView.findViewById(R.id.recycler_header_bg);

        mRecycler.setAdapter(new CardAdapter(
                getPaddingView(getContext(), MetricCalc.getDrawerHeaderHeight()),
                CardContent.TYPE_INFO,
                getDataAccess().getCards(FragSpec.HOME)
        ));

//        final View headerView = getPaddingView(getContext(), MetricCalc.getDrawerHeaderHeight());

//        List<CardContent> cardList = getDataAccess().getCards(FragSpec.HOME);
//        if(!cardList.isEmpty()) {
////            CardAdapter adapter = new CardAdapter(headerView, CardContent.TYPE_INFO, cardList);
//
//            mRecycler.setAdapter(new CardAdapter(
//                    getPaddingView(getContext(), MetricCalc.getDrawerHeaderHeight()),
//                    CardContent.TYPE_INFO,
//                    getDataAccess().getCards(FragSpec.HOME)
//            ));
//        }

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
//        Log.v("FragmentHome", "onViewCreated");
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        currentScroll = scrollY;

        getToolbar().setRawY(currentScroll);
        mRecyclerBg.scrollTo(0, currentScroll);
    }

    @Override public final void onDownMotionEvent() {}
    @Override public final void onUpOrCancelMotionEvent(ScrollState scrollState) {}
}