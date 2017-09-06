package com.esc_plan.escplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.esc_plan.escplan.db.PublicRoom;

import java.util.ArrayList;

/**
 * Created by noy on 06/08/2017.
 */

public class AllRooms  extends AppCompatActivity {

    private ListView list;
    private ArrayList<PublicRoom> allRoomsListItems ;
    private AllRoomsListAdapter adapter;

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
                final AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(AllRooms.this, R.style.AlertDialogCustom));
                b.setNegativeButton("ראה פרטים", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(AllRooms.this, PublicRoomPage.class);
                        String room_name = allRoomsListItems.get(position).getName();
                        Bundle bundle = new Bundle();
                        bundle.putString("room_name", room_name);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });

                b.setPositiveButton("הוסף ל- TODO LIST", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Intent i = new Intent(AllRooms.this, AddRoomTodo.class);
                        String room_name = allRoomsListItems.get(position).getName();
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

        Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.search_edittext);
                String search_content = et.getText().toString();
                for (int i=0; i< allRoomsListItems.size(); i++){
                    if (!(adapter.getItem(i).getName().toLowerCase().contains(search_content.toLowerCase()))) {
                        View curr_item = list.getChildAt(i);
                        curr_item.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                        curr_item.setVisibility(View.GONE);
                    }
                }
            }
        });

        Button goto_menu = (Button) findViewById(R.id.gotomenu);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllRooms.this,MainActivity.class);
                startActivity(i);
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
