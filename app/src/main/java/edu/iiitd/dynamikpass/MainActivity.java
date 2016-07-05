/* ** activity to select objects from grid view ** */

package edu.iiitd.dynamikpass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import edu.iiitd.dynamikpass.helper.CSVeditor;
import edu.iiitd.dynamikpass.model.User;
import edu.iiitd.dynamikpass.utils.DatabaseHelper;

public class MainActivity extends Activity {
	GridView gridView;
	int imageBack;
	private boolean mChecked;
	Intent iuser,icheckuser;
	String checkuser;
	User user;
	ArrayList<String> images;

	String[] objects = new String[] {
			//"Droid",
			//"Square",
			//"Smiley",
			//"Football",
			"Fish",
			"Crab",
			"Jelly-Fish",
			"Sea-Horse",
			"Star-Fish"
	};

	// Array of integers points to images stored in /res/drawable-ldpi/
	int[] android_resid = new int[]{
			//R.drawable.droid_1,
			//R.drawable.triangle_blue,
			//R.drawable.smiley_b,
			//R.drawable.football_b,
			R.drawable.fishb,
			R.drawable.crabb,
			R.drawable.jellyb,
			R.drawable.seahorseb,
			R.drawable.starb
	};

	private Button btnNext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnNext = (Button) findViewById(R.id.btn_next);

		images = new ArrayList();
		//images.removeAll(images);
		Intent ii = getIntent();
		imageBack = ii.getIntExtra("ib",0);
		System.out.println("image: "+ imageBack);
		iuser=getIntent();
		icheckuser = getIntent();
		checkuser = icheckuser.getStringExtra("checkuser");
//		user = ii.getParcelableExtra("usern");
		user = (User) ii.getSerializableExtra("usern");


		DatabaseHelper db = new DatabaseHelper(this);
	//	db.createImage("Droid", R.drawable.droid_1, R.drawable.droid_4, R.drawable.droid_3, R.drawable.droid_2);
	//	db.createImage("Square", R.drawable.triangle_blue,R.drawable.triangle_green,R.drawable.triangle_red, R.drawable.triangle_yellow);
	//	db.createImage("Smiley", R.drawable.smiley_b, R.drawable.smiley_g, R.drawable.smiley_r, R.drawable.smiley_y);
	//	db.createImage("Football", R.drawable.football_b, R.drawable.football_g, R.drawable.football_r, R.drawable.football_y);
		db.createImage("Fish", R.drawable.fishb, R.drawable.fishg, R.drawable.fishr, R.drawable.fishy);
		db.createImage("Crab", R.drawable.crabb, R.drawable.crabg, R.drawable.crabr, R.drawable.craby);
		db.createImage("Jelly-Fish", R.drawable.jellyb, R.drawable.jellyg, R.drawable.jellyr, R.drawable.jellyy);
		db.createImage("Sea-Horse", R.drawable.seahorseb, R.drawable.seahorseg, R.drawable.seahorser, R.drawable.seahorsey);
		db.createImage("Star-Fish", R.drawable.starb, R.drawable.starg, R.drawable.starr, R.drawable.stary);
		db.closeDb();


		List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<objects.length;i++){
			HashMap<String, String> hm = new HashMap<String,String>();
			hm.put("txt", objects[i]);
			hm.put("flag", Integer.toString(android_resid[i]) );
			aList.add(hm);

		}


		// Keys used in Hashmap
		String[] from = { "flag","txt"};
		int[] to = { R.id.flag,R.id.txt};

		// Instantiating an adapter to store each items
		// R.layout.listview_layout defines the layout of each item
		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.gridview_layout, from, to);
		// Getting a reference to gridview of MainActivity
		gridView = (GridView) findViewById(R.id.gridview);
		// Setting an adapter containing images to the gridview
		gridView.setAdapter(adapter);
		gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
		gridView.setMultiChoiceModeListener(new MultiChoiceModeListener());

		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (images.size() > 0) {
						Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);

						intent.putExtra("ib", imageBack);
						intent.putExtra("imageobjs", images);
						intent.putExtra("usern", user);
						intent.putExtra("checkuser", checkuser);

						long spentTime = Calendar.getInstance().getTimeInMillis() - startTime;
						CSVeditor.shared().recordTimeStamp(spentTime, 7);

						startActivity(intent);

					} else {
						Toast.makeText(getBaseContext(), "Select an image ", Toast.LENGTH_LONG).show();
					}

				}
				catch(Exception e){
					Toast.makeText(getBaseContext(), "Select an image ", Toast.LENGTH_LONG).show();
				}
				images = new ArrayList();
			}
		});


	}

	public View getView(int position, View convertView, ViewGroup parent) {

		CheckableLayout l;
		ImageView i;
		//l = new CheckableLayout(MainActivity.this);
		if (convertView == null) {
			i = new ImageView(MainActivity.this);
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
			l = new CheckableLayout(MainActivity.this);
			l.setLayoutParams(new GridView.LayoutParams(
					GridView.LayoutParams.WRAP_CONTENT,
					GridView.LayoutParams.WRAP_CONTENT));
			l.addView(i);
		} else {
			l = (CheckableLayout) convertView;
			i = (ImageView) l.getChildAt(0);
		}


		return l;
	}

	public class CheckableLayout extends FrameLayout implements Checkable {


		public CheckableLayout(Context context) {
			super(context);
			//gridView.setBackgroundColor(R.drawable.red);
		}

		@SuppressWarnings("deprecation")
		public void setChecked(boolean checked) {

			mChecked = checked;
			setBackgroundDrawable(checked ? getResources().getDrawable(
					R.drawable.blue) : null);
			//gridView.setBackgroundColor(R.drawable.red);
		}

		public boolean isChecked() {
			return mChecked;
		}

		public void toggle() {
			setChecked(!mChecked);
		}

	}

	public class MultiChoiceModeListener implements
			GridView.MultiChoiceModeListener {
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.setTitle("Select Items");
			mode.setSubtitle("One item selected");

			return true;
		}

		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			images = new ArrayList();
			return true;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			System.out.println("tick");
			return true;
		}

		public void onDestroyActionMode(ActionMode mode) {
		}

		public void onItemCheckedStateChanged(ActionMode mode, int position,
											  long id, boolean checked){

			HashMap<String,String> obj = (HashMap<String,String>)gridView.getItemAtPosition(position);

			int selectCount = gridView.getCheckedItemCount();
			if(checked ) {
				if(!(images.contains(obj.get("txt")))) {
					if (!(images.contains(obj.get("txt")))) {
						System.out.println("in mChecked");
						images.add(obj.get("txt"));
					} else {
						Toast.makeText(getBaseContext(), "Already selected ", Toast.LENGTH_LONG).show();
					}
				}

			}
			else if (!checked) {
				images.remove(obj.get("txt"));
			}
			switch (selectCount) {
				case 1:
					mode.setSubtitle("One item selected");

					break;
				default:
					mode.setSubtitle("" + selectCount + " items selected");
					break;
			}
		}

	}

	private long startTime;
	@Override
	protected void onResume() {
		super.onResume();
		startTime = Calendar.getInstance().getTimeInMillis();
	}
}
