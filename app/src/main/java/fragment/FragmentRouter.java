package fragment;

import android.app.Activity;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.echo_usa.echo.MainActivity;
import com.echo_usa.echo.R;

//import fragment.static_fragment.FragmentToolbar;
//import data.card_content.CardCat;

import util.FragSpec;

import static util.FragSpec.*;
/**
 * Created by zyuki on 7/6/2016
 * Queues and prepares fragments for replacing, depending on what the user selects from the navigation drawer
 */
public class FragmentRouter {
    /**
     * VARIABLES
     * */

    private static final String DIALOG_TRANSACTION_TAG = "dialog";
    private static final String HOME_TRANSACTION_TAG = "backstack_home";
    private static final String SUBSET_TRANSACTION_TAG = "backstack_subset";
    private static final String TOP_LEVEL_TAG = "backstack_toplevel";

    private static FragmentRouter thisInstance;
    private static FragSpec sDisplayed;
    private static FragSpec sEnqueued;
    private static FragSpec sCurrentTopFrag;

    private static MainActivity.FragDataHolder sDataHolder;

    private static FragmentManager sFragManager;
    private static int sContainerId = R.id.main_frag_content;

    /**
     * CONSTRUCTORS
     * */
    /** Instantiates this class and stores. Finds some views in Main Activity to adjust later
     * Adds/puts first fragment into FrameView */
    public static FragmentRouter newInstance(Activity activity) {
        thisInstance = new FragmentRouter();

        FragmentRouter.sFragManager = ((MainActivity)activity).getSupportFragmentManager();

        sFragManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(sContainerId, getFragInstance(HOME), HOME.toString())
                .disallowAddToBackStack()
                .commit();

        setDisplayed(HOME);
        setCurrentTopLevel(HOME);
        setEnqueued(null);

        return thisInstance;
    }

    /**
     * PUBLICS
     * */
    /** Checks whether object is instantiated and returns boolean */
    public static boolean isInstantiated() {return thisInstance != null;}
    public static FragSpec getCurrentTopLevel() {
        return sCurrentTopFrag;
    }
    public static void setCurrentTopLevel(FragSpec topLevel) {
        sCurrentTopFrag = topLevel;
    }
    /** Sets enqueued frag name */
    public static void setEnqueued(@Nullable FragSpec fragToEnqueue) {
        sEnqueued = fragToEnqueue;
    }/** Gets enqueued frag name. Returns a non-null frag name or throws exception */
    @Nullable
    public static FragSpec getEnqueued() {
        return sEnqueued;
    }
    /** Sets displayed frag name */
    @NonNull
    public static FragSpec getDisplayed() {return sDisplayed;}
    /** Executes pending/enqueued fragment replacement. Returns true if successful */
    public static void execute(Activity activity) {
        FragSpec enqueued = getEnqueued();
        if(enqueued == null) return;

        ((MainActivity)activity).updateNavEndContent(enqueued);

        if(!getDisplayed().equals(enqueued)) {
            sDataHolder = ((MainActivity)activity).getDataHolder();

            if(enqueued.equals(HOME)) popBackStackToTag(HOME_TRANSACTION_TAG);
            else doTransaction(enqueued, getDisplayed());

            setDisplayed(enqueued);
            if(!enqueued.equals(SUBLIST) && !enqueued.equals(ITEM_VIEWER)) setCurrentTopLevel(enqueued);
        } else ((MainActivity)activity).getSnackbar().show("Already Here");

        setEnqueued(null);
    }

    //TODO: still not complete
    public static boolean popStackOnBackPressed() {
        int index = sFragManager.getBackStackEntryCount() - 1;

        if(index < 0) return false;
        String tag = sFragManager.getBackStackEntryAt(index).getName();
        switch(tag) {
            case SUBSET_TRANSACTION_TAG:
                popBackStackToTag(tag);
                setDisplayed(SUBLIST);
                return true;
            case TOP_LEVEL_TAG: case HOME_TRANSACTION_TAG:
                if(getDisplayed().equals(SUBLIST) || getDisplayed().equals(ITEM_VIEWER)){
                    sFragManager.popBackStack();
                    setDisplayed(getCurrentTopLevel());
                } else {
                    popBackStackToTag(HOME_TRANSACTION_TAG);
                    setDisplayed(HOME);
                    setCurrentTopLevel(HOME);
                }
                return true;
            default: return false;
        }
    }

    /**
     * PRIVATES
     * */
    /** Sets displayed frag name */
    public static void setDisplayed(@NonNull FragSpec displayed) {
        FragmentRouter.sDisplayed = displayed;
    }
    /** Pops stack back to enqueued fragment (inclusive) */
    private static void popBackStackToTag(String tag) {
        sFragManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    /** Replaces displayed fragment to enqueued */
    private static String getTransactionTag(@NonNull FragSpec displayed) {
        if(displayed.equals(HOME)) return HOME_TRANSACTION_TAG;
        if(displayed.equals(SUBLIST) || displayed.equals(ITEM_VIEWER)) return SUBSET_TRANSACTION_TAG;
        else return TOP_LEVEL_TAG;
    }

    private static void doTransaction(@NonNull FragSpec enqueued, @NonNull FragSpec displayed) {
        sFragManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_fade_in,
                        R.anim.fragment_fade_out,
                        R.anim.fragment_pop_in,
                        R.anim.fragment_pop_out
                )
                .replace(
                        sContainerId,
                        getFragInstance(enqueued),
                        enqueued.toString()
                )
                .addToBackStack(getTransactionTag(displayed))
                .commit();
    }

    //TODO: pass static information to fragments
    /** Returns instances of fragment corresponding to FragSpec. Some fragments pass arguments */
    private static Fragment getFragInstance(FragSpec fragSpec) {
        switch(fragSpec) {
            case HOME: return FragmentHome.newInstance();
            case GUIDE: return FragmentGuide.newInstance();
            case LOCATOR: return FragmentLocator.newInstance(/*sDataHolder.getLocatorData()*/);
            case CATALOG: return FragmentCatalog.newInstance();
//                CardCat c = null;
//                if(sNavData instanceof CardCat) {c = (CardCat)sNavData;}
//                return FragmentCatalog.newInstance(c);
            case MODEL_DOCS: return FragmentModelDocs.newInstance(sDataHolder.getModelData());
//                ParcelModel m = sNavData instanceof ParcelModel ? (ParcelModel)sNavData : null;
            case SETTINGS: return FragmentSettings.newInstance();
            case CONTACT: return FragmentContact.newInstance();
            case SUBLIST: return FragmentSubList.newInstance(sDataHolder.getCardData());
            case ITEM_VIEWER: return FragmentItemViewer.newInstance(getParcelData());
        }

        return null;
    }

    @Nullable
    private static Parcelable getParcelData() {
        switch(getDisplayed()) {
            case MODEL_DOCS: case CONTACT: return sDataHolder.getListData();
            case HOME: case GUIDE: return sDataHolder.getCardData();
            default: return null;
        }
    }
}