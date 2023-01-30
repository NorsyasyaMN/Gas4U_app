package com.example.gas4u;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gas4u.databinding.ActivityCustomerProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class CustomerProfileActivity extends DrawerBaseActivity {

    ActivityCustomerProfileBinding activityCustomerProfileBinding;
    ImageView userDp;
    TextView name, name2, address, email, phone;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCustomerProfileBinding = ActivityCustomerProfileBinding.inflate(getLayoutInflater());
        setContentView(activityCustomerProfileBinding.getRoot());
        allocateActivityTitle("Profile");

        btnUpdate = findViewById(R.id.updateBtn);
        userDp = findViewById(R.id.user_imageview);
        name = findViewById(R.id.name_textview);
        name2 = findViewById(R.id.name2_textview);
        address = findViewById(R.id.address_textview);
        email = findViewById(R.id.email_textview);
        phone = findViewById(R.id.phone_textview);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UpdateCustomerProfile.class));
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("Customers").document(currentId);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){

                    String nameResult = task.getResult().getString("userName");
                    String url = task.getResult().getString("profilePhoto");
                    String addressResult = task.getResult().getString("userAddress");
                    String emailResult = task.getResult().getString("userEmail");
                    String phoneResult = task.getResult().getString("userPhone");

                    name.setText(nameResult);
                    name2.setText(nameResult);
                    address.setText(addressResult);
                    email.setText(emailResult);
                    phone.setText(phoneResult);
                    try{
                        Picasso.get().load(url).placeholder(R.drawable.ic_baseline_person_24).into(userDp);
                    }
                    catch(Exception e){
                        userDp.setImageResource(R.drawable.ic_baseline_person_24);
                    }
                }
                else{
                    startActivity(new Intent(getApplicationContext(),UpdateCustomerProfile.class));
                    finish();
                }
            }
        });

    }
}