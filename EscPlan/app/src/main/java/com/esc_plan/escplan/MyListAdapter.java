package com.esc_plan.escplan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.esc_plan.escplan.db.PrivateRoom;
import com.esc_plan.escplan.db.PublicRoom;

import java.util.ArrayList;

/**
 * Created by noy on 25/08/2017.
 */

public class MyListAdapter extends ArrayAdapter<PrivateRoom> {

    private TextView roomName;
    private TextView roomPos;
    private ArrayList<PrivateRoom> listOfItems;
    private Context context;


    public MyListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PrivateRoom> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listOfItems = objects;

    }

    public View getView (int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null){
            LayoutInflater inflatter = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflatter.inflate(R.layout.mylist_item, parent, false);
        }
        PrivateRoom curr_room = getItem(position);
        roomName = (TextView) v.findViewById(R.id.name);
        roomName.setText(curr_room.getName());
        roomPos = (TextView) v.findViewById(R.id.position);
        roomPos.setText(Integer.toString(position+1) + ". ");
        return v;
    }

    public Bitmap decodeToBitmap (byte[] decodedBytes){
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
