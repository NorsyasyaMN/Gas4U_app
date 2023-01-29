package com.example.gas4u;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gas4u.databinding.ActivityD1Binding;
import com.example.gas4u.databinding.ActivityUpdateCustomerProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class PlaceOrderActivity extends AppCompatActivity {

    //ActivityUpdateCustomerProfileBinding activityUpdateCustomerProfileBinding;
    //ImageView userPhoto;
    EditText nameEt, phoneEt, addressEt;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // activityUpdateCustomerProfileBinding = ActivityUpdateCustomerProfileBinding.inflate(getLayoutInflater());
       // setContentView(activityUpdateCustomerProfileBinding.getRoot());
       setContentView(R.layout.activity_place_order);

        //init ui views
        nameEt = findViewById(R.id.NEWname);
        phoneEt = findViewById(R.id.NEWPhone);
        addressEt = findViewById(R.id.NEWAddress);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void btnupdate(View view){
        inputData();
    }

    private String userName, userPhone, userAddress, shipstatus;
    private void inputData() {
        //1) Input data
        userName = nameEt.getText().toString();
        userPhone = phoneEt.getText().toString();
        userAddress = addressEt.getText().toString();
        shipstatus = "Ordered";

        //2) validate data
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Name is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        if(TextUtils.isEmpty(userPhone)){
            Toast.makeText(this, "Age is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        if(TextUtils.isEmpty(userAddress)){
            Toast.makeText(this, "Address is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        addUser();


    }

    private void addUser() {

        //3) Add data to db
        progressDialog.setMessage("Update user...");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();
        FirebaseUser user = firebaseAuth.getCurrentUser();

            //setup data to upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("productId", ""+timestamp);
            hashMap.put("Receiver Name: ", userName);
            hashMap.put("Phone Number", userPhone);
            hashMap.put("Address", userAddress);
            hashMap.put("Order Status", shipstatus);
            hashMap.put("timestamp", timestamp);
            hashMap.put("uid", firebaseFirestore.collection("Order").document(user.getUid()));

            //add to db
            firebaseFirestore.collection("Order").document(user.getUid())
                    .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //added to db
                                progressDialog.dismiss();
                                Toast.makeText(PlaceOrderActivity.this, "Order placed...", Toast.LENGTH_SHORT).show();
                                clearData();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PlaceOrderActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        clearData();


        }



    private void clearData() {
        //clear data after upload
        nameEt.setText("");
        phoneEt.setText("");
        addressEt.setText("");
    }


    public void btnback(View v){

        Intent intent2 = new Intent(getBaseContext(), UserCart.class);
        startActivity(intent2);
    }
}
