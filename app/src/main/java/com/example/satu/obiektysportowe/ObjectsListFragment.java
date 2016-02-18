package com.example.satu.obiektysportowe;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.satu.obiektysportowe.Data.SportObject;

import java.io.Serializable;


/**
 * A placeholder fragment containing a simple view.
 */
public class ObjectsListFragment extends android.support.v4.app.ListFragment {


    public static final String SPORT_OBJECT_EXTRA = "SPORT_OBJECT_EXTRA";

    public ObjectsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ListAdapter listAdapter = getListAdapter();
        Object item = (SportObject) listAdapter.getItem(position);
        Intent intent = new Intent(getActivity(),ObjectDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SPORT_OBJECT_EXTRA, (Serializable) item);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public int getSize (){
        if (getListAdapter() != null) {
            int count = getListAdapter().getCount();
            return count;
        }
        return -1;
    }

}
