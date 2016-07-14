/* ** activity to Log in: goto Login Panel** */
package edu.iiitd.dynamikpass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

import edu.iiitd.dynamikpass.model.User;
import edu.iiitd.dynamikpass.utils.Constants;

public class LoginActivity extends Activity {
	/** Called when the activity is first created. */

	private static final String TAG = LoginActivity.class.getSimpleName();
	public static Object ax;
	public static Object ay;
	Intent i;
	static User user;

	ArrayList<String> images = new ArrayList<>();

	static boolean signUp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		i = getIntent();

		user = (User) i.getSerializableExtra(Constants.USER);
		signUp = i.getBooleanExtra("SIGN_UP", false);
		Log.v(TAG,"signUp: "+signUp);
		int imageBack = user.getImageback();
		System.out.println("ib lp: "+imageBack);

		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set our MainGamePanel as the View
		setContentView(new LoginPanel(this,imageBack));
		Log.d(TAG, "View added");

	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopping...");
		super.onStop();
	}

	@Override
	public void onBackPressed() {

		LoginPanel.thread.setRunning(false);

		if(signUp) {
			Intent intent = new Intent(getApplicationContext(), TableLayoutExampleActivity.class);
			intent.putExtra(Constants.USER, user);
			String checkuser = "false";
			intent.putExtra(Constants.ISUSER, checkuser);
			startActivity(intent);
		}
		else {
			Toast.makeText(LoginActivity.this, "Please finish the signin process", Toast.LENGTH_SHORT).show();
		}
	}

}
