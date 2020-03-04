package com.example.food4all.activities.donor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.food4all.modals.Fooddetails;
import com.example.food4all.R;
import com.example.food4all.utilities.ConstantValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Food_Details extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public Button submit;
    private FirebaseAuth firebaseAuth;
    private TextView userEmail;
    DatabaseReference reff, databaseReference;
    DatabaseReference ref;
    EditText nam, phone, spin, add,number;
    Fooddetails fooddetails;
    boolean connected = false;

    String TAG = "TOKENS_DATA";

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
        number = (EditText)findViewById(R.id.numberofpeople);
        phone = (EditText) findViewById(R.id.phone);
        add = (EditText) findViewById(R.id.add);
        fooddetails = new Fooddetails();
        this.setTitle("Donate Food");

        ref = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Volunteers");
        reff = FirebaseDatabase.getInstance().getReference().child("Food_Details");
        databaseReference = FirebaseDatabase.getInstance().getReference("tokens");
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
            Toast.makeText(Food_Details.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("/topics/userABC1");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msg = spinner.getSelectedItem().toString();
                final String s1 = nam.getText().toString().trim();
                final String s2 = phone.getText().toString().trim();
                final String s3 = add.getText().toString().trim();
                final String tim = time.getSelectedItem().toString();
                final String num = number.getText().toString().trim();

                Date cd = Calendar.getInstance().getTime();
                System.out.println("Current time => " + cd);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String currdate = df.format(cd);

                SmsManager sms = SmsManager.getDefault();
                String inf = "Thank You for your Donation" + "\nWe Received Your Details and a Volunteer will pick food from your Doorstep Shortly." + "\n\nFor any Queries Contact us on +91 938 1384 234";
                if (s1.isEmpty()) {
                    Toast.makeText(Food_Details.this, "Please enter Name", Toast.LENGTH_LONG).show();
                } else if (s2.isEmpty()) {
                    Toast.makeText(Food_Details.this, "Please enter Phone Number", Toast.LENGTH_LONG).show();
                } else if(s2.length()<10) {
                    Toast.makeText(Food_Details.this, "Phone Number is Invalid ", Toast.LENGTH_SHORT).show();
                } else if(num.isEmpty()) {
                    Toast.makeText(Food_Details.this, "Please enter Food Sufficient to People", Toast.LENGTH_SHORT).show();
                } else if (s3.isEmpty()) {
                    Toast.makeText(Food_Details.this, "Please enter Address", Toast.LENGTH_LONG).show();
                } else if (msg.equals("Choose Place")) {
                    Toast.makeText(Food_Details.this, "Please choose Type of Place", Toast.LENGTH_LONG).show();
                } else if (tim.equals("Select Time Period")) {
                    Toast.makeText(Food_Details.this, "Please select Time of Period", Toast.LENGTH_LONG).show();
                } else {
                    fooddetails.setName(s1);
                    fooddetails.setPhone(s2);
                    fooddetails.setAddress(s3);
                    fooddetails.setPlace(msg);
                    fooddetails.setDate(currdate);
                    fooddetails.setStatus("");
                    fooddetails.setTime(tim);
                    fooddetails.setNo_of_people(num);
                    reff.push().setValue(fooddetails);

                    openDialog();

                    //sms.sendTextMessage(s2, null, inf, null, null);

                    /*ref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String phone = Objects.requireNonNull(dataSnapshot.getValue(Volunteer.class)).toString();
                            SmsManager smsManager = SmsManager.getDefault();
                            String m = "Mr./Mrs."+ s1 + " is willing to Donate Food from a ";
                            String res = m+ msg + ", Address is " + s3;
                            String add ="\nFood Cooked Before :"+ tim;
                            String gmr = res +add;
                            String fina =gmr + "\nContact: " + s2;
                            smsManager.sendTextMessage(phone, null,fina, null, null);
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
                    });*/

                    try {

                        RequestQueue queue = Volley.newRequestQueue(Food_Details.this);

                        String url = "https://fcm.googleapis.com/fcm/send";

                        String m ="Mr./Mrs." + s1 + " is willing to Donate Food from ";
                        String mid = m + msg + " which is sufficient to " + num + "person(s)";
                        String res = mid + ", Address is " + s3;
                        String add = "\nFood Cooked Before : " + tim;
                        String gmr = res + add;
                        String fina = gmr + "\nContact: " + s2;

                        TOPIC = "/topics/donateFood";
                        JSONObject data = new JSONObject();
                        data.put("title", "Food Ready to Donate");
                        data.put("message", fina);
                        Log.e(TAG, "" + data);

                        JSONObject notification_data = new JSONObject();
                        notification_data.put("data", data);

                        notification_data.put("to", TOPIC);

                        Log.e(TAG, "" + notification_data);

                        JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() {

                                Map<String, String> params = new HashMap<>();
                                params.put("Authorization", serverKey);
                                params.put("Content-Type", contentType);
                                return params;
                            }
                        };

                        queue.add(request);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


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
