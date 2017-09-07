package com.esc_plan.escplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.esc_plan.escplan.db.PrivateRoom;

import java.util.ArrayList;

/**
 * Created by noy on 26/08/2017.
 */

public class PrivateRoomPage extends AppCompatActivity {
    PrivateRoom my_room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_room);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null ) {
            my_room = MainActivity.escaper().getMyRooms()
                    .get(bundle.getInt(getString(R.string.ROOM_POS)));

            TextView name = (TextView) findViewById(R.id.name_value);
            name.setText(my_room.getName());
            TextView rate = (TextView) findViewById(R.id.rate_value);
            rate.setText(Float.toString(my_room.getRating()));

            //add image
            ImageView iv = (ImageView) findViewById(R.id.image_pr);
            MainActivity.escaper().getImage(PrivateRoomPage.this, my_room, iv);

            //add review
            if (my_room.getReview()!= null){
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mv = vi.inflate(R.layout.review_private, null);
                TextView review = (TextView) mv.findViewById(R.id.review_value);
                review.setText(my_room.getReview());
                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.review);
                insertPoint.addView(mv, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            //add note

            if (my_room.getNote() != null){
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mv = vi.inflate(R.layout.note_private, null);
                TextView note = (TextView) mv.findViewById(R.id.note_value);
                note.setText(my_room.getNote());
                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.note);
                insertPoint.addView(mv, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }



            //add address
            if (my_room.getAddress() != null){
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mv = vi.inflate(R.layout.address_private, null);
                TextView address = (TextView) mv.findViewById(R.id.address_value);
                address.setText(my_room.getAddress());
                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.address);
                insertPoint.addView(mv, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }


            //add date
            if (my_room.getDate() != null){
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mv = vi.inflate(R.layout.date_private, null);
                TextView date = (TextView) mv.findViewById(R.id.date_value);
                date.setText(my_room.getDate());
                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.date);
                insertPoint.addView(mv, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }


            //add partners
            if (my_room.getPartners() != null){
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mv = vi.inflate(R.layout.partners_private, null);
                TextView partners = (TextView) mv.findViewById(R.id.partners_value);
                partners.setText(my_room.getPartners());
                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.partners);
                insertPoint.addView(mv, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            //add time
            if (my_room.getTime() != 0){
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mv = vi.inflate(R.layout.time_private, null);
                TextView time = (TextView) mv.findViewById(R.id.time_value);
                time.setText(Integer.toString(my_room.getTime()));
                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.time);
                insertPoint.addView(mv, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

        }
        Button goto_menu = (Button) findViewById(R.id.gotomenu);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrivateRoomPage.this,MainActivity.class);
                startActivity(i);
            }
        });



    }
}
