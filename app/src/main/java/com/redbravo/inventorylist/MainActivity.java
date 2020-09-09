package com.redbravo.inventorylist;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView text;
    DatabaseReference database;
    RecyclerView mRecyclerView;
    ItemAdapter itemAdapter;
    private List<uploadClass> muploadClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
database = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());

    if(mAuth.getCurrentUser()!=null){
    Log.i("User In","A user is Logged in");

            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                    Log.d("a", "Value is: " + value);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });}else {
        Log.i("No User", "No User Logged In");
        Intent intent = new Intent(getApplicationContext(),loginactivity.class);
        startActivity(intent);
    }

    mRecyclerView = findViewById(R.id.recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    muploadClass = new ArrayList<>();
    database.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
            uploadClass upload = dataSnapshot.getValue(uploadClass.class);
            muploadClass.add(upload);
        }
        itemAdapter=new ItemAdapter(getApplicationContext(), muploadClass);
       mRecyclerView.setAdapter(itemAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT);
        }
    });

    }



    public void newProduct(View view){
        Intent intent= new Intent(this,AddProduct.class);
        startActivity(intent);
    }
    public void StockProduct(View view){
        Intent intent=new Intent(this,loginactivity.class);
        startActivity(intent);
    }


}