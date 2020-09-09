package com.redbravo.inventorylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddProduct extends AppCompatActivity {
EditText pname,pprice,psku,pquantity;
ImageView uploadimg;
Button add, upload;
Bitmap bitmap;
    FirebaseAuth mAuth;
   StorageReference mRef;
    DatabaseReference database;
 private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        //initialize storage and database (Firebase)
        mAuth= FirebaseAuth.getInstance();
        mRef = FirebaseStorage.getInstance().getReference(mAuth.getCurrentUser().getUid());
        database = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());

        //find the views with input
        pname= findViewById(R.id.product_name);
        pprice = findViewById(R.id.price);
        psku = findViewById(R.id.SKU);
        pquantity = findViewById(R.id.quantity);
        add= findViewById(R.id.additem);
        upload= findViewById(R.id.uploadbtn);
        uploadimg = findViewById(R.id.imageupload);

        //image upload button
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 19 &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]
                                        {Manifest.permission.READ_EXTERNAL_STORAGE},
                                1000);
                    }
                } else {
                    getChosenImage();
                }
            }
        });

//add item button code
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                uploadfile();
            Intent intent = new Intent(AddProduct.this, MainActivity.class);

            startActivity(intent);
            }
        });
    }
    //method for uploading image from the gallery
    private void getChosenImage() {

        Toast.makeText(getApplicationContext(), "Choose A Picture", Toast.LENGTH_SHORT).show();
        Uri imageUri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,0);
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000){
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                getChosenImage();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000){

            if (resultCode == Activity.RESULT_OK) {

                //Do Something with your chosen image
                try{
                   selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getApplication().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    bitmap = BitmapFactory.decodeFile(picturePath);
                    uploadimg.setImageBitmap(bitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        } else {
            Toast.makeText(getApplicationContext(), "Try Again!!", Toast.LENGTH_SHORT)
                    .show();
        }

    }
public String uriEXTENSION(Uri uri){
    ContentResolver cR= getContentResolver();
    MimeTypeMap mime = MimeTypeMap.getSingleton();
    return  mime.getExtensionFromMimeType(cR.getType(uri));
}
public void uploadfile(){
    final String prname = pname.getText().toString();
    final int sku =  Integer.parseInt(psku.getText().toString());
    final int quantity =  Integer.parseInt(pquantity.getText().toString());
  final  int price =  Integer.parseInt(pprice.getText().toString());
if(selectedImage!=null){
    //Set image name in the db and storage as time in millisec so as not to override previous
final StorageReference imageRef=mRef.child(System.currentTimeMillis()+ "."+ uriEXTENSION(selectedImage));
imageRef.putFile(selectedImage)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
    //Use constructor to push all data into firebase
        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                      String photouri = uri.toString();
                        uploadClass upload = new uploadClass(prname,sku,quantity,photouri ,price);
                        String uploadID = database.push().getKey();
                        database.child(uploadID).setValue(upload);
                    }
                });

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Toast.makeText(getApplicationContext(), "Error: " +e.getMessage(), Toast.LENGTH_SHORT);
            }
        });
}

}
}