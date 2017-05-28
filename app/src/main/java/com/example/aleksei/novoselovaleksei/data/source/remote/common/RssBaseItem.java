package com.example.aleksei.novoselovaleksei.data.source.remote.common;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "item", strict = false)
public abstract class RssBaseItem implements Serializable {
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    @Element(name = "title", required = true )
    private String title;

    @Element(name = "pubDate", required = true )
    private String publicationDate;

    @Element(name = "description", required = true )
    private  String description;

    public RssBaseItem(String title, String description, String publicationDate) {
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
    }
    public RssBaseItem() {}

    protected boolean isEqualTo(RssBaseItem o) {
        return o.getTitle().equals(title) &&
                o.getDescription().equals(description) &&
                o.getPublicationDate().equals(publicationDate);
    }
}
