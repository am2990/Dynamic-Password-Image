package edu.iiitd.dynamikpass.utils;



import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



import edu.iiitd.dynamikpass.RegistrationActivity;
import edu.iiitd.dynamikpass.RegistrationPanel;
import edu.iiitd.dynamikpass.model.Image;
import edu.iiitd.dynamikpass.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.nsd.NsdManager.RegistrationListener;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class DatabaseHelper extends SQLiteOpenHelper{


    private static final String TAG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "droid_db";

    // Table Names
    private static final String TABLE_DROID = "table_droid";
    private static final String TABLE_MAP = "table_map";
    private static final String TABLE_GESTURE = "t_gesture";
    private static final String TABLE_IMGCOLOR ="t_imgcolor";
    private static final String TABLE_USER ="t_user";

    private static final String KEY_ID = "_id"; //primary key
    private static final String KEY_CREATED_AT = "created_at";

    // Droid Table - column nmaes
    // private static final String KEY_DID = "did";
    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";
    private static final String KEY_BITMAP_ID = "bitmap_id";
    private static final String KEY_COLOR = "color";
    private static final String KEY_RES = "resources";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IMGPASSWORD = "imgpassword";
    private static final String KEY_GESTURE_ARR = "gesturearr";
    private static final String KEY_IMGBACK = "imgback";
    // Map Table - column names
    private static final String KEY_POSITION_M = "position_m";
    private static final String KEY_COLOR_M = "color_m";
    private static final String KEY_GESTURE_M = "gesture_m";


    // Gesture Table - column names
    private static final String KEY_GID= "g_id";
    private static final String KEY_GESTURE = "gesture";

    //Imgcolor table column name
    private static final String KEY_IMGID = "img_id";
    private static final String KEY_IMG_TEXT = "img_text";
    private static final String KEY_IMG_B = "img_blue";
    private static final String KEY_IMG_G = "img_green";
    private static final String KEY_IMG_R = "img_red";
    private static final String KEY_IMG_Y = "img_yellow";





    // Table Create Statements

    //User table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " VARCHAR," + KEY_IMGPASSWORD + " TEXT," + KEY_IMGBACK + " INTEGER, "
            + KEY_GESTURE_ARR + " VARCHAR " + " )";


    // DROID table create statement
    private static final String CREATE_TABLE_DROID = "CREATE TABLE "
            + TABLE_DROID + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_X + " INTEGER," + KEY_BITMAP_ID + " INTEGER," + KEY_Y + " INTEGER,"
            + KEY_COLOR + " TEXT" + " )";

    // MAP table create statement
    private static final String CREATE_TABLE_MAP = "CREATE TABLE " + TABLE_MAP
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_POSITION_M + " INTEGER,"
            + KEY_COLOR_M + " INTEGER," + KEY_GESTURE_M + " TEXT" + " )";

    // GESTURE table create statement
    private static final String CREATE_TABLE_GESTURE = "CREATE TABLE " + TABLE_GESTURE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GID + " INTEGER," + KEY_GESTURE + " TEXT" + ")";

    //IMG_COLOR table creat statement
    private static final String CREATE_TABLE_IMGCOLOR = "CREATE TABLE " + TABLE_IMGCOLOR
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IMG_TEXT + " TEXT,"
            + KEY_IMG_B + " INTEGER," + KEY_IMG_G + " INTEGER," + KEY_IMG_Y + " INTEGER," + KEY_IMG_R + " INTEGER" + " )";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }
    // Logcat tag



    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_DROID);
        db.execSQL(CREATE_TABLE_MAP);
        db.execSQL(CREATE_TABLE_GESTURE);
        db.execSQL(CREATE_TABLE_IMGCOLOR);
        db.execSQL(CREATE_TABLE_USER);

        Log.d(TAG, "creating tables");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DROID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GESTURE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMGCOLOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // create new tables
        onCreate(db);
    }


    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        Gson gson = new Gson();

        ContentValues values = new ContentValues();
        boolean exists = isUser(user.getUsername());
        if(exists == false) {
            values.put(KEY_USERNAME, user.getUsername());
            values.put(KEY_IMGPASSWORD, gson.toJson(user.getImgPassword()));
            values.put(KEY_IMGBACK, user.getImageback());
            values.put(KEY_GESTURE_ARR, gson.toJson(user.getGestarr()));
            // Inserting Row
            long user_id = db.insert(TABLE_USER, null, values);
            Log.d(TAG,"user " + user.getUsername() + " inserted successfully id =" + user_id);
        }
        db.close(); // Closing database connection
    }

    public User getUserByName(String username) {

        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("username: "+ username);
        String selectString = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_USERNAME + " =?";
        Gson gson = new Gson();

        Cursor cursor = db.rawQuery(selectString, new String[]{username});
        if(cursor.getCount()>0) {

            cursor.moveToFirst();
            int _id = cursor.getInt((cursor.getColumnIndex(KEY_ID)));
            String user_name = cursor.getString((cursor.getColumnIndex(KEY_USERNAME)));
            String img_password = cursor.getString((cursor.getColumnIndex(KEY_IMGPASSWORD)));
            int background = cursor.getInt((cursor.getColumnIndex(KEY_IMGBACK)));
            String gesture_arr = cursor.getString((cursor.getColumnIndex(KEY_GESTURE_ARR)));

            User user = new User();
            user.setId(_id);
            user.setUsername(user_name);

            Type listType = new TypeToken<ArrayList<Image>>(){}.getType();
            ArrayList<Image> img_password_list = (ArrayList<Image>) gson.fromJson(img_password, listType);
            user.setImgPassword(img_password_list);

            user.setImageback(background);

            Type strlistType = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> gesture_arr_list = (ArrayList<String>) gson.fromJson(gesture_arr, strlistType);
            user.setGestarr(gesture_arr_list);
            db.close();
            return user; //row exists
        }
        else
            return null;


    }


    public boolean isUser(String username) {

        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("username: "+ username);
        String selectString = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_USERNAME + " =?";
        Gson gson = new Gson();

        Cursor cursor = db.rawQuery(selectString, new String[]{username});
        if(cursor.getCount()>0) {

            return true; //row exists
        }
        else
            return false;


    }
    /* Inserting values in gesture table : hardcoded */

    public void clearTableDroid(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DROID, null, null);
    }

    public long addImage(Image img) {
        SQLiteDatabase db = this.getWritableDatabase();
        long img_id = 0;

        ContentValues values = new ContentValues();
        values.put(KEY_BITMAP_ID, img.getBitmapId());
        values.put(KEY_X, img.getX());
        values.put(KEY_Y,img.getY());
        values.put(KEY_COLOR, img.getColor());
        // insert row
        img_id = db.insert(TABLE_DROID, null, values);

        Log.d(TAG, "image saved"+ img_id);
        Log.d(TAG, "image saved" + img_id);
        db.close();
        return img_id;
    }

    /*
     * Adding a droid
     */
    public long saveOrUpdateImage(Image img) {
        SQLiteDatabase db = this.getWritableDatabase();
        long droid_id = 0;
        int id = img.getBitmapId();

        //TODO using bitmap id check if an image already exits
        String whereClause = KEY_BITMAP_ID +"= ? ";

        String[] whereArgs = {  Integer.toString(id) };
        String selQuery = "SELECT * FROM " + TABLE_DROID +" WHERE " + KEY_BITMAP_ID+ "=" + id;
        //int id = img.getBitmapId();
//        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selQuery, null);

//        if (exits ) update that object
        //else
        if(c.getCount() == 1){
            c.moveToFirst();

            ContentValues values = new ContentValues();
            values.put(KEY_BITMAP_ID, img.getBitmapId());
            values.put(KEY_X, img.getX());
            values.put(KEY_Y,img.getY());
            values.put(KEY_COLOR, img.getColor());
            droid_id =  db.update(TABLE_DROID, values, whereClause, whereArgs);

        }
        else{
            ContentValues values = new ContentValues();
            values.put(KEY_BITMAP_ID, img.getBitmapId());
            values.put(KEY_X, img.getX());
            values.put(KEY_Y,img.getY());
            values.put(KEY_COLOR, img.getColor());

//        values.put(KEY_BITMAP_ID, id);
//        values.put(KEY_X, x);
//        values.put(KEY_Y, y);
//        
//        values.put(KEY_COLOR, col);

            // insert row
            droid_id = db.insert(TABLE_DROID, null, values);

        }
        Log.d("DB HELPER", "droid_id"+ droid_id);
        db.close();
        return droid_id;
    }

    /**
     * getting all droids under single tag
     * */
    public ArrayList<Image> getAllDroids() {
        ArrayList<Image> droids = new ArrayList<Image>();

        String selectQuery = "SELECT  * FROM " + TABLE_DROID;

        Log.e(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Image image = new Image();
                image.setBitmapId(c.getInt(c.getColumnIndex(KEY_BITMAP_ID)));
                image.setX((c.getInt(c.getColumnIndex(KEY_X))));
                image.setY((c.getInt(c.getColumnIndex(KEY_Y))));
                image.setColor((c.getString(c.getColumnIndex(KEY_COLOR))));

                droids.add(image);
            } while (c.moveToNext());
        }
        db.close();
        return droids;

    }
    /* Adding maps to table */
    public long saveMap(int pos, int col, String item1) {
        SQLiteDatabase db = this.getWritableDatabase();
        long map_id;
        String whereClause = KEY_POSITION_M + "= ?" +  " AND " + KEY_COLOR_M + "= ?";

        String[] whereArgs = new String[]{  Integer.toString(pos), Integer.toString(col) };

        String selQuery = "SELECT * FROM " + TABLE_MAP +" WHERE " + KEY_POSITION_M+ "=" + pos + " AND " + KEY_COLOR_M + "=" + col;
        //int id = img.getBitmapId();
//        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selQuery, null);

//        if (exits ) update that object
        //else
        if(c.getCount() == 1){
            c.moveToFirst();

            ContentValues values = new ContentValues();
            values.put(KEY_POSITION_M, pos);
            values.put(KEY_COLOR_M, col);
            values.put(KEY_GESTURE_M,item1);
            map_id =  db.update(TABLE_MAP, values, whereClause, whereArgs);

        }
        else{
            ContentValues values = new ContentValues();
            values.put(KEY_POSITION_M, pos);
            values.put(KEY_COLOR_M, col);
            values.put(KEY_GESTURE_M,item1);


            // insert row
            map_id = db.insert(TABLE_MAP, null, values);
            //  System.out.println("pat_id: "+ patient_id);

        }
        System.out.println("map_id: "+map_id);
        db.close();
        return map_id;
    }

    /**
     * getting all gestures under single tag
     * */
    public List<String> getAllGestures() {
        List<String> gestures = new ArrayList<String>();

        String selectQuery = "SELECT " + KEY_GESTURE_M + " FROM " + TABLE_MAP;

        Log.e(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        // db.execSQL(selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                //db.execSQL(selectQuery);
                // Image image = new Image();
                // c.getInt(c.getColumnIndex(KEY_POSITION_M));
                //c.getInt(c.getColumnIndex(KEY_COLOR_M));
                String getgesture = c.getString(c.getColumnIndex(KEY_GESTURE_M));
                System.out.println("getgesture: "+getgesture);
                // image.setColor((c.getString(c.getColumnIndex(KEY_COLOR))));

                gestures.add(getgesture);
            } while (c.moveToNext());
        }
        db.close();
        return gestures;
    }

    //inserting images of a corresponding color
    public long createImage(String txt, Integer blue, Integer green, Integer red, Integer yellow) {
        SQLiteDatabase db = this.getWritableDatabase();
        long img_id;
        String whereClause = KEY_IMG_TEXT +"= ?";

        String[] whereArgs = { txt };
        String selQuery = "SELECT * FROM " + TABLE_IMGCOLOR +" WHERE " + KEY_IMG_TEXT + "='"+txt+"'";
        Log.v(TAG, selQuery);

        Cursor c = db.rawQuery(selQuery, null);
//        Cursor c = db.query(TABLE_IMGCOLOR, null, KEY_IMG_TEXT + "=" + txt, null, null, null, null, null);

//        if (exits ) update that object
        //else
        if(c.getCount() == 1){
            c.moveToFirst();

            ContentValues values = new ContentValues();
            values.put(KEY_IMG_TEXT, txt);
            values.put(KEY_IMG_B, blue);
            values.put(KEY_IMG_G,green);
            values.put(KEY_IMG_R, red);
            values.put(KEY_IMG_Y, yellow);
            img_id =  db.update(TABLE_IMGCOLOR, values, whereClause, whereArgs);

        }
        else{
            ContentValues values = new ContentValues();
            values.put(KEY_IMG_TEXT, txt);
            values.put(KEY_IMG_B, blue);
            values.put(KEY_IMG_G, green);
            values.put(KEY_IMG_R, red);
            values.put(KEY_IMG_Y, yellow);

            // insert row
            img_id = db.insert(TABLE_IMGCOLOR, null, values);
            System.out.println("img_id: "+ img_id);

        }
        db.close();
        return img_id;

    }

    //getting all images of a corresponding color


    public Integer getRedImage(String name){

        int redimg = 0;

        String selectQuery = "SELECT * FROM " + TABLE_IMGCOLOR + " WHERE " + KEY_IMG_TEXT + "='"+name+"'";;
        Log.e(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                redimg = c.getInt(c.getColumnIndex(KEY_IMG_R));
            } while (c.moveToNext());
        }
        db.close();
        return redimg;


    }
    public Integer getYellowImage(String name){

        int yellowimg = 0;

        String selectQuery = "SELECT * FROM " + TABLE_IMGCOLOR + " WHERE " + KEY_IMG_TEXT + "='"+name+"'";;
        Log.e(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                yellowimg = c.getInt(c.getColumnIndex(KEY_IMG_Y));
            } while (c.moveToNext());
        }
        db.close();
        return yellowimg;


    }
    public Integer getGreenImage(String name){

        int greenimg = 0;

        String selectQuery = "SELECT * FROM " + TABLE_IMGCOLOR + " WHERE " + KEY_IMG_TEXT + "='"+name+"'";;
        Log.e(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                greenimg = c.getInt(c.getColumnIndex(KEY_IMG_G));
            } while (c.moveToNext());
        }
        db.close();
        return greenimg;


    }
    public Integer getBlueImage(String name){

        int blue_img = 0;

        String selectQuery = "SELECT * FROM " + TABLE_IMGCOLOR + " WHERE " + KEY_IMG_TEXT + "='"+name+"'";;
        Log.e(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                blue_img = c.getInt(c.getColumnIndex(KEY_IMG_B));
            } while (c.moveToNext());
        }
        db.close();
        return blue_img;


    }

    public void closeDb() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}
