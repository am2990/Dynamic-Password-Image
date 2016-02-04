package edu.iiitd.dynamikpass.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Shreya Mehrishi on 12/13/2015.
 */
public class User implements Serializable {

    //private variables
    private static final long serialVersionUID = 8438662189730257333L;
    Integer _id;
    String username;
    ArrayList<Image> objects;
    int background;
    ArrayList<String> gesturearr;

    public User(){
        this.username = "default";
        this.background = -1;
        this.objects = new ArrayList<>();
        this.gesturearr = new ArrayList<>();

    }

    public int getId(){
        return this._id;
    }
    public void setId(int id){
        this._id = id;
    }

    public void setImageback(Integer imageback){
        this.background = imageback;
    }
    public Integer getImageback(){
        return this.background;
    }

    // getting username
    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    //getting password
    public ArrayList<Image> getImgPassword(){
        return this.objects;
    }
    public void setImgPassword(ArrayList<Image> imgpassword){
        this.objects = imgpassword;
    }


    public ArrayList<String> getGestarr(){
        return this.gesturearr;
    }
    public void setGestarr(ArrayList<String> gesturearr){
        this.gesturearr = gesturearr;
    }


//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(username);
//        parcel.writeInt(background);
//        parcel.writeStringList(gesturearr);
//        final int N = objects.size();
//        parcel.writeInt(N);
//        if(N > 0){
//            parcel.writeInt(N);
//            for(Image img: objects) {
//                parcel.writeSerializable(img);
//            }
//        }
//    }
//
//    protected User(Parcel in) {
//        username = in.readString();
//        background = in.readInt();
//        gesturearr = in.createStringArrayList();
//        final int N = in.readInt();
//        if( N > 0 ) {
//            for (int i = 0; i < N; i++) {
//                objects.add((Image) in.readSerializable());
//            }
//        }
//        else{
//            objects = new ArrayList<>();
//        }
//    }
//
//    public static final Creator<User> CREATOR = new Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel in) {
//            return new User(in);
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };

}
