package com.esc_plan.escplan;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.esc_plan.escplan.db.AuthActivity;
import com.esc_plan.escplan.db.Escaper;
import com.esc_plan.escplan.db.PrivateRoom;
import com.esc_plan.escplan.db.PublicRoom;
import com.esc_plan.escplan.db.Room;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {


    private static Escaper escaper = null;
    public static Escaper escaper() {
        return escaper;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthActivity authenticator = new AuthActivity();
        if (authenticator.getUserString() == null) {
            Intent i =new Intent(this, AuthActivity.class);
            startActivity(i);
            finish();
            return;
        }

        escaper = new Escaper(authenticator.getUserString());
        setContentView(R.layout.main_page);
        Button my_list_btn=(Button)findViewById(R.id.MyList_button);
        my_list_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MyList.class);
                startActivity(i);
            }
        });

        Button todo_list_btn=(Button)findViewById(R.id.TODO_button);
        todo_list_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,TodoList.class);
                startActivity(i);
            }
        });

        Button all_rooms_btn=(Button)findViewById(R.id.allRooms_button);
        all_rooms_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AllRooms.class);
                startActivity(i);
            }
        });

        Button recommended_btn=(Button)findViewById(R.id.Recommended_button);
        recommended_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Recommended.class);
                startActivity(i);
            }
        });

        Button add_btn=(Button)findViewById(R.id.addRoom_button);
        add_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddRoomList.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void create_db(){

            /**
            String line;
            while ((line = br.readLine()) != null) {
                String[] room_details = line.split(";");
                String name = room_details[0];
                Log.d(MainActivity.class.getSimpleName(), "name: " + name);
                String address = room_details[1];
                Log.d(MainActivity.class.getSimpleName(), "address: " + address);
                String website = room_details[2];
                Log.d(MainActivity.class.getSimpleName(), "website: " + website);
                String telephone = room_details[3];
                Log.d(MainActivity.class.getSimpleName(), "telephone: " + telephone);
                String pic = room_details[4];
                Log.d(MainActivity.class.getSimpleName(), "pic: " + pic);
                String genre = room_details[5];
                Log.d(MainActivity.class.getSimpleName(), "genre: " + genre);

            }
            */

        File sdcard = Environment.getExternalStorageDirectory();
//C:\Users\noy\AndroidStudioProjects\Git\EscPlan\app\src\main\java\com\esc_plan\escplan\room_db_90_rooms.txt
//Get the text file
        File file = new File(sdcard,"room_db_90_rooms.txt");

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            Log.d(MainActivity.class.getSimpleName(), "yayyyyyyyyyyy");
        }
        catch (IOException e) {
            Log.d(MainActivity.class.getSimpleName(), "FAILED!!!!00");
        }
    }

}
