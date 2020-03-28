package com.gmrit.food4all.activities.donor;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gmrit.food4all.R;
import com.gmrit.food4all.activities.general.MainActivity;
import com.gmrit.food4all.modals.Happy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DonorCelebrateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText don_name, don_email, don_phone, don_money, don_add, don_date;
    String name, email, phone, money, recip, key, address, date;
    Button submit;
    String admin_1, admin_2, admin_3, msg;
    DatabaseReference myref;
    private AdView mAdView;
    /*Geocoder geocoder;
    List<Address> addresses;
    LocationTrack locationTrack;
    String location_address,lat,lon,knownName,city,postalCode;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy);
        this.setTitle("Celebrate Birthday");
        don_name = (EditText) findViewById(R.id.donname);
        don_email = (EditText) findViewById(R.id.donemail);
        don_phone = (EditText) findViewById(R.id.donphone);
        don_money = (EditText) findViewById(R.id.donmoney);
        don_add = (EditText) findViewById(R.id.donaddress);
        don_date = (EditText) findViewById(R.id.celebdate);

        /*locationTrack = new LocationTrack(this);

        lat = Double.toString(locationTrack.getLatitude());
        lon = Double.toString(locationTrack.getLongitude());

        geocoder = new Geocoder(this, Locale.getDefault());*/

        submit = (Button) findViewById(R.id.submit);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /*don_add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (don_add.getRight() - don_add.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        try {
                            if(lat.equals("0.0") || lon.equals("0.0"))
                                locationTrack.showSettingsAlert();
                            else {
                                //Toast.makeText(SthreeRaksha.this, "Latitude " + lat + "\n Longitude " + lon, Toast.LENGTH_SHORT).show();
                                Log.d("Latitude", lat);
                                Log.d("Longitude", lon);
                                addresses = geocoder.getFromLocation(Double.parseDouble(lat),Double.parseDouble(lon),1);
*//*
                                knownName = addresses.get(0).getFeatureName();
                                city = addresses.get(0).getLocality();
                                postalCode = addresses.get(0).getPostalCode();*//*

                                location_address = addresses.get(0).getAddressLine(0);

                                //location_address = knownName + ", " +city + ", " +postalCode; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                Log.d("address",location_address);
                                don_add.setText(location_address);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(DonorCelebrateActivity.this, "Location set Successfully !", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });
*/


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

        myref = FirebaseDatabase.getInstance().getReference().child("Happy_Moments");

        admin_1 = "9381384234";
        admin_2 = "8639796138";
        admin_3 = "6303149161";

        don_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DonorCelebrateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    //DatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        don_date.setText("" + (month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        final Spinner spinner = findViewById(R.id.spin);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.recipient, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(DonorCelebrateActivity.this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = don_name.getText().toString().trim();
                email = don_email.getText().toString().trim();
                phone = don_phone.getText().toString().trim();
                money = don_money.getText().toString().trim();
                recip = spinner.getSelectedItem().toString();
                address = don_add.getText().toString().trim();
                date = don_date.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (name.isEmpty()) {
                    don_name.setError("Please enter Name !");
                } else if (phone.isEmpty()) {
                    don_phone.setError("Please enter Phone Number ");
                } else if (phone.length() < 10) {
                    don_phone.setError("Phone Number is Invalid");
                } else if (email.isEmpty()) {
                    don_email.setError("Please enter Email ID");
                } else if (!email.matches(emailPattern)) {
                    don_email.setError("Email is Invalid");
                } else if (address.isEmpty()) {
                    don_add.setError("Please enter Address ");
                } else if (date.isEmpty()) {
                    Toast.makeText(DonorCelebrateActivity.this, "Please choose Date of Celebration", Toast.LENGTH_SHORT).show();
                } else if (recip.equals("Select Recipient")) {
                    Toast.makeText(DonorCelebrateActivity.this, "Please select Recipient", Toast.LENGTH_SHORT).show();
                } else if (money.isEmpty()) {
                    don_money.setError("Please enter Money");
                } else {

                    key = myref.push().getKey();

                   // myref = FirebaseDatabase.getInstance().getReference().child("Happy_Moments").child(key);
                    Happy happy_1_modal = new Happy(name, email, phone, money, recip, address, date);
                    myref.push().setValue(happy_1_modal);

                    msg = "Dear Administrator,\n" + name + " is ready to donate a sum of ";
                    String res = msg + "Rs." + money + " to " + recip;
                    String re = res + ", Please collect Money at " + address;

                    admin_1 = "9381384234";
                    admin_2 = "8639796138";
                    admin_3 = "6303149161";

                   /* SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(admin_1, null, re, null, null);
                    smsManager.sendTextMessage(admin_2, null, re, nu796138ll, null);
                    smsManager.sendTextMessage(admin_3, null, re, null, null);
*/
                    Uri sendSmsTo = Uri.parse("smsto:"+admin_1+";"+admin_2+";"+admin_3);
                    Intent intent = new Intent(
                            Intent.ACTION_SENDTO, sendSmsTo);
                    intent.putExtra("sms_body",re);
                    startActivity(intent);
                   // Toast.makeText(DonorCelebrateActivity.this, "Details Successfully Submitted !", Toast.LENGTH_SHORT).show();

                    /*Intent intent1 = new Intent(DonorCelebrateActivity.this, MainActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);*/
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
