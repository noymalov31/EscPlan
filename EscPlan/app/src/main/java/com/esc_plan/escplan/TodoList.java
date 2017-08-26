package com.esc_plan.escplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.esc_plan.escplan.db.PublicRoom;
import java.util.ArrayList;

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


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder b = new AlertDialog.Builder(TodoList.this);

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

                b.setNeutralButton("פרטי החדר", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(TodoList.this, Public_room.class);
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
