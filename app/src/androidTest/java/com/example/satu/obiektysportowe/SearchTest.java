package com.example.satu.obiektysportowe;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.satu.obiektysportowe.Data.SportObject;

/**
 * Created by Bartek on 2015-06-12.
 */
public class SearchTest extends ActivityInstrumentationTestCase2<SearchObjectsActivity> {
    public static final int BOISKO = 8;
    SearchObjectsActivity searchObjectsActivity;
    private Instrumentation mInstrumentation;

    public SearchTest() {
        super(SearchObjectsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();

        searchObjectsActivity = getActivity();

    }

    @SmallTest
    public void test_types_download(){
        Spinner spinner = (Spinner) searchObjectsActivity.findViewById(R.id.spinner_type);
        assertTrue(spinner.getCount() > 1);
    }

    @SmallTest
    public void testSearchObjects() throws Exception {
        long start = System.currentTimeMillis();
        searchObjectsActivity.getObjectsFromCloud();
        while (true){
            if (searchObjectsActivity.getResultListSize() == 7) {
                assertTrue(true);
                return;
            }
            if (System.currentTimeMillis() - start > 50000) {
                assertTrue(false);
                return;
            }
        }

    }

    @MediumTest
    public void testSearchByNameTest() throws Exception {
        long start = System.currentTimeMillis();
        final EditText editText = (EditText) searchObjectsActivity.findViewById(R.id.editText_nazwa);
        final Button button = (Button) searchObjectsActivity.findViewById(R.id.button_szukaj);

        mInstrumentation.waitForIdle(new Runnable() {
            @Override
            public void run() {
                editText.setText("Sp");
                button.callOnClick();
            }
        });
        while (true){
            if (searchObjectsActivity.getResultListSize() == 2) {
                assertTrue(true);
                return;
            }
            if (System.currentTimeMillis() - start > 50000) {
                assertTrue(false);
                return;
            }
        }
    }

    @MediumTest
    public void testSearchByTypeTest() throws Exception {
        long start = System.currentTimeMillis();
        final Spinner spinner = (Spinner) searchObjectsActivity.findViewById(R.id.spinner_type);
        final Button button = (Button) searchObjectsActivity.findViewById(R.id.button_szukaj);

        mInstrumentation.waitForIdle(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(BOISKO);
                button.callOnClick();
            }
        });
        while (true){
            if (searchObjectsActivity.getResultListSize() == 2) {
                assertTrue(true);
                return;
            }
            if (System.currentTimeMillis() - start > 50000) {
                assertTrue(false);
                return;
            }
        }
    }

    @MediumTest
    public void testSearchByAdresTest() throws Exception {
        long start = System.currentTimeMillis();
        final EditText editText = (EditText) searchObjectsActivity.findViewById(R.id.editText_adres);
        final Button button = (Button) searchObjectsActivity.findViewById(R.id.button_szukaj);

        mInstrumentation.waitForIdle(new Runnable() {
            @Override
            public void run() {
                editText.setText("Wiernej Rzeki 2");
                button.callOnClick();
            }
        });
        while (true){
            if (searchObjectsActivity.getResultListSize() == 3) {
                assertTrue(true);
                return;
            }
            if (System.currentTimeMillis() - start > 50000) {
                assertTrue(false);
                return;
            }
        }
    }

}
