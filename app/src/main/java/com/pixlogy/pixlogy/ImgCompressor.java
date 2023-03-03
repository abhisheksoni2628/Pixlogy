package com.pixlogy.pixlogy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pixlogy.pixlogy.databinding.ActivityImgCompressorBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import id.zelory.compressor.Compressor;

public class ImgCompressor extends AppCompatActivity {

//    File originalImg, compressedImg;

    public static final int RESULT_IMAGE = 1;

    private StorageVolume storageVolume;

    Bitmap originalImage;

    int quality;

    byte[] bytesArray;

    ProgressDialog save;
//    private static String filePath;
//    File path = new File(System.getenv("EXTERNAL_STORAGE") + "/myCompressor");

    private ActivityImgCompressorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImgCompressorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        askPermissions();

//        filePath = path.getAbsolutePath();
//
//        if(!path.exists()){
//            path.mkdir();
//        }


        binding.btnPickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        binding.seekQuality.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.txtQuality.setText("Quality: " + i);
                seekBar.setMax(100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        binding.btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quality = binding.seekQuality.getProgress();
//                int width = Integer.parseInt(binding.etWidth.getText().toString());
//                int height = Integer.parseInt(binding.etHeight.getText().toString());
//
//                try {
//                    compressedImg = new Compressor(ImgCompressor.this)
//                            .setMaxWidth(width)
//                            .setMaxHeight(height)
//                            .setQuality(quality)
//                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                            .setDestinationDirectoryPath(filePath)
//                            .compressToFile(originalImg);
//                    File finalFile = new File(filePath, originalImg.getName());
//                    Bitmap finalBitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
//                    binding.imgCompressed.setImageBitmap(finalBitmap);
//                    binding.txtCompressed.setText("Size: " + Formatter.formatShortFileSize(ImgCompressor.this, finalFile.length()));
//                    Toast.makeText(ImgCompressor.this, "Image Compressed and Saved!", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(ImgCompressor.this, "Error While Compressing!", Toast.LENGTH_SHORT).show();
//                }
//                ---------------------

//                final Bitmap imgToSave = originalImage;
//                Thread PerformEncoding = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        saveToInternalStorage(imgToSave);
//                    }
//                });
//                save = new ProgressDialog(ImgCompressor.this);
//                save.setMessage("Saving, Please Wait...");
//                save.setTitle("Saving Image");
//                save.setIndeterminate(false);
//                save.setCancelable(false);
//                save.show();
//                PerformEncoding.start();

                StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    storageVolume = storageManager.getStorageVolumes().get(0); // internal storage
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                originalImage.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
                bytesArray = byteArrayOutputStream.toByteArray();
                Bitmap bitmapCompressedImage = BitmapFactory.decodeByteArray(bytesArray, 0, bytesArray.length);
                binding.imgCompressed.setImageBitmap(bitmapCompressedImage);

            }
        });

        binding.saveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.imgCompressed != null){
                    save = new ProgressDialog(ImgCompressor.this);
                    save.setMessage("Saving, Please Wait...");
                    save.setTitle("Saving Image");
                    save.setIndeterminate(false);
                    save.setCancelable(false);
                    save.show();

                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            save.cancel();
                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 3000);

                    String Name = binding.etFileName.getText().toString();
                    File fileOutput = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        fileOutput = new File(storageVolume.getDirectory().getPath() + "/Download/" + Name + ".jpeg");
                    }
                    try {

                        FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
                        fileOutputStream.write(bytesArray);
                        fileOutputStream.close();
                    }catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }



                }else {
                    Toast.makeText(getApplicationContext(), "Compressed Image not found", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

//    private void saveToInternalStorage(Bitmap imgToSave) {
//        OutputStream fOut;
//        File file = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM + "/Pixlogy"), "Compressed" + ".PNG"); // the File to save ,
//        try {
//            fOut = new FileOutputStream(file);
//            imgToSave.compress(Bitmap.CompressFormat.PNG, quality , fOut); // saving the Bitmap to a file
//            fOut.flush(); // Not really required
//            fOut.close(); // do not forget to close the stream
//            save.dismiss();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, RESULT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_IMAGE && resultCode == RESULT_OK){
            binding.btnCompress.setVisibility(View.VISIBLE);
            final Uri imageUri = data.getData();
//
//            try {
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                binding.imgOriginal.setImageBitmap(selectedImage);
//                originalImg = new File(imageUri.getPath().replace("raw/", ""));
//                binding.txtOriginal.setText("Size: " + Formatter.formatShortFileSize(this, originalImg.length()));
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//            }
            try {
                originalImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                binding.imgOriginal.setImageBitmap(originalImage);
                File oriImageSize = new File(imageUri.getPath());
//                binding.txtOriginal.setText("Size: " + Formatter.formatShortFileSize(this, oriImageSize.length()));
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TAG", "Error : " + e);
            }

        }
        else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void askPermissions() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();

                    }
                }).check();

    }
}
