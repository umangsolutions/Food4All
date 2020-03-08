package com.example.food4all.activities.donor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.food4all.OTP_ValidationActivity;
import com.example.food4all.modals.Fooddetails;
import com.example.food4all.R;
import com.example.food4all.utilities.ConstantValues;
import com.example.food4all.utilities.Dialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FoodDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public Button submit;
    private FirebaseAuth firebaseAuth;
    private TextView userEmail;
    DatabaseReference reff;
    DatabaseReference ref;
    EditText nam, phone, spin, add, foodcanfeed;
    Fooddetails fooddetails;
    boolean connected = false;
    FirebaseAuth mAuth;
    String name,place,address,phon,foodno,tim,currdate,codesent;

    private static String TAG = "TOKENS_DATA";

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + ConstantValues.AUTH_KEY_FCM;
    final private String contentType = "application/json";
    String TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food__details);
        firebaseAuth = FirebaseAuth.getInstance();
        submit = (Button) findViewById(R.id.sub);
        nam = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        add = (EditText) findViewById(R.id.add);
        foodcanfeed = (EditText) findViewById(R.id.noofpeople);

        mAuth = FirebaseAuth.getInstance();


        //remove this use getSupportActionBar
        //this.setTitle("Donate Food");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Donate Food");
        }


        ref = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Volunteers");

        reff = FirebaseDatabase.getInstance().getReference().child("Food_Details");


        final Spinner spinner = findViewById(R.id.spin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.placetype, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        final Spinner time = findViewById(R.id.time);
        ArrayAdapter<CharSequence> adap = ArrayAdapter.createFromResource(this, R.array.timetaken, android.R.layout.simple_spinner_dropdown_item);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adap);
        time.setOnItemSelectedListener(this);

        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            Toast.makeText(FoodDetailsActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("/topics/userABC1");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place = spinner.getSelectedItem().toString();
                name = nam.getText().toString().trim();
                phon = phone.getText().toString().trim();
                address = add.getText().toString().trim();
                tim = time.getSelectedItem().toString();
                foodno = foodcanfeed.getText().toString().trim();

               // Toast.makeText(FoodDetailsActivity.this, ""+phon, Toast.LENGTH_SHORT).show();

                Date cd = Calendar.getInstance().getTime();
                System.out.println("Current time => " + cd);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                currdate = df.format(cd);


                SmsManager sms = SmsManager.getDefault();
                String inf = "Thank You for your Donation" + "\nWe Recieved Your Details and a Volunteer will pick food from your Doorstep Shortly." + "\n\nFor any Queries Contact us on +91 938 1384 234";
                if (name.isEmpty()) {
                    Toast.makeText(FoodDetailsActivity.this, "Please enter Name", Toast.LENGTH_LONG).show();
                } else if (phon.isEmpty()) {
                    Toast.makeText(FoodDetailsActivity.this, "Please enter Phone Number", Toast.LENGTH_LONG).show();
                } else if (phon.length() < 10) {
                    Toast.makeText(FoodDetailsActivity.this, "Phone Number is Invalid", Toast.LENGTH_SHORT).show();
                } else if (address.isEmpty()) {
                    Toast.makeText(FoodDetailsActivity.this, "Please enter Address", Toast.LENGTH_LONG).show();
                } else if (foodno.isEmpty()) {
                    Toast.makeText(FoodDetailsActivity.this, "Please enter Food can feed Number", Toast.LENGTH_SHORT).show();
                } else if (place.equals("Choose Place")) {
                    Toast.makeText(FoodDetailsActivity.this, "Please choose Type of Place", Toast.LENGTH_LONG).show();
                } else if (tim.equals("Select Cooked Before Time")) {
                    Toast.makeText(FoodDetailsActivity.this, "Please select Time of Period", Toast.LENGTH_LONG).show();
                } else {


                    sendVerificationCode(phon);
                    //Toast.makeText(FoodDetailsActivity.this, ""+phon, Toast.LENGTH_SHORT).show();
                    Toast.makeText(FoodDetailsActivity.this, "One Time Password has been sent to Your Mobile Number" + phon, Toast.LENGTH_SHORT).show();
                   // startActivity(new Intent(getApplicationContext(),OTP_ValidationActivity.class));
                }
            }
        });
    }
    public void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            //remove this.finish() use onBackPressed()
            //this.finish();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codesent = s;
            goToNext();
        }
    };

    public void goToNext() {

        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("phone",phon);
        bundle.putString("address",address);
        bundle.putString("place",place);
        bundle.putString("foodno",foodno);
        bundle.putString("time",tim);
        bundle.putString("date",currdate);
        bundle.putString("code",codesent);
        Intent intent = new Intent(FoodDetailsActivity.this, OTP_ValidationActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
