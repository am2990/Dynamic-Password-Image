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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import edu.iiitd.dynamikpass.helper.CSVeditor;
import edu.iiitd.dynamikpass.helper.NotificationPublisher;

public class FeedbackActivity extends Activity {

    private Button btnSubmit;
    private RatingBar rbEaseToRemember;
    private RatingBar rbEaseOfRegistration;
    private RatingBar rbEaseOfLogin;
    private RatingBar rbIntuitivity;
    private EditText etFeedback;
    private RatingBar rbOverall;

    boolean submitPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnSubmit = (Button) findViewById(R.id.btn_submit_feedback);
        rbEaseToRemember = (RatingBar) findViewById(R.id.rb_ease_to_remember);
        rbEaseOfRegistration = (RatingBar) findViewById(R.id.rb_ease_of_registration);
        rbEaseOfLogin = (RatingBar) findViewById(R.id.rb_ease_of_login);
        rbIntuitivity = (RatingBar) findViewById(R.id.rb_intuitivity);
        etFeedback = (EditText) findViewById(R.id.et_feedback);
        rbOverall = (RatingBar) findViewById(R.id.rb_overall);

        final String userName = getIntent().getStringExtra("USERNAME");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CSVeditor.shared().insertFeedback(rbEaseToRemember.getNumStars(), rbEaseOfRegistration.getNumStars(), rbEaseOfLogin.getNumStars(), rbIntuitivity.getNumStars(), etFeedback.getText().toString(), rbOverall.getNumStars());
                CSVeditor.shared().recordTimeStamp(InstructionsActivity.endTime, 18);

                scheduleNotification(getNotification("Its time to login using "+userName), 7*AlarmManager.INTERVAL_DAY);

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
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            Toast.makeText(FeedbackActivity.this, "Please press submit", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleNotification(Notification notification, long delay) {

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
        builder.setContentTitle("Image password login");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        return builder.build();
    }

}
