package com.example.food4all;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.food4all.modals.Volunteer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SthreeRaksha extends AppCompatActivity {

    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sthree_raksha);

        this.setTitle("Sthree Raksha");


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ref = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Volunteers");

        findViewById(R.id.emergency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ref.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                           if(dataSnapshot.exists()) {
                               String phone = dataSnapshot1.getValue(Volunteer.class).toString();
                               SmsManager smsManager = SmsManager.getDefault();
                               String msg = "High Emergency Alert!!!\nWoman is under Critical Condition.";
                               String fina = msg + "Please contact this Phone Immediately.Urgent Rescue is Required";
                               smsManager.sendTextMessage(phone,null,fina,null,null);
                           }
                           else {
                               Toast.makeText(SthreeRaksha.this, "Sorry! No Volunteers Exists ", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                       Toast.makeText(SthreeRaksha.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               });

                Toast.makeText(SthreeRaksha.this, "Emergency Alert Messages Sent Successfully !", Toast.LENGTH_SHORT).show();
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


