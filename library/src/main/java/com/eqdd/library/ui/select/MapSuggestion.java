package com.eqdd.library.ui.select;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by lvzhihao on 17-5-23.
 */

public class MapSuggestion implements SearchSuggestion {

    String body;
    String address;
    double lat;
    double lon;


    public void setBody(String body) {
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeString(this.address);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
    }

    public MapSuggestion() {
    }

    protected MapSuggestion(Parcel in) {
        this.body = in.readString();
        this.address = in.readString();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
    }

    public static final Creator<MapSuggestion> CREATOR = new Creator<MapSuggestion>() {
        @Override
        public MapSuggestion createFromParcel(Parcel source) {
            return new MapSuggestion(source);
        }

        @Override
        public MapSuggestion[] newArray(int size) {
            return new MapSuggestion[size];
        }
    };

    @Override
    public String getBody() {
        return body;
    }
}