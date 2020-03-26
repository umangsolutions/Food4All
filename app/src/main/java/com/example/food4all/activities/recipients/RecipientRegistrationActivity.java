package com.example.food4all.activities.recipients;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food4all.R;
import com.example.food4all.modals.Recipient;
import com.example.food4all.utilities.LocationTrack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RecipientRegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView signin;
    EditText name, usname, pwd, phone, address;
    Button register;
    DatabaseReference databaseReference;
    Geocoder geocoder;
    List<Address> addresses;
    LocationTrack locationTrack;
    String location_address, lat, lon, knownName, city, postalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orphange_registration);
        this.setTitle("Recipient Registration");


        signin = (TextView) findViewById(R.id.signin);
        name = (EditText) findViewById(R.id.name);
        usname = (EditText) findViewById(R.id.usname);
        pwd = (EditText) findViewById(R.id.pwd);
        phone = (EditText) findViewById(R.id.phone);
        address = (EditText) findViewById(R.id.address);
        register = (Button) findViewById(R.id.register);

        locationTrack = new LocationTrack(this);

        lat = Double.toString(locationTrack.getLatitude());
        lon = Double.toString(locationTrack.getLongitude());

        geocoder = new Geocoder(this, Locale.getDefault());

        final Spinner spinner = findViewById(R.id.spin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.organization_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (address.getRight() - address.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        try {
                            if (lat.equals("0.0") || lon.equals("0.0"))
                                locationTrack.showSettingsAlert();
                            else {
                                //Toast.makeText(SthreeRaksha.this, "Latitude " + lat + "\n Longitude " + lon, Toast.LENGTH_SHORT).show();
                                Log.d("Latitude", lat);
                                Log.d("Longitude", lon);
                                addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
/*
                                knownName = addresses.get(0).getFeatureName();
                                city = addresses.get(0).getLocality();
                                postalCode = addresses.get(0).getPostalCode();*/

                                location_address = addresses.get(0).getAddressLine(0);

                                //location_address = knownName + ", " +city + ", " +postalCode; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                Log.d("address", location_address);
                                address.setText(location_address);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(RecipientRegistrationActivity.this, "Location set Successfully !", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipientRegistrationActivity.this, RecipientLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam = name.getText().toString().trim();
                String orgtype = spinner.getSelectedItem().toString();
                String usnam = usname.getText().toString().trim();
                String pw = pwd.getText().toString().trim();
                String ph = phone.getText().toString().trim();
                String add = address.getText().toString().trim();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Organization_Details");
                String id = databaseReference.push().getKey();
                if (nam.isEmpty()) {
                    name.setError("Please enter Organization Name");
                } else if (orgtype.isEmpty()) {
                    Toast.makeText(RecipientRegistrationActivity.this, "Please choose Type !", Toast.LENGTH_SHORT).show();
                } else if (usnam.isEmpty()) {
                    usname.setError("Username should not be Empty");
                } else if (pw.isEmpty()) {
                    pwd.setError("Password should not be Empty");
                } else if (pw.length() < 8) {
                    pwd.setError("Password should be minimum of 8 Characters");
                } else if (ph.isEmpty()) {
                    phone.setError("Please enter Phone Number");
                } else if (ph.length() < 10) {
                    phone.setError("Phone number is invalid !");
                } else if (add.isEmpty()) {
                    address.setError("Please enter Address ");
                } else if (add.length() < 10) {
                    address.setError("Address should be minimum of 10 Characters !");
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(RecipientRegistrationActivity.this);
                    progressDialog.setMessage("Registering...");
                    progressDialog.show();

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Organization_Details").child(id);
                    Recipient orphanage_modal = new Recipient(nam, orgtype, usnam, pw, ph, add);
                    databaseReference.setValue(orphanage_modal).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(RecipientRegistrationActivity.this, "Registered Successfully !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RecipientRegistrationActivity.this, RecipientLoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
