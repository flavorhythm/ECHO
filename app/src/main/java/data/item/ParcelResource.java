package data.item;

import android.os.Parcel;

/**
 * Created by flavorhythm on 2/2/17.
 */

public class ParcelResource extends ParcelModel {
    //URL and/or direct user to target


    public ParcelResource(Parcel src) {
        super(src);
    }

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        super.writeToParcel(dst, flags);
    }

    @Override
    protected int getSubClass() {
        return RESOURCE;
    }
}
