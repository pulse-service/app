package com.electrocraft.nirzo.pluse.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.model.GeoLayR4Location;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzo on 2/25/2018.
 */

public class LocationSearchListAdapter extends RecyclerView.Adapter<LocationSearchListAdapter.ViewHolder> {



    private List<GeoLayR4Location> locList;

    public LocationSearchListAdapter(List<GeoLayR4Location> locList) {
        this.locList = locList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lr_tv_LocName)
        TextView tvLocName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_location_search, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GeoLayR4Location loc = locList.get(position);
        holder.tvLocName.setText(loc.getLayR4ListName());
    }

    @Override
    public int getItemCount() {
        return locList.size();
    }
}
