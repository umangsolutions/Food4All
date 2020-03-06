package com.example.food4all.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food4all.activities.volunteer.VolunteerProfileActivity;
import com.example.food4all.modals.Fooddetails;
import com.example.food4all.modals.Volunteer;
import com.example.food4all.R;
import com.example.food4all.utilities.Dialog;
import com.example.food4all.utilities.Dialog_Volunteer;
import com.example.food4all.utilities.MyAppPrefsManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class VolunteerAdapter  extends RecyclerView.Adapter<VolunteerAdapter.MyViewHolder> implements AdapterView.OnItemSelectedListener {

    Context context;
    List<Fooddetails> fooddetails;
    DatabaseReference myRef, databaseReference;
    String nam, ph, pl, add, dat, status,email;
    MyAppPrefsManager myAppPrefsManager;
    FirebaseAuth firebaseAuth;

    public VolunteerAdapter(Context c, List<Fooddetails> f) {
        context = c;
        fooddetails = f;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final String phone, message;
        /*myAppPrefsManager=new MyAppPrefsManager(context);
        email=myAppPrefsManager.getUserName();*/
        firebaseAuth=FirebaseAuth.getInstance();
        phone = fooddetails.get(position).getPhone();

        myRef= FirebaseDatabase.getInstance().getReference("Volunteers");
        myRef.keepSynced(true);
        Query query = myRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        nam= Objects.requireNonNull(issue.getValue(Volunteer.class)).getName();
                        ph= Objects.requireNonNull(issue.getValue(Volunteer.class)).getPhone();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        message = "Food Robin is on his way to Pickup the Food.\nPlease feel free to contact this number.\nThanks for donation!";
        holder.name.setText(fooddetails.get(position).getName());
        holder.phone.setText(fooddetails.get(position).getPhone());
        holder.type.setText(fooddetails.get(position).getPlace());
        holder.address.setText(fooddetails.get(position).getAddress());
        holder.status1.setText(fooddetails.get(position).getStatus());

        holder.book.setVisibility(View.VISIBLE);
        if (fooddetails.get(position).getStatus().equals("Booked")) {
            holder.book.setVisibility(View.GONE);
        }
        if (fooddetails.get(position).getStatus().isEmpty()) {
            holder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseReference = FirebaseDatabase.getInstance().getReference("Food_Details");
                    databaseReference.keepSynced(true);


                    AlertDialog.Builder adb = new AlertDialog.Builder(context);
                   // adb.setView(alertDialogView);
                    adb.setTitle("Are you sure you want to Proceed ?");
                    adb.setIcon(android.R.drawable.ic_dialog_alert);
                    adb.setMessage("By clicking OK, you need to pickup food from Donor's Doorstep directly.Please feel free to contact the Donor");
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final Query que = databaseReference.orderByChild("name").equalTo(holder.name.getText().toString());
                            //Toast.makeText(context, ""+name.getText().toString(), Toast.LENGTH_SHORT).show();
                            que.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot stat : dataSnapshot.getChildren()) {
                                        stat.getRef().child("status").setValue("Booked");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phone, null, message, null, null);
                            Intent intent = new Intent(context, VolunteerProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            Toast.makeText(context, "Successfully Updated Delivery Count !", Toast.LENGTH_SHORT).show();
                        }
                    });
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           // dialog.cancel();
                            dialog.dismiss();
                        }
                    });
                    adb.show();

                    //notifyDataSetChanged();
                    //holder.book.setVisibility(View.GONE);


                }


            });


        }
    }

    @Override
    public int getItemCount() {
        return (null != fooddetails ? fooddetails.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, phone, type, address, status1;
        Button book;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            type = (TextView) itemView.findViewById(R.id.type);
            address = (TextView) itemView.findViewById(R.id.address);
            book = (Button) itemView.findViewById(R.id.book);
            status1 = (TextView) itemView.findViewById(R.id.status);
            //book.setEnabled(true);
        }
    }

    public void filterList(List<Fooddetails> filteredList) {
        fooddetails = filteredList;
        notifyDataSetChanged();
    }

}
