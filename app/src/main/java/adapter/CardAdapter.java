package adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.echo_usa.echo.R;

import java.util.List;

import data.Card;

/**
 * Created by zyuki on 6/1/2016.
 */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<Card> cardsList;
    private View header;
    private View.OnClickListener listener;

    public CardAdapter(List<Card> cardsList, View header, View.OnClickListener listener) {
        this.cardsList = cardsList;
        this.header = header;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(header);
        } else {
            int layoutRes = R.layout.card;
            return new ItemViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false)
            );
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof ItemViewHolder) {
            ((ItemViewHolder)viewHolder).image.setImageResource(cardsList.get(position).getDrawableRes());
            ((ItemViewHolder)viewHolder).cardText.setText(cardsList.get(position).getCardText());

            //Order may be important
            ((ItemViewHolder)viewHolder).cardView.setTag(position);
            ((ItemViewHolder)viewHolder).cardView.setOnClickListener(listener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {return cardsList.size();}

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {super(view);}
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView cardText;
        public CardView cardView;

        public ItemViewHolder(View card) {
            super(card);

            image = (ImageView)card.findViewById(R.id.card_img_cardImg);
            cardText = (TextView)card.findViewById(R.id.card_text_cardText);
            cardView = (CardView)card.findViewById(R.id.main_cards);
        }
    }
}