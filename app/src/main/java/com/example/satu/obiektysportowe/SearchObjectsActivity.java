package com.example.satu.obiektysportowe;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.satu.obiektysportowe.Data.SportObject;
import com.example.satu.obiektysportowe.Helpers.ObjectListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SearchObjectsActivity extends ActionBarActivity {

    @InjectView(R.id.spinner_type)
    Spinner spinnerType;

    @InjectView(R.id.editText_adres)
    EditText editText_adres;

    @InjectView(R.id.editText_nazwa)
    EditText editText_nazwa;

    @InjectView(R.id.button_szukaj)
    Button button_szukaj;

    @OnClick(R.id.button_szukaj)
    public void OnClick(){
        getObjectsFromCloud();
    }

    ObjectsListFragment objectsListFragment;
    Map types;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_objects);
        ButterKnife.inject(this);
//////////////////adaptery /////////////////////////////
        List<String> list = new ArrayList<String>();

        list.add("<Wszystkie>");

        ObiektySportowe mApplication = (ObiektySportowe)getApplicationContext();
        types = mApplication.getTypes();
        if (types != null){
            for(Object value: types.keySet())
                list.add((String) value);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(dataAdapter);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_objects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.search:
                getObjectsFromCloud();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getObjectsFromCloud(){
        final Toast toast = Toast.makeText(getApplicationContext(), "Trwa wyszukiwanie...", Toast.LENGTH_LONG);
        toast.show();
        String id = null;
        if (types != null) {
            id = (String) types.get(spinnerType.getSelectedItem().toString());
        }
        final List<SportObject> sportObjects = new ArrayList<>();

        ParseQuery<ParseObject> innerQuery1 = ParseQuery.getQuery("Addres");
        innerQuery1.whereContains("Street", editText_adres.getText().toString());
        innerQuery1.getFirstInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sport_object");
        query.whereContains("Name", editText_nazwa.getText().toString());

        if (spinnerType.getSelectedItemPosition() > 0) {
            ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("Typ");
            innerQuery.getInBackground(id);
            query.whereMatchesQuery("Typ", innerQuery);
        }
        query.whereMatchesQuery("Adress", innerQuery1);

        toast.show(); //przedluzenie wyswietlania toastu (wolniejsze polaczenia)

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> scoreList, ParseException e) {
                    if (e == null) {
                        toast.cancel();
                        Toast.makeText(getApplicationContext(), "Znaleziono " + scoreList.size() + " obiektow", Toast.LENGTH_SHORT).show();
                        for (ParseObject parseObject : scoreList) {
                            sportObjects.add(new SportObject(parseObject.getObjectId(), parseObject.getString("Name")));
                        }
                        objectsListFragment = new ObjectsListFragment();
                        ObjectListAdapter obiejctsAdapter = new ObjectListAdapter(getApplicationContext(), R.layout.object_list_widget_description, objectsListFragment);
                        obiejctsAdapter.addAll(sportObjects);
                        objectsListFragment.setListAdapter(obiejctsAdapter);
                        switchMainFragment(objectsListFragment, R.id.fragment_container);
                        Log.d("score", "Retrieved " + scoreList.size() + " scores");
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });
    }
    private void switchMainFragment(Fragment fragment, int resID){
        switchMainFragment(fragment,null,resID);
    }

    private void switchMainFragment(Fragment fragment,String transaction_name,int resID){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(resID, fragment)
                .addToBackStack(transaction_name)
                .commit();
    }
    public int getResultListSize(){
       if (objectsListFragment != null)
           return objectsListFragment.getSize();
       else
           return -1;
    }
    public void setSpinnerPosition(int position){
        if (spinnerType != null && spinnerType.getCount() > 0)
            spinnerType.setSelection(position);
    }
    public void setNameText(String nameText){
        this.editText_nazwa.setText(nameText);
    }
}
