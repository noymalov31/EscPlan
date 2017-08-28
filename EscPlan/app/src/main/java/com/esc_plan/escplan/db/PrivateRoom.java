package com.esc_plan.escplan.db;

import java.io.Serializable;

/**
 * rooms which are visible only to a specific user, holding private information
 */

public class PrivateRoom implements Room, Serializable {

    /* room name */
    private String name;

    /* own rating */
    private float rating;

    /*  date completed */
    private String date;

    /* time the room was completed (in min) */
    private int time;

    /* room theme */
    private Genre genre;

    /* review (anything) */
    private String review;

    /* review (anything) */
    private String note;

    /* review (anything) */
    private String address;

    /* list of FB id's or just names? */
    private String partners;

    /* FB link to the PublicRoom */
    private String id;

    /* privacy type */
    private static final Privacy privacy = Privacy.Private;

    public PrivateRoom() {}
    public PrivateRoom(String name, String publicRoomLink) {
        this.name = name;
        this.rating = 0;
        this.time = 0;
        this.genre = Genre.General;
        this.id = publicRoomLink;
    }

    public PrivateRoom(PublicRoom room, float rate) {
        this.name = room.getName();
        this.rating = rate;
        this.genre = room.getGenre();
        this.address = room.getAddress();
        this.id = room.getId();
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

    public String getDate() {
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Privacy privacy() {
        return privacy;
    }

    public String getNote() {
        return note;
    }

    public String getAddress() {
        return address;
    }

    public String getPartners() {
        return partners;
    }



    /* ---------------- Setters ---------------- */

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setDate(String date) {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPartners(String partners) {
        this.partners = partners;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof String) {
            return this.getId().equals(obj);
        }
        if (!(obj instanceof PrivateRoom)) return false;
        PrivateRoom other = (PrivateRoom) obj;
        return getId().equals(other.getId());
    }

}
