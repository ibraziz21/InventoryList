package com.redbravo.inventorylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddProduct extends AppCompatActivity {
EditText pname,pprice,psku,pquantity;
Button add;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        pname= findViewById(R.id.product_name);
        pprice = findViewById(R.id.price);
        psku = findViewById(R.id.SKU);
        pquantity = findViewById(R.id.quantity);
        add= findViewById(R.id.additem);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = pname.getText().toString();
                int sku =  Integer.parseInt(psku.getText().toString());
                int quantity =  Integer.parseInt(pquantity.getText().toString());
                int price =  Integer.parseInt(pprice.getText().toString());
                database.child("Product").child("ProductName").setValue(name);
                database.child("Product").child("Quantity").setValue(quantity);
                database.child("Product").child("SKU").setValue(sku);
                database.child("Product").child("Price").setValue(price);

            Intent intent = new Intent(AddProduct.this, MainActivity.class);

            startActivity(intent);
            }
        });
    }
}