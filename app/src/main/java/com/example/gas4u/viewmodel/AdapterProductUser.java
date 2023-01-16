package com.example.gas4u.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gas4u.ModelProduct;
import com.example.gas4u.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;

public class AdapterProductUser extends RecyclerView.Adapter<AdapterProductUser.HolderProductUser> implements Filterable {

    private Context context;
    public ArrayList<ModelProduct> productList, filterList;
    private FilterProductUser filter;


    public AdapterProductUser(Context context, ArrayList<ModelProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = productList;
    }

    @NonNull
    @Override
    public HolderProductUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_user, parent, false);
        return new HolderProductUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductUser holder, int position) {
        //get data
        final ModelProduct modelProduct = productList.get(position);
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountNote = modelProduct.getDiscountNote();
        String discountPrice = modelProduct.getDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String originalPrice = modelProduct.getOriginalPrice();
        String productDescription = modelProduct.getProductDescription();
        String productTitle = modelProduct.getProductTitle();
        String productQuantity = modelProduct.getProductQuantity();
        String productId = modelProduct.getProductId();
        String timestamp = modelProduct.getTimestamp();
        String productIcon = modelProduct.getProductIcon();

        //set data
        holder.titleTv.setText(productTitle);
        holder.discountedNoteTv.setText(discountNote);
        holder.descriptionTv.setText(productDescription);
        holder.originalPriceTv.setText("$"+originalPrice);
        holder.discountedPriceTv.setText("$"+originalPrice);

        if(discountAvailable.equals("true")){

            //product is on discount
            holder.discountedPriceTv.setVisibility(View.VISIBLE);
            holder.discountedNoteTv.setVisibility(View.VISIBLE);
            holder.originalPriceTv.setPaintFlags(holder.originalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // add strike through on original price

        }
        else {
            //product is not on discount
            holder.discountedPriceTv.setVisibility(View.GONE);
            holder.discountedNoteTv.setVisibility(View.GONE);
            holder.originalPriceTv.setPaintFlags(0);
        }

        try {
            Picasso.get().load(productIcon.placeholder(R.drawable.shopping_cart).into(holder.productIconTv);
        }
        catch (Exception e){
            holder.productIconTv.setImageResource(R.drawable.ic_add_shopping_primary);
        }

        holder.addToCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add product to cart
                showQuantityDialog(modelProduct);

            }
            });

        holder.addToCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                //show product details
            }
        });


    }

    private double cost = 0;
    private double finalCost = 0;
    private int quantity = 0;
    private void showQuantityDialog(ModelProduct modelProduct) {
        //inflate layout for dialog
        View view = LayoutInflater.from (context).inflate(R.layout.dialog_quantity, null);
        //init layout view
        ImageView product = view.findViewById(R.id.productIv);
        TextView productIv  = view.findViewById(R.id.titleTv);
        TextView pQuantity  = view.findViewById(R.id.pQuantityTv);
        TextView descriptionTv  = view.findViewById(R.id.descriptionTv);
        TextView discountNotedTv  = view.findViewById(R.id.discountedNoteTv);
        TextView originalPriceTv  = view.findViewById(R.id.originalPriceTv);
        TextView priceDiscountedTv  = view.findViewById(R.id.priceDiscountedTv);
        TextView finalTv  = view.findViewById(R.id.finalTv);
        ImageButton decrementBtn  = view.findViewById(R.id.decrementBtn);
        TextView quantityTv  = view.findViewById(R.id.quantityTv);
        ImageButton incrementBtn  = view.findViewById(R.id.incrementBtn);
        ImageButton continueBtn  = view.findViewById(R.id.continueBtn);

        //get data from model
        String  productId = modelProduct.getProductId();
        String  title = modelProduct.getProductTitle();
        String  productQuantity = modelProduct.getProductQuantity();
        String  description = modelProduct.getProductDescription();
        String  discountNote = modelProduct.getProductdiscountNote();
        String  image = modelProduct.getProductIcon();

        String price;
        if(modelProduct.getDiscountPrice().equals("true")){
            //product have discount
            price = modelProduct.getDiscountPrice();
            discountedNoteTv.setVisibility(View.VISIBLE);
            originalPriceTv.setPaintFlags(originalPriceTv.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);//add strike through on original price
        }
        else{
            //product dont have discount
            discountNotedTv.setVisibility(View.GONE);
            priceDiscountedTv.setVisibility(View.VISIBLE);
            price = modelProduct.getOriginalPrice();
        }

        cost = Double.parseDouble(price.replaceAll("$",""));
        finalCost = Double.parseDouble(price.replaceAll("$",""));
        quantity = 1;

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        try {
            Picasso.get().load(image).placeholder(R.drawable.ic_cart_gray).into(productIv);
        }
            catch (Exception e){
                productIv.setImageResource(R.drawable.ic_cart_gray);
            }
        titleTv.setText(""+title);
        pQuantityTv.setText(""+productQuantity);
        descriptionTv.setText(""+description);
        discountNotedTv.setText(""+discountNote);
        quantityTv.setText(""+quantityTv);
        originalPriceTv.setText(""+ modelProduct.getOriginalPrice());
        priceDiscountedTv.setText(""+ modelProduct.getDiscountPrice());
        finalPriceTv.setText(""+ finalCost);

        AlertDialog dialog= builder.create();
        dialog.show();

        //increase quantity od the product
        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCost = finalCost + cost;
                quantity++;

                finalPriceTv.setText("$"+finalCost);
                quantityTv.setText(""+ quantity);
            }
        });
        // decrement quantity of product, only if quantity is >1
        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity>1){
                    finalCost = finalCost - cost;
                    quantity--;

                    finalPricetv.setText("$"+finalCost);
                    quantityTv.setText(""+quantity);
                }
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleTv.getText().toString().trial();
                String priceEach = originalPriceTv.getText().toString().trim().replace("$","" );
                String price = finalPriceTv.getText().toString().trim().replace("$","" );
                String quantity = quantityTv.getText().toString().trim();

                addtoCart(productId, title, priceEach, price, quantity);

                dialog.dismiss();

            }
        });
    }

    private int itemId = 1;
    private void addtoCart(String productId, String title, String priceEach, String price, String quantity) {
        itemId++;

        EasyDb easyDb = EasyDb.init(context,"ITEMS_DB")
                .setTableName("ITEM_TABLE")
                .addColumn(new Column("item_Id", new String(){"text","unique"}))
                .addColumn(new Column("Item_PID", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Price_Each", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                .doneTableColumn();

        Boolean b = easyDb.addData("Item_Id", itemId)
                .addData("Item_PId", productId)
                .addData("Item_Name", title)
                .addData("Item_Price_Each", priceEach)
                .addData("Item_Price", price)
                .addData("Item_Quantity", quantity)
                .doneDataAdding();

        Toast.makeText(context, "Added to cart...", Toast.LENGTH_SHORT).show();


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterProductUser(this, filterList);
        }
        return filter;
    }

    class HolderProductUser extends RecyclerView.ViewHolder{

        //ui views
        private ImageView productIconTv;
        private TextView discountedNoteTv, titleTv, descriptionTv, addToCartTv, discountedPriceTv, originalPriceTv;

        public HolderProductUser (@NonNull View itemView) {
            super(itemView);

            //init ui views
            productIconTv = itemView.findViewById(R.id.productIconTv);
            discountedNoteTv = itemView.findViewById(R.id.discountedNoteTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            addToCartTv = itemView.findViewById(R.id.addToCartTv);
            discountedPriceTv = itemView.findViewById(R.id.discountedPriceTv);
            originalPriceTv = itemView.findViewById(R.id.originalPriceTv);
        }
    }
}
