package com.example.satu.obiektysportowe.Helpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.satu.obiektysportowe.Data.SportObject;
import com.example.satu.obiektysportowe.R;

/**
 * Created by Satu on 2015-05-03.
 */
public class ObjectListAdapter extends ArrayAdapter<SportObject> {
    private final LayoutInflater mInflater;
    private final Fragment parentFragment;
    public ObjectListAdapter(Context context, int textViewResourceId,Fragment parentFragment) {
        super(context, textViewResourceId);
        mInflater = LayoutInflater.from(getContext());
        this.parentFragment = parentFragment;
    }


    private int lastFocussedPosition = -1;
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.object_list_widget_description, null);
            holder = new ViewHolder();
            holder.answer = (TextView) convertView.findViewById(R.id.simple_object_item);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final SportObject object = getItem(position);

        holder.answer.setText(object.getNazwa());
        holder.answer.setId(position);
        return convertView;
    }


    private static class ViewHolder{
        public TextView answer;
    }
}
