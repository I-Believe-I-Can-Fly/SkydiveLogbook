package ibelieveicanfly.skydivelogbook;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Notification extends IntentService {

    private static final String KEY = "Notification";
    private static final String KEY_COUNT = "RequestCount";
    private static final String KEY_CHECKED = "isCheked";
    private static int oldCount = 0;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Request request;
    private boolean notificationOn;

    public Notification() {
        super("Notification");
    }

    @Override
    public void onHandleIntent(@Nullable Intent intent) {

        // SharedPreferences
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        notificationOn = preferences.getBoolean(KEY_CHECKED, true);
        oldCount = preferences.getInt(KEY_COUNT, 0);
        String countryCode = preferences.getString("countryCode", "en");

        // FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // If user is logged in
        if (auth.getCurrentUser() != null) {

            // Set language
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            Configuration configuration = getResources().getConfiguration();
            configuration.locale = new Locale(countryCode);
            getResources().updateConfiguration(configuration, metrics);

            // Get resources
            final String title = this.getResources().getString(R.string.newRequest);
            final String content = this.getResources().getString(R.string.approveThis);

            // Get user id
            final String userID = auth.getCurrentUser().getUid();

            // Get database references
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference refRequest = database.getReference("Requests");
            // final DatabaseReference refLogs = database.getReference("Logs");

            this.notificationManager = (NotificationManager) this.getSystemService(Service.NOTIFICATION_SERVICE);

            // Create the NotificationChannel only on API 26+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        this.getString(R.string.app_name), "MessagesChannel", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("Notification");

                // Register channel with the system
                notificationManager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(this, channel.getId());
            } else {
                builder = new NotificationCompat.Builder(this);
            }

            // Check if there is a new request
            refRequest.orderByChild("signer").equalTo(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int currentRequests = 0;
                    request = null;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        currentRequests++;
                        if (currentRequests > oldCount) {
                            request = ds.getValue(Request.class);
                        }
                    }

                    // If request is null, then there aren't any new requests AND user wants notifications
                    if (request != null && notificationOn) {

                        // Send notification if it's not from current user
                        if (!request.getUserID().equals(userID)) {

                            // Set content text
                            String contentText = request.getUserName() + " " + content + " " + request.getJumpNr();

                            // Send notification
                            sendNotification(title, contentText);
                        }

                        // Save count of requests
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(KEY, currentRequests);
                        editor.apply();

                    } else {
                        // If count is less or user don't want notifications: save new number
                        currentRequests = oldCount;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(KEY, currentRequests);
                        editor.apply();

                    }

                    oldCount = currentRequests;
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Don't do a damn shit
                }
            });
        }
    }

    private void sendNotification(String title, String content) {

        // Get intent
        Intent mainIntent = new Intent(this, EvaluateRequestActivity.class);
        // Activate the intent(?)
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);


        // Set icon
        this.builder.setSmallIcon(R.drawable.ic_notifications_white_48dp);
        // Set title
        this.builder.setContentTitle(title);
        // Set content
        this.builder.setContentText(content);
        // Set priority
        this.builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        // Set intent
        this.builder.setContentIntent(pendingIntent);
        // Close the notification when clicked on
        this.builder.setAutoCancel(true);
        // And finally, build the notification
        this.notificationManager.notify(0, builder.build());

    }
}
