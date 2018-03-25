package com.electrocraft.nirzo.pluse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.model.DoctorSearch;
import com.electrocraft.nirzo.pluse.model.Prescription;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzo on 3/25/2018.
 */

public class PrescriptionListAdapter extends RecyclerView.Adapter<PrescriptionListAdapter.ViewHolder>{

    private List<Prescription> prescriptionList;

    private Context mContext;

    public PrescriptionListAdapter(List<Prescription> prescriptionList) {
        this.prescriptionList = prescriptionList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.lr_tv_DocName)
        TextView docName;
        @BindView(R.id.lr_tv_ConsutrationDate)
        TextView tv_ConsutrationDate;



        @BindView(R.id.lr_btn_book)
        Button btnBook;


        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    @Override
    public PrescriptionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_prescription_search, parent, false);
        // set the Context here
        mContext = parent.getContext();
        return new PrescriptionListAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(PrescriptionListAdapter.ViewHolder holder, int position) {
        Prescription prescription = prescriptionList.get(position);
        holder.docName.setText(prescription.getDRI_DrName());
        holder.tv_ConsutrationDate.setText(prescription.getCI_ConsultationDate());



    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

/*    private void getDoctorImageRequest(String imageLink, final DoctorSearchListAdapter.ViewHolder holder) {


// Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(AppConfig.LIVE_IMAGE_DOCTOR_API_LINK + imageLink,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        holder.docImage.setImageBitmap(bitmap);
//                        pDialog.hide();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        holder.docImage.setImageResource(R.drawable.ic_doctor);
//                        pDialog.hide();
                    }
                });
        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(request);
    }*/
}
