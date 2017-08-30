package com.esc_plan.escplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.esc_plan.escplan.db.PublicRoom;

import java.util.ArrayList;

/**
 * Created by noy on 06/08/2017.
 */

public class Recommended extends AppCompatActivity {
    private ListView list;
    private ArrayList<PublicRoom> recommendedItems ;
    private ArrayAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommended);

        list = (ListView) findViewById(R.id.list);
        recommendedItems = MainActivity.escaper().getRecommendedRooms();
        adapter = new RecommendedAdapter(getApplicationContext(), R.layout.recommended_item, recommendedItems);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(Recommended.this, R.style.AlertDialogCustom));

                b.setNegativeButton("ראה פרטים", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(Recommended.this, Public_room.class);
                        String room_name = recommendedItems.get(position).getName();
                        Bundle bundle = new Bundle();
                        bundle.putString("room_name", room_name);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });

                b.setPositiveButton("הוסף ל- TODO LIST", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(Recommended.this, AddRoomTodo.class);
                        String room_name = recommendedItems.get(position).getName();
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

    }
}
