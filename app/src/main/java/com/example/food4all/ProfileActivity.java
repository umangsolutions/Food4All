package com.example.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.food4all.utilities.ConstantValues;
import com.example.food4all.utilities.MyAppPrefsManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView name,phone,email;
    String mail,nam,ph,em;
    DatabaseReference databaseReference;
    MyAppPrefsManager myAppPrefsManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        overridePendingTransition(0,0);

        ConstantValues.internetCheck(ProfileActivity.this);
        myAppPrefsManager=new MyAppPrefsManager(ProfileActivity.this);
        mail=myAppPrefsManager.getUserName();
        name=(TextView)findViewById(R.id.name);
        phone=(TextView)findViewById(R.id.phone);
        email=(TextView)findViewById(R.id.email);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        databaseReference= FirebaseDatabase.getInstance().getReference("Volunteers");
        databaseReference.keepSynced(true);
        Query query = databaseReference.orderByChild("email").equalTo(mail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        nam = dataSnapshot1.getValue(Volunteer.class).getName();
                        ph = dataSnapshot1.getValue(Volunteer.class).getPhone();
                        em = dataSnapshot1.getValue(Volunteer.class).getEmail();
                    }

                    name.setText(nam);
                    phone.setText(ph);
                    email.setText(em);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id == android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
