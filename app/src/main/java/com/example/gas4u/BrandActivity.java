package com.example.gas4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gas4u.databinding.ActivityBrandBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BrandActivity extends DrawerBaseActivity {

    ActivityBrandBinding activityBrandBinding;

    TextView nameTv,emailTv,phoneTv, tabProductsTv, filterProductsTv;
    EditText searchProductEt;
    ImageButton logoutBtn,addToCart,filterProductBtn; //editProfileBtn;
    ImageView profileIv;
    RelativeLayout productsRl;
    RecyclerView productsRv;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    private ArrayList<ModelProduct> productList;
    private AdapterProductSeller adapterProductSeller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBrandBinding = ActivityBrandBinding.inflate(getLayoutInflater());
        setContentView(activityBrandBinding.getRoot());
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
//        editProfileBtn = findViewById(R.id.editProfileBtn);
        profileIv = findViewById(R.id.profileIv);
        productsRl = findViewById(R.id.productsRl);
        productsRv = findViewById(R.id.productsRv);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadAllProducts();

        showProductsUI();

        //search
        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterProductSeller.getFilter().filter(s);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMeOffline();
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BrandActivity.this, CartActivity.class));
            }
        });
                //   editProfileBtn.setOnClickListener(new View.OnClickListener() {
//           @Override
//            public void onClick(View view) {
//               startActivity((new Intent(BrandActivity.this, ProfileEditUserActivity.class)));
//           }
//       });

        tabProductsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProductsUI();
            }
        });
        filterProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BrandActivity.this);
                builder.setTitle("Choose Category:").setItems(Constants.productCategories1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get selected item
                        String selected = Constants.productCategories1[which];
                        filterProductsTv.setText(selected);
                        if(selected.equals("All")){
                            //load all
                            loadAllProducts();
                        }
                        else{
                            //load filter
                            loadFilteredProducts(selected);
                        }
                    }
                }).show();
            }
        });
        }
    private void loadFilteredProducts(String selected) {
        productList = new ArrayList<>();
        //get all products
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //before getting reset list
                productList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    String productCategory =""+ds.child("productCategory").getValue();

                    //if selected category matches product category then add in list
                    if(selected.equals(productCategory)){
                        ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                        productList.add(modelProduct);
                    }
                    ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                    productList.add(modelProduct);
                }
                //setup adapter
                adapterProductSeller = new AdapterProductSeller(BrandActivity.this, productList);
                //set adapter
                productsRv.setAdapter(adapterProductSeller);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadAllProducts() {
        productList = new ArrayList<>();
        //get all products
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //before getting reset list
                productList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                    productList.add(modelProduct);
                }
                //setup adapter
                adapterProductSeller = new AdapterProductSeller(BrandActivity.this, productList);
                //set adapter
                productsRv.setAdapter(adapterProductSeller);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showProductsUI() {
        productsRl.setVisibility(View.VISIBLE);

        tabProductsTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabProductsTv.setBackgroundResource((R.drawable.shape_rect01));
    }
    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(BrandActivity.this,SignInActivity.class));
            finish();
        }
        else{
            loadMyInfo();
        }
    }
    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    //get user data
                    String username = ""+ds.child("Username").getValue();
                    String email = ""+ds.child("Email").getValue();
                    String phone = ""+ds.child("Phone").getValue();
                    String profileImage = ""+ds.child("ProfileImage").getValue();
                    String accountType = ""+ds.child("AccountType").getValue();
                    String city = ""+ds.child("City").getValue();

                    // set user data
                    nameTv.setText(username);
                    emailTv.setText(email);
                    phoneTv.setText(phone);
                    try{
                        Picasso.get().load(profileImage).placeholder(R.drawable.ic_baseline_person_24).into(profileIv);
                    }
                    catch(Exception e){
                        profileIv.setImageResource(R.drawable.ic_baseline_person_24);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void makeMeOffline() {
        progressDialog.setMessage("Logging Out...");
        HashMap<String, Object> hashMap = new HashMap<>();
    }
}