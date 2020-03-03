package com.example.food4all.activities.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.food4all.modals.Report;
import com.example.food4all.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Issues extends AppCompatActivity {
    public Button b;
    EditText email, report;
    private FirebaseAuth firebaseAuth;
    DatabaseReference ref;
    Report rep;
    public String s1, s2;
    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        this.setTitle("Report Issues");
        email = (EditText) findViewById(R.id.e);
        report = (EditText) findViewById(R.id.report);
        b = (Button) findViewById(R.id.button);
        firebaseAuth = FirebaseAuth.getInstance();
        rep = new Report();
        ref = FirebaseDatabase.getInstance().getReference().child("Application_Issues");
        s1 = email.getText().toString().trim();
        s2 = report.getText().toString().trim();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            Toast.makeText(Issues.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connected) {
                    Toast.makeText(Issues.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
                s1 = email.getText().toString().trim();
                s2 = report.getText().toString().trim();
                if (s1.isEmpty()) {
                    Toast.makeText(Issues.this, "Please enter Email", Toast.LENGTH_LONG).show();
                } else if (s2.isEmpty()) {
                    Toast.makeText(Issues.this, "Please enter the Problem", Toast.LENGTH_LONG).show();
                } else {
                    rep.setEmail(email.getText().toString().trim());
                    rep.setReport(report.getText().toString().trim());
                    ref.push().setValue(rep);
                    Toast.makeText(Issues.this, "We will Contact you Shortly!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
