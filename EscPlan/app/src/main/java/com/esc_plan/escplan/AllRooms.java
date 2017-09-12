package com.esc_plan.escplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esc_plan.escplan.db.PublicRoom;
import com.esc_plan.escplan.db.Room;
import com.esc_plan.escplan.db.Room.Type;

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


        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent i = new Intent(AllRooms.this, PublicRoomPage.class);
                Bundle bundle = new Bundle();

                bundle.putInt(getString(R.string.ROOM_TYPE), Type.ALL.ordinal());
                bundle.putInt(getString(R.string.ROOM_POS), adapter.getRealIndex(position));
                bundle.putString("src", "All");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(AllRooms.this, R.style.AlertDialogCustom));

                b.setPositiveButton("הוסף ל- TODO LIST", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.escaper().todo(adapter.getItem(position));

                        Toast.makeText(AllRooms.this, adapter.getItem(position).getName() +
                                " התווסף בהצלחה!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AllRooms.this, TodoList.class);
                        startActivity(i);
                        finish();

                    }
                });
                b.setNeutralButton("הוסף לרשימה שלי", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(AllRooms.this, AddRoomList.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(getString(R.string.ROOM_TYPE), Room.Type.MINE.ordinal());
                        bundle.putInt(getString(R.string.ROOM_POS), position);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });


                b.show();
                return true;
            }
        });


        TextView searchBox = (TextView) findViewById(R.id.search_edittext);
        searchBox.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) { }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                adapter.filterList(s);
            }
        });

        Button goto_menu = (Button) findViewById(R.id.gotomenu);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllRooms.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.escaper().setCurrAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.escaper().removeCurrAdapter();
    }
}
