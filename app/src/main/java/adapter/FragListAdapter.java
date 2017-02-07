package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.echo_usa.echo.AdapterCallbacks;

import java.util.List;

import data.frag_list.FragListItem;
import widget.DividerView;
import widget.FragListItemView;

/**
 * Created by zyuki on 6/10/2016.
 */

//http://www.survivingwithandroid.com/2014/08/android-listview-with-multiple-row.html
public class FragListAdapter extends BaseAdapter {
    private List<FragListItem> mItemList;
    private Context mContext;
    private int mTypeCount;

    private AdapterCallbacks mCallback;

    public FragListAdapter(Context context, int typeCount, List<FragListItem> itemList) {
        setContext(context);
        setViewTypeCount(typeCount);

        mCallback = (AdapterCallbacks) getContext();
        mItemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {return mItemList.get(position).getType();}

    @Override
    public int getViewTypeCount() {return mTypeCount;}

    @Override
    public int getCount() {return mItemList.size();}

    @Override
    public FragListItem getItem(int position) {return mItemList.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    private Context getContext() {return mContext;}

    private void setContext(Context c) {mContext = c;}
    private void setViewTypeCount(int tc) {mTypeCount = tc;}

    @NonNull
    @Override
    public View getView(int position, View row, @NonNull ViewGroup parent) {
        FragListItem item = getItem(position);
        switch(getItemViewType(position)) {
            case FragListItem.CONTENT:
                ItemHolder ih;
                if(row == null || row.getTag() == null) {
                    ih = new ItemHolder();
                    row = new FragListItemView(getContext());

                    ih.setView(row);
                    row.setTag(ih);
                } else ih = (ItemHolder)row.getTag();

                ih.setContent(item.getIconRes(), item.getTitle(), item.getSubtitle());
                ih.setListener(mCallback.getFragListItemListener(item));
                return ih.getView();
            case FragListItem.DIVIDER:
                DividerHolder dh;
                if(row == null || row.getTag() == null) {
                    dh = new DividerHolder();
                    row = new DividerView(getContext());

                    dh.setView(row);
                    row.setTag(dh);
                } else dh = (DividerHolder)row.getTag();

                dh.setContent(item.getTitle());
                return dh.getView();
        }

        return row;
    }

    private class DividerHolder {
        DividerView mDividerView;

        void setView(View v) {mDividerView = (DividerView)v;}
        void setContent(String text) {mDividerView.setContent(text, null);}

        View getView() {return mDividerView;}
    }

    private class ItemHolder {
        FragListItemView mItemView;

        void setListener(View.OnClickListener l) {mItemView.setOnClickListener(l);}
        void setView(View v) {mItemView = (FragListItemView)v;}
        void setContent(int resId, String text, @Nullable String subtext) {
            mItemView.setContent(resId, text, subtext);
        }

        View getView() {return mItemView;}
    }
}