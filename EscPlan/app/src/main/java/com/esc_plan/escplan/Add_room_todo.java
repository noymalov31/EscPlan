package com.esc_plan.escplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

/**
 * Created by noy on 09/08/2017.
 */

public class Add_room_todo  extends AppCompatActivity {
    AutoCompleteTextView text;
    String[] rooms={"אוז","משחקי הגורל","סודו של הענק","מרסר 112","שוד המאה","אילומינטי","שוד היהלום"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_room_todo);

        text=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);

        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,rooms);

        text.setAdapter(adapter);
        text.setThreshold(1);

        Button todo_list_btn=(Button)findViewById(R.id.add_button);
        todo_list_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Add_room_todo.this,TodoList.class);
                startActivity(i);
            }
        });

    }
}
