package com.uniquestudio.technorange;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    ImageView productImgV;
    EditText productTitleEdt, productPriceEdt, productAvailQuantityEdt, productDescriptEdt;
    Button productAddBtn;

    private ProgressDialog progressDialog;

    private String categotyName, productTitle, productPrice, productAvailQuantity, productDescript, currentDate, currentTime;
    private String productIdentifierKey, downloadImageURL;
    private  static final int galleryPick = 1;
    private Uri imageUri;
    private StorageReference ProductImageRef;  // to create file folder in Firebase Storage in order to store image files
    private DatabaseReference ProductRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        categotyName = getIntent().getExtras().get("category").toString();
        ProductImageRef = FirebaseStorage.getInstance("gs://technorange-3b319.appspot.com").getReference().child("ProductImages");
        ProductRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Products");

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        productImgV = findViewById(R.id.product_imgv);

        productTitleEdt = findViewById(R.id.product_title_edt);
        productPriceEdt = findViewById(R.id.product_price_edt);
        productAvailQuantityEdt = findViewById(R.id.product_avail_quantity_edt);
        productDescriptEdt = findViewById(R.id.product_descript_edt);

        productAddBtn = findViewById(R.id.product_add_btn);
        progressDialog = new ProgressDialog(this);
                
        productImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

       productAddBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               addProduct();
           }
       });

    }

    private void addProduct() {
        productTitle = productTitleEdt.getText().toString();
        productPrice = productPriceEdt.getText().toString();
        productDescript = productDescriptEdt.getText().toString();
        //productAvailQuantity = Integer.parseInt(productAvailQuantityEdt.getText().toString());
        // throws java.lang.NumberFormatException Error & crash the app since null inputs cannot parse to int
        productAvailQuantity = productAvailQuantityEdt.getText().toString();

        // hide keyboard
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(productDescriptEdt.getWindowToken(), 0);

        if(imageUri == null) {
            Toast.makeText(this, "Product image is Required", Toast.LENGTH_LONG).show();
        }
        else if(productTitle.isEmpty()) {
            productTitleEdt.setError("Product Title is Required");
            showErrorToastMsg();
        }
        else if(productPrice.isEmpty()) {
            productPriceEdt.setError("Product Price is Required");
            showErrorToastMsg();
        }
        else if(productDescript.isEmpty()) {
            productDescriptEdt.setError("Product Description is Required");
            showErrorToastMsg();
        }
        else if(productAvailQuantityEdt.length() == 0) {
            productAvailQuantityEdt.setError("Available Quantity is Required");
            showErrorToastMsg();
        }
        else {
            progressDialog.setTitle("Adding product is in progress");
            progressDialog.setMessage("Please wait, until complete the process");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            uploadProductImage();
        }
    }

    private void uploadProductImage() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        currentDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
        currentTime = timeFormat.format(calendar.getTime());

        // instead of generating firebase random key for each product, here trying to create unique identifierKey using currentTime and currentTime
        productIdentifierKey = currentDate + currentTime;

        StorageReference filePath = ProductImageRef.child(imageUri.getLastPathSegment() + productIdentifierKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error = e.toString();
                Toast.makeText(AddProductActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddProductActivity.this, "Product image uploaded successfully", Toast.LENGTH_SHORT).show();

                // get link of the image form the Firebase Storage and store it in Firebase Database, in order to display customers
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            downloadImageURL = task.getResult().toString();
                            // just to verifying..not necessary the following toast message
                            //Toast.makeText(AddProductActivity.this, "Product image URL get successfully", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDb();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDb() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productIdentifierKey);
        productMap.put("date", currentDate);
        productMap.put("time", currentTime);
        productMap.put("ptitle", productTitle);
        productMap.put("image", downloadImageURL);
        productMap.put("price", productPrice);
        productMap.put("avail_quantity", productAvailQuantity);
        productMap.put("description", productDescript);

        ProductRef.child(productIdentifierKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(AddProductActivity.this, SellerActivity.class);
                            startActivity(intent);

                            progressDialog.dismiss();
                            Toast.makeText(AddProductActivity.this, "Product has added successfully :)", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressDialog.dismiss();
                            String error = task.getException().toString();
                            Toast.makeText(AddProductActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }

    private void showErrorToastMsg() {
        Toast.makeText(getApplicationContext(), "Required Fields cannot be Empty. Need relevant value",Toast.LENGTH_LONG).show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            productImgV.setImageURI(imageUri);
        }
    }
}