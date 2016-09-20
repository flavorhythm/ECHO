package adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.ParallelExecutorCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo_usa.echo.R;

import java.lang.ref.WeakReference;
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

        AsyncTaskCompat.executeParallel(new ImageAsyncTask(modelHolder.modelIcon), model.getImgResource());
        modelHolder.modelName.setText(model.getModelName());
        modelHolder.serialNum.setText(model.getSerialNum());

        return row;
    }

    class ImageAsyncTask extends AsyncTask<Integer, Void, Bitmap> {
        final WeakReference<CircularImageView> imageViewReference;
        int resource;

        ImageAsyncTask(CircularImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            resource = params[0];

            return decodeSampledBitmapFromResource(
                    getContext().getResources(),
                    resource,
                    CircularImageView.SIZE,
                    CircularImageView.SIZE
            );
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(imageViewReference != null && bitmap != null) {
                final View imageView = imageViewReference.get();
                if(imageView != null) ((CircularImageView)imageView).setImageBitmap(bitmap);
            }

            super.onPostExecute(bitmap);
        }

        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 3;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                      int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }
    }

    private class ModelHolder {
        TextView modelName, serialNum;
        CircularImageView modelIcon;
    }
}
