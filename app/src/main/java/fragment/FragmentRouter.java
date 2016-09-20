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

import fragment.repacement_fragment.FragmentCatalog;
import fragment.repacement_fragment.FragmentModelInfo;
import fragment.repacement_fragment.FragmentContact;
import fragment.repacement_fragment.FragmentGuide;
import fragment.repacement_fragment.FragmentHome;
import fragment.repacement_fragment.FragmentLocator;
import fragment.repacement_fragment.FragmentSettings;
import fragment.static_fragment.FragmentToolbar;
import util.FragName;
import util.NullEnqueuedException;

import static util.FragName.*;
/**
 * Created by zyuki on 7/6/2016.
 *
 * IDEA: queue a fragment and execute change when needed
 * fragment must execute before overriding queue
 */
public class FragmentRouter {
    private static final int INIT_TRANS_CONFIG = 1000;
    private static final int SWAP_TRANS_CONFIG = 1001;

    private static final String DIALOG_TAG = "dialog";
    private static final int ACTION_BAR_TRANS = 150;

    private static FragmentRouter thisInstance;
    private static FragName displayedFragName;
    private static FragName enqueuedFragName;

    private static FragmentManager fragmentManager;

    private static TransitionDrawable actionBarBg;
    private static Activity activity;

    private static Map<FragName, Fragment> fragMap;
    private static int fragContainer = R.id.main_frag_content;
    private static int currentAlpha = 0;

    public static FragmentRouter newInstance(Activity activity) {
        if(!activity.equals(FragmentRouter.activity)) {
            FragmentRouter.activity = activity;

            thisInstance = new FragmentRouter();
            buildFragmentList();

            FragmentRouter.fragmentManager = ((MainActivity)activity).getSupportFragmentManager();
            FragmentRouter.actionBarBg = (TransitionDrawable)activity.findViewById(R.id.main_appbar).getBackground();

            fragmentManager
                    .beginTransaction()
                    .add(fragContainer, fragMap.get(HOME), HOME.toString())
                    .disallowAddToBackStack()
                    .commit();

            displayedFragName = HOME;
            enqueuedFragName = null;
        }

        return thisInstance;
    }

    public static boolean isInstantiated() {return thisInstance != null;}

    public static FragName getEnqueuedFragName() throws NullEnqueuedException {
        if(enqueuedFragName != null) return enqueuedFragName;
        else throw new NullEnqueuedException("Empty enqueued", new Throwable("No fragment has been enqueued"));
    }

    public static void setEnqueuedFragName(@Nullable FragName enqueuedFragName) {
        //TODO: prevent overrides before the queue is cleared
        FragmentRouter.enqueuedFragName = enqueuedFragName;
    }

    public static FragName getDisplayedFragName() {
        return displayedFragName;
    }

    public static void setDisplayedFragName(FragName displayedFragName) {
        FragmentRouter.displayedFragName = displayedFragName;
    }

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

    public static boolean isThisFragDisplayed(FragName fragName) {
        return getDisplayedFragName().equals(fragName);
    }

    private static boolean isModelInfoFragment(FragName toDisplayFragName) {
        return toDisplayFragName.equals(MODEL_INFO);
    }

    //TODO: fix this
    public static boolean execute() {
        try {
            FragName enqueued = getEnqueuedFragName();

            if (!isThisFragDisplayed(enqueued)) {
                FragmentToolbar.setGarageBtnVisibility(isModelInfoFragment(enqueued));

                if (!popBackToFragment()) return replaceFragment();
                else return true;
            }
        } catch (NullEnqueuedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void initAddUnit(@Nullable String account) {
        FragmentTransaction fragTransaction = clearFragments();

        DialogAddUnit newItem = DialogAddUnit.newInstance();
        newItem.show(fragTransaction, DIALOG_TAG);
    }

    private static boolean initialFragmentReplacement() {
        try{
            FragName enqueued = getEnqueuedFragName();
            doTransaction(enqueued, INIT_TRANS_CONFIG);
            return true;
        } catch(NullEnqueuedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static FragmentTransaction clearFragments() {
        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();

        Fragment previousFrag = fragmentManager.findFragmentByTag(DIALOG_TAG);
        if(previousFrag != null) {fragTransaction.remove(previousFrag);}
        fragTransaction.addToBackStack(null);

        return fragTransaction;
    }

    private static boolean replaceFragment() {
        try {
            FragName enqueued = getEnqueuedFragName();
            doTransaction(enqueued, SWAP_TRANS_CONFIG);
            return true;
        } catch(NullEnqueuedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean popBackToFragment() {
        try {
            FragName enqueued = getEnqueuedFragName();

            if(isBackStackEntryPresent()) {
                String fragmentTag = enqueued.toString();
                fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                toggleActionBarBg(enqueued);
                setDisplayedFragName(enqueued);
                setEnqueuedFragName(null);

                return true;
            }
        } catch(NullEnqueuedException e) {
            e.printStackTrace();
        }

        return false;
    }

    //TODO: must revise to work with most recent changes.
    private static void doTransaction(FragName enqueued, int config) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction
                .setCustomAnimations(
                        R.anim.fragment_fade_in,
                        R.anim.fragment_fade_out,
                        R.anim.fragment_fade_in,
                        R.anim.fragment_fade_out
                )
                .replace(
                        fragContainer,
                        fragMap.get(enqueued),
                        enqueued.toString()
                )
                .addToBackStack(enqueued.toString())
                .commit();

        toggleActionBarBg(enqueued);
        setDisplayedFragName(enqueued);
        setEnqueuedFragName(null);

//        if(!getDisplayedFragName().equals(BLANK)) {
//            fragmentTransaction.remove(fragMap.get(getDisplayedFragName()));
//        }

//        setDisplayedFragName(enqueued);
//        setEnqueuedFragName(null);
//        fragmentTransaction.commit();

//        switch (config) {
//            case INIT_TRANS_CONFIG:
//                //fragmentTransaction.disallowAddToBackStack().commit();
//                break;
//            case SWAP_TRANS_CONFIG:
//                fragmentTransaction.addToBackStack(enqueued.toString()).commit();
//                toggleActionBarBg(enqueued);
//                break;
//        }
//
//        setDisplayedFragName(enqueued);
//        setEnqueuedFragName(null);
    }

    private static boolean isBackStackEntryPresent() {
        try {
            int backStackCount = fragmentManager.getBackStackEntryCount();
            FragName enqueued = getEnqueuedFragName();

            for(int i = 0; i < backStackCount; i++) {
                FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(i);
                if(entry.getName().equals(enqueued.toString())) return true;
            }

            return false;
        } catch(NullEnqueuedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static void toggleActionBarBg(FragName enqueued) {
        if(enqueued.equals(HOME)) actionBarBg.reverseTransition(ACTION_BAR_TRANS);
        else if(getDisplayedFragName().equals(HOME)) actionBarBg.startTransition(ACTION_BAR_TRANS);
    }

    private static void buildFragmentList() {
        fragMap = new HashMap<>();

        fragMap.put(HOME, FragmentHome.newInstance());
        fragMap.put(GUIDE, FragmentGuide.newInstance());
        fragMap.put(LOCATOR, FragmentLocator.newInstance());
        fragMap.put(CATALOG, FragmentCatalog.newInstance());
        fragMap.put(MODEL_INFO, FragmentModelInfo.newInstance());
        fragMap.put(SETTINGS, FragmentSettings.newInstance());
        fragMap.put(CONTACT, FragmentContact.newInstance());
    }
}
