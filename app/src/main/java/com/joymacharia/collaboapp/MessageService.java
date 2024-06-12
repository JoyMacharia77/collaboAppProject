package com.joymacharia.collaboapp;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MessageService extends IntentService {
    //Declare a constant KEY to pass a message from the Main Activity to the Service
    public static final String EXTRA_MESSAGE = "MESSAGE";
    //Create Handler so as to post on the Main Thread
    //To create a handler on the main thread we need to create a handler object
    //in a method that runs on the main thread
    //notification Id identifies the notification
    public static final int NOTIFICATION_ID =1;
    public static final String MY_CHANNEL_ID = "1";
    private NotificationManager myNotificationManager;

    public MessageService() {
        super("MessageService");
        //required
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        synchronized (this)
        { //This method allows us to lock a particular block of code
            //from access by other threads
            try{
                //wait for 0.1 second
                wait(100);
            }catch (InterruptedException error) {
                error.printStackTrace();
            }
        }
        //get the text from the intent
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        //call showText method
        // showText(text);
        createNotificationChannel();
        deliverNotification(getApplicationContext());
    }

    private void createNotificationChannel()
    {
        //Create a notification Manager Object
        myNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //add a Check on SDK version since Notification Channels are available in OREO and higher
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O)
        {
            //Create the Notification Channel with all the parameters
            NotificationChannel notificationChannel = new NotificationChannel
                    (MY_CHANNEL_ID,"Message Notification",  NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("New Message");
            myNotificationManager.createNotificationChannel(notificationChannel);

        }
    }

    private void deliverNotification(Context context)
    {
        Intent  deliverIntent = new Intent(context, MessageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, deliverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MY_CHANNEL_ID)
                //this displays a small notification icon i.e ic_joke_round
                .setSmallIcon(R.mipmap.ic_msg_icon)
                //set title as your application name
                //.setContentTitle(getString(R.string.app_name))
                .setContentTitle("New Message")
                //set the content text
                // .setContentText("You have a New Message from ".concat(getString(R.string.name)))
                .setContentText("You Have Received a New Message")
                //set the notification to disappear when clicked
                .setAutoCancel(true)
                //give it a maximum priority to allow peeking
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //set it to vibrate to get a large heads-up notification
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                //Open main activity on clicking the notification
                .setContentIntent(pendingIntent);

        myNotificationManager.notify(NOTIFICATION_ID, builder.build());

    }

}
