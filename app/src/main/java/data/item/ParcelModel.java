package data.item;

import android.os.Parcel;

/**
 * Created by flavorhythm on 2/2/17.
 */

public class ParcelModel extends ParcelItemClass {
    //ParcelItemClass reference?
    //ParcelSpecs?


    @Override
    public void writeToParcel(Parcel dst, int flags) {
        super.writeToParcel(dst, flags);
    }

    public ParcelModel(Parcel src) {
        super(src);
    }

    @Override
    protected int getSubClass() {
        return MODEL;
    }
}
