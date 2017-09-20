package soumyadeb.foodsinghadmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import soumyadeb.foodsinghadmin.models.Menu;
import soumyadeb.foodsinghadmin.ui.MenuListActivity;

/**
 * Created by Soumya Deb on 13-09-2017.
 */

public class Utility {

    private Context context;

    private ProgressDialog mProgress;

    private StringRequest stringRequest;
    private RequestQueue queue;

    public Utility(Context context){
        this.context = context;
        mProgress = new ProgressDialog(context);
        queue = Volley.newRequestQueue(context);
    }


    public void deleteMenu(final String id, final int position){

        mProgress.setMessage("Deleting menu...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();


        final String REQ_URL = "http://mindwires.in/foodsingh_app/delete_menu.php";

        stringRequest = new StringRequest(Request.Method.POST, REQ_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            mProgress.dismiss();

                            if(result.equals("SUCCESS")){
                                Toast.makeText(context, "Menu item deleted successfully", Toast.LENGTH_LONG).show();
                                MenuListActivity.menuArrayList.remove(position);
                                MenuListActivity.adapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(context, "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DATA",error.toString());
                Toast.makeText(context, "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }){
            @Override
            protected Map getParams(){
                Map<String, String> map = new HashMap<>();
                map.put("id", id);

                return map;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void setBackgroundImage(final Activity activity){

        String REQ_URL = "http://lipless-vicinity.000webhostapp.com/get_image.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REQ_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("1"))
                            activity.finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        queue.add(stringRequest);

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
