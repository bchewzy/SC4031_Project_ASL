package com.example.sc4031_project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketStream {

    private Context context;
    private Socket mSocket;
//    private String url = "http://192.168.50.5:5000";
    private String url = "http://172.20.10.9:5000";

    SocketStream(Context context) {
        this.context = context;

        try {

            mSocket = IO.socket(url);
            mSocket.on("prediction", onNewPrediction);
            mSocket.connect();

            Log.e("SOCKETSTREAM", "SocketIO Connected to server: " + url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SOCKETSTREAM", "Error connecting to server: " + url);
        }
    }

    public void attemptSend(byte[] msg) {
        try {
            mSocket.emit("transfer", msg);
//            mSocket.emit("transfer", "hello");
            Log.e(StateSingleton.getInstance().TAG, "SocketStream: Message sent to server");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(StateSingleton.getInstance().TAG, "SocketStream: Error sending message to server");
        }
    }

    public Emitter.Listener onNewPrediction = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(args.length > 0) {
                final String prediction = args[0].toString();
                Log.e("SOCKETSTREAM", "Received prediction from server: " + prediction);

                Intent intent = new Intent();
                intent.setAction("changed");
                intent.putExtra("prediction", prediction);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                // Handle the prediction response

                /*
                JSONObject predictionData = (JSONObject) args[0];

                try {
                    String prediction = predictionData.getString("prediction");
                    // Handle the prediction received from the server
                    Log.e("SOCKETSTREAM", "Received prediction from server: " + prediction);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("SOCKETSTREAM", "Error parsing prediction data");
                */
            }
        }
    };

}
