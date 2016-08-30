package com.echo_usa.echo;

import android.util.Log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import util.FragName;

/**
 * Created by ZYuki on 6/21/2016.
 */
public class ValueChangeSupport {
    public static final String PROPERTY_MODEL = "ModelNameChange";
    public static final String NO_SELECTION = "no_selection";

    private PropertyChangeSupport propertyChange;
    private String modelName = NO_SELECTION;
    private String queuedModelName;

    private FragName fragToDisplay = FragName.BLANK;

    public ValueChangeSupport() {
        propertyChange = new PropertyChangeSupport(this);
    }

    public String getSelectedModel() {
        return modelName;
    }

    public void setSelectedModel(String modelName) {
        String prevModel = this.modelName;
        this.modelName = modelName;

        Log.d("ValueChangeSupport", "setSelectedModel: modelName property change fired");

        propertyChange.firePropertyChange(PROPERTY_MODEL, prevModel, modelName);
    }

    public void enqueueModelNameChange(String modelName) {
        this.queuedModelName = modelName;
    }
    
    public String getEnqueuedModelName() {return queuedModelName;}

    public void setQueuedModelName() {
        //TODO: error out?
        if(queuedModelName != null) setSelectedModel(queuedModelName);
    }
//
//    public FragName getFragToDisplay() {return fragToDisplay;}
//
//    public void setFragToDisplay(FragName fragToDisplay) {
//        FragName previousFrag = this.fragToDisplay;
//        this.fragToDisplay = fragToDisplay;
//
//        propertyChange.firePropertyChange(PROPERTY_MENU_ITEM, previousFrag, fragToDisplay);
//    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        Log.v("ValueChangeSupport", "addPropertyChangeListener: " + listener.toString());
        propertyChange.addPropertyChangeListener(listener);
    }
}
