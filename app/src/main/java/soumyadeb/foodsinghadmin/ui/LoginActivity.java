package soumyadeb.foodsinghadmin.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import soumyadeb.foodsinghadmin.R;

public class LoginActivity extends AppCompatActivity {
    private final String PASSWORD = "admin@foodsingh";
    SharedPreferences sp;
    EditText mPassword;
    Button mLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPassword = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.login);
        sp = getSharedPreferences("data", MODE_PRIVATE);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPass = mPassword.getText().toString();
                if(inputPass.equals(PASSWORD)){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("login","true");
                    editor.commit();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid password! Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
