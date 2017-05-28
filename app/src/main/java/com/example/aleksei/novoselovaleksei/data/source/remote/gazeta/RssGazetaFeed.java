package com.example.aleksei.novoselovaleksei.data.source.remote.gazeta;

import com.example.aleksei.novoselovaleksei.data.source.remote.common.RssBaseFeed;

import org.simpleframework.xml.Element;

public class RssGazetaFeed extends RssBaseFeed {

    @Element
    private
    RssGazetaChannel channel;

    public RssGazetaChannel getChannel() {
        return channel;
    }

    public void setChannel(RssGazetaChannel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "RSS{" +
                "version='" + version + '\'' +
                ", channel=" + channel +
                '}';
    }
}
