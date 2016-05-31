package com.freeoda.pharmacist.thepharmacist;

/**
 * Created by Aarooran on 5/18/2016.
 */
public class MarkerDataProvider {

    private String name;
    private String address;
    private float lat;
    private float lng;
    private float distance;
    private String phar_id;
    private boolean selected;


    // private float velocity;
   // private String number_plate;



    public MarkerDataProvider(String name, String address, float lat, float lng, float distance, String phar_id)
    {
        this.setName(name);
        this.setAddress(address);
        this.setLat(lat);
        this.setLng(lng);
        this.setDistance(distance);
        this.setPhar_id(phar_id);
        this.setSelected(false);
      //  this.setVelocity(velocity);
       // this.setNumber_plate(num_plate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
    public String getPhar_id() {
        return phar_id;
    }

    public void setPhar_id(String phar_id) {
        this.phar_id = phar_id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

//    public float getVelocity() {
//        return velocity;
//    }
//
//    public void setVelocity(float velocity) {
//        this.velocity = velocity;
//    }
//
//    public String getNumber_plate() {
//        return number_plate;
//    }
//
//    public void setNumber_plate(String number_plate) {
//        this.number_plate = number_plate;
//    }
}
