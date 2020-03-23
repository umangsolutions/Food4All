package com.example.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food4all.activities.donor.FoodDetailsActivity;
import com.example.food4all.activities.volunteer.UploadPhotosActivity;
import com.example.food4all.activities.volunteer.VolunteerActivity;
import com.example.food4all.activities.volunteer.VolunteerLoginActivity;
import com.example.food4all.activities.volunteer.VolunteerProfileActivity;
import com.example.food4all.modals.DonorDetails;
import com.example.food4all.modals.Fooddetails;
import com.example.food4all.utilities.MyAppPrefsManager;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DonorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView txtname,txtphone,txttype,txtaddress;
    private EditText edtcountpeople;
    private Button submit;
    DatabaseReference myref,databaseReference;
    MyAppPrefsManager myAppPrefsManager;
    ProgressDialog progressDialog;
    String name,phone,type,address,dbemail,currdate,codesent,noofpeople,cookedtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        myAppPrefsManager = new MyAppPrefsManager(this);

        dbemail = myAppPrefsManager.getUserName();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();


        txtname = (TextView) findViewById(R.id.donorname);
        txtphone =  (TextView) findViewById(R.id.donorphone);
        txttype = (TextView) findViewById(R.id.donorplace);
        txtaddress = (TextView) findViewById(R.id.donoraddress);
        edtcountpeople = (EditText) findViewById(R.id.noofpeople);
        submit = (Button) findViewById(R.id.sub);

        myref = FirebaseDatabase.getInstance().getReference("Food_Details");
        databaseReference = FirebaseDatabase.getInstance().getReference("Donor_Details");
        databaseReference.keepSynced(true);
        Query query = databaseReference.orderByChild("email").equalTo(dbemail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        name = dataSnapshot1.getValue(DonorDetails.class).getName();
                        phone = dataSnapshot1.getValue(DonorDetails.class).getPhone();
                        type = dataSnapshot1.getValue(DonorDetails.class).getType();
                        address = dataSnapshot1.getValue(DonorDetails.class).getAddress();
                    }

                    txtname.setText("Welcome " + name );
                    txtphone.setText(phone);
                    txtaddress.setText(address);
                    txttype.setText(type);
                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(DonorActivity.this, "No Data Exists !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Spinner time = findViewById(R.id.time);
        ArrayAdapter<CharSequence> adap = ArrayAdapter.createFromResource(this, R.array.timetaken, android.R.layout.simple_spinner_dropdown_item);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adap);
        time.setOnItemSelectedListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date cd = Calendar.getInstance().getTime();
                System.out.println("Current time => " + cd);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                currdate = df.format(cd);

                noofpeople = edtcountpeople.getText().toString().trim();
                cookedtime = time.getSelectedItem().toString();
                if(noofpeople.isEmpty()) {
                    edtcountpeople.setError("Please enter number");
                } else if(noofpeople.equals("0")) {
                    edtcountpeople.setError("Please enter number above 1");
                } else if(cookedtime.equals("Cooked Before")) {
                    Toast.makeText(DonorActivity.this, "Please choose Cooked Before Time", Toast.LENGTH_SHORT).show();
                } else {
                    sendVerificationCode("+91" + phone);
                    //Toast.makeText(FoodDetailsActivity.this, ""+phon, Toast.LENGTH_SHORT).show();
                    Toast.makeText(DonorActivity.this, "One Time Password has been sent to Your Mobile Number " + phone, Toast.LENGTH_SHORT).show();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
        bundle.putString("phone",phone);
        bundle.putString("address",address);
        bundle.putString("place",type);
        bundle.putString("foodno",noofpeople);
        bundle.putString("time",cookedtime);
        bundle.putString("date",currdate);
        bundle.putString("code",codesent);
        Intent intent = new Intent(DonorActivity.this, OTP_ValidationActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            // Set UserLoggedIn in MyAppPrefsManager
            MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(DonorActivity.this);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DonorActivity.this, VolunteerLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            myAppPrefsManager.setUserLoggedIn(false);
            myAppPrefsManager.setUserName("");
            finish();
            return true;
        }

        if (item.getItemId() == R.id.action_gallery) {
            Intent gall = new Intent(DonorActivity.this, UploadPhotosActivity.class);
            gall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(gall);
            return true;
        }

        if (item.getItemId() == R.id.action_settings) {

            return true;
        }

        if (item.getItemId() == R.id.action_profile) {
            Intent prof = new Intent(DonorActivity.this, VolunteerProfileActivity.class);
            prof.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(prof);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
