package com.esc_plan.escplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esc_plan.escplan.db.PublicRoom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by noy on 05/08/2017.
 */

public class TodoList extends AppCompatActivity {
    private ListView list;
    private  ArrayList<PublicRoom> todoListItems ;
    private ToDoListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list);


        list = (ListView) findViewById(R.id.list);
        todoListItems = MainActivity.escaper().getTodoRooms();
        adapter = new ToDoListAdapter(getApplicationContext(), R.layout.todo_item, todoListItems);
        list.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null ) {
            String room_name = bundle.getString("room_name");
            if (room_name != null) {
                PublicRoom toAdd = getRoomByName(room_name);
                if (toAdd != null) {
                    adapter.add(toAdd);
                    adapter.notifyDataSetChanged();
                } else {
                    final AlertDialog.Builder err = new AlertDialog.Builder(TodoList.this);
                    err.setTitle("שגיאה!");
                    err.setMessage("החדר שניסית להוסיף אינו קיים. נא נסה שנית");
                    err.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    err.show();
                }
            }
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent i = new Intent(TodoList.this, Public_room.class);
                String room_name = todoListItems.get(position).getName();
                Bundle bundle = new Bundle();
                bundle.putString("room_name", room_name);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(TodoList.this, R.style.AlertDialogCustom));
                final PublicRoom curr_room = adapter.getItem(position);

                Date scheduled = MainActivity.escaper().getScheduledDate(curr_room);
                if (scheduled == null){
                    b.setNeutralButton("הוסף תזכורת", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            final DatePicker dp = new DatePicker(TodoList.this);
                            LinearLayout layout = new LinearLayout(TodoList.this);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.addView(dp);

                            new AlertDialog.Builder(TodoList.this)
                                    .setTitle("בחר תאריך")
                                    .setView(layout)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int day = dp.getDayOfMonth();
                                            int month = dp.getMonth();
                                            int year =  dp.getYear();
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(year, month, day);

                                            MainActivity.escaper().schedule(curr_room, calendar.getTime());
                                            Toast.makeText(TodoList.this, "Date added successfully ", Toast.LENGTH_SHORT).show();
                                            View v = list.getChildAt(position);
                                            ImageView iv = (ImageView)v.findViewById(R.id.todo_item_img);
                                            iv.setImageResource(R.drawable.booked);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(TodoList.this, "oh no..", Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();
                        }
                    });
                }
                else {
                    String msg = "נקבע לתאריך :";
                    msg += scheduled.toString();
                    b.setMessage(msg);
                }

                b.setNegativeButton("מחק", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        adapter.deleteByPos(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                b.setPositiveButton("הוסף לרשימה שלי", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(TodoList.this, AddRoomList.class);
                        String room_name = todoListItems.get(position).getName();
                        Bundle bundle = new Bundle();
                        bundle.putString("room_name", room_name);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });



                b.show();
                return true;
            }
        });



        android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(TodoList.this,AddRoomTodo.class);
                startActivity(i);
            }
        });

    }

    public PublicRoom getRoomByName(String name){
        ArrayList<PublicRoom> all_rooms = MainActivity.escaper().getAllRooms();
        for (int i=0; i < all_rooms.size(); i++){

            if (name.equals(all_rooms.get(i).getName())){
                return all_rooms.get(i);
            }
        }
        return null;
    }

}
