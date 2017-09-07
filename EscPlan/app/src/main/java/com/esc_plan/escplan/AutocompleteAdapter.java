package com.esc_plan.escplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.esc_plan.escplan.db.PublicRoom;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteAdapter extends ArrayAdapter<PublicRoom> {

    private LayoutInflater layoutInflater;
    private List<PublicRoom> rooms;
//    private List<Integer> realIndex;

/*    public int getRealIndex(int i) {
        return realIndex.get(i);
    }*/
    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((PublicRoom)resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
//            realIndex = new ArrayList<>();

            if (constraint != null) {
                ArrayList<PublicRoom> suggestions = new ArrayList<>();
                for (int i = 0; i < rooms.size(); i++) {

                    // Note: change the "contains" to "startsWith" if you only want starting matches
                    if (rooms.get(i).getName().contains(constraint.toString())) {
                        suggestions.add(rooms.get(i));
//                        realIndex.add(i);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<PublicRoom>) results.values);
            } else {
                // no filter, add entire original list back in
//                addAll(rooms);
            }
            notifyDataSetChanged();
        }
    };

    public AutocompleteAdapter(Context context, int textViewResourceId, List<PublicRoom> rooms) {
        super(context, textViewResourceId, new ArrayList<>(rooms));
        // copy all the customers into a master list
        this.rooms = rooms;
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.autocomp_item, null);
        }

        PublicRoom room = getItem(position);

        TextView name = (TextView) view.findViewById(R.id.autocomp_text);
        name.setText(room.getName());

        return view;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
}