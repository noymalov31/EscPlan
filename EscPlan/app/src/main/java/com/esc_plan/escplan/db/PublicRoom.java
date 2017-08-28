package com.esc_plan.escplan.db;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rooms which are part of the open DB - all rooms manu.
 */

public class PublicRoom implements Room, Serializable {

    /* room  id */
    private String id;

    /* room name */
    private String name;

    /* number of people completed the room */
    private int peopleCompleted;

    /* average rating */
    private float rating;

    /* average time to complete the room (in sec) */
    private int time;

    /* room theme */
    private Room.Genre genre;

    /* website URL */
    private String URL;

    /* phone number */
    private String phone;

    /* physical address */
    private String address;

    /* reviews  */
    private ArrayList<String> reviews;

    /* list of FB id's */
    private ArrayList<String> similarRooms;

    /* privacy type */
    private static final Privacy privacy = Privacy.Public;

    /**
     * empty constructor for FB
     */
    PublicRoom() { }

    /**
     * basic constructor
     * @param name - room name
     */
    public PublicRoom(String name) {
        this.name = name;
        this.peopleCompleted = 0;
        this.rating = 0;
        this.time = 0;
        this.genre = Genre.General;
        this.reviews= new ArrayList<>();
        this.similarRooms = new ArrayList<>();
    }

    public PublicRoom(String name, float rating, int time, Genre genre,
                      String URL, String phone, String address) {
        this.name = name;
        this.peopleCompleted = 1;
        this.rating = rating;
        this.time = time;
        this.genre = genre;
        this.URL = URL;
        this.phone = phone;
        this.address = address;
        this.reviews= new ArrayList<>();
        this.similarRooms = new ArrayList<>();
    }

    /**
     * this method should be called everytime a user uploaded a new review
     * on this room.
     * @param newRoom - PrivateRoom added be a user
     */
    public void addPrivateRoom(PrivateRoom newRoom) {
        if (peopleCompleted == 0) {
            this.rating = newRoom.getRating();
            this.time = newRoom.getTime();
        } else {
            this.rating = ((this.rating * this.peopleCompleted) + newRoom.getRating())
                    / this.peopleCompleted;
            this.time = ((this.time * this.peopleCompleted) + newRoom.getTime())
                    / this.peopleCompleted;

        }
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.add(newRoom.getReview());
        this.peopleCompleted++;
    }

    public void addReview(String review) {
        reviews.add(review);
    }

    /* ---------------- GETTERS ---------------- */

    @Override
    public String getId() {
        return id;
    }

    public String genId(DatabaseReference dbref) {
        if (id == null) {
            id = dbref.push().getKey();
        }
        return id;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getPeopleCompleted() {
        return peopleCompleted;
    }

    @Override
    public float getRating() {
        return rating;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public Genre getGenre() {
        return this.genre;
    }

    public String getURL() {
        return URL;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public ArrayList<String> getSimilarRooms() {
        return similarRooms;
    }

    /* ---------------- SETTERS ---------------- */

    public void setPeopleCompleted(int peopleCompleted) {
        this.peopleCompleted = peopleCompleted;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public void setSimilarRooms(ArrayList<String> similarRooms) {
        this.similarRooms = similarRooms;
    }

    /* ---------------- RANKERS ---------------- */

    /**
     * function which calculates how similar are this room and other room,
     * using a mixed model of
     * 1. simple correlation of the room standard fields
     * 2. bag of words correlation using the reviews of both.
     * @param other - PublicRoom to calculate the correlation with
     * @return float in the range [0,1]
     */
    public float correlation(PublicRoom other) {
        float theta = 0.5f;
        return theta * this.primitivesCorrelation(other) +
                (1 - theta) * this.bagOfWordsScore(other);
    }

    private float primitivesCorrelation(PublicRoom other) {
        return 0;
    }

    private float bagOfWordsScore(PublicRoom other) {
        return 0;
    }

    public Map<String, Integer> getReviewsBag() {
        if (reviews == null || reviews.isEmpty()) {
            return new HashMap<>();
        }
        TreeMap<String, Integer> freqMap = new TreeMap<>();

        /* add entry counting number of words*/
        Integer cnt = 0;

        for (String review : reviews) {
            String text = review.toLowerCase();

            String currToken;
            Matcher m = Pattern.compile("\\w+").matcher(text);

            while (m.find()) {
                cnt++;
                currToken = new String(text.substring(m.start(), m.end()));
                if (freqMap.containsKey(currToken)) {
                    freqMap.put(currToken, freqMap.get(currToken) + 1);

                } else {
                    freqMap.put(currToken, 1);
                }
            }
        }
        freqMap.put("*", cnt);
        return freqMap;
    }

    @Override
    public Privacy privacy() {
        return privacy;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof String) {
            return this.getId().equals(obj);
        }
        if (!(obj instanceof PrivateRoom)) return false;
        PublicRoom other = (PublicRoom) obj;
        return getId().equals(other.getId());
    }
/*
    /**
     * @Return map object which can be serialized with fb
     *//*

    public Map<String, Object> itemToFB() throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        for(Field field : this.getClass().getFields()) {
            map.put(field.getName(), field.get(Object.class));
        }
        return map;
    }
    public String addToFB(DatabaseReference dbRef) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("peopleCompleted", peopleCompleted);
        map.put("rating", rating);
        map.put("time", time);
        map.put("genre", genre);
        map.put("URL", URL);
        map.put("address", address);
        map.put("similarRooms", similarRooms);
        map.put("reviews", reviews);
        dbRef.setValue(map);
        return "A";
    }
*/
}
