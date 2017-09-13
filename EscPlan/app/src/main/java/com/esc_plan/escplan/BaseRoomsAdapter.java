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

import com.esc_plan.escplan.db.PublicRoom;

import java.util.ArrayList;

/**
 * Created by noy on 25/08/2017.
 */

public class BaseRoomsAdapter extends ArrayAdapter<Integer> {

    private TextView roomName;

    public BaseRoomsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Integer> objects) {
        super(context, resource, new ArrayList<>(objects));

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflatter = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflatter.inflate(R.layout.base_list_item, parent, false);
        }
        PublicRoom curr_room = MainActivity.escaper().getAllRooms().get(getItem(position));
        roomName = (TextView) v.findViewById(R.id.text1);
        roomName.setText(curr_room.getName());

        return v;
    }

    public Bitmap decodeToBitmap(byte[] decodedBytes) {
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}



