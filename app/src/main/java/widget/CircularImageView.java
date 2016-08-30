package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.echo_usa.echo.R;

/**
 * Created by ZYuki on 7/20/2016.
 */
public class CircularImageView extends ImageView {
    //TODO: OR... change this so only icons are displayed
    private static Bitmap mask;
    private Bitmap image;
    private Canvas mCanvas;
    private Paint paint;

    public CircularImageView(Context context) {
        super(context);
        init(null);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if(mask == null) {
            Drawable maskDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_shape_model_img_mask);
            mask = drawableToBitmap(maskDrawable);
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; //TODO: will it always be two?
        image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_model_spec, options);
//        cardImage = decodeBitmapFromRes(R.drawable.ic_model_spec, 40, 40);

        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(result);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(image, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);

        setScaleType(ScaleType.CENTER);

        setImageBitmap(result);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
