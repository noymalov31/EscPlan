package com.esc_plan.escplan.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Class which holds the user private information, will be serialized localy,
 * only object containing relevant ranking information (ranker) will be
 * uploaded to the cloud (to find correlated users).
 */

public class Escaper implements Serializable{

    private static final Date NOT_SCHEDULED = new Date(0);

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference dbRefAllRooms = null;
    private DatabaseReference dbRefMyRooms = null;
    private DatabaseReference dbRefTodoRooms = null;
    private DatabaseReference dbRefRecommendedRooms = null;
    private DatabaseReference dbRefRanks = null;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private StorageReference storagePublic = null;
    private StorageReference storagePrivate = null;

    private Query allRoomsQuery = null;
    private Query myRoomsQuery = null;
    private Query todoRoomsQuery = null;
    private Query recommendedRoomsQuery = null;


    /* id */
    private String id;

    /* all rooms completed by this escaper */
    private ArrayList<PrivateRoom> myRooms;

    /* all rooms to do by this escaper */
    private ArrayList<PublicRoom> todoRooms;

    /* all rooms */
    private ArrayList<PublicRoom> allRooms;

    /* recommended rooms */
    private ArrayList<PublicRoom> recommendedRooms;

    /* tuples of (roomId,coorelation) */
    private TreeMap<String, Integer> recommends;

    /* rooms to do by escaper (date if scheduled) */
    private TreeMap<String, Date> todo;

    /* tuples of (roomId,rank), helpful for ranking */
    private HashMap<String, Float> ranker;

    private ImageView currIv;

    public Escaper() { }

    public Escaper(String id) {
        this.id = id;
        myRooms = new ArrayList<>();
        todoRooms = new ArrayList<>();
        allRooms = new ArrayList<>();
        recommendedRooms = new ArrayList<>();
        ranker = new HashMap<>();
        todo = new TreeMap<>();
        recommends = new TreeMap<>();

        dbRefRanks = dbRef.child("ranks").child(id);
        dbRefTodoRooms = dbRef.child(id).child("todo");
        dbRefMyRooms = dbRef.child(id).child("rooms");
        dbRefRecommendedRooms = dbRef.child(id).child("recommended");
        dbRefAllRooms = dbRef.child("public");

        storagePrivate = storageRef.child(id);
        storagePublic = storageRef.child("public");

        myRoomsQuery = dbRefMyRooms.orderByChild("name");
        allRoomsQuery = dbRefAllRooms.orderByChild("name");
        todoRoomsQuery = dbRefTodoRooms.orderByKey();
        recommendedRoomsQuery = dbRefRecommendedRooms.orderByValue();
        setAllRoomsEvents();
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
     * @return "recommended" list
     */
    public ArrayList<PublicRoom> getRecommendedRooms() {
        return recommendedRooms;
    }

    /**
     * should be called before app terminates
     */
    public void syncToFB() {
        dbRefRanks.setValue(ranker);
        dbRefTodoRooms.setValue(todo);
        /*
        for (int i = 0; i < recommendedRooms.size(); i++) {
            dbRefRecommendedRooms.child(String.valueOf(i)).setValue(recommendedRooms.get(i).getId());
        }
        */
    }

    /**
     * @param todoRoom room from the todo view
     * @return date scheduled or NULL in case not scheduled
     */
    public Date getScheduledDate(PublicRoom todoRoom) {
        Date date = todo.get(todoRoom.getId());
        return NOT_SCHEDULED.equals(date) ? null : date;
    }

    /**
     * @param recommendedRoom room from the recommended view
     * @return the correlation to this user
     */
    public Integer getCoorelation(PublicRoom recommendedRoom) {
        return recommends.get(recommendedRoom.getId());
    }

    public List<String> getAllRoomsNames() {
        ArrayList<String> names = new ArrayList<>();
        for (PublicRoom room : allRooms) {
            names.add(room.getName());
        }
        return names;
    }

    public void addPrivateRoom(PrivateRoom room) {
        dbRefMyRooms.child(room.getId()).setValue(room);
        PublicRoom pr = getPublicById(room.getId());
        pr.addPrivateRoom(room);
        addPublicRoom(pr);
        ranker.put(room.getId(), room.getRating());
    }

    public void removePrivateRoom(PrivateRoom room) {
        dbRefMyRooms.child(room.getId()).setValue(null);
        ranker.remove(room.getId());
    }

    public void addPublicRoom(PublicRoom room) {
        dbRefAllRooms.child(room.genId(dbRefAllRooms)).setValue(room);
    }

    public void removePublicRoom(PublicRoom room) {
        dbRefAllRooms.child(room.genId(dbRefAllRooms)).setValue(null);
    }

    public void schedule(PublicRoom room, Date date) {
//        todo.put(room.genId(dbRefTodoRooms), date);
        todo.get(room.getId()).setTime(date.getTime());
    }

    public void unschedule(PublicRoom room) {
        todo.put(room.getId(), NOT_SCHEDULED);
//        todo.get(room.genId(dbRefTodoRooms)).setTime(0);
    }

    public void todo(PublicRoom room) {
        todo.put(room.getId(), NOT_SCHEDULED);
        todoRooms.add(room);
    }

    public void untodo(PublicRoom room) {
        todo.remove(room.getId());
        todoRooms.remove(room);
    }

    public void saveImage(Room room, String path) {
        new DownloadImageTask().execute(room, path);
    }

    public StorageTask<UploadTask.TaskSnapshot>
    saveImage(AppCompatActivity activity, Room room, ImageView iv) {
        currIv = iv;

        Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] ci = baos.toByteArray();

        iv.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        StorageReference storage = (room.privacy() == Room.Privacy.Private)?
                storagePrivate : storagePublic;
        return storage.child(room.getId() + ".jpeg").putBytes(ci)
                .addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        currIv.clearColorFilter();
                    }
                });
    }

    public Task<byte[]> getImage(AppCompatActivity activity, Room room, ImageView iv) {
        currIv = iv;
        StorageReference storage = (room.privacy() == Room.Privacy.Private)?
                storagePrivate : storagePublic;
        iv.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        return storage.child(room.getId() + ".jpeg").getBytes(1048576)
                .addOnSuccessListener(activity, new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        currIv.setImageBitmap(bMap);
                        currIv.clearColorFilter();
                    }
                });

    }

    public Task<byte[]> getImage(AppCompatActivity activity, PublicRoom room, ImageView iv) {
        currIv = iv;
        iv.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        return storagePublic.child(room.getId() + ".jpeg").getBytes(1048576)
                .addOnSuccessListener(activity, new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        currIv.setImageBitmap(bMap);
                        currIv.clearColorFilter();
                    }
                });

    }

    private PublicRoom getPublicById(String key) {
        for (int i = 0; i < allRooms.size(); i++) {
            if (allRooms.get(i).getId().equals(key)) {
                return allRooms.get(i);
            }
        }
//        return dbRefAllRooms.child(key).;
        return null;
    }

    private void setRecommendedEvents() {
        dbRefRecommendedRooms.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot recSnap : dataSnapshot.getChildren()) {
                    recommends.put(recSnap.getKey(), recSnap.getValue(Integer.class));
                    recommendedRooms.add(getPublicById(recSnap.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                    if (myRooms.get(i).getId().equals(oldKey)) {
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

        dbRefAllRooms.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setMyRoomsEvents();
                setTodoEvents();
                setRankersEvents();
                setRecommendedEvents();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
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

    private class DownloadImageTask extends AsyncTask<Object, Void, Void> {

        protected Void doInBackground(Object... params) {
            Bitmap bitmap;
            Room room = (Room) params[0];
            String path = (String) params[1];
            try {
                HttpURLConnection connection = (HttpURLConnection) (new URL(path)).openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                return null;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] ci = baos.toByteArray();

            StorageReference storage = (room.privacy() == Room.Privacy.Private)?
                 storagePrivate : storagePublic;
            storage.child(room.getId() + ".jpeg").putBytes(ci);
            return null;
        }
    }
}
