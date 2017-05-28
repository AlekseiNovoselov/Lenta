package com.example.aleksei.novoselovaleksei.data.source.remote.lenta;

import com.example.aleksei.novoselovaleksei.data.source.remote.common.RssBaseFeed;

import org.simpleframework.xml.Element;

public class RssLentaFeed extends RssBaseFeed {

    @Element
    private
    RssLentaChannel channel;

    public RssLentaChannel getChannel() {
        return channel;
    }

    public void setChannel(RssLentaChannel channel) {
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
