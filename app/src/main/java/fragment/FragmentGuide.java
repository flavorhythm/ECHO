package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.R;

import adapter.CardAdapter;
import data.card_content.CardContent;
import util.FragSpec;
import util.MetricCalc;

/**
 * Created by ZYuki on 7/13/2016.
 */

public class FragmentGuide extends FragmentBase {
    private RecyclerView recycler;

    public static FragmentGuide newInstance() {return new FragmentGuide();}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_recycler_no_header;
        View customView = inflater.inflate(layoutRes, container, false);

        recycler = (RecyclerView)customView.findViewById(R.id.recycler_no_header_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(
                new CardAdapter(
                        getPaddingView(getContext(), MetricCalc.getActionBarSize(getContext())),
                        CardContent.TYPE_INFO,
                        getDataAccess().getCards(FragSpec.GUIDE)
                )
        );
        recycler.addOnScrollListener(getToolbar().getScrollListener(0, getToolbarAlphaThreshold(Threshold.NO_HEADER)));

        return customView;
    }

//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);
//
//        if(Build.VERSION.SDK_INT >= 11) {
//            if(animation == null && nextAnim != 0) {
//                animation = AnimationUtils.loadAnimation(getContext(), nextAnim);
//            }
//
//            if(animation != null) {
//                if(getView() != null) getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
//                animation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        if(getView() != null) getView().setLayerType(View.LAYER_TYPE_NONE, null);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//            }
//        }
//
//        return animation;
//    }

    private View passPaddingTopView() {
        View v = new View(getContext());
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                MetricCalc.getActionBarSize(getContext())
        );

        v.setLayoutParams(lp);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}