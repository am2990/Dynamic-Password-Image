/* **activity for registration** */

package edu.iiitd.dynamikpass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
	/** Called when the activity is first created. */
	private Context mContext;

	private static final String TAG = RegistrationActivity.class.getSimpleName();

	static ArrayList<String> images = new ArrayList<String>();
	Intent i;
	static int imageBack;

	public ActionMode mActionMode;
	public static View mDecorView;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		i = getIntent();
		imageBack = i.getIntExtra("ib",0);
		System.out.println("ib: : "+ Integer.toString(imageBack));
		images = i.getStringArrayListExtra("imageobjs");
		System.out.println("images size: "+images.size());
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set our MainGamePanel as the View
		final RegistrationPanel rp = new RegistrationPanel(mContext, mActionModeCallback, mSubmitCallback,  imageBack,images);
		setContentView(rp);

		Log.d(TAG, "View added");

		mDecorView = getWindow().getDecorView();
		mDecorView.setOnSystemUiVisibilityChangeListener
				(new View.OnSystemUiVisibilityChangeListener() {
					@Override
					public void onSystemUiVisibilityChange(int visibility) {
						// Note that system bars will only be "visible" if none of the
						// LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
						if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
							// TODO: The system bars are visible. Make any desired
							// adjustments to your UI, such as showing the action bar or
							// other navigational controls.
							Log.d(TAG, "s1");
						} else {
							// TODO: The system bars are NOT visible. Make any desired
							// adjustments to your UI, such as hiding the action bar or
							// other navigational controls.
							Log.d(TAG, "s2");
						}
					}
				});

		hideSystemUI();
	}


	private void hideSystemUI() {
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hide and show.
		mDecorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		Log.d(TAG, "stuff hidden");

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

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.main, menu);

			return true;
		}


		// Called each time the action mode is shown. Always called after onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			System.out.println("get Item: " + item.getItemId());
			//if((RegistrationPanel.image.isLongTouched() == true)||(RegistrationPanel.tri_b.isLongTouched() == true)){
			switch (item.getItemId()) {

				case R.id.blue:
					Toast.makeText(getBaseContext(), "Selected Blue ", Toast.LENGTH_LONG).show();
					RegistrationPanel.SelectBlue();
					mode.finish();
					return true;
				case R.id.red:
					Toast.makeText(getBaseContext(), "Selected Red ", Toast.LENGTH_LONG).show();
					RegistrationPanel.SelectRed();
					mode.finish();
					return true;
				case R.id.green:
					Toast.makeText(getBaseContext(), "Selected Green ", Toast.LENGTH_LONG).show();
					RegistrationPanel.SelectGreen();
					mode.finish();
					return true;

				case R.id.yellow:
					Toast.makeText(getBaseContext(), "Selected Yellow ", Toast.LENGTH_LONG).show();
					RegistrationPanel.SelectYellow();
					mode.finish();
					return true;

				default:
					return false;
			}

		}

		@Override
		public void onDestroyActionMode(ActionMode actionMode) {

		}
	};


	private ActionMode.Callback mSubmitCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.submit_menu, menu);

			return true;
		}

		// Called each time the action mode is shown. Always called after onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			System.out.println("get Item: "+item.getItemId());
			//if((RegistrationPanel.image.isLongTouched() == true)||(RegistrationPanel.tri_b.isLongTouched() == true)){
			switch (item.getItemId()) {

				case R.id.submit:
					Toast.makeText(getBaseContext(), "Submit Selected ", Toast.LENGTH_LONG).show();
					MainThread.setRunning(false);
					System.out.println("do submit");
					RegistrationPanel.thread.doSubmit();
					return true;

				default:

					return false;
			}


		}


		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			hideSystemUI();
			mActionMode = null;
		}
	};


	@Override
	public void onBackPressed() {
		System.out.println("back press");
		Log.d("CDA", "onBackPressed Called");
		RegistrationPanel.thread.setRunning(false);
		(this).finish();
	}
}