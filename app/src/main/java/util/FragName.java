package util;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/2/2016.
 */
public enum FragName {

    //TODO: remove "string" values. Completely unnecessary
    BLANK("blank_container"),
    HOME("Home"),
    GUIDE("Beginner's Guide"),
    CATALOG("Catalog"),
    LOCATOR("Dealer Locator"),
    SETTINGS("Settings"),
    CONTACT("Contact Us"),

    MODEL_INFO("model_info"),
    DOCS("Documentation"),
    MAINT("Maintenance"),
    SPECS("Unit Specs");

    String value;

    FragName(String value) {this.value = value;}

    public boolean isModelInfo() {
        switch(this) {
            case DOCS: case MAINT: case SPECS:
                return true;
        }

        return false;
    }

    @Override
    public String toString() {return this.value;}

    public static FragName getNameById(int menuId) {
        switch(menuId) {
            case R.id.slide_home:
                return HOME;
            case R.id.slide_modelInfo:
                return MODEL_INFO;
            case R.id.slide_catalog:
                return CATALOG;
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
