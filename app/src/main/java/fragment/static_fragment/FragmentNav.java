package fragment.static_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.echo_usa.echo.R;

import fragment.FragmentBase;
import fragment.FragmentRouter;
import util.FragName;
import util.MetricCalcs;
import widget.SettingsArrow;

/**
 * Created by zyuki on 6/27/2016.
 */
public class FragmentNav extends FragmentBase
        implements NavigationView.OnNavigationItemSelectedListener {

    private SettingsArrow settingsArrow;

    @Override
    public void onAttach(Context context) {
        Log.v("FragmentNav", "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("FragmentNav", "onCreate");
        super.onCreate(savedInstanceState);

//        this.fragName = "left_drawer_navigation";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_nav;
        View customView = inflater.inflate(layoutRes, container, false);

        NavigationView navigationView = (NavigationView)customView.findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        View leftDrawerHeader = navigationView.getHeaderView(0);
        leftDrawerHeader.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                MetricCalcs.getDrawerHeaderHeight(MetricCalcs.getActionBarSize(getContext()))
        ));

        settingsArrow = ((SettingsArrow)leftDrawerHeader.findViewById(R.id.drawer_button_settings));
        settingsArrow.setDrawerMenu(navigationView.getMenu());

        return customView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem leftDrawerMenuItem) {
        Log.d("FragmentNav", "item selected: " + leftDrawerMenuItem.toString());
        Log.d("FragmentNav", "is callback valid: " + callback.toString());

        FragName fragToDisplay = FragName.getNameById(leftDrawerMenuItem.getItemId());
        if(fragToDisplay != null) {
            if(FragmentRouter.isInstantiated()) FragmentRouter.setEnqueuedFragName(fragToDisplay);
            //TODO: Error message here?
        }

        callback.closeDrawer(GravityCompat.START);
        return true;
    }

    public void resetMenuInSettingsArrow() {
        settingsArrow.resetMenu();
    }
}
