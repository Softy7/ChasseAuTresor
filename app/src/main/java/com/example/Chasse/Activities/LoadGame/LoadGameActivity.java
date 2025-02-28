package com.example.Chasse.Activities.LoadGame;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.Chasse.ApiService;
import android.Manifest;
import com.example.Chasse.Model.SocketManager;
import com.example.Chasse.Model.System.MainSystem;
import com.example.Chasse.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadGameActivity extends AppCompatActivity {

    private MainSystem mainSystem = new MainSystem();
    protected ImageButton back, ok;
    private EditText code;
    private ProcessCameraProvider cameraProvider;
    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    protected static final String IS_JOINING_ROOM = "isJoiningRoom";
    protected static final String CODE = "code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_game_layout);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        this.back = findViewById(R.id.back_load_game);
        this.back.setOnClickListener(v -> finish());
        SocketManager.destroyInstance();
        this.previewView = findViewById(R.id.previewView);

        this.code = findViewById(R.id.editTextText2);

        this.ok = findViewById(R.id.ok_load_game);
        cameraExecutor = Executors.newSingleThreadExecutor();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        }

        this.ok.setOnClickListener(v -> {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(new MainSystem().getAddressNodejsServor())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);
                try {
                    long codeText = Long.parseLong(this.code.getText().toString());
                    Call<Void> call = apiService.getRoomStatus(codeText);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                Intent intent = new Intent(LoadGameActivity.this, InviteFriendActivity.class);
                                intent.putExtra(IS_JOINING_ROOM, true);
                                Log.e("code", String.valueOf(codeText));
                                intent.putExtra(CODE, codeText);
                                startActivity(intent);
                                finish();
                            } else if (response.code() == 202){
                                Toast.makeText(LoadGameActivity.this, "Le groupe que vous avez rentré est complet", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoadGameActivity.this, "Le groupe n'existe pas", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable throwable) {
                            Toast.makeText(LoadGameActivity.this, "Erreur : " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (NumberFormatException e){
                    Toast.makeText(LoadGameActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (RuntimeException e) {
                Toast.makeText(LoadGameActivity.this, "Une erreur s'est produit, veillez réassayer", Toast.LENGTH_LONG).show();
                throw new RuntimeException(e);
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                preview.setSurfaceProvider(previewView.getSurfaceProvider());



                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, image -> scanQRCode(image));

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (Exception e) {
                Log.e("Camera", "Erreur : " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void scanQRCode(ImageProxy imageProxy) {
        @SuppressWarnings("UnsafeOptInUsageError")
        InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());

        BarcodeScanner scanner = BarcodeScanning.getClient();
        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        String qrCodeText = barcode.getRawValue();
                        if (qrCodeText != null) {
                            ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                            toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 150); // 150ms de bip
                            runOnUiThread(() -> code.setText(qrCodeText));
                            imageProxy.close();
                            return;
                        }
                    }
                    imageProxy.close();
                })
                .addOnFailureListener(e -> {
                    Log.e("QR Code", "Erreur de scan : " + e.getMessage());
                    imageProxy.close();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        }
    }
}
