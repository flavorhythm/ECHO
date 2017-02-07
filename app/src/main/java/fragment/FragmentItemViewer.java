package fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.R;

import data.card_content.CardContent;
import data.frag_list.FragListItem;

/**
 * Created by zyuki on 6/7/2016.
 */
//TODO: accepts layoutRes to inflate. This fragment changes layoutres every time it is displayed
public class FragmentItemViewer extends FragmentBase {
    private static final String PARCEL_KEY = "parcelable";

    public static FragmentItemViewer newInstance(Parcelable data) {
        Bundle args = new Bundle();
        args.putParcelable(PARCEL_KEY, data);

        FragmentItemViewer fragment = new FragmentItemViewer();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_item_viewer;
//        layoutRes = layoutRes == 0 ? R.layout.frag_item_viewer : layoutRes;

        View fragView = inflater.inflate(layoutRes, container, false);
        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        if(b != null && !b.isEmpty()) {
            Parcelable p = b.getParcelable(PARCEL_KEY);
            //WORKING! :D
            if(p instanceof CardContent) Log.i("FragmentItemViewer", "Card: " + ((CardContent)p).getText());
            if(p instanceof FragListItem) Log.i("FragmentItemViewer", "List: " + ((FragListItem)p).getTitle());
//            if(b.getParcelable(LIST_KEY) != null) {
//                FragListItem item = (FragListItem)b.getParcelable(LIST_KEY);
//                Log.i("FragmentItemViewer", "ParcelResource: " + item.getTitle());
//            }
        }
    }
}
