package com.example.food4all.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.food4all.Modal_Class.Image_Modal;
import com.example.food4all.Adapter.MyAdapter_Gallery;
import com.example.food4all.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerView_Gallery extends AppCompatActivity {

    DatabaseReference databaseReference;
    String doc,deg,photo,number;
    androidx.recyclerview.widget.RecyclerView recyclerView;
    ListView listView;
    ArrayList<Image_Modal> list;
    MyAdapter_Gallery myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view__gallery);

        final ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        recyclerView = (androidx.recyclerview.widget.RecyclerView) findViewById(R.id.myrecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        list = new ArrayList<Image_Modal>();

        databaseReference= FirebaseDatabase.getInstance().getReference("Image_Gallery");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Image_Modal d = dataSnapshot1.getValue(Image_Modal.class);
                    list.add(d);
                }
                progressDialog.dismiss();
                myAdapter = new MyAdapter_Gallery(RecyclerView_Gallery.this, list);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
