package com.gmrit.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmrit.food4all.modals.Fooddetails;
import com.gmrit.food4all.modals.Volunteer;
import com.gmrit.food4all.utilities.LocationTrack;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SthreeRaksha extends AppCompatActivity {

    DatabaseReference ref;
    LinearLayout emergencylayout, policelayout;
    /*LocationTrack locationTrack;
    String lat, lon;*/
    String url;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    String police = "09440795852";
    private ArrayList<String> phoneno = new ArrayList<String>();
    private FusedLocationProviderClient fusedLocationClient;
    private ResultReceiver resultReceiver;
    private static String TAG = "TOKENS_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sthree_raksha);
        this.setTitle("Sthree Raksha");


        Toast.makeText(SthreeRaksha.this, "Double Click on Emergency button to send Alert Messages", Toast.LENGTH_LONG).show();


        //emergencylayout = (LinearLayout) findViewById(R.id.layout_emergency);
        policelayout = (LinearLayout) findViewById(R.id.policelayout);


        policelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", police, null));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        ref = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Volunteers");

        emergencylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            SthreeRaksha.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    getCurrentLocation();
                } */

            }

        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(SthreeRaksha.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(SthreeRaksha.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            Log.d(TAG, "Latitude:" + latitude + " Longitude:" + longitude);
                            *//*Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAddressFromLatLong(location);*//*
                            url = "https://maps.google.com/?q=" + latitude + "," + longitude + "";
                            Log.d(TAG, "" + url);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            String fooddetails = dataSnapshot1.getValue(Fooddetails.class).getPhone();
                                            Log.d(TAG, "" + fooddetails);
                                            //fooddetailsArrayList.add(fooddetails);
                                            phoneno.add(fooddetails);
                                        }
                                    } else {
                                        Toast.makeText(SthreeRaksha.this, "No Data Exists !", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(SthreeRaksha.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            String msg = "High Emergency Alert!!!\n";
                            String fina = msg + "I'm in Danger.Please help me.\nLocation: ";
                            String sam = fina + url;
                            for(int i=0;i<phoneno.size();i++)
                            Log.d(TAG, "" + phoneno.get(i));
                            Log.d(TAG, "" + sam);

                            String toNumbers = "";

                            for (String s : phoneno) {

                                Log.e("PHONELIST", "" + s);
                                toNumbers = toNumbers + s + ";";
                            }

                            phoneno.clear();
                            if (!toNumbers.isEmpty()) {
                                toNumbers = toNumbers.substring(0, toNumbers.length() - 1);
                                Uri sendSmsTo = Uri.parse("smsto:" + toNumbers);
                                Intent intent = new Intent(
                                        Intent.ACTION_SENDTO, sendSmsTo);
                                intent.putExtra("sms_body", sam);
                                startActivity(intent);
                            }
                            *//*Uri sendSmsTo = Uri.parse("smsto:8639796138");
                            Intent intent = new Intent(
                                    Intent.ACTION_SENDTO, sendSmsTo);
                            intent.putExtra("sms_body", sam);
                            startActivity(intent);*//*

                            //smsManager.sendTextMessage(phone, null, sam, null, null);

                            // Toast.makeText(SthreeRaksha.this, "Emergency Alert Messages Sent Successfully to all the Volunteers !", Toast.LENGTH_SHORT).show();

                        } else {

                        }
                    }
                }, Looper.getMainLooper());
    }

}


*/