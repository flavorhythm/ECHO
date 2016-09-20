package com.echo_usa.echo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import data.Model;
import util.NullEnqueuedException;

/**
 * Created by Zeno Yuki on 6/21/2016.
 * Stores currently displayed model and forewards data to listeners when display model changes
 */
public class ValueChangeSupport {
    /**
     * VARIABLES
     */
    public static final String PROPERTY_MODEL = "ModelNameChange";
    private PropertyChangeSupport mPropertySupport;
    private Model mDisplayedModel, mEnqueuedModel;

    /**
     * CONSTRUCTORS
     */
    public ValueChangeSupport() {
        mPropertySupport = new PropertyChangeSupport(this);
        mDisplayedModel = new Model(Model.EMPTY_FLAG); //Empty set to true
        mEnqueuedModel = new Model(Model.EMPTY_FLAG); //Empty set to true
    }

    /**
     * PUBLICS
     */
    /** Ties together the class that requires notification when new model is selected and this class via a listener object */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        mPropertySupport.addPropertyChangeListener(listener);
    }
    /** Gets displayed model */
    public Model getDisplayedModel() {return mDisplayedModel;}
    /** Sets enqueued model */
    public void setEnqueuedModel(Model model) {mEnqueuedModel = model;}
    /** Sets enqueued model as displayed, fires info to listener and clears enqueued entry */
    public void setEnqueuedAsDisplayed() throws NullEnqueuedException {
        if(mEnqueuedModel != null) {
            mPropertySupport.firePropertyChange(PROPERTY_MODEL, mDisplayedModel, mEnqueuedModel);

            setDisplayedModel(mEnqueuedModel);
            setEnqueuedModel(null);
        } else throw new NullEnqueuedException("Enqueued is null", new Throwable("User-selected model not enqueued correctly"));
    }

    /**
     * PRIVATES
     */
    /** Sets displayed model */
    private void setDisplayedModel(Model newModel) {mDisplayedModel = newModel;}
}
