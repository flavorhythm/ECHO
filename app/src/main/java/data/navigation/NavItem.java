package data.navigation;

import util.FragSpec;

/**
 * Created by ZYuki on 11/2/2016.
 */

public class NavItem {
    //TODO: implement parcelable
    public static final String IMG_RES = "img_res";
    public static final String CONTENT = "content";
    public static final String S_CONTENT = "subcontent";

    public static final String TYPE_LOCATOR = "locator";
    public static final String TYPE_MODEL = "model";

    protected FragSpec mNavType;

    private String mContentType;
    protected int mImgRes;
    protected String mContent;
    protected String mSubcontent;

    protected NavItem(String contentType, FragSpec navType, int imageRes, String content, String subcontent) {
        mContentType = contentType;
        mNavType = navType;

        mImgRes = imageRes;
        mContent = content;
        mSubcontent = subcontent;
    }

    public FragSpec getNavType() {return mNavType;}
}
