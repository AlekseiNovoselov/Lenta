package com.example.aleksei.novoselovaleksei.ui.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksei.novoselovaleksei.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class TidingViewHolder extends GroupViewHolder {

    public TextView titleTextField;
    public TextView descriptionTextField;

    public ImageView imageView;
    public TextView publicationDateTextField;

    public TidingViewHolder(View itemView) {
        super(itemView);
        titleTextField = (TextView) itemView.findViewById(R.id.title);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        descriptionTextField = (TextView) itemView.findViewById(R.id.description);
        publicationDateTextField = (TextView) itemView.findViewById(R.id.pubdate);
    }

}
