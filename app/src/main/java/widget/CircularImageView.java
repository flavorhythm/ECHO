package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.echo_usa.echo.R;

import java.lang.ref.WeakReference;

import util.MetricCalcs;

/**
 * Created by ZYuki on 7/20/2016.
 */
public class CircularImageView extends View {
    private Bitmap image;
    private Path path;

    public static final int SIZE = MetricCalcs.dpToPixels(40);

    public CircularImageView(Context context) {
        this(context, null);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        path = new Path();
        path.addCircle(SIZE / 2, SIZE / 2, SIZE / 2, Path.Direction.CW);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int resolvedWidth = View.resolveSize(desiredWidth(), widthMeasureSpec);
        int resolvedHeight = View.resolveSize(desiredHeight(), heightMeasureSpec);

        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(image != null) {
            if(path != null) canvas.clipPath(path, Region.Op.REPLACE);
            canvas.drawBitmap(image, -1 * (image.getWidth() / 2), -1 * (image.getHeight() / 4), null);
        }
    }

    public void setImageBitmap(Bitmap image) {
        this.image = image;

        invalidate();
    }

    private int desiredWidth() {return SIZE;}
    private int desiredHeight() {return SIZE;}

//    //TODO: OR... change this so only icons are displayed
//    private static Bitmap mask;
//    private Bitmap image;
//    private Bitmap result;
//    private Canvas mCanvas;
//    private Paint paint;
//
//    public CircularImageView(Context context) {
//        super(context);
//        init(null);
//    }
//
//    public CircularImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(attrs);
//    }
//
//    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(attrs);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(attrs);
//    }
//
//    private void init(@Nullable AttributeSet attrs) {
//        if(mask == null) {
////            Drawable maskDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_shape_model_img_mask);
////            mask = drawableToBitmap(maskDrawable);
//
//            mask = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_shape_model_img_mask);
//        }
//
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2; //TODO: will it always be two?
//        //image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_model_spec, options);
////        cardImage = decodeBitmapFromRes(R.drawable.ic_model_spec, 40, 40);
//
//        result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
//
//        mCanvas = new Canvas(result);
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//
//        //setImageBitmap(image);
//
//        setScaleType(ScaleType.CENTER);
//    }
//
//    @Override
//    public void setImageBitmap(Bitmap bm) {
//        //result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
//
//        mCanvas = new Canvas(result);
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        mCanvas.drawBitmap(bm, 0, 0, null);
//        mCanvas.drawBitmap(mask, 0, 0, paint);
//        paint.setXfermode(null);
//
//        super.setImageBitmap(result);
//    }

//    private static Bitmap drawableToBitmap(Drawable drawable) {
//        Bitmap bitmap = null;
//
//        if (drawable instanceof BitmapDrawable) {
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//            if(bitmapDrawable.getBitmap() != null) {
//                return bitmapDrawable.getBitmap();
//            }
//        }
//
//        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
//            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
//        } else {
//            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        }
//
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawable.draw(canvas);
//        return bitmap;
//    }
}
