package com.echo_usa.echo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fragment.BaseFragment;
import fragment.FragmentBeginners;
import fragment.FragmentDocuments;
import fragment.FragmentLocator;
import fragment.FragmentMaintenance;
import fragment.FragmentSettings;
import fragment.FragmentSpecifications;
import util.Const;

public class ContentDisplayActivity extends AppCompatActivity implements BaseFragment.Callback {

    private String fragName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_display);

        fragName = getIntent().getExtras().getString("frag_name");

        setTitle(fragName);

        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.content_fragment, getFragment(fragName), fragName);
        fragTrans.commit();
    }

    @Override
    public String getFragName() {return fragName;}

    private Fragment getFragment(String fragName) {
        switch(fragName) {
            case "documentation":
                return new FragmentDocuments();
            case "specifications":
                return new FragmentSpecifications();
            case "settings":
                return new FragmentSettings();
            case "maintenance":
                return new FragmentMaintenance();
            case "locator":
                return new FragmentLocator();
            case "beginners":
                return new FragmentBeginners();
            default:
                return null; //TODO: do something about this
        }
    }
}
