package com.echo_usa.echo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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

import fragment.BaseFragment;
import fragment.FragmentRouter;
import util.FragName;

import static util.FragName.*;

public class MainActivity extends AppCompatActivity implements BaseFragment.Callback {
    protected static final int GUIDE_REQ_CODE = 10;

    private DrawerLayout drawer;
    private Toolbar toolbar;

    private static ValueChangeSupport valueChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valueChange = ((DataAccessApplication)getApplication()).getValueChangeSupport();

        //From fragment callback
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        drawer = (DrawerLayout)findViewById(R.id.main_drawer_layout);
        setupToolbarToggle(toolbar);

        zOrdering();

        FragmentRouter.newInstance(MainActivity.this);
        FragmentRouter.replaceFragment();
    }

    private void zOrdering() {
        //brings No Model View to the front
        findViewById(R.id.no_model_view).bringToFront();
        findViewById(R.id.main_appbar).invalidate();
        findViewById(R.id.main_frag_content).invalidate();

        //brings actionbar to the front
        findViewById(R.id.main_appbar).bringToFront();
        findViewById(R.id.no_model_view).invalidate();
        findViewById(R.id.main_frag_content).invalidate();
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return super.getSupportFragmentManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        FragName fragName = FragmentRouter.getEnqueuedFragName();
                        if(!fragName.equals(NULL)) {
                            if(fragName == GUIDE) guideActivityIntent();
                            //TODO: finish here
                            else if(!FragmentRouter.replaceFragment()) Snackbar.make(
                                    findViewById(R.id.main_drawer_layout),
                                    "Already here!",
                                    Snackbar.LENGTH_SHORT
                            ).show();

                            valueChange.setFragToDisplay(NULL);
                        }

//                        if(!valueChange.getFragToDisplay().equals(NULL)) {
//                            FragName fragName = valueChange.getFragToDisplay();
//                            if(fragName != null) {
//                                if(fragName == GUIDE) guideActivityIntent();
//                                else if(!FragmentRouter.replaceFragment(fragName)) Snackbar.make(
//                                        findViewById(R.id.main_drawer_layout),
//                                        "Already here!",
//                                        Snackbar.LENGTH_SHORT
//                                ).show();
//
//                                valueChange.setFragToDisplay(NULL);
//                            }
//                        }

                        break;
                    default:
                        break;
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
        else if(!FragmentRouter.isThisFragDisplayed(HOME)) FragmentRouter.replaceFragment(HOME);
        else {
            FragmentRouter.setDisplayedFragName(NULL);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu rightDrawerToggle) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, rightDrawerToggle);
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

    private void guideActivityIntent() {
        Intent intent = new Intent(MainActivity.this, FirstTimeGuideActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, GUIDE_REQ_CODE, new Bundle());

        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentRouter.newInstance(MainActivity.this);

        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case GUIDE_REQ_CODE:
                    FragName fragName = FragName.valueOf(data.getDataString().toUpperCase());

                    if(fragName != null) {
                        if(FragmentRouter.isThisFragDisplayed(HOME) && fragName.equals(HOME)) break;
                        else FragmentRouter.replaceFragment(fragName);
                    }

                    break;
            }
        }
    }

    @Override
    public View.OnClickListener getCardListnener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View card) {
                Intent intent = new Intent(MainActivity.this, CardDisplayActivity.class);

                if(card.getTag() instanceof Integer) {
                    intent.putExtra("adCardNum", (Integer)card.getTag());
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            }
        };
    }

    @Override
    public void setToolbar(Toolbar toolbar) {this.toolbar = toolbar;}

    @Override
    public void closeDrawer(int gravity) {
        Log.d("MainActivity", "drawer closed");
        drawer.closeDrawer(gravity);
    }
}