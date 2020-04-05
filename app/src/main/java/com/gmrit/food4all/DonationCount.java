package com.gmrit.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class DonationCount extends AppCompatActivity {

    private DatabaseReference myref;
    private AdView mAdView;
    private EditText edtphone;
    private Button btnsubmit;
    private String phone;
    private TextView doncount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_count);

        edtphone = (EditText) findViewById(R.id.phone);
        btnsubmit = (Button) findViewById(R.id.submit);
        doncount = (TextView) findViewById(R.id.count);

        doncount.setVisibility(View.INVISIBLE);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-7341014042556519/2689368944");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        myref = FirebaseDatabase.getInstance().getReference("Food_Details");

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = edtphone.getText().toString().trim();
                if(phone.isEmpty()) {
                    edtphone.setError("Please enter Phone Number");
                } else if(phone.length()!=10) {
                    edtphone.setError("Invalid Phone Number");
                } else {
                    Query query = myref.orderByChild("phone").equalTo(phone);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //int  = Integer.toString(dataSnapshot.getChildrenCount())
                                String don_count = Long.toString(dataSnapshot.getChildrenCount());
                                doncount.setVisibility(View.VISIBLE);
                                doncount.setText("Total No. of Donations : " + don_count);
                                //Toast.makeText(DonationCount.this, "Donations Count : " + don_count, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(DonationCount.this, "No Donations Found", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("View your Donations Count");
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
