package com.freeoda.pharmacist.thepharmacist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.freeoda.pharmacist.thepharmacist.adapters.ViewOrderCustomAdapter;
import com.freeoda.pharmacist.thepharmacist.exceptions.CustomException;
import com.freeoda.pharmacist.thepharmacist.exceptions.HttpExceptionHandler;
import com.freeoda.pharmacist.thepharmacist.models.ModelApi;
import com.freeoda.pharmacist.thepharmacist.models.Order;
import com.freeoda.pharmacist.thepharmacist.network.NetworkCallback;
import com.freeoda.pharmacist.thepharmacist.network.NetworkCallbackWithArray;
import com.freeoda.pharmacist.thepharmacist.network.NetworkFacade;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Lakna on 5/10/2016.
 */
public class ViewConfirmedOrdersActivity extends AppCompatActivity implements ViewOrderCustomAdapter.customButtonListener {

    Activity context;
    ViewOrderCustomAdapter adapter;
    ListView listProduct;
    ArrayList<Order> records;
    ArrayList<Order> recordList = new ArrayList<Order>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_confirmed_orders);
        context = this;
        records = new ArrayList<Order>();
        listProduct = (ListView) findViewById(R.id.viewOrderList);
        getAllConfirmedOrders();
    }

    public void getAllConfirmedOrders(){

        NetworkFacade.getOrders(LoginSession.personDetails.getEmail(), getApplicationContext(), new NetworkCallbackWithArray() {
            @Override
            public void onSuccess(ModelApi[] result) {
                if (result instanceof Order[]) {

                    Order[] orders = (Order[]) result;
                    for (Order order : orders) {
                        Order confirmedOrder = new Order();
                        confirmedOrder.setLongitude(order.getLongitude());
                        confirmedOrder.setLatitude(order.getLatitude());
                        confirmedOrder.setPhotoUrl(order.getPhotoUrl());
                        confirmedOrder.setPharmacyName(order.getPharmacyName());
                        confirmedOrder.setDate(order.getDate());
                        confirmedOrder.setOrderQuotation(order.getOrderQuotation());
                        confirmedOrder.setOrderId(order.getOrderId());
                        confirmedOrder.setPharmacyId(order.getPharmacyId());

                        records.add(confirmedOrder);
                    }
                    adapter = new ViewOrderCustomAdapter(context, R.layout.display_order_custom_row, R.id.orderPharmacyName, records);
                    adapter.setCustomButtonListner(ViewConfirmedOrdersActivity.this);

                    listProduct.setAdapter(adapter);
                    System.out.println("size" + records.size());
                }


            }

            @Override
            public void onError(CustomException exception) {
                if (exception instanceof HttpExceptionHandler) {
                    Log.d("TAG", ((HttpExceptionHandler) exception).getMessage());
                }
            }
        });

    }

    @Override
    public void onButtonClickListner(int position, Order value) {
        Log.i("TAG","button click");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,You want to confirm?");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(ViewConfirmedOrdersActivity.this,"You clicked yes button", Toast.LENGTH_LONG).show();
                NetworkFacade.confirmUserOrder(value.getPharmacyId(), value.getOrderId(), getApplicationContext(), new NetworkCallback() {
                    @Override
                    public void onSuccess(ModelApi result) {
                        System.out.print(result);
                    }

                    @Override
                    public void onError(CustomException exception) {

                    }
                });
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NetworkFacade.declineUserOrder(value.getPharmacyId(), value.getOrderId(), getApplicationContext(), new NetworkCallback() {
                    @Override
                    public void onSuccess(ModelApi result) {
                        System.out.print(result);
                    }

                    @Override
                    public void onError(CustomException exception) {

                    }
                });
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onButtonClickListnerForDecline(int position, Order value) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,You want to decline?");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // final int positionToRemove = position;
                adapter.remove(value);
                adapter.notifyDataSetChanged();
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onItemViewClick(int position, Order value) {
        Toast.makeText(getApplicationContext(), "This os position" + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ViewConfirmedOrdersActivity.this, DecriptionMapActivity.class);
        intent.putExtra("Order",(Serializable)value);
        startActivity(intent);
    }
}
