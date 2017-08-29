package com.esc_plan.escplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.esc_plan.escplan.db.PublicRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noy on 09/08/2017.
 */

public class AddRoomTodo extends AppCompatActivity {
    AutoCompleteTextView autoCompleteText;
    List<String> rooms =  MainActivity.escaper().getAllRoomsNames();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_room_todo);
        Bundle bundle = getIntent().getExtras();
        String room_name = "";
        if(bundle != null ) {
            room_name = bundle.getString("room_name");
        }


        autoCompleteText=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        autoCompleteText.setText(room_name);

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rooms.toArray());

        autoCompleteText.setAdapter(adapter);
        autoCompleteText.setThreshold(1);

        Button todo_list_btn=(Button)findViewById(R.id.add_button);
        todo_list_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = autoCompleteText.getText().toString();
                Intent i = new Intent(AddRoomTodo.this,TodoList.class);
                Bundle bundle = new Bundle();
                bundle.putString("room_name", name);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }
}
