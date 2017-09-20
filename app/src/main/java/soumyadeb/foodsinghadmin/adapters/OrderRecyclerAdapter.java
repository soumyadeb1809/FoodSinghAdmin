package soumyadeb.foodsinghadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import soumyadeb.foodsinghadmin.R;
import soumyadeb.foodsinghadmin.models.Order;
import soumyadeb.foodsinghadmin.ui.OrderDetailActivity;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Order> orderArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem, tvAmount, tvMobile, tvTimestamp, tvOrderId;
        public View mView;
        public MyViewHolder(View mView) {
            super(mView);
            this.mView = mView;
            tvItem = (TextView) mView.findViewById(R.id.item);
            tvMobile = (TextView) mView.findViewById(R.id.mobile);
            tvAmount = (TextView) mView.findViewById(R.id.amount);
            tvTimestamp = (TextView) mView.findViewById(R.id.timestamp);
            tvOrderId = (TextView) mView.findViewById(R.id.order_id);
        }

    }

    public OrderRecyclerAdapter(Context mContext, ArrayList<Order> orderArrayList)
    {
        this.mContext=mContext;
        this.orderArrayList=orderArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        String item = orderArrayList.get(position).getItem();
        String mobile = orderArrayList.get(position).getMobile();
        String timestamp = orderArrayList.get(position).getTimestamp();
        String amount = orderArrayList.get(position).getAmount();
        final String orderId = orderArrayList.get(position).getId();

        holder.tvItem.setText(item);
        holder.tvMobile.setText(mobile);
        holder.tvAmount.setText("₹"+amount);
        holder.tvTimestamp.setText(timestamp);
        holder.tvOrderId.setText(orderId);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timestamp = orderArrayList.get(position).getTimestamp();
                String item = orderArrayList.get(position).getItem();
                String amount = orderArrayList.get(position).getAmount();
                String mobile = orderArrayList.get(position).getMobile();
                String address = orderArrayList.get(position).getAddress();
                String comments = orderArrayList.get(position).getComments();
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("timestamp", timestamp);
                intent.putExtra("item", item);
                intent.putExtra("amount", amount);
                intent.putExtra("mobile", mobile);
                intent.putExtra("address", address);
                intent.putExtra("id", orderId);
                intent.putExtra("comments", comments);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }


}