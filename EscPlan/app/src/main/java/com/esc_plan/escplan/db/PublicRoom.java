package com.esc_plan.escplan.db;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Rooms which are part of the open DB - all rooms manu.
 */

public class PublicRoom implements Room, Serializable {


    /* room name */
    private String name;

    /* number of people completed the room */
    private int peopleCompleted;

    /* average rating */
    private float rating;

    /*  date added to db */
    private Date date;

    /* average time to complete the room (in sec) */
    private int time;

    /* difficulty level */
    private float difficulty;

    /* room theme */
    private int[] genreVote;

    /* website URL */
    private String URL;

    /* physical address */
    private String address;

    /* reviews (for ranking) */
    private Map<String, Integer> reviewsBag;

    /* list of FB id's */
    private String[] similarRooms;

    /**
     * empty constructor for FB
     */
    PublicRoom() {
    }

    /**
     * basic constructor
     * @param name - room name
     */
    public PublicRoom(String name) {
        this.name = name;
        this.peopleCompleted = 0;
        this.rating = 0;
        this.date = new Date();
        this.time = 0;
        this.difficulty = 0;
        this.genreVote = new int[Genre.length];
        this.reviewsBag = new HashMap<>();
    }

    /**
     * this method should be called everytime a user uploaded a new review
     * on this room.
     * @param newRoom - PrivateRoom added be a user
     */
    public void addReview(Room newRoom) {
        if (peopleCompleted == 0) {
            this.rating = newRoom.getRating();
            this.time = newRoom.getTime();
            this.difficulty = newRoom.getDifficulty();
        } else {
            this.rating = (this.rating * (this.peopleCompleted - 1) + newRoom.getRating())
                    / this.peopleCompleted;
            this.time = (this.time * (this.peopleCompleted - 1) + newRoom.getTime())
                    / this.peopleCompleted;
            this.difficulty = (this.difficulty *(this.peopleCompleted - 1) + newRoom.getDifficulty())
                    / this.peopleCompleted;

        }
        this.genreVote[newRoom.getGenre().ordinal()]++;
        this.reviewsBag.putAll(newRoom.getReviewsBag());
        this.peopleCompleted++;
    }

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

    /**
     * @Return map object which can be serialized with fb
     */
    public Map<String, Object> itemToFB() throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        for(Field field : this.getClass().getFields()) {
            map.put(field.getName(), field.get(Object.class));
        }
        return map;
    }

    /* ---------------- GETTERS ---------------- */

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

    public Date getDate() {
        return date;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public float getDifficulty() {
        return difficulty;
    }

    @Override
    public Genre getGenre() {
        int max = 0;
        Genre argmax = Genre.General;
        for (int i = 0; i < this.genreVote.length; i++) {
            if (genreVote[i] > max) {
                max = genreVote[i];
                argmax = Genre.values()[i];
            }
        }
        return argmax;
    }

    public int[] getGenreVote() {
        return genreVote;
    }

    public String getURL() {
        return URL;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public Map<String, Integer> getReviewsBag() {
        return reviewsBag;
    }

    public String[] getSimilarRooms() {
        return similarRooms;
    }
}
