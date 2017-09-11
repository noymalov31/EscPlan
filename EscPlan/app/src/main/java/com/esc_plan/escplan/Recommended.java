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
import android.widget.ListView;
import android.widget.Toast;

import com.esc_plan.escplan.db.PublicRoom;
import com.esc_plan.escplan.db.Room;

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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent i = new Intent(Recommended.this, PublicRoomPage.class);
                Bundle bundle = new Bundle();

                bundle.putInt(getString(R.string.ROOM_TYPE), Room.Type.RECOMMENDED.ordinal());
                bundle.putInt(getString(R.string.ROOM_POS), position);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(Recommended.this, R.style.AlertDialogCustom));

                b.setPositiveButton("הוסף ל- TODO LIST", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.escaper().todo(recommendedItems.get(position));

                        Toast.makeText(Recommended.this, recommendedItems.get(position).getName() +
                                        " התווסף בהצלחה!", Toast.LENGTH_SHORT).show();

                    }
                });
                b.show();
                return true;
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
