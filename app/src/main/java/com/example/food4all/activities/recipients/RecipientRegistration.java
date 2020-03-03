package com.example.food4all.activities.recipients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.food4all.modals.Recipient_Modal;
import com.example.food4all.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecipientRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView signin;
    EditText name,usname,pwd,phone,address;
    Button register;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orphange_registration);



        signin=(TextView)findViewById(R.id.signin);
        name=(EditText)findViewById(R.id.name);
        usname=(EditText)findViewById(R.id.usname);
        pwd=(EditText)findViewById(R.id.pwd);
        phone=(EditText)findViewById(R.id.phone);
        address=(EditText)findViewById(R.id.address);
        register=(Button)findViewById(R.id.register);

        final Spinner spinner = findViewById(R.id.spin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.organization_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipientRegistration.this, Recipient_login.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam = name.getText().toString().trim();
                String orgtype= spinner.getSelectedItem().toString();
                String usnam= usname.getText().toString().trim();
                String pw = pwd.getText().toString().trim();
                String ph = phone.getText().toString().trim();
                String add = address.getText().toString().trim();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Organization_Details");
                String id= databaseReference.push().getKey();
                if(nam.isEmpty()) {
                    name.setError("Please enter Organization Name");
                } else if(orgtype.isEmpty()) {
                    Toast.makeText(RecipientRegistration.this, "Please choose Type !", Toast.LENGTH_SHORT).show();
                } else if(usnam.isEmpty()) {
                    usname.setError("Username should not be Empty");
                } else if(pw.isEmpty()) {
                    pwd.setError("Password should not be Empty");
                } else if(pw.length()<8) {
                    pwd.setError("Password should be minimum of 8 Characters");
                }
                else if(ph.isEmpty()) {
                    phone.setError("Please enter Phone Number");
                } else if(ph.length()<10) {
                    phone.setError("Phone number is invalid !");
                }
                else if(add.isEmpty()) {
                    address.setError("Please enter Address ");
                } else if(add.length()<10) {
                    address.setError("Address should be minimum of 10 Characters !");
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(RecipientRegistration.this);
                    progressDialog.setMessage("Registering...");
                    progressDialog.show();

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Organization_Details").child(id);
                    Recipient_Modal orphanage_modal = new Recipient_Modal(nam,orgtype,usnam,pw,ph,add);
                    databaseReference.setValue(orphanage_modal).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(RecipientRegistration.this, "Registered Successfully !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RecipientRegistration.this, Recipient_login.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id == android.R.id.home)
        {
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
