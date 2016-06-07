package com.echo_usa.echo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
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

        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.main_frag_content,
                        dataAccess.getThisFrag(HOME),
                        HOME.toString()
                ).commit();
    }

    @Override
    public void onBackPressed() {
        //TODO: Change so that on back pressed, goes back to home if drawer is closed
        //use fragment.ishidden or something similar
        if (drawer.isDrawerOpen(GravityCompat.START)) {drawer.closeDrawer(GravityCompat.START);}
        else if (drawer.isDrawerOpen(GravityCompat.END)) {drawer.closeDrawer(GravityCompat.END);}
        else {super.onBackPressed();}
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        this.menuItem = item;
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
            FragmentManager fragManager = getSupportFragmentManager();
            FragmentTransaction fragTrans = fragManager.beginTransaction();
            fragTrans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            FragName fragName = getNameById(menuItem.getItemId());
            if(fragName != null) {
                fragTrans.replace(
                        R.id.main_frag_content,
                        dataAccess.getThisFrag(fragName),
                        fragName.toString()
                );
            }

            if(fragManager.getBackStackEntryCount() < 1) {
                fragTrans.addToBackStack(null);
            }
            fragTrans.commit();

            menuItem = null;
        }

    }

    private FragName getNameById(int menuId) {
        switch(menuId) {
                case R.id.slide_home:
                    return HOME;
                case R.id.slide_docs:
                    return DOCS;
                case R.id.slide_maintenace:
                    return MAINT;
                case R.id.slide_specs:
                    return SPECS;
                case R.id.slide_guide:
                    return GUIDE;
                case R.id.slide_locator:
                    return LOCATOR;
                case R.id.slide_settings:
                    return SETTINGS;
                case R.id.slide_contact:
                    return CONTACT;
                default:
                    return null;
        }
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

                if(fragManager.getBackStackEntryCount() < 1) {
                    fragTrans.addToBackStack(null);
                }
                fragTrans.commit();
            }
        };
    }

    @Override
    public List<Card> getCards() {
        return dataAccess.getCards();
    }

    @Override
    public String getFragName() {return null;}

    @Override
    public String selectedUnit() {return null;}

    @Override
    public void onDrawerStateChanged(int newState) {}

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {}

    @Override
    public void onDrawerOpened(View drawerView) {}
}