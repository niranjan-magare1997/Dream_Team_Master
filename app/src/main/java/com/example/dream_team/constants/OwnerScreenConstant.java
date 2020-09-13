package com.example.dream_team.constants;

public class OwnerScreenConstant {
    int image;
    String title;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public OwnerScreenConstant(int image, String title) {
        this.image = image;
        this.title = title;
    }
}
