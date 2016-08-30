package data;

/**
 * Created by zyuki on 8/1/2016.
 */
public class Contact {
    public static final int ECHO_CONTACT = 0;
    public static final int V58_CONTACT = 1;
    public static final int APP_CONTACT = 2;

    private int contactType;
    private int iconRes;
    private String label;
    private String contentText;

    public Contact(int contactType, int iconRes, String label, String contentText) {
        this.contactType = contactType;
        this.iconRes = iconRes;
        this.label = label;
        this.contentText = contentText;
    }

    public void setContactType(int contactType) {this.contactType = contactType;}
    public void setIconRes(int iconRes) {this.iconRes = iconRes;}
    public void setLabel(String label) {this.label = label;}
    public void setContentText(String content) {this.contentText = content;}

    public int getContactType() {return contactType;}
    public int getIconRes() {return iconRes;}
    public String getLabel() {return label;}
    public String getContentText() {return contentText;}
}
