package com.example.food4all;

import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    List<Fooddetails> fooddetails;
    DatabaseReference ref, databaseReference;
    String nam, ph, pl, add, dat, status;

    public MyAdapter(Context c, List<Fooddetails> f) {
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
        phone = fooddetails.get(position).getPhone();
        message = "I'm coming to pickup the Food.Please feel free to contact me at this number. Thanks for donation!";
        holder.name.setText(fooddetails.get(position).getName());
        holder.phone.setText(fooddetails.get(position).getPhone());
        holder.type.setText(fooddetails.get(position).getPlace());
        holder.address.setText(fooddetails.get(position).getAddress());
        holder.status1.setText(fooddetails.get(position).getStatus());

        holder.book.setVisibility(View.VISIBLE);
        if (fooddetails.get(position).getStatus().equals("Booked")) {
            holder.book.setVisibility(View.INVISIBLE);
        }
        if (fooddetails.get(position).getStatus().isEmpty()) {
            holder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseReference = FirebaseDatabase.getInstance().getReference("Food_Details");
                    databaseReference.keepSynced(true);
                    final Query que = databaseReference.orderByChild("name").equalTo(holder.name.getText().toString());
                    //Toast.makeText(context, ""+name.getText().toString(), Toast.LENGTH_SHORT).show();
                    que.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot stat : dataSnapshot.getChildren()) {
                                stat.getRef().child("status").setValue("Booked");
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    //notifyDataSetChanged();
                    swap(fooddetails);
                    holder.book.setVisibility(View.INVISIBLE);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, message, null, null);

                }

                public void swap(List<Fooddetails> food) {
                    fooddetails.clear();
                    fooddetails.addAll(food);
                    notifyDataSetChanged();
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


    class MyViewHolder extends RecyclerView.ViewHolder {

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
