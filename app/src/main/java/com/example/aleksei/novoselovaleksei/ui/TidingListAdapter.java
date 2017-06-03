package com.example.aleksei.novoselovaleksei.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aleksei.novoselovaleksei.R;
import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.ui.holders.DescriptionViewHolder;
import com.example.aleksei.novoselovaleksei.ui.holders.TidingViewHolder;
import com.example.aleksei.novoselovaleksei.ui.holders.helper.DescriptionExpandableView;
import com.example.aleksei.novoselovaleksei.ui.holders.helper.TidingItemExpandableView;
import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class TidingListAdapter extends ExpandableRecyclerViewAdapter<TidingViewHolder, DescriptionViewHolder> {

    private List<Tiding> tidings;

    public TidingListAdapter(List<? extends ExpandableGroup> groups, List<Tiding> tidings ) {
        super(groups);
        this.tidings = tidings;
    }

    @Override
    public TidingViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tiding_item, viewGroup, false);
        return new TidingViewHolder(view);
    }

    @Override
    public DescriptionViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_description, viewGroup, false);
        return new DescriptionViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(DescriptionViewHolder holder, int i,
                                      ExpandableGroup group, int childIndex) {

        final DescriptionExpandableView descriptionV = ((TidingItemExpandableView) group).getItems().get(childIndex);
        holder.setDescriptionText(descriptionV.getDescription());
    }

    @Override
    public void onBindGroupViewHolder(TidingViewHolder holder, int i, ExpandableGroup expandableGroup) {

        Tiding item = tidings.get(i);

        holder.titleTextField.setText(item.getTitle());
        Picasso.with(holder.imageView.getContext())
                .load(item.getImageUrl())
                .into(holder.imageView);
        holder.descriptionTextField.setText(item.getDescription());
        holder.publicationDateTextField.setText(String.valueOf(item.getPublicationDate()));
    }
}