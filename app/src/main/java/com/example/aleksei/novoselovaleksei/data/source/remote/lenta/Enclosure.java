package com.example.aleksei.novoselovaleksei.data.source.remote.lenta;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "enclosure", strict = false)
public class Enclosure {
    @Attribute(name = "url")
    private String url;

    @Attribute(name = "length")
    private String length;

    @Attribute(name = "type")
    private String type;

    public Enclosure(String url, String length, String type) {
        this.url = url;
        this.length = length;
        this.type = type;
    }

    public Enclosure() {}

    public String getUrl() {
        return url;
    }
}
