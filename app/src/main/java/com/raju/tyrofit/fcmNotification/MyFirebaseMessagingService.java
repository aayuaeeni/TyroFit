package com.raju.tyrofit.fcmNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.raju.tyrofit.R;
import com.raju.tyrofit.article.ArticleActivity;
import com.raju.tyrofit.utils.AppConstats;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import static com.raju.tyrofit.utils.AppConstats.editor;

/**
 * Created by Raju.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "imageUrl";
    private static final String ADMIN_CHANNEL_ID = "admin_channel";
    private NotificationManager notificationManager;
    private JSONObject json;


    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
        editor.putString(AppConstats.FIREBASE_TOKEN, s);
        editor.apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
    {
        try
        {
            String notificationTitle = remoteMessage.getNotification().getTitle();
            String notificationBody = remoteMessage.getNotification().getBody();

            buildNotification(notificationTitle,notificationBody,remoteMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void buildNotification(String notificationTitle,String notificationMsg,RemoteMessage remoteMessage) throws Exception
    {

        Intent intent = new Intent(this, ArticleActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Build notification
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            setupChannels();
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationMsg)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        notificationManager.notify((int) Calendar.getInstance(Locale.ENGLISH).getTimeInMillis(),
                notificationBuilder.build());
    }

    public Bitmap getBitmapFromUrl(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels()
    {
        CharSequence adminChannelName = "Tyrofit";
        String adminChannelDescription = "Tyrofit App";
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName,
                NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(R.color.colorAccent);
        adminChannel.enableVibration(true);
        if (notificationManager != null)
        {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
