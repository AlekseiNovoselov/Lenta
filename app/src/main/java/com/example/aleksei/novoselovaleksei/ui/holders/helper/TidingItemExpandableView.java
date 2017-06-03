package com.example.aleksei.novoselovaleksei.ui.holders.helper;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import java.util.List;

public class TidingItemExpandableView extends ExpandableGroup<DescriptionExpandableView> {

    public TidingItemExpandableView(String title, List<DescriptionExpandableView> items) {
        super(title, items);
    }
}
