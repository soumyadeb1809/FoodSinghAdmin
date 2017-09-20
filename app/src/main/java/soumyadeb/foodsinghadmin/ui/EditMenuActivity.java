package soumyadeb.foodsinghadmin.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import soumyadeb.foodsinghadmin.R;

public class EditMenuActivity extends AppCompatActivity {

    private EditText mName, mCategory, mPrice;
    private Button mUpdateMenuItem;
    private String mMenuId;

    ProgressDialog mProgress;


    StringRequest stringRequest;
    RequestQueue queue;


    final String REQ_URL = "http://mindwires.in/foodsingh_app/update_menu.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        Intent intent = getIntent();
        if(intent == null){
            finish();
        }

        mMenuId = intent.getStringExtra("id");

        mName = (EditText) findViewById(R.id.name);
        mName.setText(intent.getStringExtra("name"));
        mCategory = (EditText)findViewById(R.id.category);
        mCategory.setText(intent.getStringExtra("category"));
        mPrice = (EditText)findViewById(R.id.price);
        mPrice.setText(intent.getStringExtra("price"));
        mUpdateMenuItem = (Button) findViewById(R.id.update);

        queue = Volley.newRequestQueue(EditMenuActivity.this);
        mProgress = new ProgressDialog(this);
        mUpdateMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mName.getText().toString();
                final String category = mCategory.getText().toString();
                final String price = mPrice.getText().toString();
                mProgress.setMessage("Updating menu item...");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();
                stringRequest = new StringRequest(Request.Method.POST, REQ_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    String result = jsonObject.getString("result");
                                    mProgress.dismiss();
                                    if(result.equals("true")) {
                                        Toast.makeText(EditMenuActivity.this, "Menu item updated successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else
                                        Toast.makeText(EditMenuActivity.this, "Error occurred, please try again\n"+jsonObject.getString("message").toString(), Toast.LENGTH_LONG).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DATA",error.toString());
                        Toast.makeText(EditMenuActivity.this, "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
                        mProgress.dismiss();
                    }
                }){
                    @Override
                    protected Map getParams(){
                        Map<String, String> map = new HashMap<>();
                        map.put("id", mMenuId);
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


    }
}
