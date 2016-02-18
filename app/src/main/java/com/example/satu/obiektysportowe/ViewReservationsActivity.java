package com.example.satu.obiektysportowe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.satu.obiektysportowe.Data.Reservation;
import com.example.satu.obiektysportowe.Helpers.ReservationsAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zmichalak on 5/6/2015.
 */
public class ViewReservationsActivity extends ActionBarActivity{

    public static final String RESERVATION_EXTRA = "reservation";
    private String userId = "5YtM04dDL2";
    private List<Reservation> reservations = new ArrayList<>();
    @InjectView(R.id.expandableListView_reservations)
    ListView reservationList;
    private ObiektySportowe obiekt;
    ReservationsAdapter tmpReservationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resevations);
        ButterKnife.inject(this);
        userId = ParseUser.getCurrentUser().getObjectId();
        getReservations();

        reservationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(),ReservationDetailsActivity.class);
                intent.putExtra(RESERVATION_EXTRA,reservations.get(position));
                startActivityForResult(intent, 1);
            }
        });
     }

    public void getReservations(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Registration");
        query.whereEqualTo("Id_user", ParseUser.getCurrentUser());
        query.include("Sport_object.Name");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    for (ParseObject parseObject : scoreList) {
                        ParseObject obj = parseObject.getParseObject("Id_obj");
                        final Reservation resevationTmp = new Reservation(parseObject.getObjectId(), parseObject.getDate("Start_date"),
                                parseObject.getDate("End_date"), parseObject.getDate("createdAt"), parseObject.getDate("updatedAt"), obj.getObjectId(), parseObject.getNumber("Cost").intValue());
                        reservations.add(resevationTmp);
                    }
                    addObiectNameToReservations();
                    //showReservations();
                } else {
                    Toast.makeText(getApplicationContext(), "Blad pobierania rezerwacji", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    public void addObiectNameToReservations() {
        for (final Reservation c : reservations) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Sport_object");
            query.getInBackground(c.getSpotrObiectId(), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject1, ParseException e) {
                    if (e == null) {
                        c.setObjectName(parseObject1.getString("Name"));
                        showReservations();
                    }
                }
            });
        }
    }

    public void showReservations(){
        tmpReservationsAdapter = new ReservationsAdapter(getApplicationContext(),R.layout.reservation_list_widget_description,null);
        tmpReservationsAdapter.addAll(reservations);
        reservationList.setAdapter(tmpReservationsAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2){
            reservations.clear();
            tmpReservationsAdapter.clear();
            getReservations();
            tmpReservationsAdapter.notifyDataSetChanged();
        }
    }
}
