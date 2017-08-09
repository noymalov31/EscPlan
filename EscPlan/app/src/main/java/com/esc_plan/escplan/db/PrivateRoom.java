package com.esc_plan.escplan.db;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * rooms which are visible only to a specific user, holding private information
 */

public class PrivateRoom implements Room, Serializable {

    /* room name */
    private String name;

    /* own rating */
    private float rating;

    /*  date completed */
    private Date date;

    /* time the room was completed (in sec) */
    private int time;

    /* difficulty level */
    private float difficulty;

    /* room theme */
    private Genre genre;

    /* review (anything) */
    private String review;

    /* list of FB id's or just names? */
    private String[] associates;

    /* FB link to the PublicRoom */
    private String publicRoomLink;

    /* picture */
    public File picture;

    /**
     * @param name
     * @param rating
     * @param date
     * @param time
     * @param difficulty
     * @param genre
     * @param publicRoomLink
     * @param picture
     */
    public PrivateRoom(String name, float rating, Date date, int time, float difficulty,
                       Genre genre, String publicRoomLink, File picture) {
        this.name = name;
        this.rating = rating;
        this.date = date;
        this.time = time;
        this.difficulty = difficulty;
        this.genre = genre;
        this.publicRoomLink = publicRoomLink;
        this.picture= picture;
    }

    /* ---------------- Getters ---------------- */

    @Override
    public String getName() {
        return name;
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
        return genre;
    }

    @Override
    public Map<String, Integer> getReviewsBag() {
        if (review == null) {
            return new HashMap<>();
        }
        TreeMap<String, Integer> freqMap = new TreeMap<>();

        String text = this.review.toLowerCase();
        /* add entry counting number of words*/
        Integer cnt = 0;

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
        freqMap.put("*", cnt);
        return freqMap;
    }

    public String getReview() {
        return review;
    }

    public String[] getAssociates() {
        return associates;
    }

    public String getPublicRoomLink() {
        return publicRoomLink;
    }

    public File getPicture() {
        return picture;
    }

    /* ---------------- Setters ---------------- */

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setAssociates(String[] associates) {
        this.associates = associates;
    }

    public void setPublicRoomLink(String publicRoomLink) {
        this.publicRoomLink = publicRoomLink;
    }

    public void setPicture(File picture) {
        this.picture = picture;
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
}
