package edu.iiitd.dynamikpass;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class FeedbackActivity extends Activity {

    private static final String TAG = FeedbackActivity.class.getSimpleName();

    public long endLoginTime;

    private Button btnSubmit;
    private RatingBar ratingBar;
    private Spinner spnMemoryBurden;
    private Spinner spnUnderstand;
    private Spinner spnRemember;

    boolean submitPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnSubmit = (Button) findViewById(R.id.btn_submit_feedback);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        spnMemoryBurden = (Spinner) findViewById(R.id.spn_memory_burden);
        spnUnderstand = (Spinner) findViewById(R.id.spn_understand);
        spnRemember = (Spinner) findViewById(R.id.spn_remember);

        final String userName = getIntent().getStringExtra("USERNAME");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int rating = (int) ratingBar.getRating();
                String memoryBurden = spnMemoryBurden.getSelectedItem().toString();
                String understand = spnUnderstand.getSelectedItem().toString();
                String remember = spnRemember.getSelectedItem().toString();

                Log.v(TAG,"rating: "+rating+" memoryBurden: "+memoryBurden+
                        " understand: "+understand+" remember: "+remember);

                CSVeditor.shared().insertFeedback(rating, memoryBurden, understand, remember);

                scheduleNotification(getNotification("Its time to login using "+userName), 10000);

                submitPressed = true;

                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {

        if(submitPressed) {
            finish();
            Intent intent = new Intent(FeedbackActivity.this, UsernameActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(FeedbackActivity.this, "Please press submit", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification getNotification(String content) {

        Intent myIntent = new Intent(getApplicationContext(), UsernameActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                myIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Numerical password login");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.icon);

        return builder.build();
    }

}
