package com.example.aleksei.novoselovaleksei.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksei.novoselovaleksei.R;
import com.example.aleksei.novoselovaleksei.data.Tiding;

import java.util.List;

public class TidingsListAdapter extends RecyclerView.Adapter<TidingsListAdapter.RssTidingHolder> {

    private List<Tiding> tidings;

    public TidingsListAdapter(List<Tiding> tidings) {
        this.tidings = tidings;
    }

    public void setData(List<Tiding> tidings) {
        this.tidings = tidings;
    }

    @Override
    public RssTidingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View v = layoutInflater.inflate(R.layout.tiding_item, viewGroup, false);
        return new RssTidingHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(RssTidingHolder holder, int i) {

        Tiding item = tidings.get(i);

        holder.titleTextField.setText(item.getTitle());
        // TODO - add Image
        holder.descriptionTextField.setText(item.getDescription());
        holder.publicationDateTextField.setText(String.valueOf(item.getPublicationDate()));
    }

    @Override
    public int getItemCount() {
        return tidings.size();
    }

    public static class RssTidingHolder extends RecyclerView.ViewHolder {

        private TextView titleTextField;
        private TextView descriptionTextField;

        private ImageView imageView;
        private TextView publicationDateTextField;

        public RssTidingHolder(View itemView) {
            super(itemView);
            titleTextField = (TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            descriptionTextField = (TextView) itemView.findViewById(R.id.description);
            publicationDateTextField = (TextView) itemView.findViewById(R.id.pubdate);
        }
    }
}
