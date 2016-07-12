package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.echo_usa.echo.R;

/**
 * Created by ZYuki on 6/30/2016.
 */
public class NoModelView extends View {
    private Animation hideAnimation, showAnimation;

    public NoModelView(Context context) {
        super(context);
        init();
    }


    public NoModelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoModelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NoModelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        hideAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.no_model_fade_out);
        showAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.no_model_fade_in);
    }

    public boolean isVisible() {return getVisibility() == View.VISIBLE;}

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
    }

    public void startAnimation(boolean toShow) {
        if(toShow) startAnimation(showAnimation);
        else startAnimation(hideAnimation);
    }
}
