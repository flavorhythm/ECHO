package com.echo_usa.echo;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import fragment.BaseFragment;

public class CardDisplayActivity extends AppCompatActivity implements BaseFragment.Callback {
    private Toolbar toolbar;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_display);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(
                ContextCompat.getDrawable(CardDisplayActivity.this, R.drawable.ic_clear)
        );
        getSupportActionBar().setBackgroundDrawable(
                ContextCompat.getDrawable(CardDisplayActivity.this, R.drawable.app_bar_fragments)
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToolbar(Toolbar toolbar) {this.toolbar = toolbar;}

    @Override
    public void closeDrawer(int gravity) {}

    @Override
    public View.OnClickListener getCardListnener() {return null;}
}
