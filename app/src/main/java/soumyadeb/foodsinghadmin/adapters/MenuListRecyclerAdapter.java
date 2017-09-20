package soumyadeb.foodsinghadmin.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import soumyadeb.foodsinghadmin.R;
import soumyadeb.foodsinghadmin.Utility;
import soumyadeb.foodsinghadmin.models.Menu;
import soumyadeb.foodsinghadmin.models.Order;
import soumyadeb.foodsinghadmin.ui.EditMenuActivity;
import soumyadeb.foodsinghadmin.ui.OrderDetailActivity;

public class MenuListRecyclerAdapter extends RecyclerView.Adapter<MenuListRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Menu> menuArrayList;
    private Utility utility;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvPrice;
        public LinearLayout edit, delete;
        public View mView;
        public MyViewHolder(View mView) {
            super(mView);
            this.mView = mView;
            tvName = (TextView) mView.findViewById(R.id.name);
            tvPrice = (TextView) mView.findViewById(R.id.price);
            delete = (LinearLayout) mView.findViewById(R.id.delete);
            edit = (LinearLayout) mView.findViewById(R.id.edit);
        }

    }

    public MenuListRecyclerAdapter(Context mContext, ArrayList<Menu> menuArrayList)
    {
        this.mContext=mContext;
        this.menuArrayList=menuArrayList;
        utility = new Utility(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_menu,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final String name = menuArrayList.get(position).getName();
        String price = menuArrayList.get(position).getPrice();

        holder.tvName.setText(name);

        holder.tvPrice.setText("₹"+price);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Delete @ "+position, Toast.LENGTH_LONG).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setMessage("Are your sure you want to delete menu item "+name+"?");
                alert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        utility.deleteMenu(menuArrayList.get(position).getId(), position);
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing.
                    }
                });
                alert.show();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditMenuActivity.class);
                intent.putExtra("id", menuArrayList.get(position).getId());
                intent.putExtra("name", menuArrayList.get(position).getName());
                intent.putExtra("category", menuArrayList.get(position).getCategory());
                intent.putExtra("price", menuArrayList.get(position).getPrice());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return menuArrayList.size();
    }


}