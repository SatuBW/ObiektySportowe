package com.example.satu.obiektysportowe;

import android.app.FragmentManager;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satu.obiektysportowe.Data.SportObject;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DatesActivity extends ActionBarActivity {

    //zbiory wartosci mozliwych do wyboru minut (dla NumberPickerow odpowiedzialnych za ustawienie Minut)
    static final String[] MINUTE_PICKER_VALUES = {"00", "30"};

   public DatesActivity() {
    }

    double timeDiff=0;
    String hourFrom="";
    String hourTo="";
    String selectedDate="";
    SportObject sportObject;

    @InjectView(R.id.textView_komunikat)
    TextView textView_komunikat;

    @InjectView(R.id.datePicker)
    DatePicker datePicker;

    @InjectView(R.id.numberPickerHourFrom)
    NumberPicker hourPicker_start;

    @InjectView(R.id.numberPickerMinFrom)
    NumberPicker minutePicker_start;

    @InjectView(R.id.numberPickerHourTo)
    NumberPicker hourPicker_end;

    @InjectView(R.id.numberPickerMinTo)
    NumberPicker minutePicker_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);
        ButterKnife.inject(this);

        Bundle extras = getIntent().getExtras();
        sportObject = (SportObject) extras.getSerializable(ObjectDetailsActivity.OBJECT_EXTRA);

        //Przygotowanie Pickerow (od daty, godzin i minut): ustawianie im zbiorow wartosci (Min, Max), formatowania wyswietlanych tekstow
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        if(sportObject.getEndDate().getTime() > System.currentTimeMillis())
            datePicker.setMaxDate(sportObject.getEndDate().getTime());

        int HOUR_PICKER_MIN_VALUE = sportObject.getStartDate().getHours();
        int HOUR_PICKER_MAX_VALUE = sportObject.getEndDate().getHours();

        hourPicker_start.setMinValue(HOUR_PICKER_MIN_VALUE);
        hourPicker_start.setMaxValue(HOUR_PICKER_MAX_VALUE);
        hourPicker_end.setMinValue(HOUR_PICKER_MIN_VALUE);
        hourPicker_end.setMaxValue(HOUR_PICKER_MAX_VALUE);

        minutePicker_start.setMinValue(0);
        minutePicker_start.setMaxValue(MINUTE_PICKER_VALUES.length - 1);
        minutePicker_end.setMinValue(0);
        minutePicker_end.setMaxValue(MINUTE_PICKER_VALUES.length - 1);

        minutePicker_start.setDisplayedValues(MINUTE_PICKER_VALUES);
        minutePicker_end.setDisplayedValues(MINUTE_PICKER_VALUES);

        hourPicker_start.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        hourPicker_end.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_dates, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Sprawdzenie dostepności obiektu w wybranym terminie

    public void checkObjectsInCloud(String object_id, final String date, String startTime, String endTime) {

        //terminy w postaci pełnej daty z czasem: yyyy-MM-dd HH:mm - w takiej samej postaci są zapisywane daty w Parse
        final Date startDate = prepareDateTime(date, startTime);
        final Date endDate = prepareDateTime(date, endTime);

        // Sprawdz czy podany termin - startDate i endDate mieści się pomiędzy Opening i Closing Time z tabeli Object
        if (startDate.before(sportObject.getStartDate()) || endDate.after(sportObject.getEndDate())) {
            //Toast.makeText(getApplication(),"false",Toast.LENGTH_SHORT).show();
            textView_komunikat.setText("Obiekt nie jest dostepny w wybranym terminie.");
            return;
        } else {
            if (getDateDiff(startDate,sportObject.getStartDate()) > 0 ||  getDateDiff(endDate,sportObject.getEndDate()) < 0){
                textView_komunikat.setText("Obiekt nie jest dostepny w wybranym terminie.");
                return;
            }
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sport_object");
        query.getInBackground(sportObject.getId(), new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Registration").whereLessThanOrEqualTo("Start_date", startDate).whereGreaterThanOrEqualTo("End_date", endDate); //Iloczyn 2 warunkow
                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Registration").whereGreaterThan("Start_date", startDate).whereLessThan("End_date", endDate);
                ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Registration").whereGreaterThan("Start_date", startDate).whereLessThan("Start_date", endDate).whereGreaterThanOrEqualTo("End_date", endDate);
                ParseQuery<ParseObject> query4 = ParseQuery.getQuery("Registration").whereLessThanOrEqualTo("Start_date", startDate).whereGreaterThan("End_date", startDate).whereLessThan("End_date", endDate);

                List<ParseQuery<ParseObject>> queries = new ArrayList<>();
                queries.add(query1);
                queries.add(query2);
                queries.add(query3);
                queries.add(query4);

                ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
                mainQuery.whereEqualTo("Id_obj", parseObject);// (Suma 4 iloczynow logicznych) AND Id_obj

                mainQuery.countInBackground(new CountCallback() {
                    @Override
                    public void done(int i, ParseException e) {
                        if (i > 0)
                            textView_komunikat.setText("Obiekt nie jest dostepny w wybranym terminie.");
                        else{
                            textView_komunikat.setText("");
                            openReservationConfirm();
                        }
                    }
                });
            }

        });
    }

    @OnClick(R.id.button_sprawdzTermin)
    public void onClicked(){

            //inicjalizacja/resetowanie komunikatu wyswietlanego po sprawdzeniu terminu w bazie. Moze ulec zmianie, gdy obiekt nie jest dostepny lub zle wprowadzono czas
            textView_komunikat.setTextColor(Color.BLACK);
            textView_komunikat.setText("Trwa sprawdzanie...");

            //odczyt danych z Pickerow
            hourFrom = prepareTime(hourPicker_start.getValue(), minutePicker_start.getValue());
            hourTo = prepareTime(hourPicker_end.getValue(), minutePicker_end.getValue());
            timeDiff = countTimeDifferenceInSeconds(hourFrom, hourTo);

            Date date = new Date(datePicker.getCalendarView().getDate());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            selectedDate=dateFormat.format(date);

            if(timeDiff > 0){
                checkObjectsInCloud(sportObject.getId(), selectedDate, hourFrom, hourTo);
            }
            else {
                textView_komunikat.setTextColor(Color.RED);
                textView_komunikat.setText("Godzina koncowa musi byc pozniejsza niz poczatkowa.");
            }
        }

    public double countTimeDifferenceInSeconds(String from, String to){

        double difference=0;
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat df = new SimpleDateFormat("H:mm");

        try {
            Date d1 = df.parse(from);
            Date d2 = df.parse(to);

            if(d2.getTime()>d1.getTime()) {
                difference = (d2.getTime() - d1.getTime()) / 1000; //getTime() zwraca czas w milisek.
            }

        } catch (java.text.ParseException pe){

        }

        return difference;
    }

    //Tworzenie stringu z czasem w postaci HH:mm
    public String prepareTime(int hour, int minutes){

        String time=Integer.toString(hour) + ":" + MINUTE_PICKER_VALUES[minutes];

        return time;
    }

    //Cena za liczbę zarezerwowanych godzin (czas wprowadzony sekundach); stawka jest za pół godziny
    public double countPrice(double duration, int rate){

        double price=(duration / 3600) * (2 * rate);

        return price;
    }

    //Zamienia datę i czas podany w osobnych stringach (rezultat z NumPickerów) na pełną Datę (yyyy-MM-dd HH:mm), ktora jest uzywana w bazie
    public Date prepareDateTime(String date, String time){

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        Date newDate = new Date();
        String inputStr = date + " " + time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            newDate = dateFormat.parse(inputStr);
        } catch (java.text.ParseException pe){

        }

        return newDate;
    }

    public Date prepareDateTime(String dateTime){

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Date newDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            newDate = dateFormat.parse(dateTime);
        } catch (java.text.ParseException pe){

        }

        return newDate;
    }

    public static float getDateDiff(Date date1, Date date2) {
        int hoursDate1 = date1.getHours();
        int minutesDate1 = date1.getMinutes();
        int hoursDate2 = date2.getHours();
        int minutesDate2 = date2.getMinutes();
        Float time1 = Float.valueOf(String.valueOf(hoursDate1) + '.' + String.valueOf(minutesDate1));
        Float time2 = Float.valueOf(String.valueOf(hoursDate2) + '.' + String.valueOf(minutesDate2));
        float v = time2 - time1;
        return v;
    }
    private void openReservationConfirm(){
        Bundle args = new Bundle();
        args.putString("object_id", sportObject.getId());
        args.putString("name", sportObject.getNazwa());
        args.putString("date", selectedDate);
        args.putString("startTime", hourFrom);
        args.putString("endTime", hourTo);
        args.putDouble("duration", (timeDiff / 3600)); //przekazywany jest czas trwania w godzinach
        args.putDouble("price", countPrice(timeDiff, sportObject.getCena()));
        args.putString("url", sportObject.getUrl());
        ReservationConfirmDialog tmp = new ReservationConfirmDialog();
        tmp.setArguments(args);
        FragmentManager fm = getFragmentManager();
        tmp.show(fm, "test");
    }
}