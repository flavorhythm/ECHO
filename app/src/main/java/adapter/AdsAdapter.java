package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/15/2016.
 */
public class AdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Integer[] imageRes;

    public AdsAdapter(Integer[] imageRes) {
        this.imageRes = imageRes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes = R.layout.header_home;
        return new ItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).image.setImageResource(imageRes[position]);
    }

    @Override
    public int getItemCount() {return imageRes.length;}

    @Override
    public long getItemId(int position) {return position;}

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ItemViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.header_item);
        }
    }
}
