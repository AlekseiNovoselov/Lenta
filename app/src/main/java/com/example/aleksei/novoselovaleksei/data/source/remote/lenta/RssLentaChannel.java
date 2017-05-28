package com.example.aleksei.novoselovaleksei.data.source.remote.lenta;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(strict = false)
public class RssLentaChannel implements Serializable {
    @ElementList(name = "item", required = true, inline = true)
    private List<RssLentaItem> itemList;

    public List<RssLentaItem> getLentaItemList() {
        return itemList;
    }

    public void setItemList(List<RssLentaItem> itemList) {
        this.itemList = itemList;
    }

    public RssLentaChannel(List<RssLentaItem> mFeedItems) {
        this.itemList = mFeedItems;
    }

    public RssLentaChannel() {
    }
}
