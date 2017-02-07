package com.echo_usa.echo;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import data.IntentKey;

public class CardDisplayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_display);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(
                    ContextCompat.getDrawable(CardDisplayActivity.this, R.drawable.ic_vector_clear)
            );
            getSupportActionBar().setBackgroundDrawable(
                    ContextCompat.getDrawable(CardDisplayActivity.this, R.drawable.app_bar_solid)
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTestText((TextView)findViewById(R.id.card_display_text));
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

    private void setTestText(TextView tv) {
        final Bundle b = getIntent().getExtras();

        if(b != null && !b.isEmpty()) {
            int cardType = getIntent().getExtras().getInt(IntentKey.CONTENT_TYPE_KEY);
            int cardId = getIntent().getExtras().getInt(IntentKey.CONTENT_ID_KEY);

            if(tv != null) tv.setText("card type: " + cardType + " card ID: " + cardId);
        }
    }
}
