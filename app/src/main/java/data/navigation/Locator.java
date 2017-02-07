package data.navigation;

import util.FragSpec;

/**
 * Created by ZYuki on 11/2/2016.
 */

public class Locator extends NavItem {
//    public LocationSetting() {super();}

    public Locator(int iconRes, String setting, String info) {
        super(TYPE_LOCATOR, FragSpec.LOCATOR, iconRes, setting, info);
    }

    public int getIconRes() {return mImgRes;}
    public String getSettingName() {return mContent;}
    public String getInfoContent() {return mSubcontent;}
}
