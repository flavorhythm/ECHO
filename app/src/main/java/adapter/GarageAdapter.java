package adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo_usa.echo.R;

import java.util.List;

import data.Model;
import util.MetricCalcs;
import widget.CircularImageView;

/**
 * Created by ZYuki on 7/20/2016.
 */
public class GarageAdapter extends ArrayAdapter<Model> {
    private LayoutInflater inflater;

    private int layoutRes;
    private List<Model> modelList;

    public GarageAdapter(Context context, int layoutRes, List<Model> modelList) {
        super(context, layoutRes, modelList);

        this.inflater = LayoutInflater.from(context);

        this.layoutRes = layoutRes;
        this.modelList = modelList;
    }

    @Override public int getCount() {return modelList.size() + 1;}
    @Override public Model getItem(int position) {return modelList.get(position);}
    @Override public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        if(position == 0) {
            View view = new View(getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, MetricCalcs.dpToPixels(10)
            );
            view.setLayoutParams(params);
            view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_model_info_lower_fade));
            return view;
        }

        ModelHolder modelHolder;

        position -= 1;
        if(row == null || row.getTag() == null) {
            modelHolder = new ModelHolder();
            row = inflater.inflate(layoutRes, parent, false);

            modelHolder.modelIcon = (CircularImageView)row.findViewById(R.id.garage_img_modelIcon);
            modelHolder.modelName = (TextView)row.findViewById(R.id.garage_text_modelName);
            modelHolder.serialNum = (TextView)row.findViewById(R.id.garage_text_unitSerial);
        } else modelHolder = (ModelHolder)row.getTag();

        Model model = getItem(position);

//        modelHolder.modelIcon.setImageBitmap();
        modelHolder.modelName.setText(model.getModelName());
        modelHolder.serialNum.setText(model.getSerialNum());

        return row;
    }

    private class ModelHolder {
        TextView modelName, serialNum;
        CircularImageView modelIcon;
    }
}
