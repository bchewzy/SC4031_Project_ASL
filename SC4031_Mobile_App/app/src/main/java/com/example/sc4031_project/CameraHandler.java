package com.example.sc4031_project;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;

public class CameraHandler {
    protected Context context;
    protected Activity activity;
    protected TextureView textureView;
    protected CameraDevice cameraDevice;
    protected CaptureRequest.Builder captureRequestBuilder;
    protected CameraCaptureSession cameraCaptureSessions;



    protected String[] permissionList = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    protected Size imgDimension;

    public CameraHandler(Activity activity, Context context, TextureView textureView) {
        this.activity = activity;
        this.context = context;
        this.textureView = textureView;
    }

//    public void openCamera(int cameraMode) {
    public void openCamera() {
        // Try to open camera
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            // Get Camera ID
            String cameraId = cameraManager.getCameraIdList()[0];

            // Configure Camera Stream
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);

            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;

            imgDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            // Prompt Permission to access camera
            if(ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.activity, permissionList, 200);
                return;
            }

            cameraManager.openCamera(cameraId, stateCallback, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void closeCamera() {
        cameraDevice.close();
        cameraDevice = null;
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;

            // Start Camera Preview
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;

            texture.setDefaultBufferSize(imgDimension.getWidth(), imgDimension.getHeight());

            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    // If camera was closed
                    if(cameraDevice == null)
                        return;

                    cameraCaptureSessions = cameraCaptureSession;
                    updateCameraPreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(activity, "Configuration changed", Toast.LENGTH_SHORT).show();
                }
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updateCameraPreview() {
        if(cameraDevice == null)
            Toast.makeText(activity, "Update preview ran with error", Toast.LENGTH_SHORT).show();

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
