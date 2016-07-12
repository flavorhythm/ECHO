package com.echo_usa.echo;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import fragment.BaseFragment;
import fragment.FragmentRouter;
import util.FragName;

import static util.FragName.GUIDE;
import static util.FragName.HOME;
import static util.FragName.NULL;

public class FirstTimeGuideActivity extends AppCompatActivity implements BaseFragment.Callback {
    private Intent backHomeIntent;

    private DrawerLayout drawer;
    private Toolbar toolbar;

    private ValueChangeSupport valueChange;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //TODO: add left-side drawer like MainActivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_guide);

        valueChange = ((DataAccessApplication)getApplication()).getValueChangeSupport();

        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(
                ContextCompat.getDrawable(FirstTimeGuideActivity.this, R.drawable.app_bar_fragments)
        );
        getSupportActionBar().setElevation(0);

        drawer = (DrawerLayout)findViewById(R.id.guide_drawer_layout);
        setupToolbarToggle();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.guide_tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        backHomeIntent = new Intent();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else homeActivityIntent(HOME);
    }

    private void setupToolbarToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {}

            @Override
            public void onDrawerClosed(View drawerView) {
                if(!valueChange.getFragToDisplay().equals(NULL)) {
                    FragName fragName = valueChange.getFragToDisplay();
                    if(fragName != GUIDE) homeActivityIntent(fragName);
                    else Snackbar.make(
                            findViewById(R.id.guide_drawer_layout),
                            "Already here!",
                            Snackbar.LENGTH_SHORT
                    ).show();

                    valueChange.setFragToDisplay(NULL);
                }
//                if(leftDrawerMenuItem != null) {
//                    FragName fragName = FragName.getNameById(drawerView.getId());
//                    if(fragName.equals()) {
//                        //TODO: remove if block and go to different activities depending on choice
//                        if(fragName != GUIDE) homeActivityIntent(fragName);
//                        else Snackbar.make(
//                                findViewById(R.id.guide_drawer_layout),
//                                "Already here!",
//                                Snackbar.LENGTH_SHORT
//                        ).show(); //TODO: go back to home with selected fragment to display
//                    }
//
//                    leftDrawerMenuItem = null;
//                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void homeActivityIntent(FragName fragName) {
        backHomeIntent.setData(Uri.parse(fragName.toString()));
        setResult(RESULT_OK, backHomeIntent);
        finish();

        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_first_time_guide, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Getting Started";
                case 1:
                    return "Choosing Chain saws";
                case 2:
                    return "Choosing Blowers";
                case 3:
                    return "Choosing Trimmers";
            }
            return null;
        }
    }

    @Override
    public View.OnClickListener getCardListnener() {
        return null;
    }

    @Override
    public void setToolbar(Toolbar toolbar) {this.toolbar = toolbar;}

    @Override
    public void closeDrawer(int gravity) {
        if (gravity == GravityCompat.START) drawer.closeDrawer(gravity);
    }
}
