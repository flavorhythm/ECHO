package fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

/**
 * Created by zyuki on 7/28/2016.
 */
public class DialogAddUnit extends DialogFragment implements DialogInterface.OnClickListener {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**PUBLIC VARIABLES**/
    /**PRIVATE VARIABLES**/
    private EditText serialNum;

    public static DialogAddUnit newInstance() {

        Bundle args = new Bundle();

        DialogAddUnit fragment = new DialogAddUnit();
        fragment.setArguments(args);
        return fragment;
    }


    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /****/
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        serialNum = new EditText(getContext());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Enter serial number")
                .setView(serialNum)
                .setNegativeButton("dismiss", this)
                .setPositiveButton("validate", this)
                .create();
    }

    /****/
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which) {
            case AlertDialog.BUTTON_NEGATIVE:
                break;
            case AlertDialog.BUTTON_POSITIVE:
                //TODO: work on this
//                getArguments().getString(ACCOUNT_NAME);
                serialNum.getText();

                //TODO: open a new dialog, send return data
                break;
        }
    }
}
