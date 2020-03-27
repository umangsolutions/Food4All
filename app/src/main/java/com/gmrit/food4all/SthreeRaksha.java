package com.gmrit.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmrit.food4all.modals.Volunteer;
import com.gmrit.food4all.utilities.LocationTrack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SthreeRaksha extends AppCompatActivity {

    DatabaseReference ref;
    LinearLayout emergencylayout, policelayout;
    LocationTrack locationTrack;
    String lat, lon;
    String url;
    String police = "09440795852";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sthree_raksha);
        this.setTitle("Sthree Raksha");

        locationTrack = new LocationTrack(this);

        lat = Double.toString(locationTrack.getLatitude());
        lon = Double.toString(locationTrack.getLongitude());


        emergencylayout = (LinearLayout) findViewById(R.id.layout_emergency);
        policelayout = (LinearLayout) findViewById(R.id.policelayout);


        policelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", police, null));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

                if (lat.equals("0.0") || lon.equals("0.0"))
                    locationTrack.showSettingsAlert();
                else {
                    //Toast.makeText(SthreeRaksha.this, "Latitude " + lat + "\n Longitude " + lon, Toast.LENGTH_SHORT).show();
                    Log.d("Latitude", lat);
                    Log.d("Longitude", lon);


                    url = "https://maps.google.com/?q=" + lat + "," + lon + "";

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot.exists()) {
                                    String phone = dataSnapshot1.getValue(Volunteer.class).toString();
                                    SmsManager smsManager = SmsManager.getDefault();
                                    String msg = "High Emergency Alert!!!\n";
                                    String fina = msg + "I'm in Danger.Please help me.\nLocation: ";
                                    String sam = fina + url;
                                    smsManager.sendTextMessage(phone, null, sam, null, null);

                                    Toast.makeText(SthreeRaksha.this, "Emergency Alert Messages Sent Successfully to all the Volunteers !", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(SthreeRaksha.this, "Emergency Alert failed to Send", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(SthreeRaksha.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

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


