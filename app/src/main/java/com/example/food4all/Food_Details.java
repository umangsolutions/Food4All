package com.example.food4all;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Food_Details extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public Button submit;
    private FirebaseAuth firebaseAuth;
    private TextView userEmail;
    DatabaseReference reff;
    DatabaseReference ref;
    EditText nam, phone, spin, add;
    Fooddetails fooddetails;
    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food__details);
        firebaseAuth = FirebaseAuth.getInstance();
        submit = (Button) findViewById(R.id.sub);
        nam = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        add = (EditText) findViewById(R.id.add);
        fooddetails = new Fooddetails();
        this.setTitle("Donate Food");

        ref = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Volunteers");
        reff = FirebaseDatabase.getInstance().getReference().child("Food_Details");

        final Spinner spinner = findViewById(R.id.spin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.placetype, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            Toast.makeText(Food_Details.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msg = spinner.getSelectedItem().toString();
                final String s1 = nam.getText().toString().trim();
                final String s2 = phone.getText().toString().trim();
                final String s3 = add.getText().toString().trim();

                Date cd = Calendar.getInstance().getTime();
                System.out.println("Current time => " + cd);
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String currdate = df.format(cd);
                SmsManager sms = SmsManager.getDefault();
                String inf = "Thank You for your Donation" + "\nWe Recieved Your Details and a Volunteer will pick food from your Doorstep Shortly." + "\n\nFor any Queries Contact us on +91 938 1384 234";
                if (s1.isEmpty()) {
                    Toast.makeText(Food_Details.this, "Please enter Name", Toast.LENGTH_LONG).show();
                } else if (s2.isEmpty()) {
                    Toast.makeText(Food_Details.this, "Please enter Phone Number", Toast.LENGTH_LONG).show();
                } else if (s3.isEmpty()) {
                    Toast.makeText(Food_Details.this, "Please enter Address", Toast.LENGTH_LONG).show();
                } else if (msg.equals("Choose Place")) {
                    Toast.makeText(Food_Details.this, "Please choose Type of Place", Toast.LENGTH_LONG).show();
                } else {
                    fooddetails.setName(s1);
                    fooddetails.setPhone(s2);
                    fooddetails.setAddress(s3);
                    fooddetails.setPlace(msg);
                    fooddetails.setDate(currdate);
                    fooddetails.setStatus("");
                    reff.push().setValue(fooddetails);
                    openDialog();

                    if (!s2.isEmpty()) {
                        sms.sendTextMessage(s2, null, inf, null, null);
                    }

                    ref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String phone = dataSnapshot.getValue(Volunteer.class).toString();
                            SmsManager smsManager = SmsManager.getDefault();
                            String m = "Hello Volunteer," + "\n" + s1 + " is willing to Donate Food from a " + msg + "\nAddress is " + s3 + "\nContact :" + s2;
                            String res = m + "\n\nPlease login to app,";
                            String fina = res + "and update your Pickup Status";
                            smsManager.sendTextMessage(phone, null, fina, null, null);

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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

    public void openDialog() {
        Dialog d = new Dialog();
        d.show(getSupportFragmentManager(), "Dialog");
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
