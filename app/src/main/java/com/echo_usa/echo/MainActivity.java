package com.echo_usa.echo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import data.Card;
import fragment.FragmentBase;
import fragment.FragmentRouter;
import fragment.static_fragment.FragmentToolbar;
import util.MetricCalcs;
import util.NullEnqueuedException;

import static util.FragName.*;

public class MainActivity extends AppCompatActivity implements FragmentBase.Callback {
    protected static final int GUIDE_REQ_CODE = 10;

    private static ValueChangeSupport valueChange;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: Deal with rejected permissions
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valueChange = ((DataAccessApplication)getApplication()).getValueChangeSupport();
        determineStatusBarHeight();

        drawer = (DrawerLayout)findViewById(R.id.main_drawer_layout);

        Toolbar toolbar = getToolbarFromFragment();
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            setupToolbarToggle(toolbar);
            getSupportActionBar().setElevation(0);
        }

        zOrdering();
        FragmentRouter.newInstance(MainActivity.this);
    }

    private Toolbar getToolbarFromFragment() {
        try {
            return ((FragmentToolbar)getSupportFragmentManager().findFragmentById(R.id.main_toolbar))
                    .getToolbar();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void zOrdering() {
        try {
            findViewById(R.id.main_appbar).bringToFront();
            findViewById(R.id.main_frag_content).invalidate();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void determineStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) result = getResources().getDimensionPixelSize(resourceId);

        MetricCalcs.setStatusBarHeight(result);
    }

    private void setupToolbarToggle(Toolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                if(drawerView.equals(findViewById(R.id.main_drawer_nav))) {
                    super.onDrawerOpened(drawerView);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if(drawerView.equals(findViewById(R.id.main_drawer_nav))) {
                    super.onDrawerClosed(drawerView);
                }

                switch(drawerView.getId()) {
                    case R.id.main_drawer_garage:
                        try {valueChange.setEnqueuedAsDisplayed();}
                        catch(NullEnqueuedException e) {e.printStackTrace();}

                        break;
                    case R.id.main_drawer_nav:
                        FragmentRouter.execute();
                        break;
                    default: break;
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(drawerView.equals(findViewById(R.id.main_drawer_nav))) {
                    super.onDrawerSlide(drawerView, slideOffset);
                }
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(!closeAllDrawers()) {
            popBackEntireStack();

            if(!HOME.equals(FragmentRouter.getDisplayedFragName())) {
                FragmentRouter.setEnqueuedFragName(HOME);
                FragmentRouter.execute();
            } else super.onBackPressed();
        }
    }

    private void popBackEntireStack() {
        FragmentManager fm = getSupportFragmentManager();

        for(int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }

    private boolean closeAllDrawers() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if(drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
            return true;
        } else return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu rightDrawerToggle) {
        getMenuInflater().inflate(R.menu.menu_main, rightDrawerToggle);
        //FragmentToolbar.setGarageBtnVisibility(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_garage:
                drawer.openDrawer(GravityCompat.END);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case GUIDE_REQ_CODE:
                    //FragmentRouter.execute();
                    break;
            }
        }
    }

    @Override
    public View.OnClickListener getCardListnener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View cardView) {
                Intent intent = new Intent(MainActivity.this, CardDisplayActivity.class);

                if(cardView.getTag() instanceof Card) {
                    Card card = (Card)cardView.getTag();
//                    intent.putExtra("cardId", card.getCardId());
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void scrollToolbar(int scrollY, int actionBarSize, int vertThreshold) {
//        View appbar = findViewById(R.id.main_appbar);
//
//        int scrollVal = -1;
//        if(scrollY <= vertThreshold) scrollVal = 0;
//        else if(scrollY > vertThreshold) {
//            int direction = getScrollDirection(scrollY);
//            directionCheck.directionChanged(direction, actionBarSize, toolbar.getScrollY());
//
//            switch(direction) {
//                case UP:
//                    scrollVal = actionBarSize - (int)ScrollUtils.getFloat((float)(revertPos - scrollY), 0, actionBarSize);
//                    break;
//                case DOWN:
//                    scrollVal = (int)ScrollUtils.getFloat((float)(scrollY - revertPos), 0, actionBarSize);
//                    break;
//            }
//        }
//
//        if(appbar != null && scrollVal != -1) {
//            toolbar.setScrollY(scrollVal);
//            appbar.setScrollY(scrollVal);
//            appbar.getLayoutParams().height = actionBarSize - scrollVal;
//            appbar.requestLayout();
//        }
//
//        prevScrollY = scrollY;
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
//        this.toolbar = toolbar;
    }

    @Override
    public void closeDrawer(int gravity) {
        Log.d("MainActivity", "drawer closed");
        drawer.closeDrawer(gravity);
    }

    @Override
    public void setGarageBtnVisibility(boolean visibility) {
        drawer.setDrawerLockMode(
                visibility ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END
        );
    }
}