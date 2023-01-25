package com.example.gas4u;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    //Initialize variable
    Activity activity;
   JSONArray jsonArray;

    //Create constructor
    public MainAdapter(Activity activity, JSONArray jsonArray){
       //initialize view
        this.activity = activity;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main,parent,false);
        //Return holder view

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            //Initialize json object
            JSONObject object = jsonArray.getJSONObject(position);
            //get rating from json array
            String sRating = object.getString("rating");
            //Set rating on text view
            holder.tvRating.setText(sRating);
            //Set rating on rating bar
            holder.ratingBar.setRating(Float.parseFloat(sRating));
        }catch (JSONException e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        //pass json length
        return jsonArray.length() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Initialize variable
        TextView tvRating;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assign variable
            tvRating = itemView.findViewById(R.id.tv_rating);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }
}
