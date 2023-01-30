package com.example.gas4u;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterOrderedItem extends RecyclerView.Adapter<AdapterOrderedItem.HolderOrderedItem> {

    private Context context;
    private ArrayList<ModelOrderedItem> orderedItemArrayList;

    public AdapterOrderedItem(Context context, ArrayList<ModelOrderedItem> orderedItemArrayList) {
        this.context = context;
        this.orderedItemArrayList = orderedItemArrayList;
    }

    @NonNull
    @Override
    public HolderOrderedItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_ordereditem, parent, false);
        return new HolderOrderedItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderedItem holder, int position) {

        //get position of data
        ModelOrderedItem modelOrderedItem = orderedItemArrayList.get(position);
        String name = modelOrderedItem.getTitle();
        String cost = modelOrderedItem.getPrice();
        String price = modelOrderedItem.getPriceEach();
        String quantity = modelOrderedItem.getQuantity();

        //setting data
        holder.itemTitleTv.setText(name);
        holder.itemPriceEachTv.setText("RM" + price);
        holder.itemPriceTv.setText("RM" + cost);
        holder.itemQuantityTv.setText("[" + quantity + "]");
    }

    @Override
    public int getItemCount() {
        return orderedItemArrayList.size(); //return size of list
    }

    // view the class of holder
    class HolderOrderedItem extends RecyclerView.ViewHolder{

        //view of the row_ordereditem.xml
        private TextView itemTitleTv, itemPriceEachTv, itemPriceTv, itemQuantityTv;

        public HolderOrderedItem(@NonNull View itemView) {
            super(itemView);

            //initialize views
            itemTitleTv = itemView.findViewById(R.id.itemTitleTv);
            itemPriceTv = itemView.findViewById(R.id.itemPriceTv);
            itemPriceEachTv = itemView.findViewById(R.id.itemPriceEachTv);
            itemQuantityTv = itemView.findViewById(R.id.itemQuantityTv);
        }
    }
}