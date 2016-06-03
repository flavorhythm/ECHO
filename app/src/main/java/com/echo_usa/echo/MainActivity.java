package com.echo_usa.echo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import fragment.BaseFragment;
import fragment.FragmentContact;
import fragment.FragmentGuide;
import fragment.FragmentDocuments;
import fragment.FragmentHome;
import fragment.FragmentLocator;
import fragment.FragmentMaintenance;
import fragment.FragmentSettings;
import fragment.FragmentSpecifications;
import util.FragName;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener,
        BaseFragment.Callback {

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
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

        setFragment(FragName.HOME);
    }

    @Override
    public void onBackPressed() {
        //TODO: Change so that on back pressed, goes back to home if drawer is closed
        //use fragment.ishidden or something similar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        this.menuItem = item;
        ((DrawerLayout)findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);

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
                ((DrawerLayout)findViewById(R.id.drawer_layout)).openDrawer(GravityCompat.END);
                break;
        }

        return true;
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if(menuItem != null) {
            switch(menuItem.getItemId()) {
                case R.id.slide_home:
                    setFragment(FragName.HOME);
                    break;
                case R.id.slide_docs:
                    setFragment(FragName.DOCS);
                    break;
                case R.id.slide_maintenace:
                    setFragment(FragName.MAINT);
                    break;
                case R.id.slide_specs:
                    setFragment(FragName.SPECS);
                    break;
                case R.id.slide_guide:
                    setFragment(FragName.GUIDE);
                    break;
                case R.id.slide_locator:
                    setFragment(FragName.LOCATOR);
                    break;
                case R.id.slide_settings:
                    setFragment(FragName.SETTINGS);
                    break;
                case R.id.slide_contact:
                    setFragment(FragName.CONTACT);
                    break;
                default:
                    break;
            }

            menuItem = null;
        }
    }

    private void setFragment(FragName fragName) {
        Fragment fragment;

        switch(fragName) {
            case HOME:
                fragment = new FragmentHome();
                break;
            case DOCS:
                fragment = new FragmentDocuments();
                break;
            case MAINT:
                fragment = new FragmentMaintenance();
                break;
            case SPECS:
                fragment = new FragmentSpecifications();
                break;
            case GUIDE:
                fragment = new FragmentGuide();
                break;
            case LOCATOR:
                fragment = new FragmentLocator();
                break;
            case SETTINGS:
                fragment = new FragmentSettings();
                break;
            case CONTACT:
                fragment = new FragmentContact();
                break;
            default:
                fragment = null;
                break;
        }

        if(fragment != null) {
            FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
            //TODO: set animation fragTrans.setCustomAnimations()
            fragTrans.replace(R.id.main_frag_content, fragment, fragName.toString());
            fragTrans.commit();
        }
    }

    @Override
    public String getFragName() {
        return null;
    }

    @Override
    public void onDrawerStateChanged(int newState) {}

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {}

    @Override
    public void onDrawerOpened(View drawerView) {}
}