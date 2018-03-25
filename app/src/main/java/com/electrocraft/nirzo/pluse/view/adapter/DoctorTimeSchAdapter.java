package com.electrocraft.nirzo.pluse.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.model.DoctorAvailableTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzo on 3/20/2018.
 */

public class DoctorTimeSchAdapter extends RecyclerView.Adapter<DoctorTimeSchAdapter.ViewHolder> {

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DoctorAvailableTime time = timeList.get(position);

        String tem = time.getInTime() + " " + time.getInTime_AMOrPM() + " to " + time.getOutTime() + time.getOutTime_AMOrPM();
        holder.tv_DocTime.setText(tem);
        if (time.getIsAlreadyBooked().equals("0")) {
            if (time.isCheck())
                holder.lr_iv_ClickButton.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
            else
                holder.lr_iv_ClickButton.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
        } else {
            holder.lr_iv_ClickButton.setImageResource(R.drawable.ic_radio_button_unchecked_gray);
            holder.lr_iv_ClickButton.setEnabled(false);
            holder.tv_DocTime.setTextColor(Color.GRAY);
        }


    }

    private List<DoctorAvailableTime> timeList;

    private Context mContext;

    public DoctorTimeSchAdapter(List<DoctorAvailableTime> times) {
        this.timeList = times;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.lr_tv_DocTime)
        TextView tv_DocTime;

        @BindView(R.id.lr_iv_ClickButton)
        ImageView lr_iv_ClickButton;


        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    @Override
    public DoctorTimeSchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_doctor_time_sch, parent, false);
        // set the Context here
        mContext = parent.getContext();
        return new DoctorTimeSchAdapter.ViewHolder(itemView);


    }


    @Override
    public int getItemCount() {
        return timeList.size();
    }


}
