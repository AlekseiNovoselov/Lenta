package com.example.aleksei.novoselovaleksei.data;

import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

public class Tiding {

    private String mTitle;
    private String mPublicationDate;
    private String mDescription;
    private String mImageUrl;
    private BaseSource.Source mSource;

    public String getImageUrl() { return mImageUrl;}

    public String getPublicationDate() { return mPublicationDate;}

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public BaseSource.Source getSource() {
        return mSource;
    }

    public Tiding(String title, String publicationData, String description, String imageUrl, BaseSource.Source source) {
        mTitle = title;
        mPublicationDate = publicationData;
        mDescription = description;
        mImageUrl = imageUrl;
        mSource = source;
    }
}
