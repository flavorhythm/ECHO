package com.echo_usa.echo;

import android.app.Application;
import android.util.Log;

import data.DataAccessObject;

/**
 * Created by ZYuki on 6/28/2016.
 */
public class DataAccessApplication extends Application {
    private static DataAccessObject dataAccess;
    private static ValueChangeSupport valueChange;

    @Override
    public void onCreate() {
        super.onCreate();

        if(dataAccess == null) dataAccess = new DataAccessObject(DataAccessApplication.this);
        if(valueChange == null) valueChange = new ValueChangeSupport();
    }

    public DataAccessObject getDataAccessObject() {
        Log.d("DataAccessApplication", "getDataAccessObject");
        return dataAccess;
    }

    public ValueChangeSupport getValueChangeSupport() {
        Log.d("DataAccessApplication", "getValueChangeSupport");
        return valueChange;
    }
}
