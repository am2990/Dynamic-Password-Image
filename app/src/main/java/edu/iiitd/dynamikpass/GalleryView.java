/* ** activity for selecting background image ** */

package edu.iiitd.dynamikpass;


import edu.iiitd.dynamikpass.helper.CSVeditor;
import edu.iiitd.dynamikpass.model.User;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Calendar;

public class GalleryView extends Activity {
	Integer[] pics = { R.drawable.antartica1, R.drawable.antartica2,
			R.drawable.antartica3, R.drawable.antartica4,
			R.drawable.antartica5, R.drawable.antartica6,
			R.drawable.antartica7, R.drawable.antartica8,
			R.drawable.antartica9, R.drawable.antartica10 ,
			R.drawable.antartica3, R.drawable.antartica4,
			R.drawable.antartica5, R.drawable.antartica6,
			R.drawable.antartica7, R.drawable.antartica8,
			R.drawable.antartica9, R.drawable.antartica10,
			R.drawable.bkgsea, R.drawable.nemo3, R.drawable.nemo1};
	LinearLayout imageView;

	private Context ctx;
	int imageBackground;
	int selectedImage = 0;
	private static final int MENU_SETBACK = 0;
	Intent iuser,icheckuser;
	User user;
	String str_usern,checkuser;
	/** Called when the activity is first created. */

	private Button btnSetBackground;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_gal);

		ActionBar toolbar = getActionBar();
		assert toolbar != null;
		toolbar.setTitle(getString(R.string.title_select_background));

		btnSetBackground = (Button) findViewById(R.id.btn_set_background);

		Intent i = getIntent();
		icheckuser = getIntent();


		checkuser = icheckuser.getStringExtra("checkuser");
//		user = i.getParcelableExtra("usern");
		user = (User) i.getSerializableExtra("usern");

		try {

		} catch (Exception e) {
			e.getMessage();
		}
		Gallery ga = (Gallery) findViewById(R.id.Gallery01);
		ga.setAdapter(new ImageAdapter(this));

		imageView = (LinearLayout) findViewById(R.id.ImageView01);

		ga.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				try {
					imageView.removeAllViews();
				} catch (Exception e) {
					e.getMessage();
				}

				TouchImageView touchImageView = new TouchImageView(
						GalleryView.this);
				touchImageView.setImageResource(pics[arg2]);
				LayoutParams lp=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				imageView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
				touchImageView.setLayoutParams(lp);
				imageView.addView(touchImageView);
				selectedImage = pics[arg2];
			}

		});

		btnSetBackground.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				user.setImageback(selectedImage);
				System.out.println("ib: "+selectedImage);
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra("ib", selectedImage);
				intent.putExtra("usern",user);
				intent.putExtra("checkuser", checkuser);

				long spentTime = Calendar.getInstance().getTimeInMillis() - startTime;
				CSVeditor.shared().recordTimeStamp(spentTime, 6);

				startActivity(intent);
			}
		});


	}

	public class ImageAdapter extends BaseAdapter {

		public ImageAdapter(Context c) {
			ctx = c;
			TypedArray ta = obtainStyledAttributes(R.styleable.Gallery1);
			imageBackground = ta.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 1);
			ta.recycle();
		}

		@Override
		public int getCount() {

			return pics.length;
		}

		@Override
		public Object getItem(int arg0) {

			return arg0;
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}


		public View getView(int arg0, View arg1, ViewGroup arg2) {
			System.out.println("get View");
			ImageView iv = new ImageView(ctx);
			iv.setImageResource(pics[arg0]);
			iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			iv.setLayoutParams(new Gallery.LayoutParams(150, 120));
			iv.setBackgroundResource(imageBackground);

			System.out.println("iv: "+ iv);
			return iv;

		}
	}

	private long startTime;
	@Override
	protected void onResume() {
		super.onResume();
		startTime = Calendar.getInstance().getTimeInMillis();
	}
}