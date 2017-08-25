package com.esc_plan.escplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.esc_plan.escplan.db.Escaper;
import com.esc_plan.escplan.db.PublicRoom;
import com.esc_plan.escplan.db.Room;

import java.util.ArrayList;

/**
 * Created by noy on 06/08/2017.
 */

public class AllRooms  extends AppCompatActivity {

    private ListView list;
    private ArrayList<PublicRoom> allRoomsListItems ;
    private ArrayAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_rooms);

        list = (ListView) findViewById(R.id.list);
        allRoomsListItems = MainActivity.escaper().getAllRooms();
        adapter = new AllRoomsListAdapter(getApplicationContext(), R.layout.all_rooms_item, allRoomsListItems);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder b = new AlertDialog.Builder(AllRooms.this);

                b.setNegativeButton("ראה פרטים", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                b.setPositiveButton("הוסף ל- TODO LIST", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });


                b.show();
                return true;
            }
        });

    }
}
