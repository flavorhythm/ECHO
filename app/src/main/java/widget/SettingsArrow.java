package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/27/2016.
 */
public class SettingsArrow extends ImageView {
    public static final int ARROW_STATE_DOWN = 0;
    public static final int ARROW_STATE_UP = 1;

    private int arrowState = ARROW_STATE_DOWN;

    private final Animation rotationUp = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_settings_arrow_up);
    private final Animation rotationDown = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_settings_arrow_down);

    private Menu drawerMenu;

    private OnClickListener clickToAnimateListener;

    public SettingsArrow(Context context) {
        super(context);
        init();
    }

    public SettingsArrow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingsArrow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingsArrow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public boolean isArrowDown() {
        return arrowState == ARROW_STATE_DOWN;
    }

//    public boolean isArrowDown(int arrowState) {
//        return arrowState == ARROW_STATE_DOWN;
//    }

    public void setDrawerMenu(Menu drawerMenu) {
        this.drawerMenu = drawerMenu;
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        setOnClickListener(null);
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();

        //when current arrow state is down, set arrow to arrow up drawable and change state
        //variable accordingly
        if(isArrowDown()) {
            setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.setting_arrow_up));
            arrowState = ARROW_STATE_UP;
        } else {
            setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.setting_arrow_down));
            arrowState = ARROW_STATE_DOWN;
        }

        drawerMenu.setGroupVisible(R.id.nav_menu_group_home, isArrowDown());
        drawerMenu.setGroupVisible(R.id.nav_menu_group_resources, isArrowDown());
        drawerMenu.setGroupVisible(R.id.nav_menu_group_settings, !isArrowDown());

        setOnClickListener(clickToAnimateListener);
    }

//    public Animation getAnimationFor(int arrowState) {
//        rotationUp.setRepeatCount(0);
//        rotationDown.setRepeatCount(0);
//
//        if (isArrowDown(arrowState)) {
//            return rotationUp;
//        } else {
//            return rotationDown;
//        }
//    }

    private void init() {
        rotationUp.setRepeatCount(0);
        rotationDown.setRepeatCount(0);

        clickToAnimateListener = new OnClickListener() {
            @Override
            public void onClick(View settingsArrow) {
                if(isArrowDown()) {
                    settingsArrow.startAnimation(rotationUp);
                } else {
                    settingsArrow.startAnimation(rotationDown);
                }
            }
        };

        setOnClickListener(clickToAnimateListener);
    }
}
