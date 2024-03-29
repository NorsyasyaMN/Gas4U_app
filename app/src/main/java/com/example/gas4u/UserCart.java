package com.example.gas4u;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gas4u.databinding.ActivityUserCartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class UserCart extends DrawerBaseActivity {

    TextView sTotalLabelTv, sTotalTv, dFeeLabelTv, dFeeTv, totalLabelTv, totalTv;
    private static final String userName = "userName";
    private static final String userPhone = "userPhone";
    private static final String userAddress = "userAddress";
    RelativeLayout pricesLayout;
    RecyclerView cartItemsRv;
    FirebaseAuth fa = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActivityUserCartBinding activityUserCartBinding;
    ArrayList<ModelCartItem> cartItems;
    AdapterCartItem adapterCartItem;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserCartBinding = ActivityUserCartBinding.inflate(getLayoutInflater());
        setContentView(activityUserCartBinding.getRoot());
        allocateActivityTitle("Carts");

        View view = LayoutInflater.from(this).inflate(R.layout.activity_user_cart, null);

        sTotalLabelTv = findViewById(R.id.sTotalLabelTv);
        sTotalTv = findViewById(R.id.sTotalTv);
        totalLabelTv = findViewById(R.id.totalLabelTv);
        totalTv = findViewById(R.id.totalTv);
        firebaseAuth = FirebaseAuth.getInstance();
        cartItemsRv = findViewById(R.id.cartItemsRv);

        loadAllCart();
//        showProductsUI();
    }

    private void showProductsUI() {
        pricesLayout.setVisibility(View.VISIBLE);
    }

    private void loadAllCart() {

        cartItems = new ArrayList<>();
        //setup adapter
        adapterCartItem = new AdapterCartItem(this, cartItems);
        //set adapter
        cartItemsRv.setAdapter(adapterCartItem);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        db.collection("Cart").document(firebaseUser.getUid()).collection("CartList").orderBy("title", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    adapterCartItem.notifyDataSetChanged();
                }
            }
        });

    }


    public void placeorder(View v){

        final String[] fonts = {
                "Default Address", "New Address"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(UserCart.this);
        builder.setTitle("Please choose the address");
        builder.setItems(fonts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Default Address".equals(fonts[which])) {


                    FirebaseUser user = fa.getCurrentUser();
                    DocumentReference docRef = db.collection("Customers").document(user.getUid());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        String name = documentSnapshot.getString(userName);
                                        String phone = documentSnapshot.getString(userPhone);
                                        String address = documentSnapshot.getString(userAddress);
                                        String shipstatus = "Ordered";


                                        String timestamp = ""+System.currentTimeMillis();
                                        FirebaseUser user = fa.getCurrentUser();

                                        //setup data to upload for default address
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        // hashMap.put("productId", ""+timestamp);
                                        hashMap.put("Receiver Name: ", name);
                                        hashMap.put("Phone Number", phone);
                                        hashMap.put("Address", address);
                                        hashMap.put("Order Status", shipstatus);

                                        //dummy
                                        String products = "PETRON LPG GASUL 14kg";
                                        String quantity = "1";
                                        String totalprice = "35.00";

                                        hashMap.put("Products", products);
                                        hashMap.put("Quantity", quantity);
                                        hashMap.put("Total Price", totalprice);
                                        //hashMap.put("timestamp", timestamp);
                                        hashMap.put("uid", db.collection("Order").document(user.getUid()));

                                        //add to db
                                        db.collection("Order").document(user.getUid())
                                                .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            //added to db
                                                            //clearData();

                                                            Intent intent2 = new Intent(getBaseContext(), CheckoutActivity.class);
                                                            startActivity(intent2);

                                                        }

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(UserCart.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                    } else {
                                        Toast.makeText(UserCart.this, "ORDER PLACEMENT FAILED!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserCart.this, "error!", Toast.LENGTH_SHORT).show();
                                }
                            });

                    // Toast.makeText(CartActivity.this, "you nailed it", Toast.LENGTH_SHORT).show();
                } else if ("New Address".equals(fonts[which])) {

                    Intent intent2 = new Intent(getBaseContext(), PlaceOrderActivity.class);
                    startActivity(intent2);
                }

            }
        });
        builder.show();




    }

}