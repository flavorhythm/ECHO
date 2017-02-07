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

import data.card_content.CardContent;
import util.FragSpec;
import util.ImageLoaderTask;
import util.MetricCalc;
import widget.ScrollingHeaderView;

/**
 * Created by flavorhythm on 1/24/17.
 */
//TODO: accepts list of Models to display as new instance param.
    //Same as FragmentHome
public class FragmentSubList extends FragmentBase implements ObservableScrollViewCallbacks {
    public static final String LIST_KEY = "model_list";
    private static int currentScroll = 0;

    private ObservableRecyclerView mRecycler;
    private ScrollingHeaderView mRecyclerBg;

    public static FragmentSubList newInstance(CardContent cc/*List<ParcelModel> modelList*/) {
        Bundle args = new Bundle();

        FragmentSubList fragment = new FragmentSubList();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View customView = inflater.inflate(R.layout.frag_recycler_header, container, false);

        mRecycler = (ObservableRecyclerView)customView.findViewById(R.id.recycler_header_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setHasFixedSize(false); //may need to change in future
        mRecycler.addOnScrollListener(
                getToolbar().getScrollListener(currentScroll, getToolbarAlphaThreshold(FragSpec.HOME))
        );

        mRecyclerBg = (ScrollingHeaderView)customView.findViewById(R.id.recycler_header_bg);

        final View headerView = new View(getContext());
        headerView.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                MetricCalc.getDrawerHeaderHeight()
        ));

        mRecycler.setScrollViewCallbacks(this);

        if(cancelPotentialWork(R.drawable.drawer_header, mRecyclerBg.getLoaderTask())) {
            mRecyclerBg.setLoaderTask(new ImageLoaderTask(this, mRecyclerBg));
            ImageLoaderTask task = mRecyclerBg.getLoaderTask();
            AsyncTaskCompat.executeParallel(task, R.drawable.drawer_header);
        }

        return customView;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        currentScroll = scrollY;

        getToolbar().setRawY(currentScroll);
        mRecyclerBg.scrollTo(0, scrollY);
    }

    @Override
    public void onDownMotionEvent() {}

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {}
}
