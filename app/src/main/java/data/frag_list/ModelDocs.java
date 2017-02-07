package data.frag_list;

import android.os.Parcel;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 9/14/2016.
 */
public class ModelDocs extends FragListItem {
    public static final int LAYOUT_COUNT = 1;

//    private static final int LAYOUT_CONTENT = R.layout.item_frag_list_single_ln;
    private static final int ICON_RES = R.drawable.ic_vector_docs_dark;

    //Class requires no constructor for dividers
    //Use this constructor (content)
    public ModelDocs(String title) {super(TYPE_CONTACT, CONTENT, ICON_RES, /*LAYOUT_CONTENT,*/ title, null);}

    public ModelDocs(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        super.writeToParcel(dst, flags);
    }

    //    public static int layoutCnt() {return LAYOUT_COUNT;}
}
