package adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.echo_usa.echo.R;

import java.util.List;

import data.ModelFiles;
import widget.CircularImageView;

/**
 * Created by zyuki on 6/10/2016.
 */

public class ModelInfoAdapter extends ArrayAdapter<ModelFiles> {
    private LayoutInflater inflater;

    private int layoutRes;
    private List<ModelFiles> docList;
    private View.OnClickListener listener;

    public ModelInfoAdapter(Context context, int layoutRes, List<ModelFiles> docList, View.OnClickListener listener) {
        super(context, layoutRes, docList);

        this.inflater = LayoutInflater.from(context);

        this.layoutRes = layoutRes;
        this.docList = docList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return docList.size();
    }

    @Override
    public ModelFiles getItem(int position) {
        return docList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        DocsHolder docsHolder;

        if (row == null || row.getTag() == null) {
            docsHolder = new DocsHolder();
            row = inflater.inflate(layoutRes, parent, false);

            docsHolder.icon = (ImageView)row.findViewById(R.id.docs_icon);
            docsHolder.text = (TextView) row.findViewById(R.id.docs_text);
            docsHolder.subtext = (TextView) row.findViewById(R.id.docs_subtext);
        } else docsHolder = (DocsHolder) row.getTag();

        ModelFiles files = getItem(position);

        docsHolder.icon.setImageDrawable(ContextCompat.getDrawable(getContext(), files.getIconRes()));
        docsHolder.text.setText(files.getText());
        docsHolder.subtext.setText(files.getSubtext());

        row.setOnClickListener(listener);

        return row;
    }

    private class DocsHolder {
        TextView text, subtext;
        ImageView icon;
    }
}
//
//public class ModelInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    //TODO: use this to figure out what content to update. use Card static variables
//    private static final Card emptyCard = new Card(Card.CARD_TYPE_PLACEHOLDER, Card.CARD_SIZE_SMALL, R.drawable.ic_vector_placeholder, "No Data");
//
//    private List<Card> cardList;
//    private View.OnClickListener listener;
//
//    private int[] colorList = new int[] {
//            R.color.bg_model_info_a,
//            R.color.bg_model_info_b,
//            R.color.bg_model_info_c,
//            R.color.bg_model_info_d,
//            R.color.bg_model_info_e
//    };
//
//    public ModelInfoAdapter(/*List<Card> cardsList, */View.OnClickListener listener) {
//        this.cardList = new ArrayList<>();
//        this.cardList.add(emptyCard);
//
//        this.listener = listener;
//    }
//
//    public void updateCardData(String modelName, List<Card> cardList) {
//        FragName displayedFrag = FragmentRouter.getDisplayedFragName();
//        FragName enqueuedFrag = FragmentRouter.getEnqueuedFragName();
//
//        //TODO: update content here
//        Log.v("ModelInfoAdapter", "updatd... with " + enqueuedFrag.toString() + " info on this model: " + modelName);
//        Log.v("ModelInfoAdapter", "updatd... with " + displayedFrag.toString() + " info on this model: " + modelName);
//
//        this.cardList.clear();
//
//        if(cardList != null && !cardList.isEmpty()) this.cardList.addAll(cardList);
//        else this.cardList.add(emptyCard);
//
//        //notifyDataSetChanged();
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        EchoCard card = new EchoCard(parent.getContext(), Card.CARD_SIZE_SMALL);
////        Log.d(this.toString(), FragmentRouter.getDisplayedFragName().toString());
////        boolean fragIsMaint = FragmentRouter.getDisplayedFragName().equals(FragName.MAINT);
////        boolean fragIsSpec = FragmentRouter.getDisplayedFragName().equals(FragName.SPECS);
////        if(fragIsMaint || fragIsSpec) card.adjustImageView();
//        return new CardHolder(card);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        CardHolder cardHolder = (CardHolder)holder;
//        Card card = cardList.get(position);
//
//        cardHolder.thisCard.setTag(card);
//        cardHolder.thisCard.setOnClickListener(listener);
//
//        cardHolder.cardImgWrapper.setBackgroundResource(colorList[position]);
//        cardHolder.cardImage.setImageResource(card.getDrawableRes());
//        cardHolder.cardTitle.setText(card.getCardTitle());
//    }
//
//    @Override
//    public int getItemCount() {return cardList.size();}
//}
