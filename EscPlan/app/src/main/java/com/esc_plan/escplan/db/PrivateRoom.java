package com.esc_plan.escplan.db;

import android.graphics.Bitmap;

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
    private String date;

    /* time the room was completed (in sec) */
    private int time;

    /* room theme */
    private Genre genre;

    /* review (anything) */
    private String review;

    /* list of FB id's or just names? */
    private String associates;

    /* FB link to the PublicRoom */
    private String publicRoomLink;

    /* note */
    private String note;

    /* address */
    private String address;

    /* picture Bitmap*/
    public Bitmap picture;

    public PrivateRoom() {}
    public PrivateRoom(String name, String publicRoomLink) {
        this.name = name;
        this.rating = 0;
        this.date = "" ;
        this.time = 0;
        this.genre = Genre.General;
        this.note = "";
        this.address = "";
        this.publicRoomLink = publicRoomLink;
    }

    public PrivateRoom(PublicRoom room) {
        this.name = room.getName();
        this.rating = room.getRating();
        this.date = "";
        this.genre = room.getGenre();
        this.publicRoomLink = room.getId();
        this.note = "";
        this.address = room.getAddress();
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

    public String getAddress() {
        return address;
    }
    public String getAssociates() {
        return associates;
    }

    public String getPublicRoomLink() {
        return publicRoomLink;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public String getNote() {
        return this.note;
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

    public void setNote(String note){this.note = note;}

    public void setAddress(String address){this.address = address;}

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setAssociates(String associates) {
        this.associates = associates;
    }

    public void setPublicRoomLink(String publicRoomLink) {
        this.publicRoomLink = publicRoomLink;
    }

    public void setPicture(Bitmap picture) {
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
