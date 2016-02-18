package com.example.satu.obiektysportowe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.Date;

/**
 * Created by Piotrek on 2015-05-22.
 */

public class ReservationConfirmDialog extends DialogFragment {

    //Dialog jest aktywowany w DatesActivity wylacznie, gdy wybrany termin jest dostepny i uzytkownik jest zalogowany

        public ReservationConfirmDialog() {
        }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.reservation_confirm_dialog, null);

            final TextView textView = (TextView) view.findViewById(R.id.textView);
            final TextView textView2 = (TextView) view.findViewById(R.id.textView2);
            final TextView textView3 = (TextView) view.findViewById(R.id.textView3);
            final TextView textView4 = (TextView) view.findViewById(R.id.textView4);
            final TextView textView5 = (TextView) view.findViewById(R.id.textView5);
            final TextView textView6 = (TextView) view.findViewById(R.id.textView6);

            //ustawianie tresci tekstow zawierajacych szczegoly dokonywanej rezerwacji

            textView.setText("Nazwa obiektu: " + getArguments().getString("name"));
            textView2.setText("Data: " + getArguments().getString("date"));
            textView3.setText("Start: " + getArguments().getString("startTime"));
            textView4.setText("Koniec: " + getArguments().getString("endTime"));
            textView5.setText("Czas trwania rezerwacji: " + Double.toString(getArguments().getDouble("duration")) + " h");
            textView6.setText("Koszt: " + Double.toString(getArguments().getDouble("price")) + " PLN");

            builder.setMessage("Obiekt jest dostepny.")
                    .setView(view)
                    .setPositiveButton("Zarezerwuj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int id) {
                            Dialog dialog  = (Dialog) dialogInterface;

                            if (ParseUser.getCurrentUser() != null) { //uwierzytelnienie uzytkownika

                                putObjectsToCloud(
                                        getArguments().getString("object_id"),
                                        ParseUser.getCurrentUser().getObjectId(), //ID zalogowanego uzytkownika
                                        getArguments().getString("date"),
                                        getArguments().getString("startTime"),
                                        getArguments().getString("endTime"),
                                        getArguments().getDouble("price")
                                );

                                //Przygotowanie paczki dla nowej Activity - ReservationConfirmation
                                Intent intent = new Intent(view.getContext(), ReservationConfirmation.class);
                                intent.putExtra("name", getArguments().getString("name"));
                                intent.putExtra("date", getArguments().getString("date"));
                                intent.putExtra("startTime", getArguments().getString("startTime"));
                                intent.putExtra("endTime", getArguments().getString("endTime"));
                                intent.putExtra("duration", Double.toString(getArguments().getDouble("duration")) + " h");
                                intent.putExtra("rate", Double.toString(getArguments().getDouble("price")) + " PLN");
                                intent.putExtra("url", getArguments().getString("url"));;
                                startActivity(intent);
                            } else {
                                ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                                        view.getContext());
                                startActivityForResult(loginBuilder.build(), 0);
                            }
                        }
                    })
                    .setNegativeButton("Popraw", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Powrot do DatesActivity
                        }
                    });

            return builder.create();
        }

    //zapis Rezerwacji do bazy danych

    public void putObjectsToCloud(String object_id, String user_id, String date, String startTime, String endTime, double price){

        DatesActivity da = new DatesActivity();
        Date startDate = da.prepareDateTime(date, startTime);
        Date endDate = da.prepareDateTime(date, endTime);

        ParseObject reservation = new ParseObject("Registration");
        reservation.put("Id_obj", ParseObject.createWithoutData("Sport_object", object_id));
        reservation.put("Id_user", ParseObject.createWithoutData("_User", user_id));
        reservation.put("Start_date", startDate);
        reservation.put("End_date", endDate);
        reservation.put("Cost", price);
        reservation.saveInBackground();
    }

}
