package fragment;

import android.app.Activity;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import fragment.repacement_fragment.FragmentModelInfo;
import fragment.repacement_fragment.FragmentContact;
import fragment.repacement_fragment.FragmentGuide;
import fragment.repacement_fragment.FragmentHome;
import fragment.repacement_fragment.FragmentLocator;
import fragment.repacement_fragment.FragmentSettings;
import fragment.static_fragment.FragmentToolbar;
import util.FragName;

import static util.FragName.*;
/**
 * Created by zyuki on 7/6/2016.
 *
 * IDEA: queue a fragment and execute change when needed
 * fragment must execute before overriding queue
 */
public class FragmentRouter implements Animation.AnimationListener {
    //TODO: new fragment,
    private static final int INIT_TRANS_CONFIG = 1000;
    private static final int SWAP_TRANS_CONFIG = 1001;

    private static final String DIALOG_TAG = "dialog";

    //TODO: CReate catalogfragment?
    private static final String ROOT_FRAG = "root";
    private static final int ACTION_BAR_TRANS = 150;

    private static FragmentRouter thisInstance;
    private static FragName displayedFragName;
    private static FragName enqueuedFragName;

    private static FragmentManager fragmentManager;
    private static ValueChangeSupport valueChange;

    private static TransitionDrawable actionBarBg;
    private static View noModelView;
    private static Animation noModelHideAnim, noModelShowAnim;
    private static Activity activity;

    private static Map<FragName, Fragment> fragMap;
    private static int fragContainer = R.id.main_frag_content;
    private static int currentAlpha = 0;

    private boolean noModelViewRequired = true;

    public static FragmentRouter newInstance(Activity activity) {
        if(!activity.equals(FragmentRouter.activity)) {
            FragmentRouter.activity = activity;

            thisInstance = new FragmentRouter();
            buildFragmentList();

            FragmentRouter.fragmentManager = ((MainActivity)activity).getSupportFragmentManager();
            FragmentRouter.actionBarBg = (TransitionDrawable)activity.findViewById(R.id.main_appbar).getBackground();
            //FragmentRouter.noModelView = activity.findViewById(R.id.no_model_view);

            FragmentRouter.noModelHideAnim = AnimationUtils.loadAnimation(activity, R.anim.no_model_fade_out);
            FragmentRouter.noModelShowAnim = AnimationUtils.loadAnimation(activity, R.anim.no_model_fade_in);

            FragmentRouter.valueChange = ((DataAccessApplication)activity.getApplication()).getValueChangeSupport();

            displayedFragName = BLANK;
            enqueuedFragName = BLANK;
        }

        return thisInstance;
    }

    public static boolean isInstantiated() {return thisInstance != null;}

    public static FragName getEnqueuedFragName() {//TODO: throw error?
        if(enqueuedFragName == null) return BLANK;
        else return enqueuedFragName;
    }

    public static void setEnqueuedFragName(@Nullable FragName enqueuedFragName) {
        //TODO: prevent overrides before the queue is cleared
        FragmentRouter.enqueuedFragName = enqueuedFragName;
    }

    public static void hideNoModelView() {
        if((noModelView != null) && (noModelView.getVisibility() != View.GONE)) {
            Log.d("FragmentBase", "updateFragContent: noModelView removed");

            noModelView.startAnimation(noModelHideAnim);
            noModelHideAnim.setAnimationListener(FragmentRouter.thisInstance);
        }
    }

//    public static void showNoModelView() {
//        if((noModelView != null) && (noModelView.getVisibility() != View.VISIBLE)) {
//            Log.d("FragmentBase", "updateFragContent: noModelView removed");
//
//            //TODO: first show might need to be moved
//            noModelView.startAnimation(noModelShowAnim);
//            noModelShowAnim.setAnimationListener(FragmentRouter.thisInstance);
//        }
//    }

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

    private static boolean isModelInfoFragment(FragName toDisplayFragName) {
             if(toDisplayFragName.equals(DOCS)) return true;
        else if(toDisplayFragName.equals(MAINT)) return true;
        else if(toDisplayFragName.equals(SPECS)) return true;

        return false;
    }

    public static boolean execute() {
        if(getEnqueuedFragName() == null) return false;

        Log.v("FragmentRouter", "stage 1");

        if(!isThisFragDisplayed(getEnqueuedFragName())) {
            Log.v("FragmentRouter", "stage 2");
            FragmentToolbar.setGarageBtnVisibility(isModelInfoFragment(getEnqueuedFragName()));

            if(!popBackToFragment()) return replaceFragment();
            else return true;

//            return replaceFragment();
            //Initial fragment setup
//            if(getEnqueuedFragName().equals(HOME)) return popBackToFragment();
//            else return replaceFragment();
        }

        //Order is important... this is the initial fragment replacement
        //if(isThisFragDisplayed(BLANK)) return initialFragmentReplacement();

        return isThisFragDisplayed(BLANK) && initialFragmentReplacement();
    }

    public static void initAddUnit(@Nullable String account) {
        FragmentTransaction fragTransaction = clearFragments();

        DialogAddUnit newItem = DialogAddUnit.newInstance();
        newItem.show(fragTransaction, DIALOG_TAG);
    }

    private static boolean initialFragmentReplacement() {
        Log.v("FragmentRouter", "stage 4");

        doTransaction(INIT_TRANS_CONFIG);

        setDisplayedFragName(HOME);
        setEnqueuedFragName(null);

        //hideNoModelView();
        return true;
    }

    private static FragmentTransaction clearFragments() {
        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();

        Fragment previousFrag = fragmentManager.findFragmentByTag(DIALOG_TAG);
        if(previousFrag != null) {fragTransaction.remove(previousFrag);}
        fragTransaction.addToBackStack(null);

        return fragTransaction;
    }

    private static boolean replaceFragment() {
        Log.v("FragmentRouter", "stage 4");
        boolean isEnqueuedModelInfo = getEnqueuedFragName().isModelInfo();
        boolean isDisplayedModelInfo = getDisplayedFragName().isModelInfo();

        if(isEnqueuedModelInfo && isDisplayedModelInfo) doModelInfoUpdate();
        else if(isEnqueuedModelInfo) {
            fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    Log.v("FragmentRouter", "backstackchanged");
                    doModelInfoUpdate();
                    fragmentManager.removeOnBackStackChangedListener(this);
                }
            });

            doTransaction(SWAP_TRANS_CONFIG);
            toggleActionBarBg(getEnqueuedFragName());
            //toggleNoModelView();

//            doModelInfoUpdate();
        } else {
            doTransaction(SWAP_TRANS_CONFIG);
            toggleActionBarBg(getEnqueuedFragName());
            //toggleNoModelView();
        }

        setDisplayedFragName(getEnqueuedFragName());
        setEnqueuedFragName(null);
        return true;
    }

//    private static void toggleNoModelView() {
//        if(isModelInfoFragment(getEnqueuedFragName())) {
//            if(valueChange.getSelectedModel().equals("no_selection")) showNoModelView();
//            else hideNoModelView();
//        } else hideNoModelView();
//    }

    private static boolean popBackToFragment() {
        Log.v("FragmentRouter", "stage 3");
        //This is when the user presses the back button or when the user selects the home button
//        Fragment fragmentFromStack = isBackStackEntryPresent();
//        fragmentManager.popBackStack(ROOT_FRAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if(isBackStackEntryPresent()) {
            String fragmentTag = getEnqueuedFragName().toString();
            fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            toggleActionBarBg(getEnqueuedFragName());
            setDisplayedFragName(getEnqueuedFragName());
            setEnqueuedFragName(null);

            //toggleNoModelView();
            return true;
        }

        return false;

//        toggleActionBarBg(getEnqueuedFragName());
//        setDisplayedFragName(getEnqueuedFragName());
//        toggleNoModelView();
//        toggleActionBarBg(HOME);
//
//        setDisplayedFragName(HOME);
//        setEnqueuedFragName(null);

//        hideNoModelView();
//        return true;
    }

    private static void doModelInfoUpdate() {
        Fragment modelInfoFrag = fragmentManager.findFragmentByTag(MODEL_INFO.toString());
        if(modelInfoFrag instanceof FragmentModelInfo)
            ((FragmentModelInfo)modelInfoFrag).updateContentFragSwitch();
        else Snackbar.make(
                activity.findViewById(R.id.main_drawer_layout),
                "not working",
                Snackbar.LENGTH_SHORT
        ).show();
    }

    //TODO: must revise to work with most recent changes.
    private static void doTransaction(int config) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch(config) {
            case INIT_TRANS_CONFIG:
                fragmentTransaction
                        .setCustomAnimations(
                                R.anim.fragment_fade_in,
                                R.anim.fragment_fade_out
                        )
                        .add(
                                fragContainer,
                                fragMap.get(HOME),
                                HOME.toString()
                        )
                        .disallowAddToBackStack()
                        .commit();
                break;
            case SWAP_TRANS_CONFIG:
                fragmentTransaction
                        .setCustomAnimations(
                                R.anim.fragment_fade_in,
                                R.anim.fragment_fade_out,
                                R.anim.fragment_fade_in,
                                R.anim.fragment_fade_out
                        )
                        .replace(
                                fragContainer,
                                fragMap.get(getFilteredEnqueued()),
                                getFilteredEnqueued().toString()
                        )
                        //TODO: must disallow but currently not working.
//                        .disallowAddToBackStack()
                        .addToBackStack(getFilteredDisplayed().toString())
                        .commit();
                break;
            default:
                fragmentTransaction.commit();
                break;
        }
    }

    private static boolean isBackStackEntryPresent() {
        int backStackCount = fragmentManager.getBackStackEntryCount();

        for(int i = 0; i < backStackCount; i++) {
            FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(i);
            if(entry.getName().equals(getEnqueuedFragName().toString())) return true;
        }

        return false;
    }

    private static FragName getFilteredEnqueued() {
        if(getEnqueuedFragName().isModelInfo()) return MODEL_INFO;
        else return getEnqueuedFragName();
    }

    private static FragName getFilteredDisplayed() {
        if(getDisplayedFragName().isModelInfo()) return MODEL_INFO;
        else return getDisplayedFragName();
    }

    private static String getBackStackTag(FragName fragName) {
        if(getDisplayedFragName().equals(HOME)) return ROOT_FRAG;
        else return fragName.toString();
    }

    private static void toggleActionBarBg(FragName toDisplayFragName) {
        if(toDisplayFragName.equals(HOME)) actionBarBg.reverseTransition(ACTION_BAR_TRANS);
        else if(getDisplayedFragName().equals(HOME)) actionBarBg.startTransition(ACTION_BAR_TRANS);
    }

    private static void buildFragmentList() {
        fragMap = new HashMap<>();

        fragMap.put(HOME, FragmentHome.newInstance());
        fragMap.put(GUIDE, FragmentGuide.newInstance());
        fragMap.put(LOCATOR, FragmentLocator.newInstance());
        fragMap.put(MODEL_INFO, FragmentModelInfo.newInstance());
        fragMap.put(SETTINGS, FragmentSettings.newInstance());
        fragMap.put(CONTACT, FragmentContact.newInstance());
    }
}
