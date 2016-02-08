/**
 *
 */
package edu.iiitd.dynamikpass.model;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import edu.iiitd.dynamikpass.R;
import edu.iiitd.dynamikpass.UsernameActivity;
import edu.iiitd.dynamikpass.utils.CircleLine;
import edu.iiitd.dynamikpass.utils.Constants;
import edu.iiitd.dynamikpass.utils.DatabaseHelper;
import edu.iiitd.dynamikpass.utils.Pair;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author impaler
 *
 */
public class Image implements Serializable{

	/**
	 *
	 */
	private static final String TAG = Image.class.getSimpleName();
	private static final long serialVersionUID = 8438662189730257314L;


	private int bitmap_id;  // the selected bitmap_id
	private int x;			// the X coordinate
	private int y;			// the Y coordinate
	private String col;     // color of selected bitmap
	private String name;

	transient private HashMap<String,Pair<Bitmap,Integer>> color_to_bitmap = new HashMap<>();
	transient private Bitmap bitmap;
	transient private boolean touched, longPressed;	// if droid is touched/picked uptransient private boolean touched, longPressed;	// if droid is touched/picked up
	transient private static Resources res;
	 public static int rad;


	public Image(){

	}
	public Image(Bitmap bitmap, int bitmap_id, int x, int y, String col, Resources res){
		this.bitmap = bitmap;
		this.bitmap_id = bitmap_id;
		this.x = x;
		this.y = y;
		this.col = col;
		Image.res = UsernameActivity.res;
	}

	public Image(HashMap<String, Pair<Bitmap, Integer>> bitmap, int bitmap_id, String name, int x, int y, String col, Resources res){
		this.bitmap_id = bitmap_id;
		this.x = x;
		this.y = y;
		this.col = col;
		this.name = name;
		this.bitmap = bitmap.get(col).getLeft();
		this.color_to_bitmap = bitmap;
		this.res = UsernameActivity.res;
	}

	public Image(String name, int red_bitmap, int blue_bitmap, int green_bitmap, int yellow_bitmap){
		this.name = name;
		this.res = UsernameActivity.res;

		color_to_bitmap.put(Constants.RED, new Pair(BitmapFactory.decodeResource(res,red_bitmap), red_bitmap));
		color_to_bitmap.put(Constants.BLUE, new Pair(BitmapFactory.decodeResource(res,red_bitmap), red_bitmap));
		color_to_bitmap.put(Constants.GREEN, new Pair(BitmapFactory.decodeResource(res,red_bitmap), red_bitmap));
		color_to_bitmap.put(Constants.YELLOW, new Pair(BitmapFactory.decodeResource(res,red_bitmap), red_bitmap));

		this.col = Constants.BLUE;
		this.bitmap_id = blue_bitmap;
	}

	public Bitmap getBitmap(Resources res) {

		if(this.bitmap == null && this.bitmap_id != 0){
			bitmap = BitmapFactory.decodeResource(res,bitmap_id);
		}
		return this.bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public int getBitmapId(){
		return bitmap_id;
	}

	public void setBitmapId(int bitmap_id){
		this.bitmap_id = bitmap_id;
		setBitmap(BitmapFactory.decodeResource(res, bitmap_id));
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isTouched() {
		return touched;
	}

	public String getColor(){
		return this.col;
	}


	public void setColor(String col){

		switch(col){

			case "YELLOW":
				setBitmap( color_to_bitmap.get("YELLOW").getLeft());
				this.col = "YELLOW";
				this.bitmap_id = color_to_bitmap.get("YELLOW").getRight();
				break;

			case "RED":
				setBitmap( color_to_bitmap.get("RED").getLeft());
				this.col = "RED";
				this.bitmap_id = color_to_bitmap.get("RED").getRight();
				break;

			case "GREEN":
				setBitmap( color_to_bitmap.get("GREEN").getLeft());
				this.col = "GREEN";
				this.bitmap_id = color_to_bitmap.get("GREEN").getRight();
				break;

			case "BLUE":
				setBitmap( color_to_bitmap.get("BLUE").getLeft());
				this.col = "BLUE";
				this.bitmap_id = color_to_bitmap.get("BLUE").getRight();
				break;
		}

	}
	public void setTouched(boolean touched) {
		this.touched = touched;
	}

	public boolean isLongTouched() {
		return longPressed;
	}

	public void setLongPressed(boolean lPressed) {
		this.longPressed = lPressed;
	}


	public Image getRange(float x, float y){

		Image d = null;
		int h= bitmap.getHeight();
		int w = bitmap.getWidth();


		rad = ((int) (Math.sqrt(((h/2)*(h/2))+((w/2)*(w/2))))+10);

		System.out.println("get xcoord: "+x);
		System.out.println("get ycoord: "+y);
		double eqn = (((x-getX())*(x-getX()))+((y-getY())*(y-getY()))-(rad*rad));
		System.out.println("eqn droid : " + eqn);




		if(eqn <= 0){
			System.out.println("inside circle");
			d =  this;

		}
		else{
			System.out.println("outside circle");
		}

		return d;



	}

	public Image getCircleLine(int event1x, int event1y, int event2x, int event2y){

		CircleLine.Point center = new CircleLine.Point(getX(), getY());
		int h= bitmap.getHeight();
		int w = bitmap.getWidth();

		CircleLine.Point pointA = new CircleLine.Point(event1x,event1y);
		CircleLine.Point pointB = new CircleLine.Point(event2x,event2y);
		CircleLine.Point p = null;
		int radius = ((int) (Math.sqrt(((h/2)*(h/2))+((w/2)*(w/2))))+10);
		List<CircleLine.Point> lp = CircleLine.getCircleLineIntersectionPoint(pointA, pointB, center, radius);

		Log.d(TAG, "LP Size " + lp.size());
		try{
			Image sp = null ;
			for(int i =0;i<lp.size();i++){
				sp = getRange((float)lp.get(0).x, (float)lp.get(0).y);

			}
			return sp;
		}
		catch(Exception e){
			Log.d(TAG, "nullll");
			return null;
		}


	}


	public void draw(Canvas canvas) {

		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(getX(), getY(), rad, paint);

	}



	/**
	 * Handles the {@link MotionEvent.ACTION_DOWN} event. If the event happens on the 
	 * bitmap surface then the touched state is set to <code>true</code> otherwise to <code>false</code>
	 * @param eventX - the event's X coordinate
	 * @param eventY - the event's Y coordinate
	 */
	public void handleActionDown(int eventX, int eventY) {
		Integer x = this.x;
		if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2))) {
			if (eventY >= (y - bitmap.getHeight() / 2) && (eventY <= (y + bitmap.getHeight() / 2))) {
				// droid touched
				setTouched(true);
				setLongPressed(true);
			} else {
				setTouched(false);
				setLongPressed(false);
			}
		} else {
			setTouched(false);
			setLongPressed(false);
		}

	}

	@Override
	public boolean equals(Object v) {
		boolean retVal = false;

		if (v instanceof Image){
			Image img = (Image) v;
			retVal = img.bitmap_id == this.bitmap_id;
		}

		return retVal;
	}


	private void writeObject(ObjectOutputStream oos) throws IOException{
		// This will serialize all fields that you did not mark with 'transient'
		// (Java's default behaviour)
		oos.defaultWriteObject();
		// Now, manually serialize all transient fields that you want to be serialized
//		oos.write(x);
//		oos.write(y);
//		oos.write(bitmap_id);
//		oos.writeUTF(name);
		if(bitmap!=null){
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
			if(success){
				oos.writeObject(byteStream.toByteArray());
			}
		}
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
		// Now, all again, deserializing - in the SAME ORDER!
		// All non-transient fields
		ois.defaultReadObject();
		// All other fields that you serialized
//		x = ois.read();
//		y = ois.read();
//		bitmap_id = ois.read();
//		name = ois.readUTF();
		byte[] image = (byte[]) ois.readObject();
		if(image != null && image.length > 0){
			bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
