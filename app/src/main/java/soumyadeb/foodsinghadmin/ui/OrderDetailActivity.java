package soumyadeb.foodsinghadmin.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import soumyadeb.foodsinghadmin.R;

public class OrderDetailActivity extends AppCompatActivity {

    TextView tvTimestamp, tvItem, tvAmount, tvMobile, tvAddress, tvId, tvComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        getSupportActionBar().setTitle("Order Details");
        tvItem = (TextView)findViewById(R.id.item);
        tvTimestamp = (TextView)findViewById(R.id.timestamp);
        tvAmount = (TextView)findViewById(R.id.amount);
        tvMobile = (TextView)findViewById(R.id.mobile);
        tvAddress = (TextView)findViewById(R.id.address);
        tvId = (TextView)findViewById(R.id.order_id);
        tvComments = (TextView)findViewById(R.id.comments);

        Intent intent = getIntent();

        if(intent!=null) {
            String item = intent.getStringExtra("item");
            tvItem.setText(item);
            String timestamp = intent.getStringExtra("timestamp");
            tvTimestamp.setText(timestamp);
            String amount = intent.getStringExtra("amount");
            tvAmount.setText("₹"+amount);
            String mobile = intent.getStringExtra("mobile");
            tvMobile.setText(mobile);
            String address = intent.getStringExtra("address");
            tvAddress.setText(address);
            String id = intent.getStringExtra("id");
            tvId.setText(id);
            String comments = intent.getStringExtra("comments");
            tvComments.setText(comments);
        }

    }
}
