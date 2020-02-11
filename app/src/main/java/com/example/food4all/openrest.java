package com.example.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class openrest extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public Button log,submit;
    private FirebaseAuth firebaseAuth;
    private TextView userEmail;
    DatabaseReference reff;
    EditText nam,phone,spin,add;
    Fooddetails fooddetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openrest);
        firebaseAuth=FirebaseAuth.getInstance();
        submit=(Button) findViewById(R.id.sub);
        nam=(EditText)findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phone);
        add=(EditText)findViewById(R.id.add);
        fooddetails=new Fooddetails();
        reff= FirebaseDatabase.getInstance().getReference().child("Food_Details");
        final Spinner spinner = findViewById(R.id.spin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.placetype, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=spinner.getSelectedItem().toString();
                fooddetails.setName(nam.getText().toString().trim());
                fooddetails.setPhone(phone.getText().toString().trim());
                fooddetails.setAddress(add.getText().toString().trim());
                fooddetails.setPlace(msg);
                reff.push().setValue(fooddetails);
                openDialog();
                Toast.makeText(openrest.this, "Details Submitted !", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void openDialog() {
        Dialog d = new Dialog();
        d.show(getSupportFragmentManager(),"Dialog");
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
