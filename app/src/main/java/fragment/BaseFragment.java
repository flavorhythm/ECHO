package fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.List;

import data.Card;
import util.FragName;

/**
 * Created by zyuki on 6/2/2016.
 */
public abstract class BaseFragment extends Fragment {
    protected Callback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        callback = (Callback)context;
    }

    public interface Callback {
        View.OnClickListener getCardListnener();
        List<Card> getCards(int cardsForPage);
        Integer[] getAdsForHeader();
    }
}
