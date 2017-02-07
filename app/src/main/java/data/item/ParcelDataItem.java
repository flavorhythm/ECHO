package data.item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by flavorhythm on 2/2/17.
 */

public class ParcelDataItem extends DataItem implements Parcelable {
    private static final int PARCEL_DATA = 1;

    static final int INFORMATION = 2;
    static final int ITEMCLASS = 3;
    static final int MODEL = 4;
    static final int RESOURCE = 5;
    static final int SPECS = 6;

    protected ParcelDataItem(String content, String subcontent, int drawableId) {
        super(content, subcontent, drawableId);
    }

    public ParcelDataItem(Parcel src) {
        super(src);
    }

//    display target layoutId

    public static final Parcelable.Creator<ParcelDataItem> CREATOR = new Creator<ParcelDataItem>() {
        @Override
        public ParcelDataItem createFromParcel(Parcel src) {return getConcreteClass(src);}

        @Override
        public ParcelDataItem[] newArray(int size) {return new ParcelDataItem[size];}
    };

    private static ParcelDataItem getConcreteClass(Parcel src) {
        switch(src.readInt()) {
            case INFORMATION: return new ParcelInfo(src);
            case ITEMCLASS: return new ParcelItemClass(src);
            case MODEL: return new ParcelModel(src);
            case RESOURCE: return new ParcelResource(src);
            case SPECS: return new ParcelSpecs(src);
            default: return null;
        }
    }

    protected int getSubClass() {return PARCEL_DATA;}

    @Override
    public int describeContents() {return hashCode();}

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        dst.writeInt(getSubClass());
        dst.writeString(getContent());
        dst.writeString(getSubcontent());
        dst.writeInt(getDrawableId());
    }
}
