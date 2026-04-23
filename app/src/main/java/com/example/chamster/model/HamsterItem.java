package com.example.chamster.model;

public class HamsterItem {
    private String name;
    private String description;
    private String imagePath; // np. "hamster/hat.png"

    public HamsterItem(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}
