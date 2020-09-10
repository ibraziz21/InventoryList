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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ItemAdapter.onItemClickListener {
    FirebaseAuth mAuth;
    TextView text;
    private FirebaseStorage mStorage;
    DatabaseReference database;
    RecyclerView mRecyclerView;
    ItemAdapter itemAdapter;
    private List<uploadClass> muploadClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mStorage=FirebaseStorage.getInstance();
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
        itemAdapter=new ItemAdapter(getApplicationContext(), muploadClass);
        mRecyclerView.setAdapter(itemAdapter);
        itemAdapter.setOnItemClickListener(MainActivity.this);
        database.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
        muploadClass.clear();
        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
            uploadClass upload = dataSnapshot.getValue(uploadClass.class);
            upload.setMkey(dataSnapshot.getKey());
            muploadClass.add(upload);
        }


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


    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal CLick", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStockAdjust(int position) {
        Toast.makeText(this, "Adjust CLick", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteClick(int position) {
    uploadClass selected = muploadClass.get(position);
    final String skey = selected.getMkey();
        StorageReference imgref = mStorage.getReferenceFromUrl(selected.getImageuri());
        imgref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                database.child(skey).removeValue();
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }
}