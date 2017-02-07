package data.card_content;

import android.os.Parcel;
import android.support.annotation.Nullable;

/**
 * Created by zyuki on 6/1/2016.
 */
public class CardInfo extends CardContent {
    //TODO: store card content here
    private static int mIdCounter = 0;

    private int mCardId = COUNTER_INIT;

    public CardInfo(String text, @Nullable String subtext) {super(TYPE_INFO, text, subtext);}

    public CardInfo(int imgRes, String text, String subtext) {
        super(TYPE_INFO, imgRes, text, subtext);

        mCardId = mIdCounter;
        mIdCounter++;
    }

    @Override
    public boolean leftAligned() {return mCardId == COUNTER_INIT || mCardId % 2 == 0;}

    @Override
    public int getViewerLayout() {
        return LAYOUT_INFO_VIEWER;
    }

    public int getCardId() {return mCardId;}

    public CardInfo(Parcel in) {
        super(in);
        mCardId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        dst.writeInt(mCardId);
        super.writeToParcel(dst, flags);
    }
}