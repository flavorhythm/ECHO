package widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zyuki on 7/8/2016.
 */
public class NoScrollRecyclerView extends RecyclerView {
    private boolean isScrollable = false;

    public NoScrollRecyclerView(Context context) {
        super(context);
    }

    public NoScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int computeVerticalScrollRange() {
        if(isScrollable) return super.computeVerticalScrollRange();
        else return 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(isScrollable) return super.onInterceptTouchEvent(e);
        else return false;
    }
}
