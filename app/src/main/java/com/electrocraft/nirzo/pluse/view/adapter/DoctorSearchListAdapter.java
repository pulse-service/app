package com.electrocraft.nirzo.pluse.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.model.DoctorSearch;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * This class is responsible for  generate single row item in list
 *
 * @author Faisal Mohammad
 * @since 2/22/2018
 */

public class DoctorSearchListAdapter extends RecyclerView.Adapter<DoctorSearchListAdapter.ViewHolder> {


    private List<DoctorSearch> doctorList;

    public DoctorSearchListAdapter(List<DoctorSearch> doctorList) {
        this.doctorList = doctorList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lr_iv_DocImage)
        ImageView docImage;
        @BindView(R.id.lr_tv_DocName)
        TextView docName;
        @BindView(R.id.lr_tv_DocInstitution)
        TextView docInstitution;

        @BindView(R.id.lr_tv_doc_consult_price)
        TextView docConsultPrice;

        @BindView(R.id.lr_btn_book)
        Button btnBook;
        @BindView(R.id.lr_iv_DocAvailableStatus)
        ImageView docAvailable;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_doctor_search, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DoctorSearch doctor = doctorList.get(position);
        holder.docName.setText(doctor.getName());
        holder.docInstitution.setText(doctor.getInstitution());
        holder.docConsultPrice.setText("Consult online for "+doctor.getAmount()+" BDT");
        if (doctor.isAvailableFlag())
            holder.docAvailable.setImageResource(R.drawable.ic_online);

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }
}
