package com.example.gas4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;

import com.example.gas4u.databinding.ActivityBrandBinding;
import com.example.gas4u.databinding.ActivityBrandBinding;
import com.example.gas4u.databinding.ActivityCartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class UserTrackingOrder extends DrawerBaseActivity {

   // UserTrackingOrderBinding userTrackingOrderBinding;

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
    TextView orderid = findViewById(R.id.orderid);
    TextView Products = findViewById(R.id.products);
    TextView Address = findViewById(R.id.address);
    TextView Quantity = findViewById(R.id.quantity);
    TextView Tprice = findViewById(R.id.totalprice);
    TextView ORDplace = findViewById(R.id.ordplace);

    TextView ORDconfirm = findViewById(R.id.ordconfirm);
    TextView ORDdelivery = findViewById(R.id.orddelivery);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        userTrackingOrderBinding = UserTrackingOrderBinding.inflate(getLayoutInflater());
//        setContentView(userTrackingOrderBinding.getRoot());
//        allocateActivityTitle("Track Order");
//        setContentView(R.layout.activity_user_tracking_order);



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
                                Toast.makeText(UserTrackingOrder.this, "ORDER NOT AVAILABLE!", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(UserTrackingOrder.this, "NO ACTIVE ORDER", Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserTrackingOrder.this, "error!", Toast.LENGTH_SHORT).show();
                    }
                });



    }

}


