package com.example.gas4u;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gas4u.databinding.ActivityD1Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class d1 extends DrawerAdminActivity{

    ActivityD1Binding activityAddProduct;
    ImageView productIconIv;
    EditText titleEt, descriptionEt;
    TextView categoryTv, quantityEt, priceEt, discountedPriceEt, discountedNoteEt;
    SwitchCompat discountSwitch;
    Button addProductBtn;

    //permission connection
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE= 300;
    //image pick connection
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    //permission arrays
    private String[] cameraPermissions;
    private String[] storagePermission;
    //image picked uri
    Uri image_uri;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddProduct = ActivityD1Binding.inflate(getLayoutInflater());
        setContentView(activityAddProduct.getRoot());
        allocateActivityTitle("Add Product");

        //init ui views
        productIconIv = findViewById(R.id.productIconIv);
        titleEt = findViewById(R.id.titleEt);
        descriptionEt = findViewById(R.id.descriptionEt);
        categoryTv = findViewById(R.id.categoryTv);
        quantityEt = findViewById(R.id.quantityEt);
        priceEt = findViewById(R.id.priceEt);
        discountedPriceEt = findViewById(R.id.discountedPriceEt);
        discountedNoteEt = findViewById(R.id.discountedNoteEt);
        discountSwitch = findViewById(R.id.discountSwitch);
        addProductBtn = findViewById(R.id.addProductBtn);

        //checked, show discountPriceEt, discountNoteEt
        discountedPriceEt.setVisibility(View.GONE);
        discountedNoteEt.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //init permission arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //if discountSwitch is checked: show discountPriceEt, discountNoteEt | if discountSwitch is not checked: hide discountPriceEt, discountNoteEt
        discountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //checked, show discountPriceEt, discountNoteEt
                    discountedPriceEt.setVisibility(View.VISIBLE);
                    discountedNoteEt.setVisibility(View.VISIBLE);
                }
                else{
                    //checked, show discountPriceEt, discountNoteEt
                    discountedPriceEt.setVisibility(View.GONE);
                    discountedNoteEt.setVisibility(View.GONE);
                }
            }
        });

        productIconIv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //show dialog to pick image
                showImagePickDialog();
            }
        });

        categoryTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //pick category
                categoryDialog();
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener(){
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

    private String productTitle, productDescription, productCategory, productQuantity, originalPrice, discountPrice, discountNote;
    private boolean discountAvailable = false;
    private void inputData() {
        //1) Input data
        productTitle = titleEt.getText().toString();
        productDescription = descriptionEt.getText().toString();
        productCategory = categoryTv.getText().toString();
        productQuantity = quantityEt.getText().toString();
        originalPrice = priceEt.getText().toString();
        discountAvailable = discountSwitch.isChecked(); // true/false

        //2) validate data
        if(TextUtils.isEmpty(productTitle)){
            Toast.makeText(this, "Title is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        if(TextUtils.isEmpty(productCategory)){
            Toast.makeText(this, "Category is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        if(TextUtils.isEmpty(originalPrice)){
            Toast.makeText(this, "Price is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        if(discountAvailable){
            //product is with discount
            discountPrice = discountedPriceEt.getText().toString();
            discountNote = discountedNoteEt.getText().toString();
            if(TextUtils.isEmpty(discountPrice)){
                Toast.makeText(this, "Discount Price is required...", Toast.LENGTH_SHORT).show();
                return; // don't proceed further
            }
            else{
                //product is without discount
                discountPrice = "0";
                discountNote = "";
            }
        }

        addProduct();


    }

    private void addProduct() {

        //3) Add data to db
        progressDialog.setMessage("Adding Product...");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (image_uri == null){
            //upload without image

            //setup data to upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("productId", ""+timestamp);
            hashMap.put("productTitle", productTitle);
            hashMap.put("productDescription", productDescription);
            hashMap.put("productCategory", productCategory);
            hashMap.put("productQuantity", productQuantity);
            hashMap.put("productIcon", "");
            hashMap.put("originalPrice", originalPrice);
            hashMap.put("discountPrice", discountPrice);
            hashMap.put("discountNote", discountNote);
            hashMap.put("discountAvailable", discountAvailable);
            hashMap.put("timestamp", timestamp);
            hashMap.put("uid", firebaseFirestore.collection("Users").document(user.getUid()));

            //add to db
            firebaseFirestore.collection("Users").document(user.getUid())
                    .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //added to db
                                progressDialog.dismiss();
                                Toast.makeText(d1.this, "Product added...", Toast.LENGTH_SHORT).show();
                                clearData();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(d1.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }else{
            //upload with image

            //first upload image to storage

            //name and path of image to be uploaded
            String filePathAndName = "product_images/" + "" + timestamp;

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
                                hashMap.put("productId", timestamp);
                                hashMap.put("productTitle", productTitle);
                                hashMap.put("productDescription", productDescription);
                                hashMap.put("productCategory", productCategory);
                                hashMap.put("productQuantity", productQuantity);
                                hashMap.put("productIcon", downloadImageUri);
                                hashMap.put("originalPrice", originalPrice);
                                hashMap.put("discountPrice", discountPrice);
                                hashMap.put("discountNote", discountNote);
                                hashMap.put("discountAvailable", discountAvailable);
                                hashMap.put("timestamp", timestamp);
                                hashMap.put("uid", firebaseFirestore.collection("Users").document(user.getUid()));

                                //add to db
                                firebaseFirestore.collection("Users").document(user.getUid())
                                        .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //added to db
                                                progressDialog.dismiss();
                                                Toast.makeText(d1.this, "Product added...", Toast.LENGTH_SHORT).show();
                                                clearData();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(d1.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(d1.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void clearData() {
        //clear data after upload
        titleEt.setText("");
        descriptionEt.setText("");
        categoryTv.setText("");
        quantityEt.setText("");
        priceEt.setText("");
        discountedPriceEt.setText("");
        discountedNoteEt.setText("");
        productIconIv.setImageResource(R.drawable.add_product);
        image_uri = null;

    }

    private void categoryDialog() {
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Category")
                .setItems(Connection.productCategories, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //get picked category
                        String category = Connection.productCategories[which];

                        //set picked category
                        categoryTv.setText(category);
                    }
                }).show();
    }

    private void showImagePickDialog() {
        //options to display in dialog
        String[] options = {"Camera","Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //handle items clicks
                        if (which == 0){
                            //camera clicked
                            if (checkCameraPermission()){
                                //permission granted
                                pickFromCamera();
                            }
                            else{
                                //permission not granted, request
                                requestCameraPermission();
                            }
                        }
                        else{
                            //gallery clicked
                            if (checkStoragePermission()){
                                //permission granted
                                pickFromGallery();
                            }
                            else{
                                //permission not granted
                                requestStoragePermission();
                            }
                        }
                    }
                }).show();
    }

    private void pickFromGallery(){
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        someActivityResultLauncher.launch(intent);
        //(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){
        //intent to pick image from camera

        //using media store to pick high/original quality image
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        someActivityResultLauncher.launch(intent);
        //startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result; //returns true or false
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }


    //handle permission results
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults){
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        //both permission granted
                        pickFromCamera();
                    }
                    else{
                        //both or one permissions denied
                        Toast.makeText(this, "Camera & Storage permissions are required...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        //permission granted
                        pickFromGallery();
                    }else{
                        //permission denied
                        Toast.makeText(this, "Storage permissions is required...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if (result.getResultCode() == IMAGE_PICK_GALLERY_CODE){
                            //image picked from gallery

                            //save picked image uri
                            image_uri = data.getData();

                            //set image
                            productIconIv.setImageURI(image_uri);
                        }
                        else if (result.getResultCode() == IMAGE_PICK_CAMERA_CODE){
                            //image picked from camera

                            productIconIv.setImageURI(image_uri);
                        }

                    }
                }
            });
//    //handle image pick results
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
//        if (resultCode == RESULT_OK){
//
//            if (requestCode == IMAGE_PICK_GALLERY_CODE){
//                //image picked from gallery
//
//                //save picked image uri
//                image_uri = data.getData();
//
//                //set image
//                productIconIv.setImageURI(image_uri);
//            }
//            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
//                //image picked from camera
//
//                productIconIv.setImageURI(image_uri);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}