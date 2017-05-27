package com.example.aleksei.novoselovaleksei.data;

public class Tiding {

    private String mTitle;
    private String mDescription;

    public Tiding(String title, String description) {
        mTitle = title;
        mDescription = description;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }
}
