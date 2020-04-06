package com.gmrit.food4all.activities.recipients;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
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

import com.gmrit.food4all.R;
import com.gmrit.food4all.modals.Recipient;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecipientRegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView signin;
    EditText name, usname, pwd, phone, address;
    Button register;
    DatabaseReference databaseReference;
    Boolean connected;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orphange_registration);
        this.setTitle("Recipient Registration");

        name = (EditText) findViewById(R.id.recregname);
        final Spinner spinner = findViewById(R.id.recregspin);
        usname = (EditText) findViewById(R.id.recregusname);
        pwd = (EditText) findViewById(R.id.recregpwd);
        phone = (EditText) findViewById(R.id.recregphone);
        address = (EditText) findViewById(R.id.recregaddress);
        register = (Button) findViewById(R.id.recregregister);
        signin = (TextView) findViewById(R.id.recregsignin);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.organization_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


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

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
            Toast.makeText(RecipientRegistrationActivity.this, "Internet Unavailable", Toast.LENGTH_SHORT).show();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipientRegistrationActivity.this, RecipientLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                    if (connected == true) {
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
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Toast.makeText(RecipientRegistrationActivity.this, "Internet Unavailable", Toast.LENGTH_SHORT).show();
                    }
                }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
