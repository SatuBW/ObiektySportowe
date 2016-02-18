package com.example.satu.obiektysportowe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Satu on 2015-04-19.
 */


public class EvaluationDialog extends DialogFragment {

    RatingBar rating;
    public EvaluationDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.evaluate_dialog, null);

        final Bundle arguments = getArguments();
        rating = (RatingBar) view.findViewById(R.id.ratingBar_ocena);

        builder.setMessage("Oceń obiekt: " + arguments.getString(ObjectDetailsActivity.OBJECT_NAME)  )
                .setView(view)
                .setPositiveButton("Oceń", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(rating.getRating() > 0)
                            putEvaluationToCloud(arguments.getString(ObjectDetailsActivity.OBIECT_ID));

                        Toast.makeText(view.getContext(), "Zapisano ocenę.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
    public void putEvaluationToCloud(String object_id){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sport_object");
        query.getInBackground(object_id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put("Count_rate", (parseObject.getNumber("Count_rate") == null ? 0 : parseObject.getNumber("Count_rate").intValue()) + 1);
                    parseObject.put("Sum_rate", (parseObject.getNumber("Sum_rate") == null ? 0 : parseObject.getNumber("Sum_rate").doubleValue()) + rating.getRating() );
                    parseObject.saveInBackground();
                }
            }
        });
    }
}
