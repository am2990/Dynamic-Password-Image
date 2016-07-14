/* **activity for registration** */

package edu.iiitd.dynamikpass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import edu.iiitd.dynamikpass.helper.CSVeditor;
import edu.iiitd.dynamikpass.model.User;

public class RegistrationActivity extends Activity {
	/** Called when the activity is first created. */
	private Context mContext;

	private static final String TAG = RegistrationActivity.class.getSimpleName();

	static ArrayList<String> images = new ArrayList<String>();


	public ActionMode mActionMode;
	public static View mDecorView;
	static Intent i;
	static String checkuser;
	static User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		i = getIntent();

		checkuser = i.getStringExtra("checkuser");
//
		user = (User) i.getSerializableExtra("usern");
//
		int imageBack = user.getImageback();
		System.out.println("ib: : "+ Integer.toString(imageBack));
		images = i.getStringArrayListExtra("imageobjs");
		System.out.println("images size: "+images.size());
		// requesting to turn the title OFF
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set our MainGamePanel as the View
		final RegistrationPanel rp = new RegistrationPanel(mContext, mActionModeCallback, mSubmitCallback, imageBack, images);
		setContentView(rp);

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
			System.out.println("get Item: " + item.toString());
			RegistrationPanel.SelectColor(item.toString());
			mode.finish();
			return true;
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
					Toast.makeText(getBaseContext(), "Submit Selected ", Toast.LENGTH_SHORT).show();

					long spentTime = Calendar.getInstance().getTimeInMillis() - startTime;
					CSVeditor.shared().recordTimeStamp(spentTime, 8);

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
		RegistrationPanel.thread.setRunning(false);
		(this).finish();

	}

	private long startTime;
	public void onResume(){
		super.onResume();

		startTime = Calendar.getInstance().getTimeInMillis();

//		i = getIntent();
//		i.putExtra(Constants.USER, user);
//		i = new Intent(this, GalleryView.class);
//		startActivity(i);
	}
}