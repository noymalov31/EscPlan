package com.esc_plan.escplan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by noy on 19/08/2017.
 */

public class Public_room extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_room);



    }

    //phone number click
    public void onClickPhone(View v)
    {
        String phone_number =  v.findViewById(R.id.phone_num_value).toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone_number));
        startActivity(intent);
    }
}
