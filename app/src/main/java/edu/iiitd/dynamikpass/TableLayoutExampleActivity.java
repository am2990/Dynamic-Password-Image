/* **Table layout for selecting the gestures ** */
package edu.iiitd.dynamikpass;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import edu.iiitd.dynamikpass.model.User;
import edu.iiitd.dynamikpass.utils.Constants;
import edu.iiitd.dynamikpass.utils.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

public class TableLayoutExampleActivity extends Activity implements OnItemSelectedListener, OnClickListener {
	/**
	 * Called when the activity is first created.
	 */

	private Spinner spinner1, spinner2, spinner3;
	private String item1, item2, item3;
	String g1, g2, g3;
	Button submit;

	Intent i;
	User user;
	Intent iuser, icheckuser;
	String checkuser;
	ArrayList<Image> imagelist = new ArrayList<Image>();
	ArrayList<Image> images = new ArrayList<Image>();
	DatabaseHelper db = new DatabaseHelper(this);
	ArrayList<String> gestures;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_1);
		i = getIntent();

		gestures = new ArrayList<>();
		iuser = getIntent();
		icheckuser = getIntent();
		//iimagelist = getIntent();

		//imagelist = (ArrayList<Image>) iimagelist.getSerializableExtra("imglist");

		/*iimagelist = getIntent();
		imagelist = iimagelist.getParcelableArrayListExtra("imglist");*/
		Bundle bundleObject = getIntent().getExtras();

		// Get ArrayList Bundle
		imagelist = (ArrayList<Image>) bundleObject.getSerializable("imglist");
		checkuser = icheckuser.getStringExtra(Constants.ISUSER);
//		user = iuser.getParcelableExtra("usern");
		user = (User) iuser.getSerializableExtra(Constants.USER);


		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner3 = (Spinner) findViewById(R.id.spinner3);
		submit = (Button) findViewById(R.id.submit);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.planets_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner1.setAdapter(adapter);
		spinner2.setAdapter(adapter);
		spinner3.setAdapter(adapter);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner3 = (Spinner) findViewById(R.id.spinner3);
		spinner1.setOnItemSelectedListener(this);
		spinner2.setOnItemSelectedListener(this);
		spinner3.setOnItemSelectedListener(this);
		submit.setOnClickListener(this);

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				item1 = parent.getItemAtPosition(position).toString();
				System.out.println("object1: " + item1);

				db.saveMap(1, 1, item1);
				gestures.add(0,item1);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				item2 = parent.getItemAtPosition(position).toString();
				System.out.println("object2: " + item2);
				db.saveMap(1, 0, item2);
				//g2=item2;
				gestures.add(1, item2);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				item3 = parent.getItemAtPosition(position).toString();
				System.out.println("object3: " + item3);
				db.saveMap(0, 1, item3);
				gestures.add(2, item3);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}


	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
							   long arg3) {


	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onClick(View arg0) {

		Intent intent = null;
		//System.out.println("list= " + list);
		if (checkuser.equalsIgnoreCase("false")) {

			user.setGestarr(gestures);

			long timeSpent = Calendar.getInstance().getTimeInMillis() - startTime;
			CSVeditor.shared().recordTimeStamp(timeSpent, 9);

			intent = new Intent(getApplicationContext(), LoginActivity.class);
			intent.putExtra("SIGN_UP", true);
		}else {
			intent = new Intent(getApplicationContext(), UsernameActivity.class);
		}
		intent.putExtra(Constants.USER, user);
		startActivity(intent);
	}


	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"TableLayoutExample Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://edu.iiitd.dynamikpass/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"TableLayoutExample Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://edu.iiitd.dynamikpass/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}

	@Override
	public void onBackPressed()
	{
		Intent i = new Intent(this, MainActivity.class);
		i.putExtra(Constants.USER,user);
		i.putExtra(Constants.ISUSER, checkuser);
		startActivity(i);
		super.onBackPressed();  // optional depending on your needs
	}

	private long startTime;
	@Override
	protected void onResume() {
		super.onResume();
		startTime = Calendar.getInstance().getTimeInMillis();
	}
}
