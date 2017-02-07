package data.card_content;

import android.os.Parcel;

import com.echo_usa.echo.R;

import data.navigation.Model;

/**
 * Created by flavorhythm on 1/31/17.
 */

public class CardModel extends CardContent {
    private static int mIdCounter = 0;

    private int mCardId = COUNTER_INIT;
//    private int mItemClass;

    public CardModel(Model m) {
        super(TYPE_MODEL, m.getImgResource(), m.getModelName(), null);
//        mItemClass = m.getItemClass();

        mCardId = mIdCounter;
        mIdCounter++;
    }

    @Override
    public boolean leftAligned() {return mCardId == COUNTER_INIT || mCardId % 2 == 0;}

    @Override
    public int getViewerLayout() {
        return LAYOUT_MODEL_VIEWER;
    }

    public CardModel(Parcel in) {
        super(in);
        mCardId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        dst.writeInt(mCardId);
        super.writeToParcel(dst, flags);
    }
}
