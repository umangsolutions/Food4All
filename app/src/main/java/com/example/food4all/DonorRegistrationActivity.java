package com.example.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.food4all.activities.general.MainActivity;
import com.example.food4all.activities.volunteer.VolunteerLoginActivity;
import com.example.food4all.activities.volunteer.VolunteerRegistrationActivity;
import com.example.food4all.modals.DonorDetails;
import com.example.food4all.modals.Volunteer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DonorRegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public Button b;
    EditText p1, p2, em, phone, name, edtadd,type;
    String s1, s2, s3 = "";
    private FirebaseAuth firebaseAuth;
    DatabaseReference reff;
    private ProgressDialog progressDialog;
    boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        p1 = (EditText) findViewById(R.id.p1);
        em = (EditText) findViewById(R.id.ema);
        phone = (EditText) findViewById(R.id.phn);
        name = (EditText) findViewById(R.id.nam);
        edtadd=(EditText)findViewById(R.id.address);
        reff = FirebaseDatabase.getInstance().getReference().child("Donor_Details");
        b = (Button) findViewById(R.id.button);
        //Network Service State
        TextView singin = findViewById(R.id.signIn_text);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            Toast.makeText(DonorRegistrationActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }

        final Spinner spinner = findViewById(R.id.spin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.placetype, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Buttons activity
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(DonorRegistrationActivity.this, DonorLoginActivity.class);
                startActivity(in);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n = name.getText().toString().trim();
                String ph = phone.getText().toString();
                String e = em.getText().toString().trim();
                String p = p1.getText().toString().trim();
                String address = edtadd.getText().toString().trim();
                String type = spinner.getSelectedItem().toString();
                int count =0;
                SmsManager sms = SmsManager.getDefault();
                String msg = "Welcome  " + n + ",\nWe feel very Happy to see you Here.\nThank you for registering as a Donor\n\n";
                if (!ph.isEmpty() && !e.isEmpty() && !n.isEmpty()) {
                    reff.push().setValue(new DonorDetails(n,e,ph,address,type));
                } else {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (n.isEmpty()) {
                        // Toast.makeText(RegistrationActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
                        name.setError("Please Enter Name ");
                        return;
                    } else if (!e.matches(emailPattern) || e.isEmpty()) {
                        //Toast.makeText(RegistrationActivity.this, "Please Enter Valid Email", Toast.LENGTH_LONG).show();
                        em.setError("Please enter Valid Email");
                        return;
                    } else if (p.isEmpty()) {
                        Toast.makeText(DonorRegistrationActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                        // p1.setError("Please enter Password");
                        return;
                    } else if (p.length() < 8) {
                        Toast.makeText(DonorRegistrationActivity.this, "Password should be more than 8 characters", Toast.LENGTH_LONG).show();
                        // p1.setError("Password should be more than 8 Characters");
                        return;
                    } else if(address.isEmpty()) {
                        edtadd.setError("Please enter Address");
                    }
                    else if (ph.isEmpty()) {
                        //Toast.makeText(RegistrationActivity.this, "Please Enter Phone Number", Toast.LENGTH_LONG).show();
                        phone.setError("Please Enter Phone Number");
                        return;
                    } else if (ph.length() < 10) {
                        phone.setError("Phone Number is Not Valid !");
                        return;
                    }
                    else if(type.equals("Choose Type of Place")) {
                        Toast.makeText(DonorRegistrationActivity.this, "Please choose Type of Place !", Toast.LENGTH_SHORT).show();
                    }
                }

                registerUser();
                Intent broadcastIntent = new Intent(DonorRegistrationActivity.this, DonorRegistrationActivity.class);
                broadcastIntent.putExtra("toastMessage", "Hi man !");
                //startActivity(broadcastIntent);
                PendingIntent actionIntent = PendingIntent.getBroadcast(DonorRegistrationActivity.this,
                        0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_forward_white);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        DonorRegistrationActivity.this
                )
                        .setSmallIcon(R.drawable.flog)
                        .setContentTitle("Thank You for Registering !")
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(getString(R.string.message))
                                .setBigContentTitle("Thank You for Registering !")
                                .setSummaryText("Registration"))
                        .setLargeIcon(largeIcon)
                        .addAction(R.mipmap.ic_launcher, "Yes", actionIntent);

                Intent intent1 = new Intent(DonorRegistrationActivity.this, DonorLoginActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("message", msg);

                PendingIntent pendingIntent = PendingIntent.getActivity(DonorRegistrationActivity.this,
                        0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);


                NotificationManager notificationManager = (NotificationManager) getSystemService(
                        Context.NOTIFICATION_SERVICE
                );
                notificationManager.notify(1, builder.build());
                if (!ph.isEmpty()) {
                    sms.sendTextMessage(ph, null, msg, null, null);
                }
                Toast.makeText(DonorRegistrationActivity.this, "Donor Created Successful!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(DonorRegistrationActivity.this, DonorLoginActivity.class);
                startActivity(i);
                finish();
                progressDialog.dismiss();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Donor Registration");
        }
    }

    private boolean isValidEmail(String Emailid) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Emailid);
        return matcher.matches();
    }
    private Intent getNotificationIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerUser() {
        s1 = em.getText().toString().trim();
        s2 = p1.getText().toString().trim();
        String s3 = phone.getText().toString().trim();
        String s4 = name.getText().toString().trim();
        progressDialog.setMessage("Registering, Please Wait...");
        progressDialog.show();
        if (!s1.isEmpty() && !s2.isEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(s1, s2)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                            } else {
                                Toast.makeText(DonorRegistrationActivity.this, "Registration Error!", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
