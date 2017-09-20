package soumyadeb.foodsinghadmin.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import soumyadeb.foodsinghadmin.adapters.MenuListRecyclerAdapter;
import soumyadeb.foodsinghadmin.models.Menu;

public class MenuListActivity extends AppCompatActivity {

    //private ListView mMenuList;
    private RecyclerView mMenuList;
    public static ArrayList<Menu> menuArrayList;
    //public static MenuListAdapter adapter;
    public static MenuListRecyclerAdapter adapter;
    private String category;

    ProgressDialog mProgress;
    Button mAddMenuItem;

    StringRequest stringRequest;
    RequestQueue queue;


    final String REQ_URL = "http://mindwires.in/foodsingh_app/get_menu.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        Intent intent = getIntent();

        if(intent != null) {
            category = intent.getStringExtra("category");
            getSupportActionBar().setTitle("Menu: " +category);
        }
        else
            finish();

        mMenuList = (RecyclerView) findViewById(R.id.menu_list);


        mAddMenuItem = (Button) findViewById(R.id.add_new_menu);


        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading data...");
        mProgress.setCanceledOnTouchOutside(false);

        queue = Volley.newRequestQueue(this);

        mAddMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.alert_menu_data, null);
                final EditText etName = (EditText) alertLayout.findViewById(R.id.name);
                final EditText etCategory = (EditText) alertLayout.findViewById(R.id.category);
                etCategory.setText(category);
                etCategory.setFocusable(false);
                final EditText etPrice = (EditText) alertLayout.findViewById(R.id.price);
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuListActivity.this);
                alert.setTitle("Add menu item");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgress.setMessage("Adding menu item...");
                        mProgress.show();
                        final String REQ_URL2 = "http://mindwires.in/foodsingh_app/add_menu.php";
                        final String name = etName.getText().toString();
                        final String category = etCategory.getText().toString();
                        final String price = etPrice.getText().toString();
                        //Toast.makeText(getContext(), category, Toast.LENGTH_LONG).show();
                        stringRequest = new StringRequest(Request.Method.POST, REQ_URL2,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {

                                            JSONObject jsonObject = new JSONObject(response);
                                            String result = jsonObject.getString("result");
                                            mProgress.dismiss();
                                            if(result.equals("true")) {
                                                Toast.makeText(MenuListActivity.this, "Menu item added successfully", Toast.LENGTH_LONG).show();
                                                updateUI();
                                            }
                                            else
                                                Toast.makeText(MenuListActivity.this, "Error occurred, please try again\n"+jsonObject.getString("message").toString(), Toast.LENGTH_LONG).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("DATA",error.toString());
                                Toast.makeText(MenuListActivity.this, "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
                                mProgress.dismiss();
                            }
                        }){
                            @Override
                            protected Map getParams(){
                                Map<String, String> map = new HashMap<>();
                                map.put("name", name);
                                map.put("category", category);
                                map.put("price", price);

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    private void updateUI() {
        mProgress.show();
        menuArrayList = new ArrayList<>();
        //adapter = new MenuListAdapter(this, R.layout.list_item_menu, menuArrayList);
        adapter = new MenuListRecyclerAdapter(MenuListActivity.this, menuArrayList);
        RecyclerView.LayoutManager mLayout= new LinearLayoutManager(MenuListActivity.this);
        mMenuList.setAdapter(adapter);
        mMenuList.setLayoutManager(mLayout);
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
                                String name = obj.getString("name");
                                String price = obj.getString("price");

                                menuArrayList.add(new Menu(id, name, category, price));

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
                Toast.makeText(MenuListActivity.this, "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }){
            @Override
            protected Map getParams(){
                Map<String, String> map = new HashMap<>();
                map.put("category", category);

                return map;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
