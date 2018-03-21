package com.electrocraft.nirzo.pluse.view.activity.doctor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electrocraft.nirzo.pluse.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by wasiun on 3/19/18.
 */

public class DrugsAdapter extends RecyclerView.Adapter<DrugsAdapter.MyViewHolder> {

    private ArrayList<DrugRVModel> drugRVModelArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView drugs, strength,days,times;
        public LinearLayout layout;
        public ImageView edit;
        WeakReference<EditClickListener> reference;
        public MyViewHolder(View view) {
            super(view);
            reference = new WeakReference<EditClickListener>(listener);
            drugs = (TextView) view.findViewById(R.id.rx_drugs);
            strength = (TextView) view.findViewById(R.id.rx_strength);
            days = (TextView) view.findViewById(R.id.rx_days);
            times = (TextView) view.findViewById(R.id.rx_times);
            layout = (LinearLayout)view.findViewById(R.id.rx_ll_color);
            edit = (ImageView)view.findViewById(R.id.row_edit_rx);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference.get().setEditClickListener(getAdapterPosition());
                }
            });
        }
    }


    public DrugsAdapter(ArrayList<DrugRVModel> drugRVModelArrayList, EditClickListener listener) {
        this.drugRVModelArrayList = drugRVModelArrayList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rx_rv_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(position%2==0){
            holder.layout.setBackgroundResource(R.drawable.rv_list_deep);
        }else{
            holder.layout.setBackgroundResource(R.drawable.rv_list_light);

        }
        DrugRVModel labTestModel = drugRVModelArrayList.get(position);
        holder.drugs.setText(labTestModel.getDrugs());
        holder.strength.setText(labTestModel.getStrength());
        holder.days.setText(labTestModel.getDays());
        holder.times.setText(labTestModel.getTimes());
    }

    @Override
    public int getItemCount() {
        return drugRVModelArrayList.size();
    }

    public interface EditClickListener{

        void setEditClickListener(int position);
    }

    EditClickListener listener;


}
