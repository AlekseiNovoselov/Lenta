package com.example.aleksei.novoselovaleksei.data.source.remote.common;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "rss", strict = false)
public abstract class RssBaseFeed implements Serializable {
    @Attribute
    protected
    String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
