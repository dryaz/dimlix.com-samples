package com.dimlix.samplesapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimlix.samplesapp.variants.BaseSampleActivity;

import java.util.List;

public class SamplesAdapter extends RecyclerView.Adapter<SamplesAdapter.ViewHolder> {

    private List<MainActivity.Sample> mData;

    public SamplesAdapter(List<MainActivity.Sample> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sample, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final MainActivity.Sample item = mData.get(position);
        holder.mItem.setText(item.header);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sampleActivity = new Intent(holder.itemView.getContext(),
                        item.classtoStart);
                sampleActivity.putExtra(BaseSampleActivity.HEADER_KEY, item.header);
                holder.itemView.getContext().startActivity(sampleActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItem = (TextView) itemView;
        }
    }
}
