/* ** displays images with random position and color, apply gesture (fling, single tap or double tap)
 to get the password right ** */
/**
 *
 */
package edu.iiitd.dynamikpass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.iiitd.dynamikpass.model.Image;
import edu.iiitd.dynamikpass.utils.Constants;
import edu.iiitd.dynamikpass.utils.DatabaseHelper;
import edu.iiitd.dynamikpass.utils.Pair;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
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
public class LoginPanel extends SurfaceView implements OnGestureListener,
		OnDoubleTapListener, SurfaceHolder.Callback {


	private static final String TAG = LoginPanel.class.getSimpleName();

	static LoginThread thread;


	String getgest=null;

	HashMap<Image, Integer> ls = new HashMap<Image, Integer>();
	HashMap<Integer, Image> cell_list = new HashMap<Integer, Image>();
	ArrayList<Image> draw = new ArrayList<>();
	List<String> gestures = null;

	private Bitmap mBackgroundImage;


	ArrayList<Image> fling = new ArrayList<Image>();
	ArrayList<Image> singletap = new ArrayList<Image>();
	ArrayList<Image> doubletap = new ArrayList<Image>();
	Map<Image, Integer> hm = new HashMap<Image, Integer>();
	int gestcounter = 0;
	private Context mContext;


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
		// initialize and populate values
		mContext = context;
		new BitmapFactory();
		mBackgroundImage = BitmapFactory.decodeResource(getResources(), backgroundImage);
		gestures = LoginActivity.user.getGestarr();
		ls = LoginActivity.user.getImgPassword();

		// all cells are initially empty
		for(int i = 1; i <= 9 ; i++) {
			cell_list.put(i, null);
		}


		DatabaseHelper db = new DatabaseHelper(mContext);
		for(Image i : ls.keySet()){
			Log.d(TAG, "i:" + i);
			// creating a hashmap to store all colors of the image
			HashMap<String,Pair<Bitmap, Integer>> bitmap1 = new HashMap<>();
			bitmap1.put("BLUE",new Pair(BitmapFactory.decodeResource(getResources(), db.getBlueImage(i.getName())), db.getBlueImage(i.getName())));
			bitmap1.put("YELLOW", new Pair(BitmapFactory.decodeResource(getResources(), db.getYellowImage(i.getName())), db.getYellowImage(i.getName())));
			bitmap1.put("GREEN",new Pair(BitmapFactory.decodeResource(getResources(), db.getGreenImage(i.getName())), db.getGreenImage(i.getName())));
			bitmap1.put("RED",new Pair(BitmapFactory.decodeResource(getResources(), db.getRedImage(i.getName())), db.getRedImage(i.getName())));

			// creating a new image
			i = new Image(bitmap1,i.getBitmapId(),i.getName(), i.getX(),i.getY(),i.getColor(),getResources());
			// giving random positions and color for correct recognition later
			int r = randomN(3);
//			int r = 3;
				switch(r){
				case 1:
				{
					Log.d(TAG, "case1:BOTH RIGHT");
					getgest = gestures.get(0);
					Log.d(TAG, "1 gestgest: " + getgest);
					substituteGesture(i);
					//image occupies it's original cell
					for( Image img : ls.keySet()){
						if(img.equals(i)) {
							if( cell_list.get(ls.get(img)) == null) {
								cell_list.put(ls.get(img), img);
							}else{
								Image im = cell_list.get(ls.get(img));
								ChangePosition(im, true);

							}
						}
					}
					draw.add(i);

					break;
				}
				case 2:
				{
					Log.d(TAG, "case2:CHANGE COLOR");
					//image occupies it's original cell
					for( Image img : ls.keySet()){
						if(img.equals(i)) {
							if( cell_list.get(ls.get(img)) == null) {
								cell_list.put(ls.get(img), img);
							}else{
								Image im = cell_list.get(ls.get(img));
								ChangePosition(im, true);
							}
						}
					}
					i = ChangeColor(i);
					getgest = gestures.get(1);
					Log.d(TAG, "2 gestgest: " + getgest);
					substituteGesture(i);
					draw.add(i);
					break;
				}
				case 3:
				{
					Log.d(TAG, "case3:CHANGE POSITION");
					i = ChangePosition(i , false);
					getgest = gestures.get(2);
					Log.d(TAG, "3 gestgest: " + getgest);
					substituteGesture(i);
					draw.add(i);
					break;
				}
				default:
					break;

			}

		}
		db.closeDb();



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

	private void substituteGesture(Image i) {
		if(getgest.equalsIgnoreCase("Single Tap")){
			singletap.add(i);
		}
		if(getgest.equalsIgnoreCase("Double Tap")){
			doubletap.add(i);
		}
		if(getgest.equalsIgnoreCase("Fling")){
			fling.add(i);
		}

	}



	private Image ChangePosition(Image img, boolean update) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels;

		int h_zero = 0;
		int h_one = screenHeight/3;
		int h_two = (screenHeight*2)/3;
		int h_three = screenHeight;
		int w_zero = 0;
		int w_one = screenWidth/3;
		int w_two = (screenWidth*2)/3;
		int w_three = screenWidth;


		//TODO add logic to update position
		Integer[] arr = {1,2,3,4,5,6,7,8,9};
		boolean already_set = false;
		Integer img_cell = null;
		for( Image i : ls.keySet()){
			if(i.equals(img)) {
				img_cell = ls.get(i);
			}
		}
		if(img_cell == null){
			return img;
		}

		if(cell_list.get(img_cell) == null) {
			cell_list.put(img_cell, img);
		}else{
			already_set = true;
		}

		Integer newCell = randomN(arr.length);
		while(cell_list.get(newCell) != null){
			newCell = randomN(arr.length);
		}
		cell_list.put(newCell, img);

		switch(newCell){

			case 7: {
				img.setY((h_three + h_two) / 2);
				img.setX((w_zero + w_one) / 2);
				break;
			}
			case 8: {
				img.setY((h_three + h_two) / 2);
				img.setX((w_one + w_two) / 2);
				break;
			}
			case 9: {
				img.setY((h_three + h_two) / 2);
				img.setX((w_three + w_two) / 2);
				break;
			}
			case 4: {
				img.setY((h_one + h_two) / 2);
				img.setX((w_zero + w_one) / 2);
				break;
			}
			case 5: {
				img.setY((h_one + h_two) / 2);
				img.setX((w_one + w_two) / 2);
				break;
			}
			case 6: {
				img.setY((h_one + h_two) / 2);
				img.setX((w_three + w_two) / 2);
				break;
			}
			case 1: {
				img.setY((h_one + h_zero) / 2);
				img.setX((w_zero + w_one) / 2);
				break;
			}
			case 2: {
				img.setY((h_one + h_zero) / 2);
				img.setX((w_two + w_one) / 2);
				break;
			}
			case 3: {
				img.setY((h_one + h_zero) / 2);
				img.setX((w_two + w_three) / 2);
				break;
			}
		}
		if( !already_set ) {
			cell_list.put(img_cell, null);
		}
		return img;

	}

	private Image ChangeColor(Image img) {
		ArrayList<String> color_list = new ArrayList<>();
		color_list.add(Constants.BLUE);color_list.add(Constants.RED);
		color_list.add(Constants.GREEN);color_list.add(Constants.YELLOW);
		String color = img.getColor();
		if( color != null) {
			color_list.remove(color);
			String setColor = color_list.get(randomN(color_list.size())-1);
			Log.d(TAG, "Color" + setColor);
			img.setColor(setColor);
		}
		return img;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
	}
	public void setSurfaceSize() {
		// synchronized to make sure these all change atomically
		synchronized (getHolder()) {
			mCanvasWidth = getWidth();

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
	}




	private int randomN(int N) {
		Random rand = new Random();
		int randomNum = rand.nextInt(N)+1;
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

		for(Image i: draw){
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
			Log.d(TAG, "Recreating");
			Toast.makeText(mContext,"Try Again !!!",
					Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.putExtra(Constants.USER, LoginActivity.user);
			mContext.startActivity(intent);

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

