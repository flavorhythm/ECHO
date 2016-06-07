package util;

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
}
