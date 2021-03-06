package com.esc_plan.escplan;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esc_plan.escplan.db.Escaper;
import com.esc_plan.escplan.db.PublicRoom;
import com.esc_plan.escplan.db.Room;

import java.util.ArrayList;

import static com.esc_plan.escplan.db.Room.Type;

/**
 * Created by noy on 19/08/2017.
 */

public class PublicRoomPage extends AppCompatActivity {
    PublicRoom curr_room;
    String src;
    int roomIndex;
    private BaseRoomsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_room);

        Bundle bundle = getIntent().getExtras();
        roomIndex = bundle.getInt(getString(R.string.ROOM_POS));
        switch (Type.vals[bundle.getInt(getString(R.string.ROOM_TYPE))]) {
            case ALL:
                curr_room = MainActivity.escaper().getAllRooms().get(roomIndex);
                break;
            case TODO:
                curr_room = MainActivity.escaper().getTodoRooms().get(roomIndex);
                break;
            case RECOMMENDED:
                curr_room = MainActivity.escaper().getRecommendedRooms().get(roomIndex);
                break;
            case MINE:
                return;
        }
        src = bundle.getString("src");
        Button btn = (Button)  findViewById(R.id.add_to_todo);
        if (src.equals("TODO")){
            btn.setBackgroundResource(R.drawable.add_to_list_btn);
        }

        TextView name = (TextView) findViewById(R.id.name_value);
        name.setText(curr_room.getName());
        TextView rate = (TextView) findViewById(R.id.rate_value);
        rate.setText(Float.toString(curr_room.getRating()));
        TextView time = (TextView) findViewById(R.id.time_value);
        time.setText(Float.toString(curr_room.getTime()));
        TextView address = (TextView) findViewById(R.id.adress_value);
        address.setText(curr_room.getAddress());
        TextView phone_num = (TextView) findViewById(R.id.phone_num_value);
        phone_num.setPaintFlags(phone_num.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        phone_num.setText(curr_room.getPhone());
        TextView website = (TextView) findViewById(R.id.website_value);
        website.setPaintFlags(website.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView genre = (TextView) findViewById(R.id.genre_value);
        genre.setText(curr_room.getGenre().getHebName());
        TextView reviews = (TextView) findViewById(R.id.reviews_value);
        String all_reviews = "";

        ImageView iv = (ImageView) findViewById(R.id.room_image);
        MainActivity.escaper().getImage(PublicRoomPage.this, curr_room, iv);

        if (curr_room.getReviews() != null) {
            for (int i = 0; i < curr_room.getReviews().size(); i++) {
                all_reviews += curr_room.getReviews().get(i) + "\n";

            }
            reviews.setText(all_reviews);
        }
        ListView similar_rooms_value = (ListView) findViewById(R.id.similar_rooms_value);
        ArrayList<Integer> all_similar = new ArrayList();
        if (curr_room.getSimilarRooms() != null) {
            for (int i = 0; i < curr_room.getSimilarRooms().size(); i++) {
                all_similar.add(MainActivity.escaper().getPosById(curr_room.getSimilarRooms().get(i)));

            }
            adapter = new BaseRoomsAdapter(getApplicationContext(), R.layout.base_list_item, all_similar);
            similar_rooms_value.setAdapter(adapter);


            similar_rooms_value.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    Intent i = new Intent(PublicRoomPage.this, PublicRoomPage.class);
                    Bundle bundle = new Bundle();

                    bundle.putInt(getString(R.string.ROOM_TYPE), Type.ALL.ordinal());
                    bundle.putInt(getString(R.string.ROOM_POS), adapter.getItem(position));
                    bundle.putString("src", "All");
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
            });
        }

        Button to_todo = (Button) findViewById(R.id.add_to_todo);
        to_todo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (src.equals("All")) {
                    MainActivity.escaper().todo(curr_room);

                    Toast.makeText(PublicRoomPage.this, curr_room.getName() +
                            " התווסף בהצלחה!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PublicRoomPage.this, TodoList.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Intent i = new Intent(PublicRoomPage.this, AddRoomList.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(getString(R.string.ROOM_TYPE), Room.Type.MINE.ordinal());
                    bundle.putInt(getString(R.string.ROOM_POS), roomIndex);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
            }
        });



        Button goto_menu = (Button) findViewById(R.id.gotomenu);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(PublicRoomPage.this,MainActivity.class);
                startActivity(i);
                finish();
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
        String url = curr_room.getURL().toString();
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(i);
    }
}
