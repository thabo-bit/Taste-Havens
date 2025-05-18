package com.example.tastehavens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Upload_Menu extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView upload;
    private Uri filepath;

    private EditText nameEditText, priceEditText, descriptionEditText;
    private Spinner categorySpinner;
    private Button uploadButton;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_menu);

        // Initialize views
        upload = findViewById(R.id.Upload_Menu);
        nameEditText = findViewById(R.id.meal_name);
        priceEditText = findViewById(R.id.price);
        descriptionEditText = findViewById(R.id.Discription);
        categorySpinner = findViewById(R.id.category);
        uploadButton = findViewById(R.id.Add_Menu);

        // Firebase setup
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore = FirebaseFirestore.getInstance();

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Open image picker when clicking image
        upload.setOnClickListener(v -> openImagePicker());

        // Upload when clicking upload button
        uploadButton.setOnClickListener(v -> {
            if (filepath != null) {
                uploadImage();
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                upload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        StorageReference ref = storageReference.child("menuImages/" + UUID.randomUUID().toString());

        ref.putFile(filepath)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            saveMenuItem(imageUrl);
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(Upload_Menu.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveMenuItem(String imageUrl) {
        String name = nameEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        Map<String, Object> menuItem = new HashMap<>();
        menuItem.put("name", name);
        menuItem.put("price", price);
        menuItem.put("description", description);
        menuItem.put("category", category);
        menuItem.put("imageUrl", imageUrl);

        firestore.collection("Menu")
                .add(menuItem)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Menu Item Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to upload Menu Item", Toast.LENGTH_SHORT).show();
                });
    }
}
