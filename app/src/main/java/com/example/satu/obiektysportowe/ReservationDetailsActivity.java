package com.example.satu.obiektysportowe;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satu.obiektysportowe.Data.Reservation;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ReservationDetailsActivity extends ActionBarActivity {
    @InjectView(R.id.share_button)
    ShareButton shareButton;

    @InjectView(R.id.textView_object)
    TextView textView_object;

    @InjectView(R.id.textView_date)
    TextView textView_date;

    @InjectView(R.id.textView_startTime)
    TextView textView_startTime;

    @InjectView(R.id.textView_endTime)
    TextView textView_endTime;

    @InjectView(R.id.textView_rate)
    TextView textView_rate;

    Reservation reservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);
        ButterKnife.inject(this);

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");


       reservation = (Reservation) getIntent().getSerializableExtra(ViewReservationsActivity.RESERVATION_EXTRA);
       textView_object.setText("Nazwa obiektu "+reservation.getObjectName());
       textView_rate.setText("Koszt: " + reservation.getCost() + "PLN");
       textView_startTime.setText("Start: " + dateFormat.format(reservation.getStart()).toString());
       textView_endTime.setText("Koniec: " + dateFormat.format(reservation.getEnd()).toString());
       dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       textView_date.setText("Data: "+dateFormat.format(reservation.getStart()).toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reservation_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @OnClick(R.id.share_button)
    public void onClicked(){
        getLinkContent();
    }
    private void getLinkContent() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sport_object");
        query.getInBackground(reservation.getSpotrObiectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

                shareButton.setShareContent(new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(parseObject.getString("url") == null ? "www.google.pl" : parseObject.getString("url")))
                        .setContentTitle("Trening !")
                        .setContentDescription("Zapraszam na trening do " + reservation.getObjectName() + " dnia " + dateFormat1.format(reservation.getStart()).toString() + " godzina " + dateFormat.format(reservation.getStart()).toString())
                        .build());
            }
        });

    }
    @OnClick(R.id.button_usun)
    public void onClicked1(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Registration");
        query.getInBackground(reservation.getId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(getApplication(),"Usunieto rezerwacje",Toast.LENGTH_SHORT).show();
                            setResult(2);
                            finish();
                        }
                    });
                }
            }
        });
    }

}
