package soumyadeb.foodsinghadmin.ui;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

import soumyadeb.foodsinghadmin.R;
import soumyadeb.foodsinghadmin.adapters.OrderRecyclerAdapter;
import soumyadeb.foodsinghadmin.models.Order;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllOrdersFragment extends Fragment {

    //ListView mOrderList;
    RecyclerView mOrderList;
    OrderRecyclerAdapter adapter;
    //OrderAdapter adapter;
    ArrayList<Order> orderArrayList;

    ProgressDialog mProgress;

    StringRequest stringRequest;
    RequestQueue queue;


    final String REQ_URL = "http://mindwires.in/foodsingh_app/get_orders.php";



    public AllOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View mView = inflater.inflate(R.layout.fragment_all_orders, container, false);

        //mOrderList = (ListView) mView.findViewById(R.id.order_list);
        mOrderList = (RecyclerView) mView.findViewById(R.id.order_list);

        updateUI();

        setHasOptionsMenu(true);
        return mView;
    }

    private void updateUI() {
        orderArrayList = new ArrayList<>();
        //adapter = new OrderAdapter(getContext(), R.layout.list_item_order, orderArrayList);
        adapter=new OrderRecyclerAdapter(getActivity(), orderArrayList);
        RecyclerView.LayoutManager mLayout= new LinearLayoutManager(getActivity());
        mOrderList.setAdapter(adapter);
        mOrderList.setLayoutManager(mLayout);


        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("Loading data...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        queue = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.POST, REQ_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            for (int index = 0; index < jsonArray.length(); index++) {
                                JSONObject obj = jsonArray.getJSONObject(index);

                                String id = obj.getString("id") ;
                                String timestamp = obj.getString("timestamp");
                                String item = obj.getString("item");
                                String amount = obj.getString("amount");
                                String mobile = obj.getString("mobile");
                                String address = obj.getString("address");
                                String comments = obj.getString("comments");

                                orderArrayList.add(new Order(id, timestamp, item, amount, mobile, address, comments));

                            }
                            adapter.notifyDataSetChanged();
                            mProgress.dismiss();
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
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_all_orders, menu);
    }

}
