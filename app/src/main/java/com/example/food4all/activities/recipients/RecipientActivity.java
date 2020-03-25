package com.example.food4all.activities.recipients;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food4all.R;
import com.example.food4all.activities.general.MainActivity;
import com.example.food4all.modals.RecipientsUpdate;
import com.example.food4all.modals.Volunteer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RecipientActivity extends AppCompatActivity {

    EditText donname,volname,volphone,noofpeople;
    Button submit;
    DatabaseReference databaseReference,volreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orphange);
        this.setTitle("Donation Details");
        donname=(EditText)findViewById(R.id.donname);
        volname=(EditText)findViewById(R.id.volname);
        volphone=(EditText)findViewById(R.id.volphone);
        noofpeople=(EditText)findViewById(R.id.noofpeople);

        submit=(Button)findViewById(R.id.submit);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Recipients_Updation");

        volreference = FirebaseDatabase.getInstance().getReference().child("Volunteers");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String don_name = donname.getText().toString().trim();
                String vol_name = volname.getText().toString().trim();
                String vol_phone = volphone.getText().toString().trim();
                String noof_people = noofpeople.getText().toString().trim();
                String key = databaseReference.push().getKey();

                if(don_name.isEmpty()) {
                    donname.setError("Please enter Donor Name");
                } else if(vol_name.isEmpty()) {
                    volname.setError("Please enter Volunteer");
                } else if(vol_phone.isEmpty()) {
                    volphone.setError("Please enter Phone Number");
                } else if (vol_phone.length() < 10) {
                    volphone.setError("Phone Number is invalid");
                } else if(noof_people.isEmpty()) {
                    noofpeople.setError("Please enter People Benefitted Number ");
                } else {

                    Query query = volreference.orderByChild("phone").equalTo(vol_phone);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    int count_var = dataSnapshot1.getValue(Volunteer.class).getCount();
                                    int res = count_var + 1;
                                    dataSnapshot1.getRef().child("count").setValue(res);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Recipients_Updation").child(key);
                    RecipientsUpdate recipients_Update_update = new RecipientsUpdate(don_name, vol_name, vol_phone, noof_people);
                    databaseReference.setValue(recipients_Update_update);


                    Toast.makeText(RecipientActivity.this, "Details Successfully Submitted !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecipientActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            }
        });
    }
}
