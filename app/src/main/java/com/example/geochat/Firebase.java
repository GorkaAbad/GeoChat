package com.example.geochat;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
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
    Database database = new Database(direccion, this, null);
    private String token = "";


    public Firebase() {
        try {
            getCurrentToken();

        } catch (Exception e) {

        }

    }


    public String getToken() {
        return this.token;
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);

        this.token = token;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...




        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //  scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());




                // TODO(developer): Handle FCM messages here.
                // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
                Log.d(TAG, "From: " + remoteMessage.getFrom());
            }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
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

    /**
     * Crea el builder para una notificacion
     *
     * @param texto
     * @return
     */
    private NotificationCompat.Builder createNotificationBuilder(String texto) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText(texto)
                .setAutoCancel(true);

        return builder;
    }


}

