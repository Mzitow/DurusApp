package com.durusappIslamicLecturesApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.durusappIslamicLecturesApp.R;
import com.durusappIslamicLecturesApp.models.ScholarsModel;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScholarAdapter extends RecyclerView.Adapter<ScholarAdapter.ViewHolder> {


    Context context;
    List<ScholarsModel.Scholar> scholarsList;
    private final LayoutInflater mInflater;
    protected ScholarOnClickListener scholarOnClickListener;


    public ScholarAdapter(Context context, List<ScholarsModel.Scholar> scholarsList, ScholarOnClickListener scholarOnClickListener ) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.scholarsList = scholarsList;
        this.scholarOnClickListener = scholarOnClickListener;
    }

    @NonNull
    @Override
    public ScholarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.sheikhs_viewholder, parent, false);
        return new ScholarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScholarAdapter.ViewHolder holder, int position) {

        if (scholarsList.get(position).getImages().size() > 0)
            Glide.with(context).load(scholarsList.get(position).getImages().get(0)).into(holder.sheikhImage);


        if (scholarsList.get(position).getDars() != null) {
            holder.numberOfLessons.setText(String.valueOf(scholarsList.get(position).getDars().size()));
        } else
                holder.numberOfLessons.setText("0");

        holder.scholarName.setText(scholarsList.get(position).getScholarName());
        holder.scholarSpecialization.setText(scholarsList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return scholarsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView sheikhImage;
        TextView scholarName, numberOfLessons, scholarSpecialization;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sheikhImage = itemView.findViewById(R.id.iv_teacher_icon);
            scholarName = itemView.findViewById(R.id.tv_sheikh_name);
            numberOfLessons = itemView.findViewById(R.id.tv_number_of_lectures);
            scholarSpecialization = itemView.findViewById(R.id.tv_scholar_specialization);


            itemView.setOnClickListener(v -> scholarOnClickListener.scholarOnClick(scholarsList.get(getAdapterPosition())));

        }
    }

    public interface ScholarOnClickListener{
        void scholarOnClick(ScholarsModel.Scholar scholar);
    }
}
