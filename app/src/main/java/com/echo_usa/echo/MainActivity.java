package com.echo_usa.echo;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import data.card_content.CardCat;
import data.card_content.CardContent;
import data.card_content.CardInfo;
import data.frag_list.FragListItem;
//import data.card_content.CardCat;
import data.navigation.Locator;
import data.navigation.Model;
import data.navigation.NavItem;
import fragment.FragmentModelDocs;
import fragment.FragmentNavEnd;
import fragment.FragmentRouter;
import util.FragSpec;
import util.MetricCalc;
import widget.EchoSnackbar;
import widget.EchoToolbar;

import static util.FragSpec.*;

public class MainActivity extends AppCompatActivity implements EchoToolbar.Callback, AdapterCallbacks {

    private DrawerLayout mDrawer;
    private EchoSnackbar mSnackbar;

    private FragDataHolder mDataHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: Deal with rejected permissions
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataHolder = new FragDataHolder();

        assessStatusBarHeight(getResources());

        mDrawer = (DrawerLayout)findViewById(R.id.main_drawer_layout);
        mSnackbar = (EchoSnackbar)findViewById(R.id.main_snackbar);

        Toolbar toolbar = getToolbar();
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            setupToolbarToggle(toolbar);
        }

        FragmentRouter.newInstance(MainActivity.this);
    }

    private void setupToolbarToggle(Toolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View view) {
                if(view.equals(findViewById(R.id.main_nav_start))) {
                    super.onDrawerOpened(view);
                }
            }

            @Override
            public void onDrawerClosed(View view) {
                if(view.equals(findViewById(R.id.main_nav_start))) {super.onDrawerClosed(view);}

                switch(view.getId()) {
                    case R.id.main_nav_end:
                        final int res = R.id.main_frag_content;
                        final Fragment f = getSupportFragmentManager().findFragmentById(res);
                        onNavEndItemClicked(f);
                        break;
                    case R.id.main_nav_start:
                        FragmentRouter.execute(MainActivity.this);
                }
            }

            @Override
            public void onDrawerSlide(View view, float slideOffset) {
                if(view.equals(findViewById(R.id.main_nav_start))) {
                    super.onDrawerSlide(view, slideOffset);
                }
            }
        };

        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(!closeAllDrawers()) {
            if(!FragmentRouter.popStackOnBackPressed()) super.onBackPressed();
        }
    }

    public boolean updateNavEndContent(@NonNull FragSpec fn) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_nav_end);
        return ((FragmentNavEnd)f).updateContent(fn);
    }

    private boolean closeAllDrawers() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            return true;
        } else if(mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
            return true;
        } else return false;
    }

    @Override
    public void lockEndDrawer(boolean toLock) {
        int mode = toLock ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED;
        mDrawer.setDrawerLockMode(mode, GravityCompat.END);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu rightDrawerToggle) {
        getMenuInflater().inflate(R.menu.menu_main, rightDrawerToggle);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_garage:
                mDrawer.openDrawer(GravityCompat.END);
                break;
        }

        return true;
    }

    @Override
    public View.OnClickListener getItemViewerListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    @Override
    public View.OnClickListener getCardListItemListener(final CardContent data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View cardView) {
                if(data != null) mDataHolder.setData(data);
                else return;

                if(data instanceof CardInfo) FragmentRouter.setEnqueued(ITEM_VIEWER);
                else if(data instanceof CardCat) FragmentRouter.setEnqueued(SUBLIST);

                FragmentRouter.execute(MainActivity.this);
            }
        };
    }

    @Override
    public View.OnClickListener getFragListItemListener(final FragListItem data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataHolder.setData(data);
                FragmentRouter.setEnqueued(ITEM_VIEWER);
                FragmentRouter.execute(MainActivity.this);
            }
        };
    }

    @Override
    public View.OnClickListener getNavEndItemListener(@NonNull final NavItem data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataHolder.setData(data);
                closeDrawer(GravityCompat.END);
            }
        };
    }

    public void closeDrawer(int gravity) {
        mDrawer.closeDrawer(gravity);
    }

    public EchoSnackbar getSnackbar() {
        return mSnackbar;
    }

    public EchoToolbar getToolbar() {return (EchoToolbar)findViewById(R.id.main_toolbar);}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void onNavEndItemClicked(final Fragment f) {
//        if(f instanceof FragmentLocator) {
//            ((FragmentLocator)f).updateContents(getDataHolder().getLocatorData());
//        }
        if(f instanceof FragmentModelDocs) {
            ((FragmentModelDocs)f).updateContents(getDataHolder().getModelData());
        }
    }

    private static void assessStatusBarHeight(Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) result = resources.getDimensionPixelSize(resourceId);

        MetricCalc.setStatusBarHeight(result);
    }

    public FragDataHolder getDataHolder() {return mDataHolder;}

    public class FragDataHolder {
        private Locator mLocatorData;
        private Model mModelData;

        private CardContent mCardData;

        private FragListItem mListData;

        void setData(CardContent data) {
            mCardData = data;
        }

        void setData(NavItem data) {
            switch(data.getNavType()) {
                case LOCATOR: mLocatorData = (Locator)data; break;
                case MODEL_DOCS: mModelData = (Model)data; break;
            }
        }

        void setData(FragListItem data) {
            mListData = data;
        }

        public Locator getLocatorData() {return mLocatorData;}
        public Model getModelData() {return mModelData;}
        public CardContent getCardData() {return mCardData;}
        public FragListItem getListData() {return mListData;}
    }
}