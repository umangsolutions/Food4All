package com.example.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class uprest extends AppCompatActivity {
    public Button b;
    public int flag=0;
    EditText p1,p2,em,name,phone,add,quantity;
    String s1,s2,s3;
    private FirebaseAuth firebaseAuth;
    DatabaseReference reff;
    private ProgressDialog progressDialog;
    Restaurants restaurants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uprest);
        firebaseAuth=FirebaseAuth.getInstance();
        name=(EditText)findViewById(R.id.name);
        progressDialog= new ProgressDialog(this);
        em=(EditText)findViewById(R.id.email);
        p1=(EditText)findViewById(R.id.p1);
        phone=(EditText)findViewById(R.id.phone);
        add=(EditText)findViewById(R.id.address);
        quantity=(EditText)findViewById(R.id.quantity);
        restaurants= new Restaurants();
        reff= FirebaseDatabase.getInstance().getReference().child("Restaurants");
        //p2=(EditText)findViewById(R.id.p2);
        b=(Button)findViewById(R.id.b);

        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                try {
                    Long phn = Long.parseLong(phone.getText().toString().trim());
                    Float quan = Float.parseFloat(quantity.getText().toString().trim());
                restaurants.setName(name.getText().toString().trim());
                restaurants.setEmail(em.getText().toString().trim());
                restaurants.setPhone(phn);
                restaurants.setAddress(add.getText().toString().trim());
                restaurants.setQuantity(quan);
                reff.push().setValue(restaurants);
                }catch (NumberFormatException e)
                {
                    Toast.makeText(uprest.this,"Please fill all  Fields",Toast.LENGTH_LONG).show();
                }

                    Toast.makeText(uprest.this, "User Created Successful!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(uprest.this, regrest.class);
                    startActivity(i);
                }
        });
        /*b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s3=em.getText().toString()
                s1=p1.getText().toString();
                s2=p2.getText().toString();
                if(s1.equals(s2) && s1!=s2 && s2!=s3)
                {

                    Toast.makeText(uprest.this,"Restaurant Registered Sucessfully !",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(uprest.this,regrest.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(uprest.this,"Password not Matched !",Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

    private void registerUser() {
        s1=em.getText().toString().trim();
        s2=p1.getText().toString().trim();
        String s3=phone.getText().toString().trim();
        String s4= add.getText().toString().trim();
        String s5=quantity.getText().toString();
        if(TextUtils.isEmpty(s1)) {
            Toast.makeText(uprest.this,"Please Enter E-mail",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(s2)) {
            Toast.makeText(uprest.this,"Please Enter Password",Toast.LENGTH_LONG).show();
            return;

        }
        if(TextUtils.isEmpty(s3) || TextUtils.isEmpty(s4) || TextUtils.isEmpty(s5)) {
            Toast.makeText(uprest.this, "Please fill all Fields", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(uprest.this,"Registration Error!",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

}
