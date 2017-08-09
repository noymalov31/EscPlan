package com.esc_plan.escplan.db;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * defines general information every room should be able to return,
 * mainly for lists display (all rooms, visited rooms etc.)
 */
public interface Room {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");

    enum Genre {
        General, Kids, Horror, Military;
        public static final int length = Genre.values().length;
    }

    String getName();

    float getRating();

    int getTime();

    float getDifficulty();

    Genre getGenre();

    Map<String,Integer> getReviewsBag();

}
