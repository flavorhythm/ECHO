package data.card_content;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

import data.navigation.Model;

/**
 * Created by ZYuki on 11/2/2016.
 */

public class CardCat extends CardContent {
    private int mItemClass;

    public CardCat(int imgRes, String text, int itemClass) {
        super(TYPE_CATALOG, imgRes, text, null);
        mItemClass = itemClass;
    }

    @Override
    public boolean leftAligned() {return true;}

    @Override
    public int getViewerLayout() {
        return -1;
    }

    //Never allow subtext
    @Override
    public String getSubtext() {return null;}
    public int getItemClass() {return mItemClass;}

    public CardCat(Parcel in) {
        super(in);
        mItemClass = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        dst.writeInt(mItemClass);
        super.writeToParcel(dst, flags);
    }
}
