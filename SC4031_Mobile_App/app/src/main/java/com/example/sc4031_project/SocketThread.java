package com.example.sc4031_project;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.TextureView;

import java.io.ByteArrayOutputStream;

public class SocketThread implements Runnable {

    private TextureView textureView;
    private SocketStream socketStream;

    public SocketThread(TextureView textureView, SocketStream socketStream) {
        this.textureView = textureView;
        this.socketStream = socketStream;
    }

    @Override
    public void run() {
        try {
            Bitmap bmp = textureView.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] byteArray = stream.toByteArray();
            bmp.recycle();

            this.socketStream.attemptSend(byteArray);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(StateSingleton.getInstance().TAG, "SocketThread runs on an error!");
        }
    }
}
