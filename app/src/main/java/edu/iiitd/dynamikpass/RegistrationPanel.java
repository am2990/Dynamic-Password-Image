/* ** displays selected images from grid view: change position and color of the image** */

/**
 *
 */
package edu.iiitd.dynamikpass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import edu.iiitd.dynamikpass.model.Image;
import edu.iiitd.dynamikpass.utils.DatabaseHelper;
import edu.iiitd.dynamikpass.utils.Pair;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

/**
 * @author impaler
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class RegistrationPanel extends SurfaceView implements
		SurfaceHolder.Callback {



	private static final String TAG = RegistrationPanel.class.getSimpleName();
	private GestureDetectorCompat mDetector;
	static MainThread thread;
	static Image image;
	static ArrayList<Image> imglist = new ArrayList<Image>();
	ActionMode mActionMode;
	private SurfaceView surfaceView;
	private Bitmap mBackgroundImage;
	private static Context mContext;
	static Image sr;
	static Image sg;
	static Image sy;
	static Image sb;

	private int callBackCode = 0;

	/**
	 * Current width of the surface/canvas.
	 *
	 * @see #setSurfaceSize
	 */
	private Callback mActionModeCallback, mSubmitCallBack;




	public RegistrationPanel(Context context, Callback mActionModeCallback, Callback mSubmitCallBack, int backgroundImage, ArrayList<String> images) {
		super(context);
		mContext = context;
		surfaceView = this;
		this.mActionModeCallback = mActionModeCallback;
		this.mSubmitCallBack = mSubmitCallBack;
		// adding the callback (this) to the surface holder to intercept events
		//System.out.println("chcekuser: "+ checkuser);
		System.out.println("RP : "+images.get(0));
		getHolder().addCallback(this);

		DatabaseHelper db = new DatabaseHelper(mContext);

		int image_id = 0;
		for(String s: images){


			//int ranx = ran.nextInt(height) + 1;
			//int rany= ran.nextInt(width) + 1;


			String image_name = s;
			Log.d(TAG, "image: "+image_name);
			HashMap<String,Pair<Bitmap, Integer>> bitmap1 = new HashMap<>();
			bitmap1.put("BLUE",new Pair(BitmapFactory.decodeResource(getResources(), db.getBlueImage(image_name)), db.getBlueImage(image_name)));
			bitmap1.put("YELLOW",new Pair(BitmapFactory.decodeResource(getResources(), db.getYellowImage(image_name)), db.getYellowImage(image_name)));
			bitmap1.put("GREEN",new Pair(BitmapFactory.decodeResource(getResources(), db.getGreenImage(image_name)), db.getGreenImage(image_name)));
			bitmap1.put("RED",new Pair(BitmapFactory.decodeResource(getResources(), db.getRedImage(image_name)), db.getRedImage(image_name)));

			image = new Image(bitmap1,db.getBlueImage(image_name), image_name, 150 ,150 ,"BLUE",getResources());

			imglist.add(image);

		}

		Random ran = new Random();
		DisplayMetrics dm= new DisplayMetrics();
		//ran.setSeed((long)i);
		Image image1 = imglist.get(0);
		Image checkpos,checkposother;
		Iterator iter = imglist.iterator();
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels-50;
		int height =dm.heightPixels-50;
		for(Image img: imglist){

			//int randomNumx = ran.nextInt((height)-img.getBitmap().getHeight()) + 1;
			//int randomNumy= ran.nextInt((width)-img.getBitmap().getWidth()) + 1;
			int randomNumx = ran.nextInt(width) + 1;
			int randomNumy = ran.nextInt(height) + 1;
			boolean overlapp = true;
			Image pos = null;
			// till the flag is true
			// generate random x and y
			while(overlapp) {
				randomNumx = (randomNumx-40) % width ;
				randomNumy = (randomNumy-40) % height;
				System.out.println("height: " + height + "randomNumx" + randomNumx);
				System.out.println("width: " + width + "randomNumy" + randomNumy);
				//int randomNumx = ran.nextInt((400 - 25) + 1) + 25;
				//int randomNumy = ran.nextInt((400 - 25) + 1) + 25;


				//check if the x and y lie inside any other image
				for(Image i: imglist) {
					pos = i.getRange(randomNumx, randomNumy);
					if(pos != null) {
						continue;
					}
				}
				if(pos == null){
					img.setX(randomNumx);
					img.setY(randomNumy);
					overlapp = false;
				}
				// if it lies inside any image set flag to true

				// else set the image with the new x and y
				// get upper limits from canvas
				//int randomNumx = ran.nextInt((350 - 25) + 1) + 25;
				//int randomNumy = ran.nextInt((350 - 25) + 1) + 25;

			}


		}


		//}

		System.out.println("image list: "+imglist.size());
		for(Image ii: imglist){
			System.out.println("xxx" + ii.getX());
			System.out.println("yyy" + ii.getY());
		}
		new BitmapFactory();



		mBackgroundImage = BitmapFactory.decodeResource(getResources(), backgroundImage);


		// create the game loop thread
		thread = new MainThread(getHolder(), this, mContext);

		mDetector = new GestureDetectorCompat(mContext, new MyGestureListener());
		mDetector.setIsLongpressEnabled(true);
		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		surfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// delegating event handling to the droid
					for (Image img : imglist) {
						img.handleActionDown((int) event.getX(), (int) event.getY());
					}
					// check if in the lower part of the screen we exit
					/*if (event.getY() > getHeight() - 50) {
						thread.setRunning(false);
						((Activity) getContext()).finish();
					} else {
						Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
					}*/
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					// the gestures
					for (Image img : imglist) {
						if (img.isTouched()) {
							img.setX((int) event.getX());
							img.setY((int) event.getY());
						}
					}
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// touch was released
					for (Image img : imglist) {
						if (img.isTouched()) {
							img.setTouched(false);
						}
					}
				}

				mDetector.onTouchEvent(event);
				return true;
			}


		});

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
	}

	public void setSurfaceSize() {
		// synchronized to make sure these all change atomically
		synchronized (getHolder()) {
			int mCanvasWidth = getWidth();
			mBackgroundImage = Bitmap.createScaledBitmap(
					mBackgroundImage, getWidth(), getHeight(), true);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the registration loop
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}




	@Override
	protected void onDraw(Canvas canvas) {

		mBackgroundImage = Bitmap.createScaledBitmap(
				mBackgroundImage, getWidth(), getHeight(), true);

		canvas.drawBitmap(mBackgroundImage, 0, 0, null);

		for(Image img : imglist){
			img.draw(canvas);
		}

	}

	private void showSystemUI() {
		RegistrationActivity.mDecorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}


	class MyGestureListener extends SimpleOnGestureListener {
		private static final String DEBUG_TAG = "Gestures";

		public boolean onTouchEvent(MotionEvent event) {
			Log.d(DEBUG_TAG,"onTe: ");
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent m){
			Log.d(DEBUG_TAG, "onTapup Event: ");
//			showSystemUI();
			mActionMode = ((Activity)mContext).startActionMode(mSubmitCallBack);
			return true;
		}


		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			ArrayList<Image> imagelist = new ArrayList<Image>();
			for(Image img: imglist){
				imagelist.add(img);


				Image droidz = null;

				Image droidz1 = img.getRange(arg0.getX(), arg0.getY());



				System.out.println("on Long Press");
				Log.d("hello","onLP: ");

				if (mActionMode != null) {
					Log.v(TAG, "mActionMode is not null");
					//                return;
				}


				if((droidz1 != null)){
					mActionMode = ((Activity)mContext).startActionMode(mActionModeCallback);
					surfaceView.setSelected(true);
					System.out.println("on Long Press");
					return;
				}
			}



		}

	}



	public static Image SelectRed() {


		for(Image img: imglist){

			if(img.isLongTouched()){

				img.setColor("RED");
				Toast.makeText(mContext,"Selected Red",
						Toast.LENGTH_SHORT).show();
				img.setLongPressed(false);
				sr = img;
				return sr;

			}
		}


		return null;
	}
	public static Image SelectBlue() {

		for(Image img: imglist){
			if(img.isLongTouched()){
				img.setColor("BLUE");
				Toast.makeText(mContext,"Selected Blue",
						Toast.LENGTH_SHORT).show();
				img.setLongPressed(false);
				sb = img;

				return sb;
			}
		}

		return sb;
	}
	public static Image SelectGreen() {
		// TODO Auto-generated method stub

		for(Image img: imglist){
			if(img.isLongTouched()){
				img.setColor("GREEN");
				Toast.makeText(mContext,"Selected Green",
						Toast.LENGTH_SHORT).show();
				img.setLongPressed(false);
				sg = img;

				return sg;
			}
		}

		return sg;
	}
	public static Image SelectYellow() {
		// TODO Auto-generated method stub
		for(Image img: imglist){
			if(img.isLongTouched()){
				img.setColor("YELLOW");
				//Toast.makeText(getBaseContext(), "Selected Yellow ", Toast.LENGTH_LONG).show();
				Toast.makeText(mContext,"Selected Yellow",
						Toast.LENGTH_SHORT).show();
				img.setLongPressed(false);
				sy = img;
				//droid.setTouched(true);
				return sy;
			}
		}
		return sy;
	}

}

