package fragment.repacement_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.R;

import adapter.GuideAdapter;
import data.Card;
import fragment.FragmentBase;
import util.MetricCalcs;

/**
 * Created by ZYuki on 7/13/2016.
 */

public class FragmentGuide extends FragmentBase {
    private static final int GUIDE_SCROLL_LIMIT = 0;
    public static FragmentGuide thisFragment;

    private RecyclerView recycler;

    public static FragmentGuide newInstance() {
        if(thisFragment == null) thisFragment = new FragmentGuide();
        return thisFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(this.toString(), "onAttach");


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(this.toString(), "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_guide;
        View customView = inflater.inflate(layoutRes, container, false);

        recycler = (RecyclerView)customView.findViewById(R.id.guide_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(new GuideAdapter(getContext(), dataAccess.getCards(Card.CARD_TYPE_GUIDE), callback.getCardListnener()));
        recycler.addOnScrollListener(getListenerWithThreshold(0, GUIDE_SCROLL_LIMIT));

        return customView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}