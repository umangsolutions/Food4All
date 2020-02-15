package com.example.food4all.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food4all.Modal_Class.Image_Modal;
import com.example.food4all.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter_Gallery extends RecyclerView.Adapter<MyAdapter_Gallery.MyViewHolder> {

    Context context;
    List<Image_Modal> imagemodal;

    public MyAdapter_Gallery(Context c , List<Image_Modal> f) {
        context = c;
        imagemodal = f;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.gallery_card,parent,false));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView image;

        public ViewHolder(View v)
        {
            super(v);
            image =(ImageView)v.findViewById(R.id.image);
        }

        public ImageView getImage(){ return this.image;}
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        //final Image image = images.get(position);
        holder.name.setText(imagemodal.get(position).getName());
       // holder.date.setText(imagemodal.get(position).getDate());
        holder.area.setText(imagemodal.get(position).getArea());
        //holder.people.setText(imagemodal.get(position).getNoofplaces());
        final String url = imagemodal.get(position).getUrl();
        Picasso.get().load(url).into(holder.gallimage);
    }



    @Override
    public int getItemCount() {
        return imagemodal.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView gallimage;
        TextView name,area,people,date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            //date=(TextView)itemView.findViewById(R.id.date);
            area=(TextView)itemView.findViewById(R.id.place);
            //people=(TextView)itemView.findViewById(R.id.noofpeople);
            gallimage=(ImageView)itemView.findViewById(R.id.imag);
        }
    }

    public void filterList(List<Image_Modal> filteredList) {
        imagemodal = filteredList;
        notifyDataSetChanged();
    }
}
