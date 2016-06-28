package edu.iiitd.dynamikpass;

import android.app.Activity;
import android.os.Bundle;

import java.util.Calendar;

public class InstructionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
    }

    long startTime;
    @Override
    protected void onResume() {
        super.onResume();
        startTime = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long endTime = Calendar.getInstance().getTimeInMillis() - startTime;
        CSVeditor.shared().recordTimeStamp(endTime, 16);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
