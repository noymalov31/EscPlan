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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.esc_plan.escplan.R;
import com.esc_plan.escplan.db.PublicRoom;

import java.util.ArrayList;

/**
 * Created by noy on 25/08/2017.
 */

public class RecommendedAdapter  extends ArrayAdapter<PublicRoom> {

    private TextView roomName;
    private ProgressBar progressBar;
    private ArrayList<PublicRoom> listOfItems;
    private Context context;


    public RecommendedAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PublicRoom> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listOfItems = objects;

    }

    public void add(PublicRoom item, String Key) {
        super.add(item);
    }



    public void deleteItem(int index){
        this.listOfItems.remove(index);
    }

    public PublicRoom getItem(int index){

        return this.listOfItems.get(index);
    }

    public View getView (int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null){
            LayoutInflater inflatter = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflatter.inflate(R.layout.recommended_item, parent, false);
        }
        PublicRoom curr_room = getItem(position);
        roomName = (TextView) v.findViewById(R.id.name);
        roomName.setText(curr_room.getName());
        progressBar = (ProgressBar) v.findViewById(R.id.matching);
        progressBar.setProgress(MainActivity.escaper().getCoorelation(curr_room));

        return v;
    }

    public Bitmap decodeToBitmap (byte[] decodedBytes){
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}

