package com.example.chamster.data.model;

import java.io.Serializable;

public class HamsterItem implements Serializable {
    private final String name;
    private final String description;
    private final String imagePath;
    private final int price;
    private boolean owned;

    public HamsterItem(String name, String description, String imagePath, int price) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
        this.owned = false;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
    public int getPrice() { return price; }
    public boolean isOwned() { return owned; }
    public void setOwned(boolean owned) { this.owned = owned; }
}