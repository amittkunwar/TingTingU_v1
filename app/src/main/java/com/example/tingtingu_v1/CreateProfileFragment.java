package com.example.tingtingu_v1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateProfileFragment extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;
    private ImageView profileImageView;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_profile, container, false);

        profileImageView = view.findViewById(R.id.profileImage);
        Button uploadImageButton = view.findViewById(R.id.uploadButton);

        // Load saved image URI if available
        loadImageFromPreferences();

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });

        return view;
    }

    private void requestPermissions() {
        // Check for camera and storage permissions
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Request both camera and storage permissions
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_REQUEST_CODE);
        } else {
            // Permissions are granted, show options to choose from camera or gallery
            showImagePickerOptions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImagePickerOptions();
            } else {
                // Handle permission denial
                Toast.makeText(getActivity(), "Permissions are required to proceed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImagePickerOptions() {
        // Open gallery to choose an image
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);  // Show selected image

            // Save the image to storage
            saveImageToStorage(imageUri);

            // Save the image URI for later use
            saveImageUriToPreferences(imageUri);
        }
    }

    private void saveImageToStorage(Uri imageUri) {
        try {
            // Get the Bitmap from the URI
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

            // Create a file in the external storage directory
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String fileName = "profile_image_" + System.currentTimeMillis() + ".jpg";
            File file = new File(storageDir, fileName);

            // Save the bitmap to the file
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Notify the gallery about the new image
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);

            Toast.makeText(getActivity(), "Image saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageUriToPreferences(Uri uri) {
        SharedPreferences preferences = getActivity().getSharedPreferences("profile_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("imageUri", uri.toString());
        editor.apply();
    }

    private void loadImageFromPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("profile_prefs", Activity.MODE_PRIVATE);
        String uriString = preferences.getString("imageUri", null);
        if (uriString != null) {
            imageUri = Uri.parse(uriString);
            profileImageView.setImageURI(imageUri);
        }
    }
}
