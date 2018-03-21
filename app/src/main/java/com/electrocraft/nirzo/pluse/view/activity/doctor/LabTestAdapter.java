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

public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.MyViewHolder> {

    private ArrayList<LabTestModel> labtestList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView symptom, test;
        public LinearLayout layout;
        public ImageView edit;
        private WeakReference<EditClickListener> listenerRef;

        public MyViewHolder(View view) {
            super(view);
            listenerRef = new WeakReference<>(setEditClickListener);
            symptom = (TextView) view.findViewById(R.id.labtest_symptom);
            test = (TextView) view.findViewById(R.id.labtest_test);
            layout = (LinearLayout) view.findViewById(R.id.labtest_ll_color);
            edit = (ImageView) view.findViewById(R.id.row_edit_labtest);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerRef.get().OnClick(getAdapterPosition());
                }
            });

        }
    }


    public LabTestAdapter(ArrayList<LabTestModel> labtestList, EditClickListener setEditClickListener) {
        this.labtestList = labtestList;
        this.setEditClickListener = setEditClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.labtest_rv_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.layout.setBackgroundResource(R.drawable.rv_list_deep);
        } else {
            holder.layout.setBackgroundResource(R.drawable.rv_list_light);

        }
        LabTestModel labTestModel = labtestList.get(position);
        holder.symptom.setText(labTestModel.getSymptom());
        holder.test.setText(labTestModel.getTest());
    }

    @Override
    public int getItemCount() {
        return labtestList.size();
    }

    EditClickListener setEditClickListener;

    public interface EditClickListener {
        void OnClick(int position);
    }
}
