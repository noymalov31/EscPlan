package com.esc_plan.escplan.db;

import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Class which holds the user private information, will be serialized localy,
 * only object containing relevant ranking information (ranker) will be
 * uploaded to the cloud (to find correlated users).
 */

public class Escaper implements Serializable{

    /* id */
    private String id;

    /* all rooms completed by this escaper */
    private ArrayList<PrivateRoom> rooms;

    /* all rooms scheduled by escaper */
    private HashMap<String, Date> scheduled;

    /* tuples of (roomId,rank), helpful for ranking */
    private HashMap<String, Float> ranker;

    public Escaper(String id) {
        this.id = id;
        rooms = new ArrayList<>();
        ranker = new HashMap<>();
    }

    public void syncToFB(DatabaseReference rankRef, DatabaseReference privateRef) {
        rankRef.child(id).setValue(ranker);
        privateRef.child(id).child("rooms").setValue(rooms);
        privateRef.child(id).child("scheduled").setValue(scheduled);
    }

    public void addRoom(PrivateRoom room) {
        rooms.add(room);
        ranker.put(room.getPublicRoomLink(), room.getRating());
    }

    public void removeRoom(PrivateRoom room) {
        rooms.remove(room);
        ranker.remove(room.getPublicRoomLink());
    }

    public void schedule(PrivateRoom room, Date date) {
        scheduled.put(room.getPublicRoomLink(), date);
    }

    public void unschedule(PrivateRoom room) {
        scheduled.remove(room.getPublicRoomLink());
    }

    /**
     * function which calculates how similar are this escaper and other escaper,
     * using simple correlation of rooms and thier ranking
     * @param other - Escaper to calculate the correlation with
     * @return float in the range [0,1]
     */
    public float userCorrelation(Escaper other) {
        return 0;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    }
}
