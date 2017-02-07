package util;

import android.support.annotation.Nullable;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/2/2016.
 */
public enum FragSpec {
    HOME(R.id.slide_home),
    GUIDE(R.id.slide_guide),
    CATALOG(R.id.slide_catalog),
    LOCATOR(R.id.slide_locator),
    VIDEO(0),
    MODEL_DOCS(R.id.slide_modelInfo),
    SETTINGS(R.id.slide_settings),
    CONTACT(R.id.slide_contact),
    SUBLIST(0),
    ITEM_VIEWER(0);

    private int mMenuId;

    @Nullable
    public static FragSpec getName(int menuId) {
        for(FragSpec fragSpec : FragSpec.values()) {
            if(fragSpec.getId() == menuId) return fragSpec;
        }

        return null;
    }

    FragSpec(int menuId) {
        mMenuId = menuId;
    }

    public int getId() {return mMenuId;}

    public boolean hasRecycler() {
        switch(this) {
            case HOME: return true;
            case GUIDE: return true;
            case CATALOG: return true;
            case LOCATOR: return true;
            default: return false;
        }
    }

    public boolean isToolbarBtnEnabled() {
        switch(this) {
//            case LOCATOR: return true;
            case MODEL_DOCS: return true;
            default: return false;
        }
    }

    public boolean isToolbarFilled() {
        switch(this) {
            case HOME: return false;
            case CATALOG: return false;
//            case LOCATOR: return false;
            case SUBLIST: return false;
            default: return true;
        }
    }

//    public boolean isToolbarTextDark() {
//        switch(this) {
//            case LOCATOR: return true;
//            default: return false;
//        }
//    }

    public boolean isSubFragment() {
        switch(this) {
            case SUBLIST: return true;
            case ITEM_VIEWER: return true;
            default: return true;
        }
    }

    public boolean hasIntent() {
        switch(this) {
            case LOCATOR: return true;
            case VIDEO: return true;
            default: return false;
        }
    }
}
