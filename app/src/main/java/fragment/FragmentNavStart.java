package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.echo_usa.echo.MainActivity;
import com.echo_usa.echo.R;

import util.FragSpec;
import util.MetricCalc;

/**
 * Created by zyuki on 6/27/2016.
 */
public class FragmentNavStart extends FragmentBase
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_nav_start;
        View customView = inflater.inflate(layoutRes, container, false);

        NavigationView navigationView = (NavigationView)customView.findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        View leftDrawerHeader = navigationView.getHeaderView(0);
        leftDrawerHeader.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                MetricCalc.getDrawerHeaderHeight()
        ));

        return customView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem leftDrawerMenuItem) {
        FragSpec toDisplay = FragSpec.getName(leftDrawerMenuItem.getItemId());
        if(toDisplay != null && toDisplay.hasIntent()) FragmentRouter.setEnqueued(toDisplay);
//        else //TODO: do intent

        ((MainActivity)getActivity()).closeDrawer(GravityCompat.START);
        return true;
    }
}
