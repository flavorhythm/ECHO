package com.echo_usa.echo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.Locale;

import data.Card;
import fragment.FragmentBase;
import fragment.FragmentRouter;
import fragment.static_fragment.FragmentNav;
import fragment.static_fragment.FragmentToolbar;
import util.FragName;

import static util.FragName.*;

public class MainActivity extends AppCompatActivity implements FragmentBase.Callback, DirectionCheck.DirectionListener {
    protected static final int GUIDE_REQ_CODE = 10;

    private static final int UP = 0;
    private static final int DOWN = 1;

    private DrawerLayout drawer;
    private Toolbar toolbar;

    private static ValueChangeSupport valueChange;

    private int prevScrollY = 0;
    private int revertPos = 0;

    private DirectionCheck directionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: Deal with rejected permissions
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valueChange = ((DataAccessApplication)getApplication()).getValueChangeSupport();

        //From fragment callback
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        drawer = (DrawerLayout)findViewById(R.id.main_drawer_layout);
        setupToolbarToggle(toolbar);

        zOrdering();

        directionCheck = new DirectionCheck();
        directionCheck.setDirectionChangeListener(this);

        FragmentRouter.newInstance(MainActivity.this);
        FragmentRouter.execute();
    }

    private void zOrdering() {
        findViewById(R.id.main_appbar).bringToFront();
        findViewById(R.id.main_frag_content).invalidate();
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
                        boolean isNotSelf = !valueChange.getSelectedModel().equals(valueChange.getEnqueuedModelName());
                        if(isNotSelf) valueChange.setQueuedModelName();

                        break;
                    case R.id.main_drawer_nav:
                        resetMenuInFragmentNav();

                        FragName fragName = FragmentRouter.getEnqueuedFragName();
                        if(!fragName.equals(BLANK)) {
                            Log.v("MainActivity", "FragName enqueued: " + fragName.toString());

                            if(!FragmentRouter.execute()) Snackbar.make(
                                    findViewById(R.id.main_drawer_layout),
                                    "Already here!",
                                    Snackbar.LENGTH_SHORT
                            ).show();
                        }

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
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else if (drawer.isDrawerOpen(GravityCompat.END)) drawer.closeDrawer(GravityCompat.END);
        else if(!FragmentRouter.isThisFragDisplayed(HOME)) {
            FragmentRouter.setEnqueuedFragName(HOME);
            FragmentRouter.execute();
        }
        else {
            FragmentRouter.setDisplayedFragName(BLANK);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu rightDrawerToggle) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, rightDrawerToggle);
        FragmentToolbar.setGarageBtnVisibility(false);
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
//        FragmentRouter.newInstance(MainActivity.this);

        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case GUIDE_REQ_CODE:
                    FragmentRouter.execute();
                    break;
            }
        }
    }

    private void resetMenuInFragmentNav() {
        ((FragmentNav)getSupportFragmentManager().findFragmentById(R.id.main_drawer_nav)).resetMenuInSettingsArrow();
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
    public void scrollToolbar(int scrollY, int actionBarSize, int vertThreshold) {
        View appbar = findViewById(R.id.main_appbar);

        int scrollVal = -1;
        if(scrollY < vertThreshold) scrollVal = 0;
        else if(scrollY >= vertThreshold) {
            int direction = getScrollDirection(scrollY);
            directionCheck.directionChanged(direction, actionBarSize, toolbar.getScrollY());

            switch(direction) {
                case UP:
                    scrollVal = actionBarSize - (int)ScrollUtils.getFloat((float)(revertPos - scrollY), 0, actionBarSize);
                    break;
                case DOWN:
                    scrollVal = (int)ScrollUtils.getFloat((float)(scrollY - revertPos), 0, actionBarSize);
                    break;
            }
        }

        if(appbar != null && scrollVal != -1) {
            toolbar.setScrollY(scrollVal);
            appbar.setScrollY(scrollVal);
            appbar.getLayoutParams().height = actionBarSize - scrollVal;
            appbar.requestLayout();
        }

        prevScrollY = scrollY;
    }

    private int getScrollDirection(int scrollY) {
        if(scrollY > prevScrollY) return DOWN;
        else return UP;
    }

    @Override
    public void onDirectionChange(int direction, int actionBarSize, int toolbarScroll) {
        Log.d(this.toString(), String.format(Locale.US, "direction: %d revertPos: %d prevScroll: %d toolbarScroll: %d", direction, revertPos, prevScrollY, toolbarScroll));
        if(DirectionCheck.prevDirection != direction) {
            if(direction == UP) revertPos = prevScrollY + actionBarSize - toolbarScroll;
            if(direction == DOWN) revertPos = prevScrollY - toolbarScroll;

            DirectionCheck.prevDirection = direction;
        }
    }

    @Override
    public void setToolbar(Toolbar toolbar) {this.toolbar = toolbar;}

    @Override
    public void closeDrawer(int gravity) {
        Log.d("MainActivity", "drawer closed");
        drawer.closeDrawer(gravity);
    }

    @Override
    public void setGarageBtnVisibility(boolean visibility) {
        toolbar.getMenu().setGroupVisible(R.id.menu_garage_group, visibility);
        drawer.setDrawerLockMode(
                visibility ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END
        );
    }
}

class DirectionCheck {
    DirectionListener listener;
    static int prevDirection = -1;

    void setDirectionChangeListener(DirectionListener listener) {
        this.listener = listener;
    }

    void directionChanged(int direction, int actionBarSize, int toolbarScroll) {
        if(listener != null) listener.onDirectionChange(direction, actionBarSize, toolbarScroll);
    }

    interface DirectionListener {
        void onDirectionChange(int direction, int actionBarSize, int toolbarScroll);
    }
}