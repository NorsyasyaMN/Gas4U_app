package com.example.gas4u;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gas4u.databinding.ActivityAdminViewProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminViewProduct extends DrawerAdminActivity{

    ActivityAdminViewProductBinding activityAdminViewProductBinding;

    TextView nameTv,emailTv,phoneTv, tabProductsTv, filterProductsTv;
    EditText searchProductEt;
    ImageButton logoutBtn,addToCart,filterProductBtn;
    ImageView profileIv;
    RelativeLayout productsRl;
    RecyclerView productsRv;

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    ArrayList<ModelProduct> brandList;
    AdapterProduct adapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminViewProductBinding = ActivityAdminViewProductBinding.inflate(getLayoutInflater());
        setContentView(activityAdminViewProductBinding.getRoot());
        allocateActivityTitle("Products");

        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        tabProductsTv = findViewById(R.id.tabProductsTv);
        filterProductsTv = findViewById(R.id.filterProductsTv);
        searchProductEt = findViewById(R.id.searchProductEt);
        logoutBtn = findViewById(R.id.logoutBtn);
        addToCart = findViewById(R.id.addToCart);
        filterProductBtn = findViewById(R.id.filterProductBtn);
        profileIv = findViewById(R.id.profileIv);
        productsRl = findViewById(R.id.productsRl);
        productsRv = findViewById(R.id.adminProductsRV);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        checkUser();
        loadAllProducts();

        showProductsUI();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMeOffline();
            }
        });

        tabProductsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProductsUI();
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminViewProduct.this, d1.class));
            }
        });


    }

    private void loadAllProducts() {
        brandList = new ArrayList<>();
        //setup adapter
        adapterProduct = new AdapterProduct(AdminViewProduct.this, brandList);
        //set adapter
        productsRv.setAdapter(adapterProduct);
        db.collection("Product").orderBy("productTitle", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        brandList.clear();
                        if (error != null) {

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                brandList.add(dc.getDocument().toObject(ModelProduct.class));
                            }
                            adapterProduct.notifyDataSetChanged();
                        }

                    }
                });
    }
    private void showProductsUI() {
        productsRl.setVisibility(View.VISIBLE);

        tabProductsTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabProductsTv.setBackgroundResource((R.drawable.shape_rect01));
    }
    private void checkUser() {
        loadMyInfo();
    }
    private void loadMyInfo() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("Customers").document(user.getUid());
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {

                    String nameResult = task.getResult().getString("userName");
                    String url = task.getResult().getString("profilePhoto");
                    String emailResult = task.getResult().getString("userEmail");
                    String phoneResult = task.getResult().getString("userPhone");

//                    Picasso.get().load(url).into(profileIv);
                    nameTv.setText(nameResult);
                    emailTv.setText(emailResult);
                    phoneTv.setText(phoneResult);
                    try{
                        Picasso.get().load(url).placeholder(R.drawable.ic_baseline_person_24).into(profileIv);
                    }
                    catch(Exception e){
                        profileIv.setImageResource(R.drawable.ic_baseline_person_24);
                    }
                }
            }
        });
    }

    private void makeMeOffline() {
        startActivity(new Intent(AdminViewProduct.this, d4.class));
    }
}