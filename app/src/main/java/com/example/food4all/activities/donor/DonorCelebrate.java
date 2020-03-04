package com.example.food4all.activities.donor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food4all.activities.general.MainActivity;
import com.example.food4all.modals.Happy_Modal;
import com.example.food4all.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DonorCelebrate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText don_name,don_email,don_phone,don_money,don_add,don_date;
    String name,email,phone,money,recip,key,address,date;
    Button submit;
    String admin_1,admin_2,admin_3,msg;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy);

        don_name=(EditText)findViewById(R.id.donname);
        don_email=(EditText)findViewById(R.id.donemail);
        don_phone=(EditText)findViewById(R.id.donphone);
        don_money=(EditText)findViewById(R.id.donmoney);
        don_add = (EditText)findViewById(R.id.donaddress);
        don_date = (EditText) findViewById(R.id.celebdate);




        submit=(Button)findViewById(R.id.submit);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(DonorCelebrate.this, new DatePickerDialog.OnDateSetListener() {
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
        spinner.setOnItemSelectedListener(DonorCelebrate.this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=don_name.getText().toString().trim();
                email=don_email.getText().toString().trim();
                phone=don_phone.getText().toString().trim();
                money=don_money.getText().toString().trim();
                recip=spinner.getSelectedItem().toString();
                address = don_add.getText().toString().trim();
                date = don_date.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(name.isEmpty()) {
                    don_name.setError("Please enter Name !");
                } else if(phone.isEmpty()) {
                    don_phone.setError("Please enter Phone Number ");
                } else if(phone.length()<10) {
                    don_phone.setError("Phone Number is Invalid");
                }else if(email.isEmpty()) {
                    don_email.setError("Please enter Email ID");
                }
                else if(!email.matches(emailPattern))
                {
                    don_email.setError("Email is Invalid");
                }
                else if(address.isEmpty()) {
                    don_add.setError("Please enter Address ");
                } else if(date.isEmpty()) {
                    Toast.makeText(DonorCelebrate.this, "Please choose Date of Celebration", Toast.LENGTH_SHORT).show();
                } else if(recip.equals("Select Recipient")) {
                    Toast.makeText(DonorCelebrate.this, "Please select Recipient", Toast.LENGTH_SHORT).show();
                } else if(money.isEmpty()) {
                    don_money.setError("Please enter Money");
                } else {

                    key = myref.push().getKey();

                    myref = FirebaseDatabase.getInstance().getReference("Happy_Moments").child(key);
                    Happy_Modal happy_modal = new Happy_Modal(name, email, phone, money, recip, address, date);
                    myref.setValue(happy_modal);

                    msg = "Dear Administrator,\n" + name + " is ready to donate a sum of ";
                    String res = msg + "Rs." + money + " to " + recip;
                    String re = res + ", Please collect Money at " + address;

                    admin_1 = "9381384234";
                    admin_2 = "8639796138";
                    admin_3 = "6303149161";

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(admin_1, null, re, null, null);
                    smsManager.sendTextMessage(admin_2, null, re, null, null);
                    smsManager.sendTextMessage(admin_3, null, re, null, null);

                    Toast.makeText(DonorCelebrate.this, "Details Successfully Submitted !", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DonorCelebrate.this, MainActivity.class);
                    startActivity(intent);
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
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
