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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Notification extends IntentService {

    private static final String KEY_CODE = "countryCode";
    private static final String KEY_CHECKED = "isCheked";
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
        String countryCode = preferences.getString(KEY_CODE, "en");

        // FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // If user is logged in
        if (auth.getCurrentUser() != null) {

            // Set language
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            Configuration configuration = getResources().getConfiguration();
            configuration.locale = new Locale(countryCode);
            getResources().updateConfiguration(configuration, metrics);

            // Get user id
            final String userID = auth.getCurrentUser().getUid();

            // Get database references
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference refRequest = database.getReference("Requests");

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

            requestRecieved(refRequest, userID);
            jumpApproved(refRequest, userID);

        }
    }

    // Send notification when a user get asked to sign a jump
    private void requestRecieved(DatabaseReference reference, final String userId) {

        // Get resources
        final String title = this.getResources().getString(R.string.newRequest);
        final String content = this.getResources().getString(R.string.approveThis);

        // TODO: actually send user to the jump
        final Intent intent = new Intent(this, EvaluateRequestActivity.class);

        request = null;

        reference.orderByChild("signer").equalTo(userId).addChildEventListener(new ChildEventListener() {

            // If an request with signer 'userId' is added
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                request = dataSnapshot.getValue(Request.class);

                if(request != null && notificationOn){
                    String contentText = request.getUserName() + " " + content + " " + request.getJumpNr();
                    sendNotification(title, contentText, intent);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Send notification when a users jump is approved
    private void jumpApproved(final DatabaseReference reference, String userId){

        // Get resources
        final String title = this.getResources().getString(R.string.jumpApproved);
        final String title2 = this.getResources().getString(R.string.hasApproved);
        final String content = this.getResources().getString(R.string.contentApproved);

        final Intent intent = new Intent(this, MainActivity.class);

        request = null;
        reference.orderByChild("userID").equalTo(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            // If an request with userId 'userId' is deleted, it means it has been approved
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                request = dataSnapshot.getValue(Request.class);
                if(request != null && notificationOn){
                    String fullTitle = title + " " + request.getJumpNr() + " " + title2;

                    sendNotification(fullTitle, content, intent);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void sendNotification(String title, String content, Intent intent) {

        // Activate the intent(?)
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

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
