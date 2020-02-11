package com.example.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class upvolun extends AppCompatActivity {
    public Button b;
    EditText p1,p2,em,phone,name,add;
    String s1,s2,s3="";
    private FirebaseAuth firebaseAuth;
    DatabaseReference reff;
    private ProgressDialog progressDialog;
    Volunteer volunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upvolun);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new  ProgressDialog(this);
        p1=(EditText)findViewById(R.id.p1);
        p2=(EditText)findViewById(R.id.p2);
        em=(EditText)findViewById(R.id.ema);
        phone=(EditText)findViewById(R.id.phn);
        name=(EditText)findViewById(R.id.nam);
        add=(EditText)findViewById(R.id.add);
        volunteer=new Volunteer();
        reff= FirebaseDatabase.getInstance().getReference().child("Volunteers");
        b=(Button)findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                try {
                    //Long phn = Long.parseLong(phone.getText().toString().trim());
                    volunteer.setName(name.getText().toString().trim());
                    volunteer.setEmail(em.getText().toString().trim());
                    volunteer.setPhone(phone.getText().toString().trim());
                    //volunteer.setAddress(add.getText().toString().trim());
                    reff.push().setValue(volunteer);
                }catch (NumberFormatException e)
                {
                    Toast.makeText(upvolun.this,"Please fill all  Fields",Toast.LENGTH_LONG).show();
                }

                Toast.makeText(upvolun.this, "User Created Successful!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(upvolun.this, regvolun.class);
                startActivity(i);
            }
        });
    }

    private void registerUser() {
        s1=em.getText().toString().trim();
        s2=p1.getText().toString().trim();
        String s3=phone.getText().toString().trim();
        //String s4= add.getText().toString().trim();
        if(TextUtils.isEmpty(s1)) {
            Toast.makeText(upvolun.this,"Please Enter E-mail",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(s2)) {
            Toast.makeText(upvolun.this,"Please Enter Password",Toast.LENGTH_LONG).show();
            return;

        }
        if(TextUtils.isEmpty(s3)) {
            Toast.makeText(upvolun.this, "Please fill all Fields", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Registering, Please Wait...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(s1,s2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //Toast.makeText(uprest.this,"Registered Sucesfully!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(upvolun.this,"Registration Error!",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}
