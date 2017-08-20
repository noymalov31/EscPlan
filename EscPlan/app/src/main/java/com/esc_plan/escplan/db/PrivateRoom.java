package com.esc_plan.escplan.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

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

    /* room theme */
    private Genre genre;

    /* review (anything) */
    private String review;

    /* list of FB id's or just names? */
    private ArrayList<String> associates;

    /* FB link to the PublicRoom */
    private String publicRoomLink;

    /* picture path*/
    public String picture;

    public PrivateRoom() {}
    public PrivateRoom(String name, String publicRoomLink) {
        this.name = name;
        this.rating = 0;
        this.date = new Date();
        this.time = 0;
        this.genre = Genre.General;
        this.publicRoomLink = publicRoomLink;
    }

    /**
     * @param name
     * @param rating
     * @param date
     * @param time
     * @param genre
     * @param publicRoomLink
     * @param picture
     */
    public PrivateRoom(String name, float rating, Date date, int time,
                       Genre genre, String publicRoomLink, String picture) {
        this.name = name;
        this.rating = rating;
        this.date = date;
        this.time = time;
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
    public Genre getGenre() {
        return genre;
    }

    public String getReview() {
        return review;
    }

    public ArrayList<String> getAssociates() {
        return associates;
    }

    public String getPublicRoomLink() {
        return publicRoomLink;
    }

    public String getPicture() {
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


    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setAssociates(ArrayList<String> associates) {
        this.associates = associates;
    }

    public void setPublicRoomLink(String publicRoomLink) {
        this.publicRoomLink = publicRoomLink;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof String) {
            return this.getPublicRoomLink().equals(obj);
        }
        if (!(obj instanceof PrivateRoom)) return false;
        PrivateRoom other = (PrivateRoom) obj;
        return getPublicRoomLink().equals(other.getPublicRoomLink());
    }

}
