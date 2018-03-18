package com.electrocraft.nirzo.pluse.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.model.AppointmentModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * This class is responsible for  generate single row item in list
 *
 * @author Faisal Mohammad
 * @since 2/22/2018
 */

public class DoctorsAppointmentListAdapter extends RecyclerView.Adapter<DoctorsAppointmentListAdapter.ViewHolder> {


    private List<AppointmentModel> list;

    private Context mContext;

    public DoctorsAppointmentListAdapter(List<AppointmentModel> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.lr_tv_PatientId)
        TextView tvPatientID;
        @BindView(R.id.lr_tv_PatientName)
        TextView tvPatientName;

        @BindView(R.id.lr_tv_AppointmentDateNTime)
        TextView tv_AppointmentDateNTime;



        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

//    ViewHolder holder;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_doctor_appointment, parent, false);

        mContext = parent.getContext();
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppointmentModel model = list.get(position);

//        holder.docName.setText(model.getDoctorName());
//        holder.tv_AppointmentDateNTime.setText(model.getAppointmentDate()+" ("+model.getInTime()+")");
////        holder.docInstitution.setText(doctor.getExpertise());
////        holder.docConsultPrice.setText("Consult online for " + doctor.getAmount() + " BDT");
//////        if (doctor.getPhoto() != null)
////            getDoctorImageRequest(doctor.getPhoto(), holder);
////        if (doctor.isAvailableFlag())
////            holder.docAvailable.setImageResource(R.drawable.ic_online);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
