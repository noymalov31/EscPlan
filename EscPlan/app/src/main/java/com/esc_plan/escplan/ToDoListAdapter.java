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
import android.widget.ImageView;
import android.widget.TextView;

import com.esc_plan.escplan.R;
import com.esc_plan.escplan.db.PublicRoom;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by noy on 25/08/2017.
 */

public class ToDoListAdapter  extends ArrayAdapter<PublicRoom> {

    private TextView roomName;
    private ArrayList<PublicRoom> listOfItems;
    private Context context;


    public ToDoListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PublicRoom> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listOfItems = objects;

    }

    public void add(PublicRoom item) {
        MainActivity.escaper().todo(item);
    }

    public void deleteByPos(int index){
        PublicRoom toDelete = getItem(index);
        deleteItem(toDelete);
    }

    public void deleteItem(PublicRoom item){

        MainActivity.escaper().untodo(item);
    }

    public PublicRoom getItem(int index){

        return this.listOfItems.get(index);
    }



   public View getView (int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null){
            LayoutInflater inflatter = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflatter.inflate(R.layout.todo_item, parent, false);
        }
        PublicRoom curr_room = getItem(position);
        roomName = (TextView) v.findViewById(R.id.name);
        roomName.setText(curr_room.getName());
        Date scheduled = MainActivity.escaper().getScheduledDate(curr_room);
        ImageView iv = (ImageView) v.findViewById(R.id.todo_item_img);
        if (scheduled == null) {
            iv.setImageResource(R.drawable.not_booked);
        } else {
            iv.setImageResource(R.drawable.booked);

        }
        return v;
    }

    public Bitmap decodeToBitmap (byte[] decodedBytes){
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public int getSize(){
        return listOfItems.size();
    }
}
