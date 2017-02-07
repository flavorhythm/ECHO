package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.echo_usa.echo.R;

import adapter.CardAdapter;
import data.card_content.CardContent;
import data.navigation.Locator;
import util.FragSpec;
import util.MetricCalc;

/**
 * Created by zyuki on 6/2/2016.
 */
//TOD: locator legend requires information buttons that display text on a DialogFragment when clicked



//THIS MAY NOT BE POSSIBLE SINCE SERVER COSTS MAY INCREASE. THIS PROJECT TECHNICALLY HAS
//A BUDGET OF $0

public class FragmentLocator extends FragmentBase {
    //TODO: loads top 10 nearest dealers to current location. Clicking card will open Map intent
    //for directions.

    public static FragmentLocator newInstance(/*@Nullable LocationSetting l*/) {
        FragmentLocator f = new FragmentLocator();
        Bundle b = new Bundle();

//        if(l != null) {
//            //TODO: finish here
//
//        }

        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO: will require AsyncTask to load dealer list

        View customView = inflater.inflate(R.layout.frag_recycler_no_header, container, false);

        int padding = MetricCalc.dpToPxById(getContext(), R.dimen.standard_padding);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        TextView tv = new TextView(getContext());
        tv.setPadding(padding, padding / 2, padding, padding / 2);
        tv.setLayoutParams(lp);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText(getResources().getString(R.string.locator_text));

        if(customView instanceof FrameLayout) {
            final View headerView = getPaddingView(getContext(), MetricCalc.getActionBarSize(getContext()));
            ((FrameLayout)customView).addView(tv, 0);
            ((FrameLayout)customView).addView(headerView, 0);
        }

        RecyclerView recycler = (RecyclerView)customView.findViewById(R.id.recycler_no_header_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setHasFixedSize(false);

        recycler.setAdapter(
                new CardAdapter(null, CardContent.TYPE_DEALER, getDataAccess().getCards(FragSpec.LOCATOR))
        );

        recycler.addOnScrollListener(
                getToolbar().getScrollListener(0, getToolbarAlphaThreshold(Threshold.NO_HEADER))
        );
        return customView;
    }

    public void updateContents(Locator data) {}
}
