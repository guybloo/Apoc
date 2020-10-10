package com.example.apoc.Storage;

public class StoredImage {
    private String name;
    private String url;
    public StoredImage() {
        //empty constructor needed
    }
    public StoredImage(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        url = imageUrl;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String imageUrl) {
        url = imageUrl;
    }
}

