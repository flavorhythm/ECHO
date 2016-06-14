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
 * Created by zyuki on 6/10/2016.
 */
public class DocumentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements View.OnClickListener {
    private List<Card> cardList;

    public DocumentAdapter(List<Card> cardsList) {
        this.cardList = cardsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes = R.layout.card_docs;

        return new ItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).docCard.setOnClickListener(this);
        ((ItemViewHolder)holder).docImage.setImageResource(cardList.get(position).getDrawableRes());
        ((ItemViewHolder)holder).docName.setText(cardList.get(position).getCardText());
    }

    @Override
    public int getItemCount() {return cardList.size();}

    @Override
    public void onClick(View v) {
        //TODO: Opens PDF of item
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView docCard;
        ImageView docImage;
        TextView docName;

        public ItemViewHolder(View itemView) {
            super(itemView);

            docCard = (CardView)itemView.findViewById(R.id.card_docs);
            docImage = (ImageView)itemView.findViewById(R.id.card_docs_img);
            docName = (TextView)itemView.findViewById(R.id.card_docs_text);
        }
    }
}
