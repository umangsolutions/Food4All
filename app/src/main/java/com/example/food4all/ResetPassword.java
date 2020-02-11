package com.example.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    Button send;
    EditText reset;
    TextView af;
    FirebaseAuth firebaseAuth;
    boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        this.setTitle("Reset Password");
        af=(TextView)findViewById(R.id.after);
        firebaseAuth=FirebaseAuth.getInstance();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        if(!connected) {
            Toast.makeText(ResetPassword.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }

        send=(Button)findViewById(R.id.btn_send);
        reset=(EditText)findViewById(R.id.res_email);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = reset.getText().toString().trim();
                 if(e.equals("")) {
                     Toast.makeText(ResetPassword.this,"Please Enter Email",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     firebaseAuth.sendPasswordResetEmail(e).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()) {
                                 //Toast.makeText(ResetPassword.this,"Reset Link send to Email",Toast.LENGTH_SHORT).show();
                                 //startActivity(new Intent(ResetPassword.this,LoginActivity.class));
                                 af.setText("An Reset Password link is sent to \nYour Registered Email Address");
                             }
                             else {
                                 String error=task.getException().getMessage();
                                 Toast.makeText(ResetPassword.this,error,Toast.LENGTH_SHORT).show();
                             }
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
}
