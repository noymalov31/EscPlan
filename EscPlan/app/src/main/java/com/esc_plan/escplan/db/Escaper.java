package com.esc_plan.escplan.db;

import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Class which holds the user private information, will be serialized localy,
 * only object containing relevant ranking information (ranker) will be
 * uploaded to the cloud (to find correlated users).
 */

public class Escaper implements Serializable{

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference dbRefAllRooms = null;
    private DatabaseReference dbRefMyRooms = null;
    private DatabaseReference dbRefTodoRooms = null;
    private DatabaseReference dbRefRanks = null;

    private Query allRoomsQuery = null;
    private Query myRoomsQuery = null;
    private Query todoRoomsQuery = null;

    /* id */
    private String id;

    /* all rooms completed by this escaper */
    private ArrayList<PrivateRoom> myRooms;

    /* all rooms to do by this escaper */
    private ArrayList<PublicRoom> todoRooms;

    /* all rooms */
    private ArrayList<PublicRoom> allRooms;

    /* rooms to do by escaper (date if scheduled) */
    private TreeMap<String, Date> todo;

    /* tuples of (roomId,rank), helpful for ranking */
    private HashMap<String, Float> ranker;

    public Escaper() { }

    public Escaper(String id) {
        this.id = id;
        myRooms = new ArrayList<>();
        todoRooms = new ArrayList<>();
        allRooms = new ArrayList<>();
        ranker = new HashMap<>();
        todo = new TreeMap<>();

        dbRefRanks = dbRef.child("ranks").child(id);
        dbRefTodoRooms = dbRef.child(id).child("todo");
        dbRefMyRooms = dbRef.child(id).child("rooms");
        dbRefAllRooms = dbRef.child("public");

        myRoomsQuery = dbRefMyRooms.orderByChild("name");
        allRoomsQuery = dbRefAllRooms.orderByChild("name");
        todoRoomsQuery = dbRefTodoRooms.orderByChild("name");
        setAllRoomsEvents();
        setMyRoomsEvents();
        setTodoEvents();
        setRankersEvents();
    }

    /**
     * @return "my rooms" list
     */
    public ArrayList<PrivateRoom> getMyRooms() {
        return myRooms;
    }

    /**
     * @return "all rooms" list
     */
    public ArrayList<PublicRoom> getAllRooms() {
        return allRooms;
    }

    /**
     * @return "TO DO" list
     */
    public ArrayList<PublicRoom> getTodoRooms() {
        return todoRooms;
    }

    /**
     * should be called before app terminates
     */
    private void syncToFB() {
        dbRefRanks.setValue(ranker);
        dbRefTodoRooms.setValue(todo);
    }

    public void addPrivateRoom(PrivateRoom room) {
        dbRefMyRooms.child(room.getPublicRoomLink()).setValue(room);
        ranker.put(room.getPublicRoomLink(), room.getRating());
    }

    public void removePrivateRoom(PrivateRoom room) {
        dbRefMyRooms.child(room.getPublicRoomLink()).setValue(null);
        ranker.remove(room.getPublicRoomLink());
    }

    public void addPublicRoom(PublicRoom room) {
        dbRefAllRooms.child(room.genId(dbRefAllRooms)).setValue(room);
    }

    public void removePublicRoom(PublicRoom room) {
        dbRefAllRooms.child(room.genId(dbRefAllRooms)).setValue(null);
    }

    public void schedule(PublicRoom room, Date date) {
//        todo.put(room.genId(dbRefTodoRooms), date);
        todo.get(room.genId(dbRefTodoRooms)).setTime(date.getTime());
    }

    public void unschedule(PublicRoom room) {
//        todo.put(room.genId(dbRefTodoRooms), new Date(0));
        todo.get(room.genId(dbRefTodoRooms)).setTime(0);
    }

    public void todo(PublicRoom room) {
        todo.put(room.genId(dbRefTodoRooms), new Date(0));
        todoRooms.add(room);
    }

    public void untodo(PublicRoom room) {
        todo.remove(room.genId(dbRefTodoRooms));
        todoRooms.remove(room);
    }

    private void setTodoEvents() {
        dbRefTodoRooms.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot todoSnap : dataSnapshot.getChildren()) {
                    todo.put(todoSnap.getKey(), todoSnap.getValue(Date.class));
                    todoRooms.add(getPublicById(todoSnap.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private PublicRoom getPublicById(String key) {
        for (int i = 0; i < allRooms.size(); i++) {
            if (allRooms.get(i).getId().equals(key)) {
                return allRooms.get(i);
            }
        }
        return null;
    }

    private void setRankersEvents() {
        dbRefRanks.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rankSnap : dataSnapshot.getChildren()) {
                    ranker.put(rankSnap.getKey(), rankSnap.getValue(Float.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setMyRoomsEvents() {

        dbRefMyRooms.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                myRooms.add(dataSnapshot.getValue(PrivateRoom.class));
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String oldKey) {
                for (int i = 0; i < myRooms.size(); i++) {
                    if (myRooms.get(i).getPublicRoomLink().equals(oldKey)) {
                        myRooms.set(i, dataSnapshot.getValue(PrivateRoom.class));
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                myRooms.remove(dataSnapshot.getValue(PrivateRoom.class));
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //Moving still not supported (for using query ordering)
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(mContext, "Failed to load comments.",
//                        Toast.LENGTH_SHORT).show()
            }
        });
    }

    private void setAllRoomsEvents() {

        dbRefAllRooms.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                allRooms.add(dataSnapshot.getValue(PublicRoom.class));
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String oldKey) {
                for (int i = 0; i < allRooms.size(); i++) {
                    if (allRooms.get(i).genId(dbRefAllRooms).equals(oldKey)) {
                        allRooms.set(i, dataSnapshot.getValue(PublicRoom.class));
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                allRooms.remove(dataSnapshot.getValue(PublicRoom.class));
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //Moving still not supported (for using query ordering)
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(mContext, "Failed to load comments.",
//                        Toast.LENGTH_SHORT).show()
            }
        });
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
