package com.example.satu.obiektysportowe.Helpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.satu.obiektysportowe.Data.Reservation;
import com.example.satu.obiektysportowe.R;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Satu on 2015-05-03.
 */
public class ReservationsAdapter extends ArrayAdapter<Reservation> {
    private final LayoutInflater mInflater;
    private final Fragment parentFragment;
    public ReservationsAdapter(Context context, int textViewResourceId, Fragment parentFragment) {
        super(context, textViewResourceId);
        mInflater = LayoutInflater.from(getContext());
        this.parentFragment = parentFragment;
    }


    private int lastFocussedPosition = -1;
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.reservation_list_widget_description, null);
            holder = new ViewHolder();
            holder.answer = (TextView) convertView.findViewById(R.id.simple_object_item);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Reservation object = getItem(position);

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String newDate ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        newDate = dateFormat.format(object.getStart());
        holder.answer.setText(object.getObjectName() + "  "+ newDate);
        holder.answer.setId(position);
        return convertView;
    }


    private static class ViewHolder{
        public TextView answer;
    }
}
