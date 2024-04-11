package com.example.sc4031_project;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.view.TextureView;

public class CustomSurfaceListener implements TextureView.SurfaceTextureListener {

    protected CameraHandler cameraHandler;
    protected TextureView textureView;
    protected int cameraMode;

    protected SocketStream socketStream;
    protected boolean wait = false;
    protected int interval = 1000;

    public CustomSurfaceListener(CameraHandler cameraHandler, TextureView textureView, Context context) {
        this.cameraHandler = cameraHandler;
        this.textureView = textureView;
        this.socketStream = new SocketStream(context);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        this.cameraHandler.openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        StateSingleton stateSingleton = StateSingleton.getInstance();

        // If run scanning is enabled and
        if(!stateSingleton.waitInterval && stateSingleton.runScanning) {
            stateSingleton.waitInterval = true;

            Thread socketThread = new Thread(new SocketThread(this.textureView, this.socketStream));
            socketThread.start();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    StateSingleton.getInstance().waitInterval = false;
                }
            }, interval);
        }
    }
}
