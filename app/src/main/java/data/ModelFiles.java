package data;

/**
 * Created by zyuki on 9/14/2016.
 */
public class ModelFiles {
    private int iconRes;
    private String text;
    private String subtext;

    public ModelFiles(String text, String subtext, int iconRes) {
        this.iconRes = iconRes;
        this.text = text;
        this.subtext = subtext;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }
}
