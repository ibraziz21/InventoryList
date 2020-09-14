package com.redbravo.inventorylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class itemsale extends AppCompatActivity {
DatabaseReference db, selldb;
FirebaseAuth mAuth;
    TextView itemname, itemprice, itemquant, itemsku;
    ImageView itemimage;
    EditText sell, additems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemsale);
        mAuth= FirebaseAuth.getInstance();

        sell = findViewById(R.id.sellit);
        itemname = findViewById(R.id.productnme);
        itemprice = findViewById(R.id.itemprice);
        itemquant = findViewById(R.id.itemstock);
    additems = findViewById(R.id.stockInc);
        itemimage = findViewById(R.id.imageview2);
       final String newKey = getIntent().getStringExtra("Key");
           Log.i("NewKey","key is" +newKey);

        db = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("items").child(newKey);
selldb = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("Sales");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                uploadClass upload = snapshot.getValue(uploadClass.class);
                upload.setMkey(newKey);
                    final int stock = upload.getQuantity();
             /*   String itemname = upload.getProductname();
                String itemprice = String.valueOf(upload.getPrice());
                String itemq = String.valueOf(upload.getQuantity());
                String itemsk = String.valueOf(upload.getSku());
              */
                itemname.setText(upload.getProductname());
                itemprice.setText(String.valueOf(upload.getPrice()));
                itemquant.setText(String.valueOf(upload.getQuantity()));

                Picasso.with(getApplicationContext())
                        .load(upload.getImageuri())
                        .fit()
                        .into(itemimage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sellItems(View view) {

        final String Name = itemname.getText().toString();
        final int Price  = Integer.parseInt(itemprice.getText().toString());

            final int newStock = Integer.parseInt(sell.getText().toString());
        final int total = Price*newStock;

            db.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                uploadClass uploads = snapshot.getValue(uploadClass.class);

                    int stock = uploads.getQuantity();
                    final int sold = stock-newStock;
                uploads.setQuantity(sold);

                    db.child("quantity").setValue(uploads.getQuantity()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Successfull. New Stock is: "+sold, Toast.LENGTH_SHORT).show();
                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                            Sales sales = new Sales(Name,Price,date,newStock,total);
                            String key = selldb.push().getKey();
                            selldb.child(key).setValue(sales);
                        }
                    });
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    public void addstock(View view) {
        final int newStock = Integer.parseInt(additems.getText().toString());

        db.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadClass uploads = snapshot.getValue(uploadClass.class);
                int stock = uploads.getQuantity();
                final int sold = stock+newStock;
                uploads.setQuantity(sold);

                db.child("quantity").setValue(uploads.getQuantity()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successfull. New Stock is: "+sold, Toast.LENGTH_SHORT).show();
                        Log.i("SOLD", "New Remaining: "+sold );
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}