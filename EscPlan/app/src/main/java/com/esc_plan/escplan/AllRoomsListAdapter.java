package com.esc_plan.escplan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esc_plan.escplan.db.Escaper;
import com.esc_plan.escplan.db.PublicRoom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by noy on 25/08/2017.
 */

public class AllRoomsListAdapter extends ArrayAdapter<PublicRoom> {

    private TextView roomName;
    private TextView roomRate;
    private ArrayList<PublicRoom> listOfItems;
    private Context context;
    private ArrayList<Integer> realIndex;

    public AllRoomsListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PublicRoom> objects) {
        super(context, resource, new ArrayList<>(objects));
        this.context = context;
        this.listOfItems = objects;

    }

    public View getView (int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null){
            LayoutInflater inflatter = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflatter.inflate(R.layout.all_rooms_item, parent, false);
        }
        PublicRoom curr_room = getItem(position);
        roomName = (TextView) v.findViewById(R.id.name);
        roomName.setText(curr_room.getName());
        roomRate = (TextView) v.findViewById(R.id.room_rate);
        //roomRate.setText(Float.toString(curr_room.getRating()));
        roomRate.setText(String.format("%.2f",curr_room.getRating()));

        return v;
    }

    public Bitmap decodeToBitmap (byte[] decodedBytes){
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void filterList(CharSequence s) {
        clear();
        if ((s == null) || (s.length() == 0)) {
            addAll(this.listOfItems);
            realIndex = null;
            return;
        }
        if (realIndex  == null) {
            realIndex = new ArrayList<>();
        } else {
            realIndex.clear();
        }
        for (int i = 0; i < listOfItems.size(); i++) {
            if (listOfItems.get(i).getName().contains(s.toString())) {
                add(listOfItems.get(i));
                realIndex.add(i);
            }
        }
        notifyDataSetChanged();
    }
    public int getRealIndex(int i){
        return (realIndex == null) ? i:realIndex.get(i);
    }

}



