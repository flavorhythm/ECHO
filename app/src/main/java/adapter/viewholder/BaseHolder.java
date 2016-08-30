package adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import util.MetricCalcs;

/**
 * Created by ZYuki on 7/13/2016.
 */
public class BaseHolder extends RecyclerView.ViewHolder {
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_BLANK_DIVIDER = 1;
    public static final int VIEW_TYPE_CONTENT_DIVIDER = 2;
    public static final int VIEW_TYPE_ITEM_SINGLE = 3;
    public static final int VIEW_TYPE_ITEM_DOUBLE = 4;

    public static final int DIVIDER_HEIGHT = MetricCalcs.dpToPixels(10);

    public BaseHolder(View itemView) {
        super(itemView);
    }
}
