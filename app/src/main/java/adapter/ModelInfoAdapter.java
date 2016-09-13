package adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.R;

import java.util.ArrayList;
import java.util.List;

import adapter.viewholder.CardHolder;
import data.Card;
import fragment.FragmentRouter;
import util.FragName;
import widget.EchoCard;

/**
 * Created by zyuki on 6/10/2016.
 */
public class ModelInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //TODO: use this to figure out what content to update. use Card static variables
    private static final Card emptyCard = new Card(Card.CARD_TYPE_PLACEHOLDER, Card.CARD_SIZE_SMALL, R.drawable.ic_vector_placeholder, "No Data");

    private List<Card> cardList;
    private View.OnClickListener listener;

    private int[] colorList = new int[] {
            R.color.bg_model_info_a,
            R.color.bg_model_info_b,
            R.color.bg_model_info_c,
            R.color.bg_model_info_d,
            R.color.bg_model_info_e
    };

    public ModelInfoAdapter(/*List<Card> cardsList, */View.OnClickListener listener) {
        this.cardList = new ArrayList<>();
        this.cardList.add(emptyCard);

        this.listener = listener;
    }

    public void updateCardData(String modelName, List<Card> cardList) {
        FragName displayedFrag = FragmentRouter.getDisplayedFragName();
        FragName enqueuedFrag = FragmentRouter.getEnqueuedFragName();

        //TODO: update content here
        Log.v("ModelInfoAdapter", "updatd... with " + enqueuedFrag.toString() + " info on this model: " + modelName);
        Log.v("ModelInfoAdapter", "updatd... with " + displayedFrag.toString() + " info on this model: " + modelName);

        this.cardList.clear();

        if(cardList != null && !cardList.isEmpty()) this.cardList.addAll(cardList);
        else this.cardList.add(emptyCard);

        //notifyDataSetChanged();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EchoCard card = new EchoCard(parent.getContext(), Card.CARD_SIZE_SMALL);
//        Log.d(this.toString(), FragmentRouter.getDisplayedFragName().toString());
//        boolean fragIsMaint = FragmentRouter.getDisplayedFragName().equals(FragName.MAINT);
//        boolean fragIsSpec = FragmentRouter.getDisplayedFragName().equals(FragName.SPECS);
//        if(fragIsMaint || fragIsSpec) card.adjustImageView();
        return new CardHolder(card);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardHolder cardHolder = (CardHolder)holder;
        Card card = cardList.get(position);

        cardHolder.thisCard.setTag(card);
        cardHolder.thisCard.setOnClickListener(listener);

        cardHolder.cardImgWrapper.setBackgroundResource(colorList[position]);
        cardHolder.cardImage.setImageResource(card.getDrawableRes());
        cardHolder.cardTitle.setText(card.getCardTitle());
    }

    @Override
    public int getItemCount() {return cardList.size();}
}
