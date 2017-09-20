package soumyadeb.foodsinghadmin.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import soumyadeb.foodsinghadmin.R;
import soumyadeb.foodsinghadmin.Utility;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    Utility utility;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_all_orders:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new AllOrdersFragment()).commit();
                    getSupportActionBar().setTitle("All Orders");
                    return true;
                case R.id.navigation_menu:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new MenuFragment()).commit();
                    getSupportActionBar().setTitle("Menu Categories");
                    return true;

                case R.id.navigation_accept_order:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new ServiceStatusFragment()).commit();
                    getSupportActionBar().setTitle("Service Status");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("data", MODE_PRIVATE);
        utility = new Utility(this);
        utility.setBackgroundImage(MainActivity.this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_all_orders);

        if(!utility.isNetworkAvailable()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("No internet connection. Please make sure your device is connected to the internet and open the app again.");
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        String login = sp.getString("login","false");
        if(login.equals("false")){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.action_logout){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("login", "false");
            editor.commit();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        if(item.getItemId() == R.id.action_refresh){
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new AllOrdersFragment()).commit();
            getSupportActionBar().setTitle("All Orders");
        }

        return true;
    }
}
