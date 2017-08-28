package com.esc_plan.escplan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esc_plan.escplan.db.PublicRoom;

import java.util.ArrayList;

/**
 * Created by noy on 19/08/2017.
 */

public class Public_room extends AppCompatActivity {
    PublicRoom curr_room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_room);
        Bundle bundle = getIntent().getExtras();
        String room_name = bundle.getString("room_name");
        ArrayList<PublicRoom> all_rooms = MainActivity.escaper().getAllRooms();
        for (int i=0; i < all_rooms.size(); i++){
            if (room_name.equals(all_rooms.get(i).getName())){
                curr_room = all_rooms.get(i);
                break;
            }
        }

        TextView name = (TextView) findViewById(R.id.name_value);
        name.setText(curr_room.getName());
        TextView rate = (TextView) findViewById(R.id.rate_value);
        rate.setText(Float.toString(curr_room.getRating()));
        TextView address = (TextView) findViewById(R.id.adress_value);
        address.setText(curr_room.getAddress());
        TextView phone_num = (TextView) findViewById(R.id.phone_num_value);
        phone_num.setText(curr_room.getPhone());
        TextView website = (TextView) findViewById(R.id.website_value);
        website.setText(curr_room.getURL());
        TextView genre = (TextView) findViewById(R.id.genre_value);
        genre.setText(String.valueOf(curr_room.getGenre()));
        TextView reviews = (TextView) findViewById(R.id.reviews_value);
        String all_reviews = "";
        for (int i=0; i < curr_room.getReviews().size(); i++){
            all_reviews += curr_room.getReviews().get(i) + "\n";

        }
        reviews.setText(all_reviews );
        TextView similar_rooms_value = (TextView) findViewById(R.id.similar_rooms_value);
        String all_similar = "";
        if (curr_room.getSimilarRooms() != null) {
            for (int i = 0; i < curr_room.getSimilarRooms().size(); i++) {
                all_similar += curr_room.getSimilarRooms().get(i) + "\n";

            }
            similar_rooms_value.setText(all_similar);
        }

        Button to_todo = (Button) findViewById(R.id.add_to_todo);
        to_todo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Public_room.this, AddRoomTodo.class);
                Bundle bundle = new Bundle();
                bundle.putString("room_name", curr_room.getName());
                i.putExtras(bundle);
                startActivity(i);
            }
        });






    }

    //phone number click
    public void onClickPhone(View v)
    {
        TextView phone = (TextView) v.findViewById(R.id.phone_num_value);
        String phone_number = phone.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone_number));
        startActivity(intent);
    }

    //website click
    public void onClickWebsite(View v){

        TextView website = (TextView) v.findViewById(R.id.website_value);
        String url = website.getText().toString();
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://"+url));
        startActivity(i);
    }
}
