package soumyadeb.foodsinghadmin.ui;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

import soumyadeb.foodsinghadmin.R;
import soumyadeb.foodsinghadmin.adapters.CategoryRecyclerAdapter;
import soumyadeb.foodsinghadmin.models.Category;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    //ListView mCategory;

    RecyclerView mCategory;
    //CategoryAdapter adapter;
    CategoryRecyclerAdapter adapter;

    ArrayList<Category> categoryArrayList;

    ProgressDialog mProgress;

    StringRequest stringRequest;
    RequestQueue queue;

    Button mAddCategory;


    final String REQ_URL = "http://mindwires.in/foodsingh_app/get_categories2.php";

    public MenuFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_menu, container, false);

        //mCategory = (ListView) mView.findViewById(R.id.category_list);
        mCategory = (RecyclerView) mView.findViewById(R.id.recycler_view);
        mAddCategory = (Button)mView.findViewById(R.id.add_new_category);
        categoryArrayList = new ArrayList<>();
        adapter = new CategoryRecyclerAdapter(getActivity(), categoryArrayList);
        RecyclerView.LayoutManager mLayout= new GridLayoutManager(getContext(), 2);
        mCategory.setAdapter(adapter);
        mCategory.setLayoutManager(mLayout);

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
                            Log.d("DATA", response);
                            JSONArray jsonArray = new JSONArray(response);

                            for (int index = 0; index < jsonArray.length(); index++) {

                                JSONObject obj = jsonArray.getJSONObject(index);
                                String category = obj.getString("name");
                                String image = obj.getString("image");
                                Log.d("DATA", category);
                                categoryArrayList.add(new Category(category, image));
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
                mProgress.dismiss();
                Toast.makeText(getContext(), "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        /*
        mCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = categoryArrayList.get(position).getCategory();
                Intent intent = new Intent(getActivity(), MenuListActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });

        */

        mAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.alert_add_category, null);
                final EditText etCategory = (EditText) alertLayout.findViewById(R.id.category);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Add category");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgress.setMessage("Adding category...");
                        mProgress.show();
                        final String REQ_URL2 = "http://mindwires.in/foodsingh_app/add_category.php";
                        final String category = etCategory.getText().toString();
                        //Toast.makeText(getContext(), category, Toast.LENGTH_LONG).show();
                        stringRequest = new StringRequest(Request.Method.POST, REQ_URL2,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {

                                            JSONObject jsonObject = new JSONObject(response);
                                            String result = jsonObject.getString("result");
                                            mProgress.dismiss();
                                            if(result.equals("true"))
                                                Toast.makeText(getContext(), "Menu category added successfully", Toast.LENGTH_LONG).show();
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
                                map.put("name", category);

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

}
