package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo_usa.echo.R;

import util.MetricCalcs;

/**
 * Created by ZYuki on 7/19/2016.
 */
public class CardDivider extends RelativeLayout {
    private String dividerTitle, dividerSubtitle;
    private TextView dividerText;

    public CardDivider(Context context) {
        super(context);
        init(null);
    }

    public CardDivider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CardDivider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardDivider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if(attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CardDivider, 0, 0);
            setDividerTitle(a.getString(R.styleable.CardDivider_dividerTitle));

            boolean disableSubtitle = a.getBoolean(R.styleable.CardDivider_diableSubtitle, false);
            if(!disableSubtitle) setDividerSubtitle(a.getString(R.styleable.CardDivider_dividerSubtitle));
            a.recycle();
        }

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_card_divider, this, true);

        dividerText = (TextView)findViewById(R.id.divider_text);

        if(getDividerTitle() != null) setDividerText(getDividerTitle(), getDividerSubtitle());
    }

    public void setDividerText(@NonNull String dividerTitle, @Nullable String dividerSubtitle) {
        setDividerTitle(dividerTitle);
        setDividerSubtitle(dividerSubtitle);
        final String newLine = "\n";

        SpannableString spannedTitle = new SpannableString(dividerTitle);
        spannedTitle.setSpan(new TextAppearanceSpan(getContext(), android.R.style.TextAppearance_Large), 0, spannedTitle.length(), 0);
        spannedTitle.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorAccent)), 0, spannedTitle.length(), 0);

        dividerText.setText(spannedTitle);

        if(dividerSubtitle != null) {
            dividerText.append(newLine + dividerSubtitle);
        }
    }

    public void addSpacer() {
        setPadding(0, MetricCalcs.getActionBarSize(getContext()), 0, 0);
    }

    public String getDividerTitle() {return dividerTitle;}
    public String getDividerSubtitle() {return dividerSubtitle;}

    public void setDividerTitle(@NonNull String dividerTitle) {this.dividerTitle = dividerTitle;}
    public void setDividerSubtitle(@Nullable String dividerSubtitle) {this.dividerSubtitle = dividerSubtitle;}
}
