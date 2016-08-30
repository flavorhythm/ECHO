package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo_usa.echo.R;

/**
 * Created by ZYuki on 7/19/2016.
 *
 *     android:layout_width="wrap_content"
 android:layout_height="wrap_content"
 android:padding="10dp"
 */
public class ChipView extends RelativeLayout {
    private TextView chipTextView;
    private ImageView chipBgView;

    public ChipView(Context context) {
        super(context);
        init(null);
    }

    public ChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChipView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void setChipText(String tagText) {
        if(tagText != null) chipTextView.setText(tagText);
    }

    private void init(@Nullable AttributeSet attrs) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        String chipText = "";
        if(attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ChipView, 0, 0);
            chipText = a.getString(R.styleable.ChipView_chipText);
            a.recycle();
        }

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_chip, this, true);

        chipTextView = (TextView)findViewById(R.id.chip_text);
        chipBgView = (ImageView)findViewById(R.id.chip_bg);

        setChipText(chipText);
        bringTextToFront();
    }

    private void bringTextToFront() {
        chipTextView.bringToFront();
        chipBgView.invalidate();
    }
}
