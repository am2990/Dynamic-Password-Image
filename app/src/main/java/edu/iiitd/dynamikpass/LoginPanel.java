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
import edu.iiitd.dynamikpass.utils.DatabaseHelper;
import edu.iiitd.dynamikpass.utils.Pair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

	boolean flag;
	boolean check;
	String getgest=null;
	boolean colflag;
	ArrayList<Image> drawimg = new ArrayList<Image>();
	ArrayList<Image> ls = new ArrayList<Image>();
	List<String> gestures = null;

	private Bitmap mBackgroundImage;

	ArrayList<Image> templist;
	ArrayList<Image> fling = new ArrayList<Image>();
	ArrayList<Image> singletap = new ArrayList<Image>();
	ArrayList<Image> doubletap = new ArrayList<Image>();
	Map<Image, Integer> hm = new HashMap<Image, Integer>();
	private int mCanvasHeight = 1;
	int gestcounter = 0;
	private Context mContext;

	Image tri_b,tri_g,tri_y,tri_r;


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
//		ls = db.getAllDroids();
//		gestures = db.getAllGestures();

		gestures = LoginActivity.user.getGestarr();
		ls = LoginActivity.user.getImgPassword();

		for(Image i :ls){
			int r = randomN(ls.size());
//int r =3;
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
					hm.put(i,1);
					break;
				}
				case 2:
				{
					Log.d(TAG, "case2:CHANGE COLOR");
					ChangeColor(i);
					hm.put(i,2);
					break;
				}
				case 3:
				{
					Log.d(TAG, "case2:CHANGE POSITION");
					ChangePosition(i);
					hm.put(i,3);
					break;
				}
				default:
					break;

			}

		}
		Iterator iter = hm.keySet().iterator();
		while (iter.hasNext()) {

			Image image = (Image) iter.next();
			drawimg.add(image);

		}

		//retrieve integer(gesture) corresponding to the image
		for(Image i: drawimg){
			int j = hm.get(i);
			Log.d(TAG, "hm value"+ j);

			switch(j){
				case 1:{
					getgest = gestures.get(0);
					Log.d(TAG, "1 gestgest: " + getgest);
					substitutegesture(i);
					break;
				}
				case 2:{
					getgest = gestures.get(1);
					Log.d(TAG, "2 gestgest: " + getgest);
					substitutegesture(i);
					break;
				}
				case 3:{
					getgest = gestures.get(2);
					Log.d(TAG, "3 gestgest: " + getgest);
					substitutegesture(i);
					break;
				}
				case 4:{

					break;
				}
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
		}
		if(getgest.equalsIgnoreCase("Fling")){
			fling.add(i);
		}

	}

	private void ChangePosition(Image img) {
		Random ran = new Random();

		DisplayMetrics dm= new DisplayMetrics();
		//ran.setSeed((long)i);

		Image checkpos,checkposother;
		Iterator iter = hm.keySet().iterator();
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height =dm.heightPixels;
		do{


			int randomNumx = ran.nextInt(height) + 1;
			int randomNumy= ran.nextInt(width) + 1;
			System.out.println("height: "+ height);
			System.out.println("width: "+ width);
			//int randomNumx = ran.nextInt((400 - 25) + 1) + 25;
			//int randomNumy = ran.nextInt((400 - 25) + 1) +
			checkpos = img.getRange(randomNumx,randomNumy);

			// get upper limits from canvas
			//int randomNumx = ran.nextInt((350 - 25) + 1) + 25;
			//int randomNumy = ran.nextInt((350 - 25) + 1) + 25;

			//


			img.setX(randomNumx);
			img.setY(randomNumy);

		}while(checkpos != null);

		while (iter.hasNext()) {

			Image image = (Image) iter.next();

			do{
				checkposother = img.getRange(image.getX(),image.getY());
				int randomNumx = ran.nextInt(height) + 1;
				int randomNumy= ran.nextInt(width) + 1;

				// randomNumx = ran.nextInt((getHeight() - getWidth()) + 1) + getWidth();
				//int randomNumy = ran.nextInt((getHeight() - getWidth()) + 1) + getWidth();
				checkpos = img.getRange(randomNumx,randomNumy);

				img.setX(randomNumx);
				img.setY(randomNumy);

			}while(checkposother != null);

		}

		//img.setX(randomNumx);
		//img.setY(randomNumy);

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







	private int randomN(int length) {

		Random rand = new Random();

		int randomNum = rand.nextInt((length - 1) + 1) + 1;
		System.out.println("random no" + randomNum);
		return randomNum;


	}

	@Override
	protected void onDraw(Canvas canvas) {

		mBackgroundImage = Bitmap.createScaledBitmap(
				mBackgroundImage, getWidth(), getHeight(), true);
		canvas.drawBitmap(mBackgroundImage, 0,0, null);

		for(Image i: drawimg){
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

			gestcounter++;
			for(Image p : ls){
				Log.d(TAG, p.getColor());
				if(fling.contains(p)){
					try {        //DoubleDroid = doubletap;
						Image droidz = null;
						// f = listdroid.get(l);
						for (Image f : fling) {
							//droidz = f.getRange(e.getX(), e.getY());
							droidz = f.getCircleLine((int) event1.getX(), (int) event1.getY(), (int) event2.getX(), (int) event2.getY());
							System.out.println("droidz ST: " + droidz);
							//templist.add(droidz);
							// while(l<reach){
							if (droidz != null) {
								fling.remove(droidz);
							}



						}
					}
					catch(Exception e1){
						System.out.println("Runtime exception");
					}
					/*if((singletap.size() == 0)&& (doubletap.size() == 0) && (fling.size()==0)){
						Toast.makeText(mContext,"Correct password",
								Toast.LENGTH_SHORT).show();
						System.out.println("yes");

					}else{
						Toast.makeText(mContext,"Wrong Password",
								Toast.LENGTH_SHORT).show();
					}*/

				}

			}
			correctpass(gestcounter);
			/*ArrayList<Image> notF = new ArrayList<Image>(ls);
			notF.remove(fling);
			for(Image nf : notF){
				Toast.makeText(mContext,"Wrong password",
						Toast.LENGTH_SHORT).show();
			}*/
			//Image droidz = null;
			/*try {
				for (Image f : fling) {
					droidz = f.getCircleLine((int) event1.getX(), (int) event1.getY(), (int) event2.getX(), (int) event2.getY());
					System.out.println("droidz: " + droidz);
					System.out.println("droidz Fling: " + droidz);

					fling.remove(droidz);


				}

			}
			catch(Exception e){
				System.out.println("runtime exception");
			}*/
			/*if((singletap.size() == 0)&& (doubletap.size() == 0) && (fling.size()==0)){
				Toast.makeText(mContext,"Correct password",
						Toast.LENGTH_SHORT).show();
				System.out.println("yes");

			}*/
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
		Log.d(TAG, "on Single Tap confirmed");
		gestcounter++;
	/*	ArrayList<Image> notST = new ArrayList<Image>(ls);
		notST.remove(singletap);
		for(Image nst :0 notST){
			Toast.makeText(mContext,"Wrong password",
					Toast.LENGTH_SHORT).show();
		}*/

		/*try {
			for (Image f : singletap) {
				Image droidz = f.getRange(arg0.getX(), arg0.getY());
				System.out.println("droidz ST: " + droidz);

				singletap.remove(droidz);
				System.out.println("singleDroid size: " + singletap.size());


			}
		}
		catch(Exception e){
			System.out.println("runtime exception");
		}
		 
		 if((singletap.size() == 0)&& (doubletap.size() == 0) && (fling.size()==0)){
			 Toast.makeText(mContext,"Correct password",
		                Toast.LENGTH_SHORT).show();
			 System.out.println("yes");

		 }*/

		for(Image p : ls){
			Log.d(TAG, p.getColor());
			if(singletap.contains(p)){
				try {        //DoubleDroid = doubletap;
					Image droidz = null;
					// f = listdroid.get(l);
					for (Image f : singletap) {
						droidz = f.getRange(e.getX(), e.getY());
						System.out.println("droidz ST: " + droidz);
						//templist.add(droidz);
						// while(l<reach){
						if (droidz != null) {
							singletap.remove(droidz);
						}



					}
				}
				catch(Exception e1){
					System.out.println("Runtime exception");
				}
				//while
				/*if((singletap.size() == 0)&& (doubletap.size() == 0) && (fling.size()==0)){
					Toast.makeText(mContext,"Correct password",
							Toast.LENGTH_SHORT).show();
					System.out.println("yes");

				}else{
					Toast.makeText(mContext,"Wrong Password",
							Toast.LENGTH_SHORT).show();
				}*/

			}

		}
		correctpass(gestcounter);
		Log.d(TAG,"onSTC: ");

		return false;

	}

	private void correctpass(int gestcounter) {
		if(gestcounter == ls.size()) {
			if ((singletap.size() == 0) && (doubletap.size() == 0) && (fling.size() == 0)) {
				Toast.makeText(mContext, "Correct password",
						Toast.LENGTH_SHORT).show();
				System.out.println("yes");

				// adding user to db
				DatabaseHelper db = new DatabaseHelper(mContext);
				db.addUser(LoginActivity.user);
				Intent intent = new Intent(mContext, UsernameActivity.class);
				mContext.startActivity(intent);
				thread.setRunning(false);
			}
			else {
				Toast.makeText(mContext,"Wrong Password",
						Toast.LENGTH_SHORT).show();
				//this.recreate;

			}
		}

			/*else{
				Toast.makeText(mContext,"Wrong Password",
						Toast.LENGTH_SHORT).show();
			}*/


		if(gestcounter > ls.size()){
			Toast.makeText(mContext,"Wrong Password",
					Toast.LENGTH_SHORT).show();
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

		gestcounter++;
		//	ArrayList<Image> DoubleDroid = new ArrayList<Image>();
		/*ArrayList<Image> notDT = new ArrayList<Image>(ls);
		notDT.remove(doubletap);
		for(Image ndt : notDT){
			Toast.makeText(mContext,"Wrong password",
					Toast.LENGTH_SHORT).show();
		}*/
		/*try {        //DoubleDroid = doubletap;
			Image droidz = null;
			// f = listdroid.get(l);
			for (Image f : doubletap) {
				droidz = f.getRange(e.getX(), e.getY());
				System.out.println("droidz DT: " + droidz);
				//templist.add(droidz);
				// while(l<reach){
				if (droidz != null) {
					doubletap.remove(droidz);
				}



			}
		}
		catch(Exception e1){
			System.out.println("Runtime exception");
		}
		if((singletap.size() == 0)&& (doubletap.size() == 0) && (fling.size()==0)){
			Toast.makeText(mContext,"Correct password",
					Toast.LENGTH_SHORT).show();
			System.out.println("yes");

		}
*/

		for(Image p : ls){
			Log.d(TAG, p.getColor());
			if(doubletap.contains(p)){
				try {        //DoubleDroid = doubletap;
					Image droidz = null;
					// f = listdroid.get(l);
					for (Image f : doubletap) {
						droidz = f.getRange(e.getX(), e.getY());
						System.out.println("droidz ST: " + droidz);
						//templist.add(droidz);
						// while(l<reach){
						if (droidz != null) {
							doubletap.remove(droidz);
						}



					}
				}
				catch(Exception e1){
					System.out.println("Runtime exception");
				}
				/*if((singletap.size() == 0)&& (doubletap.size() == 0) && (fling.size()==0)){
					Toast.makeText(mContext,"Correct password",
							Toast.LENGTH_SHORT).show();
					System.out.println("yes");

				}else{
					Toast.makeText(mContext,"Wrong Password",
							Toast.LENGTH_SHORT).show();
				}*/

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

