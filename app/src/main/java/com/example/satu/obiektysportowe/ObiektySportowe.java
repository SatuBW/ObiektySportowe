package com.example.satu.obiektysportowe;

import android.app.Application;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Satu on 2015-05-02.
 */
public class ObiektySportowe extends Application {
    private static ObiektySportowe singleton;
    private Map typesMap;
    public ObiektySportowe getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
        ParseFacebookUtils.initialize(this);


      //  ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        getTypesFromCloud();
    }

    private void getTypesFromCloud(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Typ");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> types, ParseException e) {
                if (e == null) {
                    typesMap = new HashMap();
                    for (ParseObject object : types)
                        typesMap.put(object.getString("nazwa"),object.getObjectId());
                } else {
                    Toast.makeText(getApplicationContext(),"ojojoj",Toast.LENGTH_SHORT).show();
                    // handle Parse Exception here
                }
            }
        });
    }
    public Map getTypes(){
        return typesMap;
    }
}
