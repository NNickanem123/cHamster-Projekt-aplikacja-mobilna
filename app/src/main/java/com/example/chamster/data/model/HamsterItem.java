package com.example.chamster.data.model;

import java.io.Serializable;

public class HamsterItem implements Serializable {
    private final String name;
    private final String description;
    private final String imagePath;

    public HamsterItem(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
}