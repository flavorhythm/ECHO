package util;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/2/2016.
 */
public enum FragName {
    HOME("home"),
    DOCS("documents"),
    MAINT("maintenance"),
    SPECS("specifications"),
    GUIDE("beginners_guide"),
    LOCATOR("locator"),
    SETTINGS("settings"),
    CONTACT("contact"),
    CARD_DISP("card_display");

    String value;

    FragName(String value) {this.value = value;}

    @Override
    public String toString() {return this.value;}

    public static FragName getNameById(int menuId) {
        switch(menuId) {
            case R.id.slide_home:
                return HOME;
            case R.id.slide_docs:
                return DOCS;
            case R.id.slide_maintenace:
                return MAINT;
            case R.id.slide_specs:
                return SPECS;
            case R.id.slide_guide:
                return GUIDE;
            case R.id.slide_locator:
                return LOCATOR;
            case R.id.slide_settings:
                return SETTINGS;
            case R.id.slide_contact:
                return CONTACT;
            default:
                return null;
        }
    }
}
