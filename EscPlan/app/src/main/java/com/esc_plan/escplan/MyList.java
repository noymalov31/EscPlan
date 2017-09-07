package com.esc_plan.escplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.esc_plan.escplan.db.PrivateRoom;
import com.esc_plan.escplan.db.Room;


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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent i = new Intent(MyList.this, PrivateRoomPage.class);
                Bundle bundle = new Bundle();

                bundle.putInt(getString(R.string.ROOM_POS), position);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(MyList.this, R.style.AlertDialogCustom));

                b.setNegativeButton("מחק", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.escaper().removePrivateRoom(myListItems.get(position));
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

        Button goto_menu = (Button) findViewById(R.id.gotomenu);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyList.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.escaper().setCurrAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.escaper().removeCurrAdapter();
    }
}
