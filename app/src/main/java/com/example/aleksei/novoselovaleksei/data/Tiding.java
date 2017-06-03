package com.example.aleksei.novoselovaleksei.data;

public class Tiding {

    private String mTitle;
    private long mPublicationDate;
    private String mDescription;
    private String mImageUrl;
    private String mSource;

    public String getImageUrl() { return mImageUrl;}

    public long getPublicationDate() { return mPublicationDate;}

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getSource() {
        return mSource;
    }

    public Tiding(String title, long publicationData, String description, String imageUrl, String source) {
        mTitle = title;
        mPublicationDate = publicationData;
        mDescription = description;
        mImageUrl = imageUrl;
        mSource = source;
    }
}
