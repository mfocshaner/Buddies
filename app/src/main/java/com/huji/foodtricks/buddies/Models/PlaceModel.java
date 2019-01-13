package com.huji.foodtricks.buddies.Models;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PlaceModel implements Serializable {
    // mark it transient so defaultReadObject()/defaultWriteObject() ignore it
    private transient com.google.android.gms.maps.model.LatLng mLocation;
    private Address latLng;


    public PlaceModel() {

        this.mLocation = new com.google.android.gms.maps.model.LatLng(47.6062095, -122.3320708);
    }


    public PlaceModel(LatLng mLocation) {
        this.mLocation = mLocation;
    }


    public LatLng getmLocation() {
        return mLocation;
    }

    public void setmLocation(LatLng mLocation) {
        this.mLocation = mLocation;
    }


    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeDouble(mLocation.latitude);
        out.writeDouble(mLocation.longitude);
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        mLocation = new LatLng(in.readDouble(), in.readDouble());
    }
}
