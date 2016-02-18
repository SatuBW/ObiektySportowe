package com.example.satu.obiektysportowe;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.TextView;

import com.example.satu.obiektysportowe.Data.SportObject;

/**
 * Created by Bartek on 2015-06-13.
 */
public class DetailsTests extends ActivityInstrumentationTestCase2<ObjectDetailsActivity> {
    public static final String OCENA_OB_174 = "Ocena: 3,9";
    ObjectDetailsActivity objectDetailsActivity;
        private Instrumentation mInstrumentation;

        public DetailsTests() {
            super(ObjectDetailsActivity.class);
        }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();
        SportObject sportObject = new SportObject("TCJRiijlVw","Sp 174");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ObjectsListFragment.SPORT_OBJECT_EXTRA, sportObject);
        intent.putExtras(bundle);
        setActivityIntent(intent);
        objectDetailsActivity = getActivity();

    }

    @MediumTest
    public void testPrizeTest() throws Exception {
        long start = System.currentTimeMillis();
        final TextView text = (TextView) objectDetailsActivity.findViewById(R.id.textView_cena);


        mInstrumentation.waitForIdle(new Runnable() {
            @Override
            public void run() {
                assertEquals(text.getText().toString(), String.valueOf("Cena 120 PLN/h"));
            }
        });
    }

    @MediumTest
    public void testOpenClosetimeTest() throws Exception {
        long start = System.currentTimeMillis();
        final TextView text = (TextView) objectDetailsActivity.findViewById(R.id.textView_daty);


        mInstrumentation.waitForIdle(new Runnable() {
            @Override
            public void run() {
                assertEquals(text.getText().toString(), String.valueOf("Godziny otwarcia: 15:00 - 19:00"));
            }
        });
    }

    @MediumTest
    public void testDateStartEndTest() throws Exception {
        long start = System.currentTimeMillis();
        final TextView text = (TextView) objectDetailsActivity.findViewById(R.id.textView_daty);


        mInstrumentation.waitForIdle(new Runnable() {
            @Override
            public void run() {
                assertEquals(text.getText().toString(), String.valueOf("Okres rezerwacji: 04-05-2015 - 30-09-2015"));
            }
        });
    }

    @MediumTest
    public void testEvaluationTest() throws Exception {
        long start = System.currentTimeMillis();
        final TextView text = (TextView) objectDetailsActivity.findViewById(R.id.textView_ocena);


        mInstrumentation.waitForIdle(new Runnable() {
            @Override
            public void run() {
                assertEquals(text.getText().toString(), String.valueOf(OCENA_OB_174));
            }
        });
    }
}
