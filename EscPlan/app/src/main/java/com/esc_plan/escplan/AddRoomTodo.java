package com.esc_plan.escplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.esc_plan.escplan.db.PublicRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noy on 09/08/2017.
 */

public class AddRoomTodo extends AppCompatActivity {
    AutoCompleteTextView autoCompleteText;
    ArrayList<PublicRoom> rooms =  MainActivity.escaper().getAllRooms();
    private PublicRoom selectedRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_room_todo);

        autoCompleteText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        final ArrayAdapter adapter = new AutocompleteAdapter(this, R.layout.autocomp_item, rooms);
        autoCompleteText.setAdapter(adapter);
        autoCompleteText.setThreshold(1);


        autoCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRoom = (PublicRoom) adapterView.getAdapter().getItem(i);
            }
        });

        Button todo_list_btn = (Button)findViewById(R.id.add_button);
        todo_list_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.escaper().todo(selectedRoom);

                Toast.makeText(AddRoomTodo.this, selectedRoom.getName() +
                        "התווסף בהצלחה!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
