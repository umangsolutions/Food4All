
package com.gmrit.food4all.activities.volunteer;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gmrit.food4all.R;
import com.gmrit.food4all.activities.administrator.AdminActivity;
import com.gmrit.food4all.utilities.ConstantValues;
import com.gmrit.food4all.utilities.MyAppPrefsManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class VolunteerLoginActivity extends AppCompatActivity {
    public Button b1, b2;
    EditText t1, t2;
    String s1, s2;
    private FirebaseAuth firebaseAuth;
    MyAppPrefsManager myAppPrefsManager;
    private ProgressDialog progressDialog;
    boolean connected = false;
    TextView reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        this.setTitle("Volunteer Login");
        myAppPrefsManager = new MyAppPrefsManager(VolunteerLoginActivity.this);
        ConstantValues.internetCheck(VolunteerLoginActivity.this);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        assert connectivityManager != null;
        connected = Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED;
        if (!connected) {
            Toast.makeText(VolunteerLoginActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }

        progressDialog = new ProgressDialog(this);
        t1 = (EditText) findViewById(R.id.volunlogemail);
        t2 = (EditText) findViewById(R.id.volunlogpwd);
        b1 = (Button) findViewById(R.id.volunlogsubmit);
        reset = (TextView) findViewById(R.id.volunlogreset);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connected == true) {
                    userLogin();
                } else {
                    Toast.makeText(VolunteerLoginActivity.this, "Internet Unavailable", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.volunlogregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VolunteerLoginActivity.this, VolunteerRegistrationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VolunteerLoginActivity.this, ResetPasswordActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
    }

    private void userLogin() {
        s1 = t1.getText().toString().trim();
        s2 = t2.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (s1.equals("admin@gmail.com") && s2.equals("Admin@123")) {
            Intent i = new Intent(VolunteerLoginActivity.this, AdminActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            progressDialog.dismiss();
        }

        if (s1.isEmpty()) {
            t1.setError("Please enter Email ID");
            //Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
        } else if (!s1.matches(emailPattern)) {
            t1.setError("Email is Invalid");
        } else if (s2.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            //t2.setError("Please enter Password");
        } else if (s2.length() < 8) {
            Toast.makeText(this, "Invalid Password !", Toast.LENGTH_SHORT).show();
            //t2.setError("Password should be more than 8 Characters !");
        } else {
            progressDialog.setMessage("Logging in...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(s1, s2)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {

                                progressDialog.dismiss();
                                Toast.makeText(VolunteerLoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();

                            } else {
                                myAppPrefsManager.setUserLoggedIn(true);
                                myAppPrefsManager.setUserName(s1);

                                // Set isLogged_in of ConstantValues
                                ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();


                                Intent intent = new Intent(VolunteerLoginActivity.this, VolunteerActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                Toast.makeText(VolunteerLoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();


                            }
                        }
                    });
        }
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