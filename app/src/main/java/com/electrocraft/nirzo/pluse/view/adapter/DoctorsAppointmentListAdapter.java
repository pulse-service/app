package com.electrocraft.nirzo.pluse.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.model.AppointmentModel;

import java.lang.ref.WeakReference;
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

    public DoctorsAppointmentListAdapter(List<AppointmentModel> list, EditClickListener listener, EditClickListener1 listener1) {
        this.list = list;
        this.listener = listener;
        this.listener1 = listener1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.lr_tv_PatientId)
        TextView tvPatientID;
        @BindView(R.id.lr_tv_PatientName)
        TextView tvPatientName;

        @BindView(R.id.lr_tv_AppointmentDateNTime)
        TextView tv_AppointmentDateNTime;


        @BindView(R.id.lr_tv_patientShortDes)
        TextView tv_patientShortDes;



        @BindView(R.id.lr_btn_Call)
        Button lr_btn_Call;

        @BindView(R.id.lr_btn_Pref)
        Button lr_btn_Pref;


        private ViewHolder(View itemView) {
            super(itemView);
            reference = new WeakReference<EditClickListener>(listener);
            reference1 = new WeakReference<EditClickListener1>(listener1);
            ButterKnife.bind(this, itemView);

            lr_btn_Call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference.get().setEditClickListener(getAdapterPosition());
                }
            });

            lr_btn_Pref.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference1.get().setEditClickListener(getAdapterPosition());
                }
            });

        }
    }

    WeakReference<EditClickListener> reference;
    WeakReference<EditClickListener1> reference1;
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
        holder.tvPatientID.setText(model.getPatientID());
        holder.tvPatientName.setText(model.getPatientName());
        holder.tv_patientShortDes.setText(model.getProbShortDescribtion());
        holder.tv_AppointmentDateNTime.setText(model.getAppointmentDate()+" ("+model.getInTime()+")");



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface EditClickListener{

        void setEditClickListener(int position);
    }

    EditClickListener listener;

    public interface EditClickListener1{

        void setEditClickListener(int position);
    }

    EditClickListener1 listener1;

}
