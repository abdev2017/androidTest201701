package com.example.master.androidtest2017;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.IccOpenLogicalChannelResponse;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLogs";

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Location mLocation;
    private TextView mLatitudeTextView, mLongitudeTextView;

    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 10; // в метрах
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 2000; // в мс

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TextView myLatitude = (TextView) findViewById(R.id.latitude);
        TextView myLongitude = (TextView) findViewById(R.id.longitude);
        TextView myAddress = (TextView) findViewById(R.id.address);

        mLatitudeTextView = (TextView) findViewById(R.id.textViewLatitude);
        mLongitudeTextView = (TextView) findViewById(R.id.textViewLongitude);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        String provider = mLocationManager.getBestProvider(criteria, true);
        Log.d(TAG, "point 2");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mLocation = mLocationManager.getLastKnownLocation(provider);
        mLocationListener = new MyLocationListener();

        showCurrentLocation(mLocation);

        // Регистрируемся для обновлений
        mLocationManager.requestLocationUpdates(provider,
                MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_FOR_UPDATES,
                mLocationListener);

        // Присвоить полученные значения
        myLatitude.setText("Широта: " + String.valueOf(mLocation.getLatitude()));
        myLongitude.setText("Долгота: " + String.valueOf(mLocation.getLongitude()));

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder(
                        "Адрес:\n");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                myAddress.setText(strReturnedAddress.toString());
            } else {
                myAddress.setText("Нет адресов!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            myAddress.setText("Не могу получить адрес!");
        }

    }

///*
    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, "point 11");
        mLocationManager.removeUpdates(mLocationListener);
    }
//*/
    public void onClick(View v) {
        showCurrentLocation(mLocation);
    }

    protected void showCurrentLocation(Location location) {
        if (location == null) {
            mLatitudeTextView.setText("Не работает");
            mLongitudeTextView.setText("Не работает");
        }
        if (location != null) {
            mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
            mLongitudeTextView.setText(String.valueOf(location.getLongitude()));
        }
    }

    public void myFunction() {

    }
}
