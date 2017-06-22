package com.example.lttha.a14110180_lethithao_calendarwidget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lttha.a14110180_lethithao_calendarwidget.Untils.EventModel;

import java.util.ArrayList;

/**
 * Created by lttha on 5/27/2017.
 */

public class AdapterEvent extends BaseAdapter {
    Context context;
    ArrayList<EventModel> array=new ArrayList<>();
    LayoutInflater inflater;

    public AdapterEvent(Context context, ArrayList<EventModel> array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        TextView txtDay,txtTime,txtTitle;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.item_event, viewGroup, false);

        txtDay=(TextView) view.findViewById(R.id.itemDay);
        txtTime=(TextView) view.findViewById(R.id.itemTime);
        txtTitle=(TextView) view.findViewById(R.id.itemTitle);

        txtDay.setText(array.get(i).getEventDate());
        txtTime.setText(array.get(i).getEventTime());
        txtTitle.setText(array.get(i).getTitle());
        Log.d("event:::",array.get(i).getTitle());

        return null;
    }
}
