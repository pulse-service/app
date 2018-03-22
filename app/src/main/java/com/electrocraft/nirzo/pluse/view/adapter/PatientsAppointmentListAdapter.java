package com.electrocraft.nirzo.pluse.view.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.model.AppointmentModel;
import com.electrocraft.nirzo.pluse.model.DoctorSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * This class is responsible for  generate single row item in list
 *
 * @author Faisal Mohammad
 * @since 2/22/2018
 */

public class PatientsAppointmentListAdapter extends RecyclerView.Adapter<PatientsAppointmentListAdapter.ViewHolder> {

    private static final String TAG = "PatientsAppointmentListAdapter";
    private List<AppointmentModel> list;

    private Context mContext;
    private ProgressDialog pDialog;
    EditClickListener setEditClickListener;


    public interface EditClickListener {
        void OnClick(int position);
    }
    public PatientsAppointmentListAdapter(List<AppointmentModel> list, EditClickListener setEditClickListener) {
        this.list = list;
        this.setEditClickListener = setEditClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private WeakReference<EditClickListener> listenerRef;
        @BindView(R.id.lr_iv_DocImage)
        CircleImageView docImage;
        @BindView(R.id.lr_tv_DocName)
        TextView docName;
        @BindView(R.id.lr_tv_AppointmentDateNTime)
        TextView tv_AppointmentDateNTime;
        @BindView(R.id.lr_btn_Call)
        Button callButton;
        @BindView(R.id.lr_tv_Specialization)
        TextView lr_tv_Specialization;

        /*   @BindView(R.id.lr_btn_book)
        Button btnBook;
        @BindView(R.id.lr_iv_DocAvailableStatus)
        ImageView docAvailable;*/

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            listenerRef = new WeakReference<>(setEditClickListener);
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerRef.get().OnClick(getAdapterPosition());
                }
            });
        }
    }

//    ViewHolder holder;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_patient_appointment, parent, false);

        mContext = parent.getContext();
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppointmentModel model = list.get(position);
        holder.docName.setText(model.getDoctorName());
        holder.tv_AppointmentDateNTime.setText(model.getAppointmentDate() + " (" + model.getInTime() + ")");

        if (model.getDoctorID() != null)
            getDoctorProfile(model.getDoctorID(),holder);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void getDoctorImageRequest(String imageLink, final ViewHolder holder) {

/*    pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();*/


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
    }

    /**
     *
     * @param doctorId
     * @param holder
     */

    private void getDoctorProfile(final String doctorId, final ViewHolder holder) {
        String patient_login_tag = "doc_profile_tag";
        if (pDialog == null)
            pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getdoctorProfileView/" + doctorId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("DIM", response);


                        closeDialog();

                        String Photo = "";


                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getString("status").equals("success")) {
                                if (!object.isNull("data")) {
                                    JSONArray array = object.getJSONArray("data");

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject = array.getJSONObject(i);

                                        Photo = jsonObject.getString("Photo");

                                    }
                                    getDoctorImageRequest(Photo, holder);

                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                closeDialog();
            }
        });

        AppController.getInstance().

                addToRequestQueue(stringRequest, patient_login_tag);

    }

    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }
}
