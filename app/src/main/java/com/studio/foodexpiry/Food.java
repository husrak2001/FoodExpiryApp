package com.studio.foodexpiry;


public class Food {
    private int id;
    private String name;
    private String type;
    private String expiry;
    private String expected;
    private String favStatus;
    private byte[] image;

    public Food(String name, String type, String expiry, String expected, byte[] image, String favStatus, int id) {
        this.name = name;
        this.type = type;
        this.expiry = expiry;
        this.expected = expected;
        this.image = image;
        this.id = id;
        this.favStatus=favStatus;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }


    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }
}
