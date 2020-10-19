package com.project.android.exampledictionary.model;

public class Favorite {
    private String word;
    private int imageDelete;

    public Favorite(String word, int imageDelete) {
        this.word = word;
        this.imageDelete = imageDelete;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getImageDelete() {
        return imageDelete;
    }

    public void setImageDelete(int imageDelete) {
        this.imageDelete = imageDelete;
    }
}
