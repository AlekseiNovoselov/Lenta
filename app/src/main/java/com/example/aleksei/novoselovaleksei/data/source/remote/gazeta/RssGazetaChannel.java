package com.example.aleksei.novoselovaleksei.data.source.remote.gazeta;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(strict = false)
public class RssGazetaChannel implements Serializable {
    @ElementList(name = "item", required = true, inline = true)
    private List<RssGazetaItem> itemList;

    public List<RssGazetaItem> getGazetaItemList() {
        return itemList;
    }

    public void setItemList(List<RssGazetaItem> itemList) {
        this.itemList = itemList;
    }

    public RssGazetaChannel(List<RssGazetaItem> mFeedItems) {
        this.itemList = mFeedItems;
    }

    public RssGazetaChannel() {
    }
}
