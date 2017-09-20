package soumyadeb.foodsinghadmin.ui;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import soumyadeb.foodsinghadmin.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceStatusFragment extends Fragment {

    private Button mChangeStatus;
    private TextView mCurrentStatus;
    private TextView mDiscount;

    ProgressDialog mProgress;

    StringRequest stringRequest;
    RequestQueue queue;

    private String status, discount;

    final String REQ_URL_GET_STATUS = "http://mindwires.in/foodsingh_app/get_service_status.php";
    final String REQ_URL_SET_STATUS = "http://mindwires.in/foodsingh_app/set_service_status.php";
    final String REQ_URL_GET_DISCOUNT = "http://mindwires.in/foodsingh_app/get_discount.php";
    final String REQ_URL_SET_DISCOUNT = "http://mindwires.in/foodsingh_app/set_discount.php";

    public ServiceStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_service_status, container, false);

        mChangeStatus = (Button) mView.findViewById(R.id.change_status);
        mCurrentStatus = (TextView)mView.findViewById(R.id.current_status);
        mDiscount = (TextView)mView.findViewById(R.id.coupon);

        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("Loading data...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        queue = Volley.newRequestQueue(getContext());
        
        //Fetch service status data:
        stringRequest = new StringRequest(Request.Method.POST, REQ_URL_GET_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("DATA", response);
                            JSONObject jsonObject = new JSONObject(response);

                            status = jsonObject.getString("service");

                            updateButton();


                            mProgress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DATA",error.toString());
                mProgress.dismiss();
                Toast.makeText(getContext(), "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        
        //Fetch discount data:
        stringRequest = new StringRequest(Request.Method.POST, REQ_URL_GET_DISCOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("DATA", response);
                            JSONObject jsonObject = new JSONObject(response);

                            discount = jsonObject.getString("discount");
                            
                            mDiscount.setText("Current discount: "+discount+"%");


                            mProgress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DATA",error.toString());
                mProgress.dismiss();
                Toast.makeText(getContext(), "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        mChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Changing service status...");
                mProgress.show();
                final String newStatus;
                if (status.equals("true")){
                    newStatus = "false";
                }
                else{
                    newStatus = "true";
                }

                stringRequest = new StringRequest(Request.Method.POST, REQ_URL_SET_STATUS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    String result = jsonObject.getString("result");
                                    mProgress.dismiss();
                                    if(result.equals("true")) {
                                        Toast.makeText(getContext(), "Service status changed successfully", Toast.LENGTH_LONG).show();
                                        status = newStatus;
                                        updateButton();
                                    }
                                    else
                                        Toast.makeText(getContext(), "Error occurred, please try again\n"+jsonObject.getString("message").toString(), Toast.LENGTH_LONG).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DATA",error.toString());
                        Toast.makeText(getContext(), "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
                        mProgress.dismiss();
                    }
                }){
                    @Override
                    protected Map getParams(){
                        Map<String, String> map = new HashMap<>();
                        map.put("service", newStatus);

                        return map;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });


        mDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.alert_set_discount, null);
                final EditText etDiscount = (EditText) alertLayout.findViewById(R.id.discount);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Set discount");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgress.setMessage("Updating discount...");
                        mProgress.show();

                        final String newDiscount = etDiscount.getText().toString();
                        //Toast.makeText(getContext(), category, Toast.LENGTH_LONG).show();
                        stringRequest = new StringRequest(Request.Method.POST, REQ_URL_SET_DISCOUNT,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {

                                            JSONObject jsonObject = new JSONObject(response);
                                            String result = jsonObject.getString("result");
                                            mProgress.dismiss();
                                            if(result.equals("true")) {
                                                discount = newDiscount;
                                                Toast.makeText(getContext(), "Discount added successfully", Toast.LENGTH_LONG).show();
                                                mDiscount.setText("Current discount: "+discount+"%");
                                            }
                                            else
                                                Toast.makeText(getContext(), "Error occurred, please try again\n"+jsonObject.getString("message").toString(), Toast.LENGTH_LONG).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("DATA",error.toString());
                                Toast.makeText(getContext(), "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
                                mProgress.dismiss();
                            }
                        }){
                            @Override
                            protected Map getParams(){
                                Map<String, String> map = new HashMap<>();
                                map.put("discount", newDiscount);

                                return map;
                            }
                        };
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();

            }
        });

        return mView;
    }

    private void updateButton() {
        if (status.equals("true")){
            mCurrentStatus.setText("Accepting customer orders");
            Drawable textBack = getResources().getDrawable(R.drawable.bck_accent);
            mCurrentStatus.setBackground(textBack);
            mChangeStatus.setText("STOP ACCEPTING ORDERS");
            Drawable drawable = getResources().getDrawable(R.drawable.bck_accpeting);
            mChangeStatus.setBackground(drawable);
        } else if (status.equals("false")){
            mCurrentStatus.setText("Not accepting orders");
            Drawable textBack = getResources().getDrawable(R.drawable.bck_primary);
            mCurrentStatus.setBackground(textBack);
            mChangeStatus.setText("START ACCEPTING ORDERS");
            Drawable drawable = getResources().getDrawable(R.drawable.bck_nt_accepting);
            mChangeStatus.setBackground(drawable);
        }
    }

}
