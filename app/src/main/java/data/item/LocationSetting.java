package data.item;

import android.support.annotation.Nullable;

/**
 * Created by flavorhythm on 2/2/17.
 */

public class LocationSetting extends DataItem {
    //settings - max distance, all stores - full service - promo stores, saved locations
    private String mDescription;

    public LocationSetting(String setting, String description, int iconId) {
        super(setting, null, iconId);
        mDescription = description;
    }

    public String getSetting() {return super.getContent();}
    public String getDescription() {return mDescription;}
    public int getIconId() {return super.getDrawableId();}

    public void setSetting(String setting) {super.setContent(setting);}
    public void setDescription(String description) {mDescription = description;}
    public void setIconId(int iconId) {super.setDrawableId(iconId);}
}
