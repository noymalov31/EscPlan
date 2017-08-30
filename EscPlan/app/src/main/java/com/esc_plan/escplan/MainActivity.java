package com.esc_plan.escplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.esc_plan.escplan.db.Escaper;
import com.esc_plan.escplan.db.PrivateRoom;
import com.esc_plan.escplan.db.PublicRoom;
import com.esc_plan.escplan.db.Room;


public class MainActivity extends AppCompatActivity {

    private static String userId = "Nisan"; //generated by authentication: FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static Escaper escaper = null;

    public static Escaper escaper() {
        return escaper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        escaper = new Escaper(userId);
        Button my_list_btn=(Button)findViewById(R.id.MyList_button);
        my_list_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MyList.class);
                startActivity(i);
                //PublicRoom publicRoom = new PublicRoom("סודו של הענק", 7.4f,123, Room.Genre.Fantasy,"pum", "054","add");
                //escaper.addPublicRoom(publicRoom);

//                PublicRoom publicRoom = escaper.getAllRooms().get(1);
//                PrivateRoom pr = new PrivateRoom(publicRoom, 4f);
//                pr.setReview("like it threeee3!");
//                pr.setTime(200);
//                escaper.addPrivateRoom(pr);

//                escaper.saveImage(publicRoom, "http://blog.nowescape.com/wp-content/uploads/2016/03/escape1.jpg");
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
}
