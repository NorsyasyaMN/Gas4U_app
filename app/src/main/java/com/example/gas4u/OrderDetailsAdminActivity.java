package com.example.gas4u;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderDetailsAdminActivity extends AppCompatActivity {

    // view of ui
    private ImageButton backBtn, editBtn, mapBtn;
    private TextView orderIdTv, dateTv, orderStatusTv, emailTv, phoneTv,totalItemsTv, amountTv, addressTv;
    private RecyclerView itemsRv;

    String orderId, orderBy;
    // open in map the destination
    String sourceLatitude,sourceLongitude, destinationLatitude, destinationLongitude;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    ArrayList<ModelCartItem> cartItems;
    private ArrayList<ModelOrderedItem> orderedItemArrayList;
    private AdapterOrderedItem adapterOrderedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_admin);

        // initialize view of ui
        backBtn = findViewById(R.id.backBtn);
        mapBtn = findViewById(R.id.mapBtn);
        orderIdTv = findViewById(R.id.orderIdTv);
        dateTv = findViewById(R.id.dateTv);
        orderStatusTv = findViewById(R.id.orderStatusTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        totalItemsTv = findViewById(R.id.totalItemsTv);
        amountTv = findViewById(R.id.amountTv);
        addressTv = findViewById(R.id.addressTv);
        itemsRv = findViewById(R.id.itemsRv);

        // from intent get data
        orderId = getIntent().getStringExtra("orderId");
        orderBy = getIntent().getStringExtra("orderBy");

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        loadMyInfo();
        loadBuyerInfo();
        loadOrderDetails();
        loadOrderedItems();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back
                onBackPressed();
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit order status
                editOrderStatusDialog();
            }
        });

    }

    private void editOrderStatusDialog() {
        //options to display in dialog
        final String[] options = {"In Progress", "Completed", "Cancelled"};

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Order Status")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // handle the click of item
                        String selectedOption = options[which];
                        editOrderStatus(selectedOption);
                    }
                });
    }

    private void editOrderStatus(String selectedOption) {

        //setting data to place in firebase db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("OrderStatus", "" + selectedOption);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Orders").child(orderId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //status is update
                        Toast.makeText(OrderDetailsAdminActivity.this, "Order is now " + selectedOption, Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //status fail to update, display reason
                        Toast.makeText(OrderDetailsAdminActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void openMap() {
        //saddr is source address
        //daddr is the destination address
        String address = "https://maps.google.com/maps?saddr=" + sourceLatitude + "," + sourceLongitude + "&daddr=" + destinationLatitude + "," + destinationLongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        startActivity(intent);
    }

    private void loadMyInfo() {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
//        ref.child(firebaseAuth.getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        sourceLatitude = ""+snapshot.child("latitude").getValue();
//                        sourceLongitude = ""+snapshot.child("longitude").getValue();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("Orders").document("0KxYWa4CbfNVBe4odiMscRbe5a63");
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {

                    String orderCost = task.getResult().getString("orderCost");
                    String orderId = task.getResult().getString("orderId");
                    String orderStatus = task.getResult().getString("orderStatus");
                    String orderTime = task.getResult().getString("orderTime");
                    String orderAddress = task.getResult().getString("address");
                    String orderEmail = task.getResult().getString("email");
                    String orderPhone = task.getResult().getString("phone");
                    String orderItem = task.getResult().getString("items");


                    //status of order
                    if (orderStatus.equals("In Progress")){
                        orderStatusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else if(orderStatus.equals("Completed")){
                        orderStatusTv.setTextColor(getResources().getColor(R.color.colorGreen));
                    }
                    else if(orderStatus.equals("Cancelled")){
                        orderStatusTv.setTextColor(getResources().getColor(R.color.colorRed));
                    }
//
                    orderIdTv.setText(orderId);
                    dateTv.setText(orderTime);
                    emailTv.setText(orderEmail);
                    phoneTv.setText(orderPhone);
                    totalItemsTv.setText(orderItem);
                    amountTv.setText(orderCost);
                    addressTv.setText(orderAddress);

                }
            }
        });
    }

    private void loadBuyerInfo() {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
//        ref.child(orderBy)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        //get info of buyer
//                        destinationLatitude = ""+snapshot.child("latitude").getValue();
//                        destinationLongitude = ""+snapshot.child("longitude").getValue();
//                        String email = ""+snapshot.child("email").getValue();
//                        String phone = ""+snapshot.child("phone").getValue();
//
//                        // set buyer info
//                        emailTv.setText(email);
//                        phoneTv.setText(phone);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("Orders").document("0KxYWa4CbfNVBe4odiMscRbe5a63");
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {

                    String orderCost = task.getResult().getString("orderCost");
                    String orderId = task.getResult().getString("orderId");
                    String orderStatus = task.getResult().getString("orderStatus");
                    String orderTime = task.getResult().getString("orderTime");
                    String orderAddress = task.getResult().getString("address");
                    String orderEmail = task.getResult().getString("email");
                    String orderPhone = task.getResult().getString("phone");
                    String orderItem = task.getResult().getString("items");


                    //status of order
                    if (orderStatus.equals("In Progress")){
                        orderStatusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else if(orderStatus.equals("Completed")){
                        orderStatusTv.setTextColor(getResources().getColor(R.color.colorGreen));
                    }
                    else if(orderStatus.equals("Cancelled")){
                        orderStatusTv.setTextColor(getResources().getColor(R.color.colorRed));
                    }
//
                    orderIdTv.setText(orderId);
                    dateTv.setText(orderTime);
                    emailTv.setText(orderEmail);
                    phoneTv.setText(orderPhone);
                    totalItemsTv.setText(orderItem);
                    amountTv.setText(orderCost);
                    addressTv.setText(orderAddress);

                }
            }
        });
    }

    private void loadOrderDetails(){
        // based on order id, load the the details of order
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("Orders").document("0KxYWa4CbfNVBe4odiMscRbe5a63");
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {

                    String orderCost = task.getResult().getString("orderCost");
                    String orderId = task.getResult().getString("orderId");
                    String orderStatus = task.getResult().getString("orderStatus");
                    String orderTime = task.getResult().getString("orderTime");
                    String orderAddress = task.getResult().getString("address");
                    String orderEmail = task.getResult().getString("email");
                    String orderPhone = task.getResult().getString("phone");
                    String orderItem = task.getResult().getString("items");


                    //status of order
                    if (orderStatus.equals("In Progress")){
                        orderStatusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else if(orderStatus.equals("Completed")){
                        orderStatusTv.setTextColor(getResources().getColor(R.color.colorGreen));
                    }
                    else if(orderStatus.equals("Cancelled")){
                        orderStatusTv.setTextColor(getResources().getColor(R.color.colorRed));
                    }
//
                    orderIdTv.setText(orderId);
                    dateTv.setText(orderTime);
                    emailTv.setText(orderEmail);
                    phoneTv.setText(orderPhone);
                    totalItemsTv.setText(orderItem);
                    amountTv.setText(orderCost);
                    addressTv.setText(orderAddress);

                }
            }
        });
    }

    private void findAddress(String latitude, String longitude) {
        double la = Double.parseDouble(latitude);
        double lo = Double.parseDouble(longitude);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try{
            addresses = geocoder.getFromLocation(la, lo, 1);

            //the complete address
            String address = addresses.get(0).getAddressLine(0);
            addressTv.setText(address);
        }
        catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadOrderedItems(){
        // loading the products/items of the order

        //initialize list
        cartItems = new ArrayList<>();

        firestore.collection("Cart").document("0KxYWa4CbfNVBe4odiMscRbe5a63").collection("CartList").orderBy("title", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                cartItems.clear();
                if(error !=null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        cartItems.add(dc.getDocument().toObject(ModelCartItem.class));
                    }
                }
                adapterOrderedItem = new AdapterOrderedItem(OrderDetailsAdminActivity.this, orderedItemArrayList);
                //setting adapter to recyclerView
                itemsRv.setAdapter(adapterOrderedItem);
            }
        });

    }
}