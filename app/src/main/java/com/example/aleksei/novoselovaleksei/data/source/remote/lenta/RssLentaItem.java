package com.example.aleksei.novoselovaleksei.data.source.remote.lenta;

import com.example.aleksei.novoselovaleksei.data.source.remote.common.RssBaseItem;

import org.simpleframework.xml.Element;

public class RssLentaItem extends RssBaseItem {

    public Enclosure getEnclosure() {return enclosure;}

    @Element(name = "enclosure", required = false)
    private Enclosure enclosure;

    public RssLentaItem(String title, String description, String publicationDate, Enclosure enclosure) {
        super(title, description, publicationDate);
        this.enclosure = enclosure;
    }
    public RssLentaItem() {}

    public boolean isEqualTo(RssLentaItem o) {
        if ( super.isEqualTo(o) &&
                o.getEnclosure().equals(enclosure)) {
            return true;
        }
        else
            return false;
    }

}
