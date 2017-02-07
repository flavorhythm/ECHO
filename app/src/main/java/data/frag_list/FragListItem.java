package data.frag_list;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by ZYuki on 11/14/2016.
 *
 * FragListItem used in Adapaters for Fragment lists.
 * Items are either content or divider.
 *
 * Subclasses
 */
//TODO: clean up variable names (make consistent)
public abstract class FragListItem implements Parcelable {
    public static final int CONTENT = 0;
    public static final int DIVIDER = 1;

    public static final String TYPE_CONTACT = "contact";
    public static final String TYPE_DOCS = "documents";

    public static final int INIT = -1;

    private String mContentType;
    private int mType = INIT;
    private int mIconRes = INIT;
//    protected int mLayoutRes = INIT;
    private String mTitle;
    @Nullable private String mSubtitle;

    //Use this constructor (both dividers and content)
    protected FragListItem(String contentType, int type, String title) {
        mContentType = contentType;

        mType = type;
        mTitle = title;
    }

    //Use this constructor (both dividers and content)
    protected FragListItem(String contentType, int type, int iconRes, String title, @Nullable String subtitle) {
        mContentType = contentType;
        mType = type;
        mIconRes = iconRes;
        mTitle = title;
        mSubtitle = subtitle;
    }

    public int getType() {return mType;}
    public int getIconRes() {return mIconRes;}
    public String getTitle() {return mTitle;}
    public String getSubtitle() {return mSubtitle;}

    public FragListItem(Parcel in) {
        mContentType = in.readString();
        mType = in.readInt();

        mIconRes = in.readInt();
        mTitle = in.readString();
        mSubtitle = in.readString();
    }

    @Override
    public int describeContents() {return hashCode();}

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        dst.writeInt(mType);
        dst.writeInt(mIconRes);
        dst.writeString(mTitle);
        dst.writeString(mSubtitle);
    }

    private static FragListItem getConcreteClass(Parcel src) {
        switch(src.readString()) {
            case TYPE_CONTACT: return new Contact(src);
            case TYPE_DOCS: return new ModelDocs(src);
            default: return null;
        }
    }

    public static final Parcelable.Creator<FragListItem> CREATOR = new Creator<FragListItem>() {
        @Override
        public FragListItem createFromParcel(Parcel src) {
            return FragListItem.getConcreteClass(src);
        }

        @Override
        public FragListItem[] newArray(int size) {
            return new FragListItem[size];
        }
    };
}
