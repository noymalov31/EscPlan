package com.esc_plan.escplan;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
    private TextView matching_presents;
    private ProgressBar progressBar;
    private ArrayList<PublicRoom> listOfItems;
    private Context context;


    public RecommendedAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PublicRoom> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listOfItems = objects;

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
        Integer coorelation = MainActivity.escaper().getCoorelation(curr_room);
        matching_presents = (TextView) v.findViewById(R.id.present);
        matching_presents.setText(String.valueOf(coorelation)+"% ");
        progressBar = (ProgressBar) v.findViewById(R.id.matching);
        progressBar.setProgress(coorelation);
        if (coorelation < 50) {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }
        else if (coorelation > 85) {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));

        }


        return v;
    }

    public Bitmap decodeToBitmap (byte[] decodedBytes){
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}

