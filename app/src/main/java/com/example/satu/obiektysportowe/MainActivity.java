package com.example.satu.obiektysportowe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {
    @InjectView(R.id.button_rezerwacja)
    Button buttonMyReservation;
    ParseUser currentUser = null;

    private Menu actionMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(),currentUser.getUsername(),Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    @OnClick(R.id.button_rezerwacja)
    public void onClicked(){
        Intent intent = new Intent(getApplicationContext(),SearchObjectsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_moje_rezeracje)
    public void onClicked2(){
        if (currentUser == null)
            loginIn();
        else {
            Intent intent = new Intent(getApplicationContext(),ViewReservationsActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        actionMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (currentUser != null) {
            actionMenu.findItem(R.id.action_logout).setVisible(true);
        } else {
            actionMenu.findItem(R.id.action_login).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){
            case R.id.action_login:
                loginIn();
                actionMenu.findItem(R.id.action_logout).setVisible(true);
                actionMenu.findItem(R.id.action_login).setVisible(false);
                break;
            case R.id.action_settings:
                break;
            case R.id.action_logout:
                ParseUser.logOut();
                currentUser = null;
                actionMenu.findItem(R.id.action_logout).setVisible(false);
                actionMenu.findItem(R.id.action_login).setVisible(true);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    private void loginIn(){
        ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                MainActivity.this);
        startActivityForResult(loginBuilder.build(), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }
}
