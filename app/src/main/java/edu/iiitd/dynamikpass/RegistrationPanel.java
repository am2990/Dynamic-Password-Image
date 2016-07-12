/* ** displays selected images from grid view: change position and color of the image** */

/**
 *
 */
package edu.iiitd.dynamikpass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import edu.iiitd.dynamikpass.model.Image;
import edu.iiitd.dynamikpass.utils.DatabaseHelper;
import edu.iiitd.dynamikpass.utils.Pair;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
	static ArrayList<Image> imglist = null;
	ActionMode mActionMode;
	private SurfaceView surfaceView;
	private Bitmap mBackgroundImage;
	private static Context mContext;


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
		imglist = new ArrayList<Image>();
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
		DisplayMetrics metrics= new DisplayMetrics();
		DisplayMetrics dm= new DisplayMetrics();
		//ran.setSeed((long)i);

		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels-50;
		int height =dm.heightPixels-50;
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		int h_zero = 0;
		int h_one = screenHeight/3;
		int h_two = (screenHeight*2)/3;
		int h_three = screenHeight;
		int w_zero = 0;
		int w_one = screenWidth/3;
		int w_two = (screenWidth*2)/3;
		int w_three = screenWidth;



		int pos=1;
		for(Image img : imglist){

				switch (pos) {

					case 7: {
						img.setY((h_three + h_two) / 2);
						img.setX((w_zero + w_one) / 2);
						pos++;
						break;
					}
					case 8: {
						img.setY((h_three + h_two) / 2);
						img.setX((w_one + w_two) / 2);
						pos++;
						break;
					}
					case 9: {
						img.setY((h_three + h_two) / 2);
						img.setX((w_three + w_two) / 2);
						pos++;
						break;
					}
					case 4: {
						img.setY((h_one + h_two) / 2);
						img.setX((w_zero + w_one) / 2);
						pos++;
						break;
					}
					case 5: {
						img.setY((h_one + h_two) / 2);
						img.setX((w_one + w_two) / 2);
						pos++;
						break;
					}
					case 6: {
						img.setY((h_one + h_two) / 2);
						img.setX((w_three + w_two) / 2);
						pos++;
						break;
					}
					case 1: {
						img.setY((h_one + h_zero) / 2);
						img.setX((w_zero + w_one) / 2);
						pos++;
						break;
					}
					case 2: {
						img.setY((h_one + h_zero) / 2);
						img.setX((w_two + w_one) / 2);
						pos++;
						break;
					}
					case 3: {
						img.setY((h_one + h_zero) / 2);
						img.setX((w_two + w_three) / 2);
						pos++;
						break;
					}

				}


		}



		/*for(Image img: imglist){

			//int randomNumx = ran.nextInt((height)-img.getBitmap().getHeight()) + 1;
			//int randomNumy= ran.nextInt((width)-img.getBitmap().getWidth()) + 1;
			int randomNumx = ran.nextInt(width) + 1;
			int randomNumy = ran.nextInt(height) + 1;
			boolean overlapp = true;
			Image pos = null;
			// till the flag is true
			// generate random x and y
			/*while(overlapp) {
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


			}


		}*/

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
							/*HashMap<Image, Integer> hm = FindCell();
							int y = hm.get(img);
							System.out.println("yy:" + y);*/
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
//		thread.setRunning(false);
		Log.d(TAG, " Thread state " +thread.getState().toString());
		if(thread.getState() == Thread.State.NEW) {
			if( thread.isAlive() == false) {
				thread.start();
			}
		}
		else if( thread.getState() == Thread.State.TERMINATED){
			thread = new MainThread(getHolder(), this, mContext);
			thread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		Log.d(TAG, "Thread was shut down cleanly");
	}


	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		mBackgroundImage = Bitmap.createScaledBitmap(
				mBackgroundImage, getWidth(), getHeight(), true);

		canvas.drawBitmap(mBackgroundImage, 0, 0, null);

		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels;

		//  Set paint options
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.argb(255, 255, 255, 255));

		canvas.drawLine((screenWidth/3)*2,0,(screenWidth/3)*2,screenHeight,paint);
		canvas.drawLine((screenWidth/3),0,(screenWidth/3),screenHeight,paint);
		canvas.drawLine(0,(screenHeight/3)*2,screenWidth,(screenHeight/3)*2,paint);
		canvas.drawLine(0,(screenHeight/3),screenWidth,(screenHeight/3),paint);


		for(Image img : imglist){
			img.draw(canvas);
		}

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
			ArrayList<Integer> list = new ArrayList<>();

			for(Image i : imglist){
				int cell_num = findCell().get(i);
				list.add(cell_num);

			}
			Set<Integer> set = new HashSet<Integer>(list);
			if(!(set.size() < list.size())) {


				mActionMode = ((Activity) mContext).startActionMode(mSubmitCallBack);
			}
			else{
				Toast.makeText(mContext,"not allowed ",
						Toast.LENGTH_SHORT).show();
			}
			return true;
		}


		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			ArrayList<Image> imagelist = new ArrayList<Image>();
			for(Image img: imglist){
				imagelist.add(img);
				Image i = img.getRange(arg0.getX(), arg0.getY());
				System.out.println("on Long Press");
				Log.d("hello","onLP: ");

				if (mActionMode != null) {
					Log.v(TAG, "mActionMode is not null");
				}


				if((i != null)){
					mActionMode = ((Activity)mContext).startActionMode(mActionModeCallback);
					surfaceView.setSelected(true);
					System.out.println("on Long Press");
					return;
				}
			}
		}
	}


	public static Image SelectColor(String color) {
		for(Image img: imglist){
			if(img.isLongTouched()){
				img.setColor(color);
				Toast.makeText(mContext,"Selected " + color,
						Toast.LENGTH_SHORT).show();
				img.setLongPressed(false);
				Image i = img;
				return i;
			}
		}
		return null;
	}


	public static HashMap<Image,Integer> findCell(){
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int screenWidth = metrics.widthPixels;
		int screenHeight = (int) (metrics.heightPixels * 0.99);
		HashMap<Image,Integer> cell = new HashMap<>();
		int h_zero = 0;
		int h_one = screenHeight/3;
		int h_two = (screenHeight*2)/3;
		int h_three = screenHeight;
		int w_zero = 0;
		int w_one = screenWidth/3;
		int w_two = (screenWidth*2)/3;
		int w_three = screenWidth;
		for(Image img : imglist){
			System.out.println("x,y" + img.getX()+ img.getY());
			if(img.getX()>w_zero && img.getX()<w_one){
				if(img.getY()>h_zero && img.getY()<h_one){
					cell.put(img,1);
				}
				else if(img.getY() > h_one && img.getY() < h_two){
					cell.put(img,4);
				}
				else if(img.getY() > h_two && img.getY() < h_three){
					cell.put(img,7);
				}


			}
			else if(img.getX()>w_one && img.getX()<w_two){
				if(img.getY()>h_zero && img.getY()<h_one){
					cell.put(img,2);
				}
				else if(img.getY() > h_one && img.getY() < h_two){
					cell.put(img,5);
				}
				else if(img.getY() > h_two && img.getY() < h_three){
					cell.put(img,8);
				}
			}
			else if(img.getX()>w_two && img.getX()<w_three){
				if(img.getY()>h_zero && img.getY()<h_one){
					cell.put(img,3);
				}
				else if(img.getY() > h_one && img.getY() < h_two){
					cell.put(img,6);
				}
				else if(img.getY() > h_two && img.getY() < h_three){
					cell.put(img,9);
				}
			}
			System.out.println("cell: "+ cell.get(img));

		}
	return cell;
	}
}

