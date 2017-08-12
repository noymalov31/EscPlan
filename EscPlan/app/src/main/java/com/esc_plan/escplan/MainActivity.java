package com.esc_plan.escplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Button my_list_btn=(Button)findViewById(R.id.MyList_button);
        my_list_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,my_list.class);
                startActivity(i);
            }
        });

        Button todo_list_btn=(Button)findViewById(R.id.TODO_button);
        todo_list_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,TodoList.class);
                startActivity(i);
            }
        });

        Button all_rooms_btn=(Button)findViewById(R.id.allRooms_button);
        all_rooms_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AllRooms.class);
                startActivity(i);
            }
        });

        Button recommended_btn=(Button)findViewById(R.id.Recommended_button);
        recommended_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Recommended.class);
                startActivity(i);
            }
        });



    }
}
