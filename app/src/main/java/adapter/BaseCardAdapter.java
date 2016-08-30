package adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import data.Card;

/**
 * Created by zyuki on 7/15/2016.
 */
public class BaseCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Card> cardList;
    private View header;
    private View divider;
    private View.OnClickListener listener;

    private static LruCache<String, Bitmap> memoryCache;
    private Resources resources;

    public BaseCardAdapter(List<Card> cardList, View.OnClickListener listener) {
        this.cardList = cardList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
