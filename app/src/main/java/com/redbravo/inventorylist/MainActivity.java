package com.redbravo.inventorylist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    boolean internet_connection() {
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mStorage=FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        if(internet_connection()==true) {
            if (mAuth.getCurrentUser() != null) {
                Log.i("User In", "A user is Logged in");
              database=FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
                database.addListenerForSingleValueEvent(new ValueEventListener() {
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

                });
            } else {
                Log.i("No User", "No User Logged In");
                Intent intent = new Intent(getApplicationContext(), loginactivity.class);
                startActivity(intent);

            }

            mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            muploadClass = new ArrayList<>();
            itemAdapter = new ItemAdapter(getApplicationContext(), muploadClass);


            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    database.keepSynced(true);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        uploadClass upload = dataSnapshot.getValue(uploadClass.class);
                        upload.setMkey(dataSnapshot.getKey());
                        muploadClass.add(upload);
                    }
                    mRecyclerView.setAdapter(itemAdapter);
                    itemAdapter.setOnItemClickListener(MainActivity.this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT);
                }
            });

        }else {
            //create a snackbar telling the user there is no internet connection and issuing a chance to reconnect
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle("Application Error");
            builder.setMessage("Please connect to the internet");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(refresh);
                    // Passing each menu ID as a set of Ids because each
                    // menu should be considered as top level destinations.

                }

            }).show();
        }

    }



    public void newProduct(View view){
        Intent intent= new Intent(this,AddProduct.class);
        startActivity(intent);
    }



    @Override
    public void onItemClick(int position) {


    }

    @Override
    public void onStockAdjust(int position) {
        uploadClass selectedItem = muploadClass.get(position);
        Intent intent = new Intent(MainActivity.this, itemsale.class);
        intent.putExtra("Key",selectedItem.getMkey());
        startActivity(intent);

    }

    @Override
    public void onDeleteClick(int position) {
        //get position of the recyclerview, find corresponding key and use it to delete a record.
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(),loginactivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
