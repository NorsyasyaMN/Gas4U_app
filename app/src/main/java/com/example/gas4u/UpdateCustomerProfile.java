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

public class UpdateCustomerProfile extends AppCompatActivity {

    ActivityUpdateCustomerProfileBinding activityUpdateCustomerProfileBinding;
    ImageView userPhoto;
    EditText nameEt, emailEt, phoneEt, addressEt;
    Button updateBtn, cancelBtn;

    Uri image_uri;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUpdateCustomerProfileBinding = ActivityUpdateCustomerProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUpdateCustomerProfileBinding.getRoot());
//        setContentView(R.layout.activity_update_customer_profile);

        //init ui views
        userPhoto = findViewById(R.id.userPhoto);
        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        phoneEt = findViewById(R.id.phoneEt);
        addressEt = findViewById(R.id.addressEt);
        updateBtn = findViewById(R.id.updateBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), CustomerProfileActivity.class));
            }
        });
        activityUpdateCustomerProfileBinding.userPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                launcher.launch("image/*");
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //flow:
                //1) Input data
                //2) Validate data
                //3) Add data to db
                inputData();
            }
        });
    }

    private String userName, userEmail, userPhone, userAddress;
    private void inputData() {
        //1) Input data
        userName = nameEt.getText().toString();
        userEmail = emailEt.getText().toString();
        userPhone = phoneEt.getText().toString();
        userAddress = addressEt.getText().toString();

        //2) validate data
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Name is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Email is required...", Toast.LENGTH_SHORT).show();
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

        if (image_uri == null){
            //upload without image

            //setup data to upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("productId", ""+timestamp);
            hashMap.put("userName", userName);
            hashMap.put("userEmail", userEmail);
            hashMap.put("userPhone", userPhone);
            hashMap.put("userAddress", userAddress);
            hashMap.put("profilePhoto", "");
            hashMap.put("timestamp", timestamp);
            hashMap.put("uid", firebaseFirestore.collection("Users").document(user.getUid()));

            //add to db
            firebaseFirestore.collection("Customers").document(user.getUid())
                    .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //added to db
                                progressDialog.dismiss();
                                Toast.makeText(UpdateCustomerProfile.this, "Profile updated...", Toast.LENGTH_SHORT).show();
                                clearData();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateCustomerProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }else{
            //upload with image

            //first upload image to storage

            //name and path of image to be uploaded
            String filePathAndName = "customer_images/" + timestamp;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image uploaded
                            //get url of uploaded image
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if (uriTask.isSuccessful()){
                                //url of image received, upload to db
                                //setup data to upload
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("productId", ""+timestamp);
                                hashMap.put("userName", userName);
                                hashMap.put("userEmail", userEmail);
                                hashMap.put("userPhone", userPhone);
                                hashMap.put("userAddress", userAddress);
                                hashMap.put("profilePhoto", downloadImageUri);
                                hashMap.put("timestamp", timestamp);
                                hashMap.put("uid", firebaseFirestore.collection("Customers").document(user.getUid()));

                                //add to db
                                firebaseFirestore.collection("Customers").document(user.getUid())
                                        .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //added to db
                                                progressDialog.dismiss();
                                                Toast.makeText(UpdateCustomerProfile.this, "Profile updated...", Toast.LENGTH_SHORT).show();
                                                clearData();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(UpdateCustomerProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateCustomerProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void clearData() {
        //clear data after upload
        nameEt.setText("");
        emailEt.setText("");
        phoneEt.setText("");
        addressEt.setText("");
        userPhoto.setImageResource(R.drawable.profile_photo);
        image_uri = null;

    }


    ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            activityUpdateCustomerProfileBinding.userPhoto.setImageURI(result);
            image_uri = result;
        }
    });
}
