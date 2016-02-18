package com.example.satu.obiektysportowe;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.satu.obiektysportowe.Data.Adress;
import com.example.satu.obiektysportowe.Data.SportObject;
import com.example.satu.obiektysportowe.Helpers.Helper;
import com.example.satu.obiektysportowe.Helpers.TouchHighlightImageButton;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ObjectDetailsActivity extends ActionBarActivity {

    public static final String OBJECT_EXTRA = "OBJECT_EXTRA";
    public static final String ADRES_EXTRA = "ADRES_EXTRA";
    public static final String OBIECT_ID = "obiect_id";
    public static final String OBJECT_NAME = "object_name";
    SportObject sportObject;

    @InjectView(R.id.textView_cena)
    TextView textView_cena;
    @InjectView(R.id.textView_opis)
    TextView textView_opis;
    @InjectView(R.id.textView_nazwa)
    TextView textView_nazwa;

    @InjectView(R.id.textView_godziny)
    TextView textView_godziny;
    @InjectView(R.id.textView_daty)
    TextView textView_daty;

    @InjectView(R.id.textView_ocena)
    TextView textView_ocena;

    @InjectView(R.id.button_rezerwacja)
    Button button_rezerwacja;

    @InjectView(R.id.button_mapa)
    Button button_mapa;

    @InjectView(R.id.button_ocen)
    Button button_ocena;

    @InjectView(R.id.container)
    FrameLayout frame_container;

    @InjectView(R.id.expanded_image)
    ImageView zoom_image;

    @InjectView(R.id.imageView_image)
    TouchHighlightImageButton object_image;

    Helper helper;

    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_details);
        ButterKnife.inject(this);
        helper = new Helper(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        sportObject = (SportObject) bundle.getSerializable(ObjectsListFragment.SPORT_OBJECT_EXTRA);

        textView_nazwa.setText(sportObject.getNazwa());
        getObjectsFromCloud();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_object_details, menu);
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
    public void getObjectsFromCloud(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sport_object");
        query.getInBackground(sportObject.getId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                sportObject.setCena(parseObject.getInt("Price"));
                sportObject.setOpis(parseObject.getString("Description"));
                sportObject.setStartDate(parseObject.getDate("Opening_time"));
                sportObject.setEndDate(parseObject.getDate("Closing_time"));
                sportObject.setUrl(parseObject.getString("url"));
                if (parseObject.getNumber("Sum_rate") != null)
                    textView_ocena.setText(Html.fromHtml("Ocena: ") + new DecimalFormat("##.#").format(parseObject.getNumber("Sum_rate").doubleValue() / parseObject.getNumber("Count_rate").intValue()));//Count_rate
                textView_opis.setText(sportObject.getOpis());
                textView_cena.append(" " + 2 * sportObject.getCena() + " PLN/h"); //stawka za godzine
                textView_godziny.append(helper.parseDateToString(sportObject.getStartDate(), "HH:mm") + " - " + helper.parseDateToString(sportObject.getEndDate(), "HH:mm"));
                textView_daty.append(helper.parseDateToString(sportObject.getStartDate(), "dd-MM-yyyy") + " - " + helper.parseDateToString(sportObject.getEndDate(), "dd-MM-yyyy"));
                ParseObject obj = parseObject.getParseObject("Adress");
                getAdressFromCloud(obj.getObjectId());
                ParseFile image = parseObject.getParseFile("Image");
                try {
                    byte[] data = image.getData();
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    object_image.setImageBitmap(bitmap);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    private void getAdressFromCloud(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Addres");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    Adress adress = new Adress();
                    adress.setCity(parseObject.getString("City"));
                    adress.setStreet(parseObject.getString("Street"));
                    ParseGeoPoint geo_coordinates = parseObject.getParseGeoPoint("Geo_coordinates");
                    adress.setLatitude(geo_coordinates.getLatitude());
                    adress.setLongitude(geo_coordinates.getLongitude());
                    sportObject.setAdres(adress);
                }
            }
        });
    }

    @OnClick(R.id.button_rezerwacja)
    public void onClicked(){
            Intent intent = new Intent(getApplicationContext(),DatesActivity.class);
            intent.putExtra(OBJECT_EXTRA, sportObject);
            startActivity(intent);
    }
    @OnClick(R.id.button_mapa)
    public void onClicked1(){
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
        intent.putExtra(ADRES_EXTRA,sportObject.getAdres());
        startActivity(intent);
    }
    @OnClick(R.id.button_ocen)
    public void onClicked2(){
        Bundle bundle = new Bundle();
        bundle.putString(OBIECT_ID,sportObject.getId());
        bundle.putString(OBJECT_NAME,sportObject.getNazwa());
        EvaluationDialog tmp = new EvaluationDialog();
        tmp.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        tmp.show(fm, "test");
    }
    @OnClick(R.id.imageView_image)
    public void onClicked3(){
        helper.zoomImageFromThumb(object_image, bitmap, zoom_image, frame_container);
    }

}
