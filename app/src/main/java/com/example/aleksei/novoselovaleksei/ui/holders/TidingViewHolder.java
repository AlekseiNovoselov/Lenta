package com.example.aleksei.novoselovaleksei.ui.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksei.novoselovaleksei.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class TidingViewHolder extends GroupViewHolder {

    public TextView titleTextField;
    public TextView sourceTextField;

    public ImageView imageView;
    public TextView publicationDateTextField;

    public TidingViewHolder(View itemView) {
        super(itemView);
        titleTextField = (TextView) itemView.findViewById(R.id.title);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        sourceTextField = (TextView) itemView.findViewById(R.id.source);
        publicationDateTextField = (TextView) itemView.findViewById(R.id.publication_date);
    }

}
