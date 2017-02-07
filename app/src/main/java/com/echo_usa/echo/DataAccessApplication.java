package com.echo_usa.echo;

import android.app.Application;
import android.util.Log;

import data.DataAccessObject;

/**
 * Created by Zeno Yuki on 6/28/2016.
 * Extends the Application class to give the entire application access to helper classes.
 * Accessible only by classes that have access to Activity.getApplication()
 */
public class DataAccessApplication extends Application {
    /**
     * VARIABLES
     */
    private static DataAccessObject mDataAccess;
//    private static ValueChangeSupport mValueChange;

    /**
     * OVERRIDES
     */
    /** onCreate override method; instantiates helper classes DataAcessObject and ValueChangeSupport */
    @Override
    public void onCreate() {
        super.onCreate();

        if(mDataAccess == null) mDataAccess = new DataAccessObject(DataAccessApplication.this);
//        if(mValueChange == null) mValueChange = new ValueChangeSupport();
    }

    /**
     * PUBLICS
     */
    /** Gets instance of DataAccessObject */
    public DataAccessObject getDataAccessObject() {return mDataAccess;}

    /** Gets instance of ValueChangeSupport */
//    public ValueChangeSupport getValueChangeSupport() {return mValueChange;}
}
