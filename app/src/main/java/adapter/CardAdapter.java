package adapter;

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
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Card> cardsList;

    public CardAdapter(List<Card> cardsList) {
        this.cardsList = cardsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes = R.layout.card;
        View card = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageResource(cardsList.get(position).getDrawableRes());
        holder.cardText.setText(cardsList.get(position).getCardText());
    }

    @Override
    public int getItemCount() {return cardsList.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView cardText;

        public ViewHolder(View card) {
            super(card);

            image = (ImageView)card.findViewById(R.id.card_img_cardImg);
            cardText = (TextView)card.findViewById(R.id.card_text_cardText);
        }
    }
}