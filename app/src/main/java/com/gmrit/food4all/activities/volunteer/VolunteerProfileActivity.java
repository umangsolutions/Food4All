package com.gmrit.food4all.activities.volunteer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gmrit.food4all.R;
import com.gmrit.food4all.modals.Volunteer;
import com.gmrit.food4all.utilities.ConstantValues;
import com.gmrit.food4all.utilities.MyAppPrefsManager;
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

import java.util.Objects;

public class VolunteerProfileActivity extends AppCompatActivity {

    TextView name, phone, email, inc;
    String mail, nam, ph, em;
    int cou;
    DatabaseReference databaseReference;
    MyAppPrefsManager myAppPrefsManager;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.setTitle("Volunteer Profile");
        overridePendingTransition(0, 0);
        ConstantValues.internetCheck(VolunteerProfileActivity.this);
        myAppPrefsManager = new MyAppPrefsManager(VolunteerProfileActivity.this);
        mail = myAppPrefsManager.getUserName();
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        inc = (TextView) findViewById(R.id.count);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }

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



        databaseReference = FirebaseDatabase.getInstance().getReference("Volunteers");
        databaseReference.keepSynced(true);
        Query query = databaseReference.orderByChild("email").equalTo(mail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        nam = Objects.requireNonNull(dataSnapshot1.getValue(Volunteer.class)).getName();
                        ph = Objects.requireNonNull(dataSnapshot1.getValue(Volunteer.class)).getPhone();
                        em = Objects.requireNonNull(dataSnapshot1.getValue(Volunteer.class)).getEmail();
                        cou = Objects.requireNonNull(dataSnapshot1.getValue(Volunteer.class)).getCount();
                    }
                    name.setText(nam);
                    phone.setText(ph);
                    email.setText(em);
                    inc.setText("Total No. of Deliveries : " + cou);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
