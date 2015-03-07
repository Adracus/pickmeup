package com.pickmeupscotty.android.maps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axel on 07/03/15.
 */
public class ChoosePlaceDialogFragment extends DialogFragment {
    private List<GooglePlaces.Place> places;
    private String[] placeNames;

    private ChoosePlaceListener mListener;
    private GooglePlaces.Place selectedPlace;

    public void setPlaces(List<GooglePlaces.Place> places) {
        this.places = places;
        placeNames = new String[places.size()];
        for (int i = 0; i < places.size(); i++) {
            placeNames[i] = places.get(i).getName();
        }
        selectedPlace = places.get(0);
    }

    GooglePlaces.Place placeWithName(String name) {
        for (int i = 0; i < places.size(); i++) {
            if (name == places.get(i).getName()) return places.get(i);
        }
        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());



        // Set the dialog title
        builder.setTitle("Pick the correct place")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(placeNames, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPlace = placeWithName(placeNames[which]);
                    }
                })
                        // Set the action buttons
                .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.placeChosen(selectedPlace);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.canceled();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ChoosePlaceListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ChoosePlaceListener");
        }
    }

    public interface ChoosePlaceListener {
        public void placeChosen(GooglePlaces.Place place);

        public void canceled();
    }
}
