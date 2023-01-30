package com.example.gas4u;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gas4u.databinding.ActivityUsertrackorderBinding;
import com.example.gas4u.databinding.ActivityUsertrackorderBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserTrackOrder extends DrawerBaseActivity {

    ActivityUsertrackorderBinding activityUsertrackorderBinding;

    //initialize to extract from database (the green one must be exactly same with db)
    private static final String productt = "Products";
    private static final String quantityy = "Quantity";
    private static final String totalpricee = "Total price";
    private static final String Addresss = "Address";
    private static final String shipstatus = "Order Status";

    //declaration to read,authenticate from db
    FirebaseAuth fa = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = fa.getCurrentUser();

    //Take from database (read from Order collection)
    TextView orderid;
    TextView Products;
    TextView Address;
    TextView Quantity;
    TextView Tprice;
    TextView ORDplace;

    TextView ORDconfirm;
    TextView ORDdelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUsertrackorderBinding = ActivityUsertrackorderBinding.inflate(getLayoutInflater());
        setContentView(activityUsertrackorderBinding.getRoot());
        allocateActivityTitle("Track Orders");

        View view = LayoutInflater.from(this).inflate(R.layout.activity_usertrackorder, null);

        orderid = findViewById(R.id.orderid);
        Products = findViewById(R.id.products);
        Address = findViewById(R.id.address);
        Quantity = findViewById(R.id.quantity);
        Tprice = findViewById(R.id.totalprice);
        ORDplace = findViewById(R.id.ordplace);

        ORDconfirm = findViewById(R.id.ordconfirm);
        ORDdelivery = findViewById(R.id.orddelivery);

        DocumentReference docRef = db.collection("Order").document(user.getUid());
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String product = documentSnapshot.getString(productt);
                            String quantity = documentSnapshot.getString(quantityy);
                            String address = documentSnapshot.getString(Addresss);
                            String tprice = documentSnapshot.getString(totalpricee);
                            String shipstat = documentSnapshot.getString(shipstatus);

                            Products.setText("Products: " + product);
                            Quantity.setText("Quantity: " + quantity);
                            Tprice.setText("Total price: " + tprice);
                            Address.setText("Address: " + address);


                            if (Objects.equals(shipstat, "Ordered")){

                                ORDplace.setText("Your order has been placed.");

                            } else if (Objects.equals(shipstat, "confirmed")){

                                ORDplace.setText("Your order has been placed!");
                                ORDconfirm.setText("Your order has been confirmed!");

                            } else if (Objects.equals(shipstat, "shipped")){

                                ORDplace.setText("Your order has been placed!");
                                ORDconfirm.setText("Your order has been confirmed!");
                                ORDdelivery.setText("Your order is out for delivery!");

                            } else {
                                Toast.makeText(UserTrackOrder.this, "ORDER NOT AVAILABLE!", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(UserTrackOrder.this, "NO ACTIVE ORDER", Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserTrackOrder.this, "error!", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}