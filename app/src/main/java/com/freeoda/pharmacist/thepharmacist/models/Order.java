package com.freeoda.pharmacist.thepharmacist.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Lakna on 5/9/2016.
 */
public class Order extends BaseModel implements Serializable {

    @SerializedName("pharmacy_info_name")
    private
    String pharmacyName;
    @SerializedName("order_id")
    private
    String orderId;
    @SerializedName("user_id")
    private
    String userId;
    @SerializedName("quotation")
    private
    Double orderQuotation;
    @SerializedName("order_date_time")
    private
    String date;
    @SerializedName("order_image_path")
    private
    String photoUrl;
    @SerializedName("pharmacy_info_latitude")
    private Double latitude;
    @SerializedName("pharmacy_info_longitude")
    private Double longitude;
    @SerializedName("pharmacy_info_id")
    private
    String pharmacyId;

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getOrderQuotation() {
        return orderQuotation;
    }

    public void setOrderQuotation(Double orderQuotation) {
        this.orderQuotation = orderQuotation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }
}

