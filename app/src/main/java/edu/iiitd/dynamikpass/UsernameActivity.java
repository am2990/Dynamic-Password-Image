package edu.iiitd.dynamikpass;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.iiitd.dynamikpass.model.Image;
import edu.iiitd.dynamikpass.model.User;
import edu.iiitd.dynamikpass.utils.DatabaseHelper;

public class UsernameActivity extends Activity {
    EditText username;
    Button bcontinue;

   User user = new User();
    DatabaseHelper db;

    String checkuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username_layout);

        username = (EditText) findViewById(R.id.username);
        bcontinue = (Button) findViewById(R.id.bcontinue);


        db = new DatabaseHelper(this);

        bcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String u = username.getText().toString();
                System.out.println("u: " + u);
                if (!(u.equals(""))){
                    user.setUsername(u);
                    //TODO the function name should be addUser
                    // db.addUsername(user);

                    // DatabaseHelper db = new DatabaseHelper(Context);
                    //db.getUserByName(u)
                    if (true) {
                        checkuser = "false";
                        Intent intent = new Intent();
                        intent.setClass(UsernameActivity.this, GalleryView.class);
                        user.setUsername(u);
                        intent.putExtra("usern", user);
                        intent.putExtra("checkuser", checkuser);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Login!", Toast.LENGTH_SHORT).show();
                        checkuser = "true";
                        //String str_selected = db.getSelectedByName(u);
                        //String str_notSelected = db.getNselectedByName(u);


                        Intent intent = new Intent();
                        intent.setClass(UsernameActivity.this, LoginActivity.class);
                        intent.putExtra("usern", u);
                        intent.putExtra("checkuser", checkuser);
                        //intent.putStringArrayListExtra("selected", selected);
                        //intent.putStringArrayListExtra("notSelected", notSelected);
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter a username!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
