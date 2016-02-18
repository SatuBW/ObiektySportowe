package com.example.satu.obiektysportowe;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.satu.obiektysportowe.Helpers.PrintPdf;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ReservationConfirmation extends ActionBarActivity {

    //Aktywowane wylacznie, gdy nacisnieto Rezerwuj w Dialogu Reserv.Confirm.
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

    @InjectView(R.id.textView_duration)
    TextView textView_duration;

    @InjectView(R.id.linearLayout_print)
    LinearLayout print_layout;

    @InjectView(R.id.button_PDF)
    Button button_PDF;

    Bundle extras ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirmation);
        ButterKnife.inject(this);

        extras = getIntent().getExtras();

        if (extras != null) {
            textView_object.setText("Nazwa obiektu "+extras.getString("name"));
            textView_rate.setText("Koszt: "+ extras.getString("rate"));
            textView_duration.setText("Czas trwania rezerwacji: "+ extras.getString("duration"));
            textView_startTime.setText("Start: "+extras.getString("startTime"));
            textView_endTime.setText("Koniec: "+extras.getString("endTime"));
            textView_date.setText("Data: "+extras.getString("date"));
        }

        //button z PDF staje sie widoczny w systemach Android od wersji 4.4 (KitKat, API level 19)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            button_PDF.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reservation_confirmation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_usun)
    public void onClicked(){
        Intent intent = new Intent(getApplicationContext(),ViewReservationsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_nowa)
    public void onClicked2(){
        Intent intent = new Intent(getApplicationContext(), SearchObjectsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_PDF)
    public void onClicked3(){
        PrintPdf pdf = new PrintPdf();
        pdf.generatePDF(getApplicationContext(), print_layout);
  }

    @OnClick(R.id.share_button)
    public void onClicked4(){
        shareButton.setShareContent(getLinkContent());
    }
    private ShareLinkContent getLinkContent() {
        return new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(extras.getString("url") == null ? "www.google.pl" : extras.getString("url") ))
                .setContentTitle("Trening !")
                .setContentDescription("Zapraszam na trening do " + extras.getString("name") + " dnia "+ extras.getString("date") + " godzina " + extras.getString("startTime"))
                .build();
    }
}
