package com.example.gas4u;

import static java.util.Objects.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Objects;


public class AdapterCartItem extends RecyclerView.Adapter<AdapterCartItem.HolderCart> {

    private Context context;
    private ArrayList<ModelCartItem> cartItems;

    @Override
    public HolderCart onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate layout row_cartitem.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_cartitem, parent, false);

        return new HolderCart(view);
    }

    @Override
    public void onBindViewHolder(HolderCart holder, @SuppressLint("RecyclerView") int position) {
        // get data
        ModelCartItem modelCartItem = cartItems.get(position);

        String id = modelCartItem.getId();

        String gpId = modelCartItem.getpID();
        String title = modelCartItem.getName();
        String cost = modelCartItem.getCost();
        String price = modelCartItem.getPrice();
        String quantity = modelCartItem.getQuantity();

        // set data
        holder.itemTitleTv.setText(""+title);
        holder.itemPriceTv.setText(""+cost);
        holder.itemQuantityTv.setText("["+quantity+"]");
        holder.itemPriceEachTv.setText(""+price);

        // handle remove click listener, delete item from cart
        holder.itemRemoveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // table will be created if not exists, however will exist
//                EasyDB easyDB =  EasyDB.init(context, "ITEMS_DB")
//                        .setTableName("ITEMS_TABLE")
//                        .addColumn(new Column("Item_Id", new String[]{"text", "unique"}))
//                        .addColumn(new Column("Item_PID", new String[]{"text", "not null"}))
//                        .addColumn(new Column("Item_Name", new String[]{"text", "not null"}))
//                        .addColumn(new Column("Item_Price_Each", new String[]{"text", "not null"}))
//                        .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
//                        .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
//                        .doneTableColumn();
//
//                easyDB.deleteRow(1,id);
//                Toast.makeText(context, "Removed from cart...", Toast.LENGTH_SHORT).show();
//
//                // refresh list
//                cartItems.remove(position);
//                notifyItemChanged(position);
//                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size(); // return number of records
    }

    // view holder class
    class HolderCart extends RecyclerView.ViewHolder{
        private TextView itemTitleTv, itemPriceTv, itemPriceEachTv, itemQuantityTv, itemRemoveTv;
        // ui views of row_cartitems.xml
        public HolderCart(@NonNull View itemView) {
            super(itemView);

            //init views
            itemTitleTv = itemView.findViewById(R.id.itemTitleTv);
            itemPriceTv = itemView.findViewById(R.id.itemPriceTv);
            itemPriceEachTv = itemView.findViewById(R.id.itemPriceEachTv);
            itemQuantityTv = itemView.findViewById(R.id.itemQuantityTv);
            itemRemoveTv = itemView.findViewById(R.id.itemRemoveTv);
        }
    }
}
