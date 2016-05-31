package com.freeoda.pharmacist.thepharmacist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aarooran on 5/18/2016.
 */
public class MarkerAdapter extends ArrayAdapter {
    List list=new ArrayList();
    public MarkerAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    static class DataHandler{
        ImageView phar_image;
        TextView phar_name;
        TextView distance;
        CheckBox phar_id;
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View row;
        row=convertView;
        DataHandler handler;
        if(convertView==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.custom_layout,parent,false);
            handler=new DataHandler();
            handler.phar_image=(ImageView)row.findViewById(R.id.cardview_image);
            handler.phar_name=(TextView)row.findViewById(R.id.phar_name);
            handler.distance=(TextView)row.findViewById(R.id.distance_phar);
            handler.phar_id=(CheckBox) row.findViewById(R.id.checkBox1);
            row.setTag(handler);

            handler.phar_id.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    MarkerDataProvider marDP = (MarkerDataProvider) cb.getTag();
                    marDP.setSelected(cb.isChecked());
                }
            });
        }


        else
        {
            handler=(DataHandler)row.getTag();
        }
        MarkerDataProvider markerDataProvider;
        markerDataProvider=(MarkerDataProvider)this.getItem(position);

        handler.phar_id.setChecked(markerDataProvider.isSelected());
        handler.phar_id.setTag(markerDataProvider);
        handler.phar_image.setImageResource(R.drawable.pharmacylist_icon);
        handler.phar_name.setText(markerDataProvider.getName());
        handler.distance.setText("Pharmacy Distance : "+Float.toString(markerDataProvider.getDistance())+" Km");
        return row;
    }

    public void listClear()
    {
        list.clear();
    }


}
