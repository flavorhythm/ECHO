package data.item;

import android.os.Parcel;

/**
 * Created by flavorhythm on 2/2/17.
 */

public class ParcelInfo extends ParcelDataItem {
    private static int sIdCounter = 0;
    //itemid
    //text
    //alignment
    private String mText;
    private int mId;

    public ParcelInfo(String title, String subtitle, String text, int drawableId) {
        super(title, subtitle, drawableId);
        mText = text;

        mId = sIdCounter;
        sIdCounter++;
    }

    public ParcelInfo(Parcel src) {
        super(src);
        mText = src.readString();
        mId = src.readInt();
    }

    public int getId() {return mId;}
    public String getTitle() {return super.getContent();}
    public String getSubtitle() {return super.getSubcontent();}
    public String getText() {return mText;}
    public int getDrawableId() {return super.getDrawableId();}

//    public void setId(int id) {mId = id;}
    public void setTitle(String title) {super.setContent(title);}
    public void setSubtitle(String subtitle) {super.setSubcontent(subtitle);}
    public void setText(String text) {mText = text;}
    public void setDrawableId(int drawableId) {super.setDrawableId(drawableId);}

    public Alignment getAlignment() {
        return mId % 2 == 0 ? Alignment.LEFT : Alignment.RIGHT;
    }

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        dst.writeString(mText);
        dst.writeInt(mId);
        super.writeToParcel(dst, flags);
    }

    @Override
    protected int getSubClass() {
        return INFORMATION;
    }

    public enum Alignment {
        LEFT, RIGHT
    }
}
