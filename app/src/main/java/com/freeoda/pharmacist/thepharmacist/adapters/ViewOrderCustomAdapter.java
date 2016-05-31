package com.freeoda.pharmacist.thepharmacist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.freeoda.pharmacist.thepharmacist.R;
import com.freeoda.pharmacist.thepharmacist.models.Order;

import java.util.ArrayList;

/**
 * Created by Lakna on 5/10/2016.
 */
public class ViewOrderCustomAdapter extends ArrayAdapter<Order> {
    int groupid;
    ArrayList<Order> records;
    Context context;
    private LayoutInflater inflater;
    customButtonListener customListner;

    public ViewOrderCustomAdapter(Context context, int vg, int id, ArrayList<Order> records) {
        super(context, vg, id, records);
        this.context = context;
        groupid = vg;
        this.records = records;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = inflater.inflate(R.layout.display_order_custom_row,parent,false);

        TextView pharmacyName = (TextView)convertView.findViewById(R.id.orderPharmacyName);
        TextView quotation = (TextView)convertView.findViewById(R.id.orderQuatation);
        TextView date = (TextView)convertView.findViewById(R.id.orderDate);
        ImageView pharmacyImage = (ImageView)convertView.findViewById(R.id.orderImage);
        Button acceptBtn = (Button)convertView.findViewById(R.id.orderAcceptedBtn);
        Button declineBtn = (Button)convertView.findViewById(R.id.orderDeclinedBtn);

        pharmacyName.setText(records.get(position).getPharmacyName());
        quotation.setText("Rs: " +String.valueOf(records.get(position).getOrderQuotation()) + "/=");
        date.setText(records.get(position).getDate());
        pharmacyImage.setBackgroundResource(R.drawable.medicine_bottle);
        acceptBtn.setBackgroundResource(R.drawable.accepted);
        declineBtn.setBackgroundResource(R.drawable.declined);

        final Order temp = getItem(position);
        //final Order temp = records.get(position);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, temp);
                }
            }
        });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customListner != null) {
                    customListner.onButtonClickListnerForDecline(position, temp);
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customListner != null) {
                    customListner.onItemViewClick(position,temp);
                }
            }
        });
        return convertView;

    }

    public interface customButtonListener {
        public void onButtonClickListner(int position, Order value);
        public void onButtonClickListnerForDecline(int position, Order value);
        public void onItemViewClick(int position, Order value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

}
