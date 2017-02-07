package data.frag_list;

import android.os.Parcel;
import android.support.annotation.Nullable;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 8/1/2016.
 */
public class Contact extends FragListItem {
    public static final int LAYOUT_COUNT = 2;

//    public static final int LAYOUT_DIVIDER = R.layout.item_frag_list_divider;
//    public static final int LAYOUT_CONTENT = R.layout.item_frag_list_single_ln;

    //Use this constructor (divider)
    public Contact(String title) {super(TYPE_CONTACT, DIVIDER, /*LAYOUT_DIVIDER,*/ title);}

    //Use this constructor (content)
    public Contact(int iconRes, /*int layoutRes,*/ String title, @Nullable String subtitle) {
        super(TYPE_CONTACT, CONTENT, iconRes, /*layoutRes,*/ title, subtitle);
    }

    public Contact(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        super.writeToParcel(dst, flags);
    }
}
