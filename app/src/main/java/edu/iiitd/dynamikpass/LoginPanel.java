/* ** displays images with random position and color, apply gesture (fling, single tap or double tap)
 to get the password right ** */
/**
 *
 */
package edu.iiitd.dynamikpass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.iiitd.dynamikpass.model.Image;
import edu.iiitd.dynamikpass.utils.Constants;
import edu.iiitd.dynamikpass.utils.DatabaseHelper;
import edu.iiitd.dynamikpass.utils.Pair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

/**
 * @author impaler
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class LoginPanel extends SurfaceView implements OnGestureListener,
		OnDoubleTapListener, SurfaceHolder.Callback {


	private static final String TAG = LoginPanel.class.getSimpleName();

	static LoginThread thread;


	String getgest=null;

	ArrayList<Image> drawimg = new ArrayList<Image>();
	HashMap<Image, Integer> ls = new HashMap<Image, Integer>();
	List<String> gestures = null;

	private Bitmap mBackgroundImage;


	ArrayList<Image> fling = new ArrayList<Image>();
	ArrayList<Image> singletap = new ArrayList<Image>();
	ArrayList<Image> doubletap = new ArrayList<Image>();
	Map<Image, Integer> hm = new HashMap<Image, Integer>();
	private int mCanvasHeight = 1;
	int gestcounter = 0;
	private Context mContext;
ArrayList<Integer> forr = new ArrayList<>();
	transient public static int rad;


	/**
	 * Current width of the surface/canvas.
	 *
	 * @see #setSurfaceSize
	 */
	private int mCanvasWidth = 1;

	private GestureDetectorCompat mDetector;
	private SurfaceView surfaceView;

	public LoginPanel(Context context, int backgroundImage) {
		super(context);
		mContext = context;
		new BitmapFactory();
		mBackgroundImage = BitmapFactory.decodeResource(getResources(), backgroundImage);

		DatabaseHelper db = new DatabaseHelper(mContext);

		gestures = LoginActivity.user.getGestarr();
		ls = LoginActivity.user.getImgPassword();
		for(int y : ls.values()){
			System.out.println("valueset: "+ y);
		}

		for(Image i : ls.keySet()){

			//int r = randomN();
			System.out.println("keyset: "+ i);
int r =3;
			// creating a hashmap to store all colors of the image
			HashMap<String,Pair<Bitmap, Integer>> bitmap1 = new HashMap<>();
			bitmap1.put("BLUE",new Pair(BitmapFactory.decodeResource(getResources(), db.getBlueImage(i.getName())), db.getBlueImage(i.getName())));
			bitmap1.put("YELLOW", new Pair(BitmapFactory.decodeResource(getResources(), db.getYellowImage(i.getName())), db.getYellowImage(i.getName())));
			bitmap1.put("GREEN",new Pair(BitmapFactory.decodeResource(getResources(), db.getGreenImage(i.getName())), db.getGreenImage(i.getName())));
			bitmap1.put("RED",new Pair(BitmapFactory.decodeResource(getResources(), db.getRedImage(i.getName())), db.getRedImage(i.getName())));

			// creating a new image
			i = new Image(bitmap1,i.getBitmapId(),i.getName(), i.getX(),i.getY(),i.getColor(),getResources());
			// giving random positions and color for correct recognition later

			switch(r){
				case 1:
				{
					Log.d(TAG, "case1:BOTH RIGHT");
					getgest = gestures.get(0);
					Log.d(TAG, "1 gestgest: " + getgest);
					substitutegesture(i);

					break;
				}
				case 2:
				{
					Log.d(TAG, "case2:CHANGE COLOR");
					ChangeColor(i);
					getgest = gestures.get(1);
					Log.d(TAG, "2 gestgest: " + getgest);
					substitutegesture(i);

					break;
				}
				case 3:
				{
					Log.d(TAG, "case3:CHANGE POSITION");
					ChangePosition(i);
					getgest = gestures.get(2);
					Log.d(TAG, "3 gestgest: " + getgest);
					substitutegesture(i);

					break;
				}
				default:
					break;

			}

		}



		thread = new LoginThread(getHolder(), this, context);
		getHolder().addCallback(this);
		surfaceView = this;

		mContext=context;
		mDetector = new GestureDetectorCompat(mContext, new MyGestureListener());
		mDetector.setIsLongpressEnabled(true);
		mDetector.setOnDoubleTapListener(this);


		//mDetector.setOnDoubleTapListener(listener)


		surfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mDetector.onTouchEvent(event);
				return true;
			}


		});
		setFocusable(true);


	}

	private void substitutegesture(Image i) {
		if(getgest.equalsIgnoreCase("Single Tap")){
			singletap.add(i);
		}
		if(getgest.equalsIgnoreCase("Double Tap")){
			doubletap.add(i);
			System.out.println("doubletapadded");
		}
		if(getgest.equalsIgnoreCase("Fling")){
			fling.add(i);
		}

	}

	private void ChangePosition(Image img) {
		Random ran = new Random();
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int screenWidth = metrics.widthPixels;
		int screenHeight = (int) (metrics.heightPixels*0.9);

		int h_zero = 0;
		int h_one = screenHeight/3;
		int h_two = (screenHeight*2)/3;
		int h_three = screenHeight;
		int w_zero = 0;
		int w_one = screenWidth/3;
		int w_two = (screenWidth*2)/3;
		int w_three = screenWidth;
		ArrayList<Integer> change_cell = new ArrayList<>();
		change_cell.add(1);
		change_cell.add(2);
		change_cell.add(3);
		change_cell.add(4);
		change_cell.add(5);
		change_cell.add(6);
		change_cell.add(7);
		change_cell.add(8);
		change_cell.add(9);

		int u = 0;
		Log.d(TAG,"img"+ img);

		Log.d(TAG,"CHECK CELL"+ ls.get(img));
		for(Image i: ls.keySet()) {
			if (img.equals(i)){
				 u = ls.get(i);
				System.out.println("uu: " + u);

			}

		}

		for(int val: ls.values()){
			int counter = 1;
			change_cell.remove(val-counter);
			counter++;
		}
		int r = ran.nextInt(change_cell.size()+1) + 1;


			/*do {
				r = ran.nextInt(9) + 1;
				System.out.println("chanegposr:" + r);
				//forr.add(r);
				break;
			} while (!(forr.contains(r)));*/


		// r = 5;
		System.out.println("changecell "+ change_cell.size() + r);

		switch(change_cell.get(r-1)){

			case 7: {
				img.setY((h_three + h_two) / 2);
				img.setX((w_zero + w_one) / 2);
				System.out.println("in switch");
				change_cell.add(u);
				break;
			}
			case 8: {
				img.setY((h_three + h_two) / 2);
				img.setX((w_one + w_two) / 2);
				change_cell.add(u);
				System.out.println("in switch");
				break;
			}
			case 9: {
				img.setY((h_three + h_two) / 2);
				img.setX((w_three + w_two) / 2);
				change_cell.add(u);
				break;
			}
			case 4: {
				img.setY((h_one + h_two) / 2);
				img.setX((w_zero + w_one) / 2);
				change_cell.add(u);
				break;
			}
			case 5: {
				img.setY((h_one + h_two) / 2);
				img.setX((w_one + w_two) / 2);
				change_cell.add(u);
				break;
			}
			case 6: {
				img.setY((h_one + h_two) / 2);
				img.setX((w_three + w_two) / 2);
				change_cell.add(u);
				break;
			}
			case 1: {
				img.setY((h_one + h_zero) / 2);
				img.setX((w_zero + w_one) / 2);
				change_cell.add(u);
				break;
			}
			case 2: {
				img.setY((h_one + h_zero) / 2);
				img.setX((w_two + w_one) / 2);
				change_cell.add(u);
				break;
			}
			case 3: {
				img.setY((h_one + h_zero) / 2);
				img.setX((w_two + w_three) / 2);
				change_cell.add(u);

				break;
			}


		}

		/*DisplayMetrics dm= new DisplayMetrics();
		//ran.setSeed((long)i);

		Image checkpos,checkposother;
		Iterator iter = hm.keySet().iterator();
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels-50;
		int height =dm.heightPixels-50;


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
			//	for(Image i: hm) {
				while(iter.hasNext()){
					Image i = (Image) iter.next();
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

			}*/





	}

	private void ChangeColor(Image img) {

		String[] colors = {"BLUE", "GREEN", "YELLOW", "RED"};
		String color = img.getColor();

		String setColor = color;
		do{
			Random r = new Random();
			setColor = colors[r.nextInt(colors.length)];
		}while(color.equalsIgnoreCase(setColor));


		Log.d(TAG, "Color" + setColor);
		img.setColor(setColor);

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
	}
	public void setSurfaceSize() {
		// synchronized to make sure these all change atomically
		synchronized (getHolder()) {
			mCanvasWidth = getWidth();

			mCanvasHeight = getHeight();

			// don't forget to resize the background image
			mBackgroundImage = Bitmap.createScaledBitmap(
					mBackgroundImage, getWidth(), getHeight(), true);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		Log.d(TAG,"state"+ thread.getState().toString());
		if(thread.getState() == Thread.State.NEW || thread.getState() == Thread.State.TERMINATED) {
			thread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
//		boolean retry = true;
//		while (retry) {
//			try {
//				thread.join();
//				retry = false;
//			} catch (InterruptedException e) {
//				// try again shutting down the thread
//			}
//		}
		Log.d(TAG, "Thread was shut down cleanly");
	}







	private int randomN() {

		Random rand = new Random();

		int randomNum = rand.nextInt(3)+1;
		System.out.println("random no" + randomNum);
		return randomNum;


	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		mBackgroundImage = Bitmap.createScaledBitmap(
 				mBackgroundImage, getWidth(), getHeight(), true);
		canvas.drawBitmap(mBackgroundImage, 0,0, null);

		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int screenWidth = (metrics.widthPixels);
		int screenHeight = ((int) (metrics.heightPixels*0.9))+80;

		//  Set paint options
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.argb(255, 255, 255, 255));

		canvas.drawLine((screenWidth/3)*2,0,(screenWidth/3)*2,screenHeight,paint);
		canvas.drawLine((screenWidth/3),0,(screenWidth/3),screenHeight,paint);
		canvas.drawLine(0,(screenHeight/3)*2,screenWidth,(screenHeight/3)*2,paint);
		canvas.drawLine(0,(screenHeight/3),screenWidth,(screenHeight/3),paint);

		for(Image i: ls.keySet()){
			i.draw(canvas);

		}


	}

	class MyGestureListener extends SimpleOnGestureListener {
		private static final String DEBUG_TAG = "Gestures";

		@Override
		public boolean onDown(MotionEvent event) {
			Log.d(DEBUG_TAG,"onDown: ");
			return true;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {

			System.out.println("on Long Press");
			Log.d("hello","onLP: ");

		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
							   float velocityX, float velocityY) {
			Log.d("hello", "on fling confirmed");
			gestcounter++;
			for(Image p : ls.keySet()){
				Log.d(TAG, p.getColor());
				if(fling.contains(p)){
					try {
						Image droidz = null;

						for (Image f : fling) {

							droidz = f.getCircleLine((int) event1.getX(), (int) event1.getY(), (int) event2.getX(), (int) event2.getY());
							System.out.println("droidz ST: " + droidz);

							if (droidz != null) {
								fling.remove(droidz);
							}



						}
					}
					catch(Exception e1){
						System.out.println("Runtime exception");
					}


				}

			}
			correctpass(gestcounter);

			Log.d(TAG, "onFling: ");
			return false;
		}
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent arg0) {
		Log.d("hello","onDTE: ");

		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		Log.d("hello", "on Single Tap confirmed");
		gestcounter++;


		for(Image p : ls.keySet()){
			Log.d(TAG, p.getColor());
			if(singletap.contains(p)){
				try {        //DoubleDroid = doubletap;
					Image droidz = null;
					// f = listdroid.get(l);
					for (Image f : singletap) {
						droidz = f.getRange(e.getX(), e.getY());
						System.out.println("droidz ST: " + droidz);

						if (droidz != null) {
							singletap.remove(droidz);
						}



					}
				}
				catch(Exception e1){
					System.out.println("Runtime exception");
				}

			}

		}
		correctpass(gestcounter);
		Log.d(TAG,"onSTC: ");

		return false;

	}

	private void correctpass(int gestcounter) {

		System.out.println("sizes: "+ singletap.size() + doubletap.size() + fling.size());
		if(gestcounter == ls.size()) {
			if ((singletap.size() == 0) && (doubletap.size() == 0) && (fling.size() == 0)) {
				Toast.makeText(mContext, "Correct password",
						Toast.LENGTH_SHORT).show();
				System.out.println("yes");

				// adding user to db
				DatabaseHelper db = new DatabaseHelper(mContext);
				db.addUser(LoginActivity.user);
				db.close();
				Intent intent = new Intent(mContext, UsernameActivity.class);
				mContext.startActivity(intent);
				thread.setRunning(false);
			}
			else {
				Toast.makeText(mContext,"Wrong Password",
						Toast.LENGTH_SHORT).show();


				try {
					thread.sleep(100);
					thread.setRunning(false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Toast.makeText(mContext,"Try Again !!!",
						Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(mContext, LoginActivity.class);
				intent.putExtra(Constants.USER, LoginActivity.user );
				mContext.startActivity(intent);

				//DatabaseHelper db = new DatabaseHelper(mContext);
				//mContext.db.close();
				/*Intent startintent = ((Activity) mContext).getIntent();



				thread.setRunning(false);
				((Activity) mContext).finish();
				mContext.startActivity(startintent);*/

			}
		}



		if(gestcounter > ls.size()){
			Toast.makeText(mContext,"Wrong Password",
					Toast.LENGTH_SHORT).show();

			thread.setRunning(false);
			System.out.println("recreating");
			Toast.makeText(mContext,"Try Again !!!",
					Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.putExtra(Constants.USER, LoginActivity.user);
			mContext.startActivity(intent);


			/*Intent startintent = ((Activity) mContext).getIntent();


			thread.setRunning(false);
			((Activity) mContext).finish();
			mContext.startActivity(startintent);*/
			////this.recreate;
		}
	}


	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
						   float arg3) {

		// TODO Auto-generated method stub

		return false;
	}



	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
							float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		Log.d("hello", "on double Tap confirmed");
		gestcounter++;
	for(Image dt: doubletap){
		System.out.println("dt: "+ dt);
	}

		for(Image p : ls.keySet()){
			Log.d(TAG, p.getColor());
			if(doubletap.contains(p)){
				try {
					Image droidz = null;

					for (Image f : doubletap) {
						droidz = f.getRange(e.getX(), e.getY());
						System.out.println("droidz ST: " + droidz);

						if (droidz != null) {
							doubletap.remove(droidz);
						}



					}
				}
				catch(Exception e1){
					Log.e(TAG, e1.toString());
				}

			}

		}
		correctpass(gestcounter);
		Log.d("hello","onDT: ");
		return false;
	}



	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}






}

