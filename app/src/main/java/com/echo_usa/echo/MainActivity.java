package com.echo_usa.echo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import data.Card;
import data.DataAccessObject;
import fragment.BaseFragment;
import fragment.FragmentCardDisplay;
import util.FragName;

import static util.FragName.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener,
        BaseFragment.Callback {

    private MenuItem menuItem;
    private DrawerLayout drawer;

    private DataAccessObject dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        dataAccess = new DataAccessObject();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                if(drawerView.equals(findViewById(R.id.nav_menu))) {
                    super.onDrawerOpened(drawerView);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if(drawerView.equals(findViewById(R.id.nav_menu))) {
                    super.onDrawerClosed(drawerView);
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(drawerView.equals(findViewById(R.id.nav_menu))) {
                    super.onDrawerSlide(drawerView, slideOffset);
                }
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);
        menuItem = navigationView.getMenu().findItem(R.id.slide_home);

        fragTrans(HOME);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {drawer.closeDrawer(GravityCompat.START);}
        else if (drawer.isDrawerOpen(GravityCompat.END)) {drawer.closeDrawer(GravityCompat.END);}
        else {super.onBackPressed();}
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(dataAccess.isFragVisible(item.getItemId())) {
            menuItem = null;
        } else {
            menuItem = item;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onDrawerClosed(View drawerView) {
        if(menuItem != null) {
            FragName fragName = FragName.getNameById(menuItem.getItemId());
            if(fragName != null) {fragTrans(fragName);}

            menuItem = null;
        }
    }

    private void fragTrans(FragName fragName) {
        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragManager.beginTransaction();
        fragTrans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

        if(fragName != HOME) {
            if(dataAccess.isFragVisible(HOME)) {
                fragTrans.addToBackStack(HOME.toString());
            }
        }

        fragTrans.replace(
                R.id.main_frag_content,
                dataAccess.getThisFrag(fragName),
                fragName.toString()
        );

        fragTrans.commit();
    }

    @Override
    public View.OnClickListener getCardListnener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View card) {
                FragmentManager fragManager = getSupportFragmentManager();
                FragmentTransaction fragTrans = fragManager.beginTransaction();
                fragTrans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

                FragmentCardDisplay fragment = (FragmentCardDisplay)dataAccess.getThisFrag(CARD_DISP);
                if(card.getTag() instanceof Integer) {
                    fragment.posOfCard((Integer)card.getTag());
                }

                fragTrans.replace(
                        R.id.main_frag_content,
                        fragment,
                        CARD_DISP.toString()
                );

                fragTrans.commit();
            }
        };
    }

    @Override
    public List<Card> getCards() {return dataAccess.getCards();}

    @Override
    public String getFragName() {return null;}

    @Override
    public void onDrawerStateChanged(int newState) {}

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {}

    @Override
    public void onDrawerOpened(View drawerView) {}
}