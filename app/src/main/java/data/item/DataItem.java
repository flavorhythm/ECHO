package data.item;

import android.os.Parcel;
import android.support.annotation.Nullable;

/**
 * Created by flavorhythm on 2/2/17.
 */

public class DataItem {
    protected static final int NONE = -1;

    private String mContent;
    @Nullable private String mSubcontent;
    private int mDrawableId; //may change to URL

    public DataItem(Parcel src) {
        setContent(src.readString());
        setSubcontent(src.readString());
        setDrawableId(src.readInt());
    }

    protected DataItem(String content, @Nullable String subcontent, int drawableId) {
        mContent = content;
        mSubcontent = subcontent;
        mDrawableId = drawableId;
    }

    public String getContent() {return mContent;}
    @Nullable public String getSubcontent() {return mSubcontent;}
    public int getDrawableId() {return mDrawableId;}

    public void setContent(String content) {mContent = content;}
    public void setSubcontent(@Nullable String subcontent) {mSubcontent = subcontent;}
    public void setDrawableId(int drawableId) {mDrawableId = drawableId;}
    //mContent
    //mSubcontent
    //mDrawableId
}
