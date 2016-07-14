package edu.iiitd.dynamikpass;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.iiitd.dynamikpass.helper.CSVeditor;
import edu.iiitd.dynamikpass.model.User;
import edu.iiitd.dynamikpass.utils.DatabaseHelper;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class UsernameActivity extends Activity {

    private static final String TAG = UsernameActivity.class.getSimpleName();

    EditText username;
    Button bcontinue;

    User user;
    DatabaseHelper db;

    String checkuser;
    public static Resources res = null;

    // variables for screen recording and media projection
    private static final int REQUEST_CODE = 1000;
    private static final int DISPLAY_WIDTH = 720;
    private static final int DISPLAY_HEIGHT = 1280;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    private int screenDensity;
    MediaRecorder mediaRecorder;
    static MediaProjection mediaProjection;
    MediaProjectionManager mediaProjectionManager;
    static MediaProjectionCallback mediaProjectionCallback;
    static VirtualDisplay virtualDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username_layout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

        Log.v(TAG,"Density Dpi: "+displayMetrics.densityDpi);
        Log.v(TAG,"Scale Density: "+displayMetrics.scaledDensity);

        if(displayMetrics.densityDpi > 480) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(R.string.dialog_title);
            dialogBuilder.setMessage(R.string.dialog_message);
            dialogBuilder.setNegativeButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialogBuilder.show();
        }
        else {
            onCreateStart();
        }
    }

    public void onCreateStart() {
        CSVeditor.shared().init(getApplicationContext());

        init();

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");

        username = (EditText) findViewById(R.id.username);
        bcontinue = (Button) findViewById(R.id.bcontinue);
        username.requestFocus();

        res = getResources();

        db = new DatabaseHelper(this);

        bcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String u = username.getText().toString();
                System.out.println("u: " + u);
                if (!(u.equals(""))){

                    User user = db.getUserByName(u);
                    if (user == null) {

                        String timeStamp = simpleDateFormat.format(new Date());

                        String location = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/UserStudyFramework/"+
                                u+ "_"+timeStamp+
                                "_img_sign_up.mp4";

                        initRecorder(location);

                        Toast.makeText(UsernameActivity.this, "started", Toast.LENGTH_SHORT).show();
                        shareScreen();

                        checkuser = "false";
                        user = new User();
                        Intent intent = new Intent();
                        intent.setClass(UsernameActivity.this, GalleryView.class);
                        user.setUsername(u);
                        intent.putExtra("usern", user);
                        intent.putExtra("checkuser", checkuser);

                        long timeSpent = Calendar.getInstance().getTimeInMillis() - startTime;
                        CSVeditor.shared().insertNewUser(u, u+ "_"+timeStamp+ "_img_sign_up.mp4", timeSpent);

                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Login!", Toast.LENGTH_SHORT).show();
                        checkuser = "true";

                        String timeStamp = simpleDateFormat.format(new Date());

                        String location = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/UserStudyFramework/"+
                                u+ "_"+timeStamp+
                                "_img_sign_in.mp4";

                        initRecorder(location);

                        shareScreen();

                        Intent intent = new Intent();
                        intent.setClass(UsernameActivity.this, LoginActivity.class);
                        intent.putExtra("usern", user);
                        intent.putExtra("checkuser", checkuser);

                        long timeSpent = Calendar.getInstance().getTimeInMillis() - startTime;
                        CSVeditor.shared().insertSignInLog(u, u+ "_"+timeStamp+ "_img_sign_in.mp4" , timeSpent);

                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter a username!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_instructions:
                Intent intent = new Intent(UsernameActivity.this, InstructionsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

    public void init() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenDensity = metrics.densityDpi;

        mediaRecorder = new MediaRecorder();
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        if(mediaProjection == null) {
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        }
    }

    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.v(TAG,"Recording Stopped");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) {
            Log.e(TAG, "Unknown request code: " + requestCode);
            finish();
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                    "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mediaProjectionCallback = new MediaProjectionCallback();
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        mediaProjection.registerCallback(mediaProjectionCallback, null);
    }

    private void shareScreen() {
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();
    }

    private VirtualDisplay createVirtualDisplay() {
        return mediaProjection.createVirtualDisplay("UsernameActivity",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(), null /*Callbacks*/, null
                /*Handler*/);
    }

    private void initRecorder(String location) {

        try {
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

            mediaRecorder.setOutputFile(location);
            mediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mediaRecorder.setVideoEncodingBitRate(512 * 1000);
            mediaRecorder.setVideoFrameRate(30);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATIONS.get(rotation + 90);
            mediaRecorder.setOrientationHint(orientation);
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopScreenSharing() {

        Log.v(TAG,"Recording Stopped");

        if(virtualDisplay == null) {
            return;
        }
        virtualDisplay.release();
        destroyMediaProjection();
    }

    private static void destroyMediaProjection() {
        if(mediaProjection != null) {
            mediaProjection.unregisterCallback(mediaProjectionCallback);
            mediaProjection.stop();
            mediaProjection = null;
        }
        Log.v(TAG,"MediaProjection Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScreenSharing();
    }

    public static long startTime;
    @Override
    protected void onResume() {
        super.onResume();
        startTime = Calendar.getInstance().getTimeInMillis();
    }
}
