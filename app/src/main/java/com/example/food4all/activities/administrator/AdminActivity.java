package com.example.food4all.activities.administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food4all.modals.Volunteer;
import com.example.food4all.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    TextView name,email,phone,count,high,normal;
    Button search;
    EditText vol_phone;
    String nam,em,ph;
    int cou;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        this.setTitle("Administrator");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        name = (TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.emailid);
        phone=(TextView)findViewById(R.id.phone);
        count=(TextView)findViewById(R.id.count);


        high = (TextView)findViewById(R.id.high);
        normal=(TextView)findViewById(R.id.norma);

        vol_phone=(EditText)findViewById(R.id.volphone);

        search=(Button)findViewById(R.id.sear);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Volunteers");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String volphone = vol_phone.getText().toString().trim();

                if (volphone.isEmpty()) {
                    vol_phone.setError("Please enter Volunteer Phone Number ");
                }
                else if (volphone.length()<10) {
                    vol_phone.setError("Phone Number is Invalid !");

                }else {
                    //Toast.makeText(AdminActivity.this, ""+volphone, Toast.LENGTH_SHORT).show();

                    Query query = databaseReference.orderByChild("phone").equalTo(volphone);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    nam = dataSnapshot1.getValue(Volunteer.class).getName();
                                    em = dataSnapshot1.getValue(Volunteer.class).getEmail();
                                    ph = dataSnapshot1.getValue(Volunteer.class).getPhone();
                                    cou = dataSnapshot1.getValue(Volunteer.class).getCount();
                                }
                                name.setText(nam);
                                email.setText(em);
                                phone.setText(ph);
                                count.setText(Integer.toString(cou));

                                if (cou > 10) {
                                    high.setVisibility(View.VISIBLE);
                                } else {
                                    normal.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(AdminActivity.this, "No Data Found !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AdminActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
