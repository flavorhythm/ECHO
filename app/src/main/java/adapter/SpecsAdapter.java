package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.echo_usa.echo.R;

import java.util.List;

import data.Specs;

/**
 * Created by ZYuki on 7/7/2016.
 */
public class SpecsAdapter extends ArrayAdapter<Specs> {
    private LayoutInflater inflater;
    private List<Specs> specsList;

    private int layoutRes;

    public SpecsAdapter(Activity activity, int layoutRes, List<Specs> specsList) {
        super(activity, layoutRes, specsList);

        this.inflater = LayoutInflater.from(activity);
        this.layoutRes = layoutRes;
        this.specsList = specsList;
    }

    @Override
    public int getCount() {
        return specsList.size();
    }

    @Override
    public Specs getItem(int position) {
        return specsList.get(position);
    }

    @Override
    public int getPosition(Specs item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            viewHolder = new ViewHolder();
            row = inflater.inflate(layoutRes, parent, false);

            viewHolder.label = (TextView)row.findViewById(R.id.specs_text_specsLabel);
            viewHolder.value = (TextView)row.findViewById(R.id.specs_text_specsValue);
        } else viewHolder = (ViewHolder)row.getTag();

        Specs thisItem = getItem(position);

        viewHolder.label.setText(thisItem.getLabel());
        viewHolder.value.setText(thisItem.getValue());

        return row;
    }

    private class ViewHolder {
        TextView label;
        TextView value;
    }
}
