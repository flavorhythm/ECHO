package fragment;

import android.app.Activity;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.echo_usa.echo.MainActivity;
import com.echo_usa.echo.R;
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
//import fragment.static_fragment.FragmentToolbar;
import util.FragName;
import util.NullEnqueuedException;

import static util.FragName.*;
/**
 * Created by zyuki on 7/6/2016
 * Queues and prepares fragments for replacing, depending on what the user selects from the navigation drawer
 */
public class FragmentRouter {
    /**
     * VARIABLES
     * */
    private static final String DIALOG_TAG = "dialog";
    private static final int ACTION_BAR_TRANS = 150;

    private static FragmentRouter thisInstance;
    private static FragName displayedFragName;
    private static FragName enqueuedFragName;

    private static FragmentManager fragmentManager;

    //private static TransitionDrawable actionBarBg;
    private static Activity activity;

    private static Map<FragName, Fragment> fragMap = buildFragmentList();
    private static View snackbarContainer;
    private static int fragContainer = R.id.main_frag_content;
    private static int actionBarAlpha = 0; //Used to change actionbar alpha dependent on scroll

    /**
     * CONSTRUCTORS
     * */
    /** Instantiates this class and stores. Finds some views in Main Activity to adjust later
     * Adds/puts first fragment into FrameView */
    public static FragmentRouter newInstance(Activity activity) {
        if(!activity.equals(FragmentRouter.activity)) {
            FragmentRouter.activity = activity;
            thisInstance = new FragmentRouter();

            FragmentRouter.fragmentManager = ((MainActivity)activity).getSupportFragmentManager();
            //FragmentRouter.actionBarBg = (TransitionDrawable)activity.findViewById(R.id.main_appbar).getBackground();

            fragmentManager
                    .beginTransaction()
                    .add(fragContainer, fragMap.get(HOME), HOME.toString())
                    .disallowAddToBackStack()
                    .commit();

            snackbarContainer = ((MainActivity)activity).findViewById(R.id.main_drawer_layout);

            displayedFragName = HOME;
            enqueuedFragName = null;
        }

        return thisInstance;
    }

    /**
     * PUBLICS
     * */
    /** Checks whether object is instantiated and returns boolean */
    public static boolean isInstantiated() {return thisInstance != null;}
    /** Sets enqueued frag name */
    public static void setEnqueuedFragName(@Nullable FragName fragToEnqueue) {
        enqueuedFragName = fragToEnqueue;
    }
    /** Sets displayed frag name */
    public static FragName getDisplayedFragName() {
        return displayedFragName;
    }
    /** Fades actionbar according to scroll */
    public static void actionBarFadeScroll(int scrollY, int actionBarSize, float rangeMax) {
        final float rangeMin = rangeMax - (actionBarSize / 2);

        actionBarAlpha = convertValue(scrollY, rangeMin, rangeMax);
        //actionBarBg.getDrawable(0).setAlpha(actionBarAlpha);
    }
    /** Executes pending/enqueued fragment replacement. Returns true if successful */
    public static boolean execute() {
        try {
            FragName enqueued = getEnqueuedFragName();

            if(!enqueued.equals(getDisplayedFragName())) {
                //FragmentToolbar.setGarageBtnVisibility(enqueued.equals(MODEL_INFO));
                return replaceFragment();
            } else Snackbar.make(
                    snackbarContainer,
                    "Already here!",
                    Snackbar.LENGTH_SHORT
            ).show();

        } catch (NullEnqueuedException e) {
            e.printStackTrace();
        }

        return false;
    }
    /** Shows fragment to register a new unit */
    public static void initAddUnit(@Nullable String account) {
        FragmentTransaction fragTransaction = clearFragments();

        DialogAddUnit newItem = DialogAddUnit.newInstance();
        newItem.show(fragTransaction, DIALOG_TAG);
    }

    /**
     * PRIVATES
     * */
    /** Sets displayed frag name */
    private static void setDisplayedFragName(FragName displayedFragName) {
        FragmentRouter.displayedFragName = displayedFragName;
    }
    /** Gets enqueued frag name. Returns a non-null frag name or throws exception */
    @NonNull
    private static FragName getEnqueuedFragName() throws NullEnqueuedException {
        if(enqueuedFragName != null) return enqueuedFragName;
        else throw new NullEnqueuedException("Empty enqueued", new Throwable("No fragment has been enqueued"));
    }
    /** Converts scroll value, returning an adjusted value. Used to fade toolbar */
    private static int convertValue(int scrollY, float rangeMin, float rangeMax) {
        final int noFill = 0;
        final int fullFill = 255;

        if(rangeMin == ScrollUtils.getFloat(scrollY, rangeMin, rangeMax)) return noFill;
        if(rangeMax == ScrollUtils.getFloat(scrollY, rangeMin, rangeMax)) return fullFill;
        else return (int)(scrollY * (fullFill / (rangeMax - rangeMin)));
    }
    /** Clears dialog fragments that are still in the backstack */
    private static FragmentTransaction clearFragments() {
        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();

        Fragment previousFrag = fragmentManager.findFragmentByTag(DIALOG_TAG);
        if(previousFrag != null) {fragTransaction.remove(previousFrag);}
        fragTransaction.addToBackStack(null);

        return fragTransaction;
    }
    /** Tries to pop back to enqueued fragment. If unsuccessful, initializes a fragment transaction */
    private static boolean replaceFragment() {
        try {
            FragName enqueued = getEnqueuedFragName();
            if(!popBackToFragment()) doTransaction(enqueued);

            toggleActionBarBg(enqueued);
            setDisplayedFragName(enqueued);
            setEnqueuedFragName(null);
            return true;
        } catch(NullEnqueuedException e) {
            e.printStackTrace();
        }

        return false;
    }
    /** Pops stack back to enqueued fragment (inclusive) */
    private static boolean popBackToFragment() {
        try {
            FragName enqueued = getEnqueuedFragName();

            if(isBackStackEntryPresent()) {
                String fragmentTag = enqueued.toString();
                fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                return true;
            }
        } catch(NullEnqueuedException e) {
            e.printStackTrace();
        }

        return false;
    }
    /** Replaces displayed fragment to enqueued */
    private static void doTransaction(FragName enqueued) {
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
                        fragMap.get(enqueued)
                )
                .addToBackStack(getDisplayedFragName().toString())
                .commit();
    }
    /** Checks for enqueued fragment within backstack and returns true if found */
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
    /** Changes actionbar background dependent on enqueued frag name */
    private static void toggleActionBarBg(FragName enqueued) {
//        if(enqueued.equals(HOME)) actionBarBg.reverseTransition(ACTION_BAR_TRANS);
//        else if(getDisplayedFragName().equals(HOME)) actionBarBg.startTransition(ACTION_BAR_TRANS);
    }
    /** Puts instances of all necessary frag into one Map object */
    private static Map<FragName, Fragment> buildFragmentList() {
        Map<FragName, Fragment> fragMap = new HashMap<>();

        fragMap.put(HOME, FragmentHome.newInstance());
        fragMap.put(GUIDE, FragmentGuide.newInstance());
        fragMap.put(LOCATOR, FragmentLocator.newInstance());
        fragMap.put(CATALOG, FragmentCatalog.newInstance());
        fragMap.put(MODEL_INFO, FragmentModelInfo.newInstance());
        fragMap.put(SETTINGS, FragmentSettings.newInstance());
        fragMap.put(CONTACT, FragmentContact.newInstance());

        return fragMap;
    }
}
