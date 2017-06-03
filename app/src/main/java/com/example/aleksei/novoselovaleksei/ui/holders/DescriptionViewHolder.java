package com.example.aleksei.novoselovaleksei.ui.holders;

import android.view.View;
import android.widget.TextView;

import com.example.aleksei.novoselovaleksei.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class DescriptionViewHolder extends ChildViewHolder {

    private TextView descriptionView;

    public DescriptionViewHolder(View itemView) {
        super(itemView);
        descriptionView = (TextView) itemView.findViewById(R.id.list_item_description);
    }

    public void setDescriptionText(String text) {
        descriptionView.setText(text);
    }
}
