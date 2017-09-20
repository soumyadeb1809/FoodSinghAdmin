package soumyadeb.foodsinghadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import soumyadeb.foodsinghadmin.R;
import soumyadeb.foodsinghadmin.models.Category;
import soumyadeb.foodsinghadmin.ui.MenuListActivity;
import soumyadeb.foodsinghadmin.ui.OrderDetailActivity;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Category> categoryArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ImageView ivImage;
        public View mView;
        public MyViewHolder(View mView) {
            super(mView);
            this.mView = mView;
            tvName = (TextView) mView.findViewById(R.id.category);
            ivImage = (ImageView) mView.findViewById(R.id.image);
        }

    }

    public CategoryRecyclerAdapter(Context mContext, ArrayList<Category> categoryArrayList)
    {
        this.mContext=mContext;
        this.categoryArrayList=categoryArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        String name = categoryArrayList.get(position).getCategory();
        String image = categoryArrayList.get(position).getImage();

        Glide.with(mContext).load(image).skipMemoryCache(true).thumbnail(0.05f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop().into(holder.ivImage);

        holder.tvName.setText(name);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = categoryArrayList.get(position).getCategory();

                Intent intent = new Intent(mContext, MenuListActivity.class);
                intent.putExtra("category", category);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }


}