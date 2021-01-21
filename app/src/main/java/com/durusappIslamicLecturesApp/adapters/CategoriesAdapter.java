package com.durusappIslamicLecturesApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.durusappIslamicLecturesApp.R;
import com.durusappIslamicLecturesApp.models.CategoriesResponse;


import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    Context context;
    List<CategoriesResponse.Category> categoryList;
    private final LayoutInflater mInflater;
    protected  CategoryOnClickListener categoryOnClickListener;

    public CategoriesAdapter(Context context, List<CategoriesResponse.Category> categoryList, CategoryOnClickListener categoryOnClickListener) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.categoryList = categoryList;
        this.categoryOnClickListener = categoryOnClickListener;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.category_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {

        holder.categoryName.setText(categoryList.get(position).getCategory_name());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView categoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.tv_category_name);

            itemView.setOnClickListener(v -> categoryOnClickListener.categoryOnClick(categoryList.get(getAdapterPosition())));
        }
    }

    public  interface CategoryOnClickListener{
        void categoryOnClick(CategoriesResponse.Category category);
    }
}
