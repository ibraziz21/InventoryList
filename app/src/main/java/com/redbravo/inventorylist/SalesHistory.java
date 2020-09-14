package com.redbravo.inventorylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SalesHistory extends AppCompatActivity {
RecyclerView recyclerView;
FirebaseAuth mAuth;
DatabaseReference mdb;
SalesAdapter adapter;
private List<Sales> salesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);
    recyclerView = findViewById(R.id.salesRecycler);
    mAuth = FirebaseAuth.getInstance();
    mdb = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("Sales");
    recyclerView.setLayoutManager( new LinearLayoutManager(getApplicationContext()));
    salesList = new ArrayList<>();
    adapter= new SalesAdapter(getApplicationContext(),salesList);

    mdb.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Sales upload = dataSnapshot.getValue(Sales.class);
                if (upload != null) {
                    upload.setMkey(dataSnapshot.getKey());
                }
                salesList.add(upload);
            }
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("Not Showing", error.getMessage());
        }
    });
    }
}