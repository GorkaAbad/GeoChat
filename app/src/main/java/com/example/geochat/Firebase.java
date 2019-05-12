package com.example.geochat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Firebase extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "1";

    private static final String TAG = "firebase";
    final String direccion = "";
 //   Database database = new Database(direccion, this, null);
    private String token = "";

    public Firebase() {
        try {
            getCurrentToken();

        } catch (Exception e) {

        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);

        this.token = token;
        //Si tiene un token nuevo tiene que crear otro usuario, para ese movil.
        //Intent intent = new Intent(this, SignUpActivity.class);
       // startActivity(intent);

        Log.d("TOKEN",s);
    }
    public String getToken() {
        return this.token;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        Log.i("Hi",remoteMessage.getFrom());

        Log.d("", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("", "Message data payload: " + remoteMessage.getData());
            showNotification(remoteMessage.getData().toString());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            showNotification(remoteMessage.getNotification().getBody());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void showNotification(String body) {
        //Llama a la actividad principal enviandole el mensaje recibido
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", body);

        startActivity(intent);
    }


    /**
     * Recupera el token para el usuario actual
     */
    private void getCurrentToken() throws InterruptedException {


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token

                        String _token = task.getResult().getToken();
                        token = _token;
                        Log.i("a", "Token: " + _token);
                    }
                });


    }
}
