package com.example.gas4u;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import com.example.gas4u.databinding.ActivityD3Binding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class d3 extends DrawerAdminActivity{

    RecyclerView recyclerView;
    ArrayList<User> userArrayList;
    UserAdapter myAdapter;
    FirebaseFirestore db;

    ActivityD3Binding activityD3Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d3);
        activityD3Binding = ActivityD3Binding.inflate(getLayoutInflater());
        setContentView(activityD3Binding.getRoot());
        allocateActivityTitle("Customer");

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<User>();
        myAdapter = new UserAdapter(d3.this,userArrayList);

        recyclerView.setAdapter(myAdapter);
        
        EventChangeListener();
        
    }

    private void EventChangeListener() {

        db.collection("Customer").orderBy("fullName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                userArrayList.add(dc.getDocument().toObject(User.class));
                            }
                            myAdapter.notifyDataSetChanged();

                        }

                    }
                });
    }
}