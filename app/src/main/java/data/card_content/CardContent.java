package data.card_content;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.echo_usa.echo.R;

/**
 * Created by flavorhythm on 1/6/17.
 */

public abstract class CardContent implements Parcelable {
    public static final int LAYOUT_MODEL_VIEWER = R.layout.frag_item_viewer_model;
    public static final int LAYOUT_INFO_VIEWER = R.layout.frag_item_viewer_info;

    public static final int HEADER = 0;
    public static final int CONTENT = 1;
    public static final int DIVIDER = 2;

    public static final String TYPE_CATALOG = "cat_content";
    public static final String TYPE_INFO = "info_content";
    public static final String TYPE_MODEL = "model_content";
    public static final String TYPE_DEALER = "dealer_content";

    static final int COUNTER_INIT = -1;

    private String mContentType;
    private int mCardType;

    private int mImgRes;
    private String mText, mSubtext;

    public CardContent(String contentType, String text, @Nullable String subtext) {
        mContentType = contentType;
        mCardType = DIVIDER;

        mText = text;
        mSubtext = subtext;
    }

    public CardContent(String contentType, int imgRes, String text, @Nullable String subtext) {
        mContentType = contentType;
        mCardType = CONTENT;

        mImgRes = imgRes;
        mText = text;
        mSubtext = subtext;
    }

    public abstract boolean leftAligned();
    public abstract int getViewerLayout();

    public int getCardType() {return mCardType;}
    public int getImgRes() {return mImgRes;}
    public String getText() {return mText;}
    public String getSubtext() {return mSubtext;}

    public CardContent(Parcel in) {
        mContentType = in.readString();
        mCardType = in.readInt();

        mImgRes = in.readInt();
        mText = in.readString();
        mSubtext = in.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        dst.writeString(mContentType);
        dst.writeInt(mCardType);

        dst.writeInt(mImgRes);
        dst.writeString(mText);
        dst.writeString(mSubtext);
    }

    private static CardContent getConcreteClass(Parcel src) {
        switch(src.readString()) {
            case TYPE_CATALOG: return new CardCat(src);
            case TYPE_INFO: return new CardInfo(src);
            case TYPE_MODEL: return new CardModel(src);
            default: return null;
        }
    }

    public static final Parcelable.Creator<CardContent> CREATOR = new Creator<CardContent>() {
        @Override
        public CardContent createFromParcel(Parcel src) {
            return CardContent.getConcreteClass(src);
        }

        @Override
        public CardContent[] newArray(int size) {
            return new CardContent[size];
        }
    };
}
