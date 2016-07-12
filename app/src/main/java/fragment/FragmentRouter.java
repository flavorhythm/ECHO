package fragment;

import android.app.Activity;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.echo_usa.echo.DataAccessApplication;
import com.echo_usa.echo.MainActivity;
import com.echo_usa.echo.R;
import com.echo_usa.echo.ValueChangeSupport;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.HashMap;
import java.util.Map;

import util.FragName;

import static util.FragName.*;
/**
 * Created by zyuki on 7/6/2016.
 */
public class FragmentRouter implements Animation.AnimationListener {
    private static final String ROOT_FRAG = "root";
    private static final int ACTION_BAR_TRANS = 150;

    private static FragmentRouter thisInstance;
    private static FragName displayedFragName = NULL;
    private static FragName enqueuedFragName = NULL;

    private static FragmentManager fragmentManager;
    private static ValueChangeSupport valueChange;

    private static TransitionDrawable actionBarBg;
    private static View noModelView;
    private static Animation noModelHideAnim, noModelShowAnim;
    private static Activity activity;

    private static Map<FragName, Fragment> fragMap;
    private static int fragContainer = R.id.main_frag_content;
    private static int currentAlpha = 0;

    private FragmentRouter() {}

    public static FragmentRouter newInstance(Activity activity) {
        if(!activity.equals(FragmentRouter.activity)) {
            FragmentRouter.activity = activity;

            thisInstance = new FragmentRouter();
            buildFragmentList();

            FragmentRouter.fragmentManager = ((MainActivity)activity).getSupportFragmentManager();
            FragmentRouter.actionBarBg = (TransitionDrawable)activity.findViewById(R.id.main_appbar).getBackground();
            FragmentRouter.noModelView = activity.findViewById(R.id.no_model_view);

            FragmentRouter.noModelHideAnim = AnimationUtils.loadAnimation(activity, R.anim.no_model_fade_out);
            FragmentRouter.noModelShowAnim = AnimationUtils.loadAnimation(activity, R.anim.no_model_fade_in);

            FragmentRouter.valueChange = ((DataAccessApplication)activity.getApplication()).getValueChangeSupport();
        }

        return thisInstance;
    }

    public static boolean isInstantiated() {return thisInstance != null;}

    public static FragName getEnqueuedFragName() {return enqueuedFragName;}

    public static void setEnqueuedFragName(FragName enqueuedFragName) {
        FragmentRouter.enqueuedFragName = enqueuedFragName;
    }

    public static void hideNoModelView() {
        if((noModelView != null) && (noModelView.getVisibility() != View.GONE)) {
            Log.d("BaseFragment", "updateFragContent: noModelView removed");

            noModelView.startAnimation(noModelHideAnim);
            noModelHideAnim.setAnimationListener(FragmentRouter.thisInstance);
        }
    }

    public static void showNoModelView() {
        if((noModelView != null) && (noModelView.getVisibility() != View.VISIBLE)) {
            Log.d("BaseFragment", "updateFragContent: noModelView removed");

            //TODO: first show might need to be moved
            noModelView.startAnimation(noModelShowAnim);
            noModelShowAnim.setAnimationListener(FragmentRouter.thisInstance);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        final boolean isVisible = noModelView.getVisibility() == View.VISIBLE;

        noModelView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        noModelView.setClickable(!isVisible);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}

    public static void actionBarFadeScroll(int scrollY, int actionBarSize, float rangeMax) {
        final float rangeMin = rangeMax - (actionBarSize / 2);

        currentAlpha = convertValue(scrollY, rangeMin, rangeMax);
        actionBarBg.getDrawable(0).setAlpha(currentAlpha);
    }

    private static int convertValue(int scrollY, float rangeMin, float rangeMax) {
        final int noFill = 0;
        final int fullFill = 255;

        if(rangeMin == ScrollUtils.getFloat(scrollY, rangeMin, rangeMax)) return noFill;
        if(rangeMax == ScrollUtils.getFloat(scrollY, rangeMin, rangeMax)) return fullFill;
        else return (int)(scrollY * (255 / (rangeMax - rangeMin)));
    }

    public static FragName getDisplayedFragName() {
        return displayedFragName;
    }

    public static void setDisplayedFragName(FragName displayedFragName) {
        FragmentRouter.displayedFragName = displayedFragName;
    }

    public static boolean isThisFragDisplayed(FragName fragName) {
        return getDisplayedFragName().equals(fragName);
    }

    public static boolean isNoModelViewRequired(FragName toDisplayFragName) {
        if(toDisplayFragName.equals(HOME)) return false;
        else if(toDisplayFragName.equals(LOCATOR)) return false;
        else if(toDisplayFragName.equals(SETTINGS)) return false;
        else if(toDisplayFragName.equals(CONTACT)) return false;

        return true;
    }

    public static boolean replaceFragment() {
        FragName toDisplayFragName = getEnqueuedFragName();

        for(Fragment fragment : fragmentManager.getFragments()) {
            if(fragment != null) Log.v("FragmentRouter", "FragList Before: " + fragment.toString());
        }

        if(!isThisFragDisplayed(toDisplayFragName)) {
            //Initial fragment setup
            if(isThisFragDisplayed(NULL)) {
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.fragment_fade_in,
                                R.anim.fragment_fade_out
                        ).add(
                                fragContainer,
                                fragMap.get(HOME),
                                NULL.toString()
                        ).disallowAddToBackStack()
                        .commit();

                setDisplayedFragName(HOME);
                setEnqueuedFragName(NULL);
                hideNoModelView();
                return true;
            }

            if(toDisplayFragName.equals(HOME)) {
                //This is when the user presses the back button or when the user selects the home button
                fragmentManager.popBackStack(ROOT_FRAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                for(Fragment fragment : fragmentManager.getFragments()) {
                    if(fragment != null) Log.d("FragmentRouter", "after back pressed: " + fragment.toString());
                }

                toggleActionBarHome(HOME);

                setDisplayedFragName(HOME);
                setEnqueuedFragName(NULL);
                hideNoModelView();
                return true;
            } else {
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.fragment_fade_in,
                                R.anim.fragment_fade_out,
                                R.anim.fragment_fade_in,
                                R.anim.fragment_fade_out
                        ).replace(
                                fragContainer,
                                fragMap.get(toDisplayFragName),
                                toDisplayFragName.toString()
                        ).addToBackStack(getBackStackTag(toDisplayFragName))
                        .commit();

                toggleActionBarHome(toDisplayFragName);

                setDisplayedFragName(toDisplayFragName);
                setEnqueuedFragName(NULL);
                if(isNoModelViewRequired(toDisplayFragName)) {
                    if(valueChange.getSelectedModel().equals("no_selection")) showNoModelView();
                    else hideNoModelView();
                }
                return true;
            }
        }

        return false;
    }

    private static String getBackStackTag(FragName fragName) {
        if(getDisplayedFragName().equals(HOME)) return ROOT_FRAG;
        else return fragName.toString();
    }

    private static void toggleActionBarHome(FragName toDisplayFragName) {
        if(toDisplayFragName.equals(HOME)) actionBarBg.reverseTransition(ACTION_BAR_TRANS);
        else if(getDisplayedFragName().equals(HOME)) actionBarBg.startTransition(ACTION_BAR_TRANS);
    }

    private static void buildFragmentList() {
        fragMap = new HashMap<>();

        fragMap.put(HOME, FragmentHome.newInstance());
        fragMap.put(CONTACT, FragmentContact.newInstance());
        fragMap.put(LOCATOR, FragmentLocator.newInstance());
        fragMap.put(MAINT, FragmentMaintenance.newInstance());
        fragMap.put(SETTINGS, FragmentSettings.newInstance());
        fragMap.put(SPECS, FragmentSpecifications.newInstance());
        fragMap.put(DOCS, FragmentDocuments.newInstance());
        fragMap.put(CARD_DISP, FragmentCardDisplay.newInstance());
    }
}
