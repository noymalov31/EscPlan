package com.esc_plan.escplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.esc_plan.escplan.db.PrivateRoom;


import java.util.ArrayList;

/**
 * Created by noy on 05/08/2017.
 */

public class MyList extends AppCompatActivity {
    private ListView list;
    private ArrayList<PrivateRoom> myListItems ;
    private MyListAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_list);


        list = (ListView) findViewById(R.id.list);
        myListItems = MainActivity.escaper().getMyRooms();
        adapter = new MyListAdapter(getApplicationContext(), R.layout.mylist_item, myListItems);
        list.setAdapter(adapter);


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder b = new AlertDialog.Builder(MyList.this);

                b.setNegativeButton("מחק", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        adapter.deleteByPos(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                b.setPositiveButton("ראה פרטים", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(MyList.this, Private_room.class);
                        String room_name = myListItems.get(position).getName();
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
                Intent i = new Intent(MyList.this,AddRoomList.class);
                startActivity(i);
            }
        });

    }
}
