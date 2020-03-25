package com.example.food4all.activities.recipients;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food4all.R;
import com.example.food4all.modals.Recipient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RecipientLoginActivity extends AppCompatActivity {

    TextView login;
    EditText usname,pwd;
    Button submit;
    DatabaseReference databaseReference;
    String dbpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orphanage_login);
        this.setTitle("Recipient Login");
        login=(TextView)findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipientLoginActivity.this, RecipientRegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        usname=(EditText)findViewById(R.id.usname);
        pwd=(EditText)findViewById(R.id.pwd);
        submit=(Button)findViewById(R.id.button);

        databaseReference= FirebaseDatabase.getInstance().getReference("Organization_Details");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usnam = usname.getText().toString().trim();
             final String pw = pwd.getText().toString().trim();

                if(usnam.isEmpty()) {
                    usname.setError("Please enter Username");
                } else if(pw.isEmpty()) {
                    pwd.setError("Password should not be Empty");
                }
                else {
                    Query query = databaseReference.orderByChild("usname").equalTo(usnam);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                // dataSnapshot is the "issue" node with all children with id 0
                                for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                    // do something with the individual "issues"
                                    dbpass=issue.getValue(Recipient.class).getPassword();
                                }
                                if(pw.equals(dbpass)) {
                                    Toast.makeText(RecipientLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RecipientLoginActivity.this, RecipientActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(RecipientLoginActivity.this, "login Failed !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
