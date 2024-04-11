package com.example.sc4031_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.camera2.CameraCaptureSession;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CameraHandler cameraHandler;
    private TextureView textureView;
    private ImageButton flipCameraBtn;
    private Button startPreviewBtn;
    private TextView predictionValue;

    private int cameraMode = 0;     // 0: Back camera (default), 1: Front camera

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textureView = findViewById(R.id.cameraPreview);
        assert textureView != null;
        startPreviewBtn = findViewById(R.id.startPreviewBtn);
        predictionValue = findViewById(R.id.predictionValue);
//        flipCameraBtn = findViewById(R.id.flipCameraBtn);

        // Set Camera Preview State
//        final CustomSurfaceListener surfaceListener = new CustomSurfaceListener(cameraHandler, textureView);
        cameraHandler = new CameraHandler(this, getApplicationContext(), textureView);
        textureView.setSurfaceTextureListener(new CustomSurfaceListener(cameraHandler, textureView, getApplicationContext()));

        /*
        // Flip Camera
        this.flipCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the camera first
                cameraHandler.closeCamera();

                // Toggle camera mode
                cameraMode = (cameraMode == 0) ? 1 : 0;
                surfaceListener.switchCameraMode(cameraMode);
                textureView.setSurfaceTextureListener(surfaceListener);
            }
        });

         */

        // Start/Stop client to server
        this.startPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle run scanning status
                StateSingleton.getInstance().runScanning = !StateSingleton.getInstance().runScanning;

                Toast.makeText(MainActivity.this, "Scanning Preview: " + StateSingleton.getInstance().runScanning, Toast.LENGTH_SHORT).show();

                // Update Button Text
                startPreviewBtn.setText(startPreviewBtn.getText().equals("Start") ? "Stop" : "Start");
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("changed"));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String prediction = intent.getStringExtra("prediction");
            predictionValue.setText(prediction);
        }
    };
}