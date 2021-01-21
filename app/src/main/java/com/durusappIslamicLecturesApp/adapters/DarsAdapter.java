package com.durusappIslamicLecturesApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.durusappIslamicLecturesApp.R;
import com.durusappIslamicLecturesApp.models.ScholarsModel;


import java.util.List;

import static com.durusappIslamicLecturesApp.utils.Constants.FROM_CATAGORIES;
import static com.durusappIslamicLecturesApp.utils.Constants.FROM_SCHOLARS;

public class DarsAdapter extends RecyclerView.Adapter<DarsAdapter.ViewHolder> {



    Context context;
    int from;
    List<ScholarsModel.Dar> darList;
    private final LayoutInflater mInflater;
    DarsOnClick darsOnClick;

    public DarsAdapter(Context context, List<ScholarsModel.Dar> darList, int from, DarsOnClick darsOnClick) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.from = from;
        this.darList = darList;
        this.darsOnClick = darsOnClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.dars_viewholder, parent, false);
        return new DarsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.darsType.setText(darList.get(position).getDarsType_name());
        holder.index.setText(String.valueOf(position + 1));
        holder.location.setText(darList.get(position).getLocation());

        holder.darsTitle.setText(darList.get(position).getDarsTitle());

        if(from == FROM_CATAGORIES)
            holder.scholar_or_category_name.setText(darList.get(position).getScholarName());
            else if (from == FROM_SCHOLARS)
                holder.scholar_or_category_name.setText(darList.get(position).getCategoryName());

        if (darList.get(position).getDarAudio() != null) {
            holder.number_of_lessons.setText(String.valueOf(darList.get(position).getDarAudio().size()));
        } else
            holder.number_of_lessons.setText("0");
    }

    @Override
    public int getItemCount() {
        return darList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView darsTitle, scholar_or_category_name, number_of_lessons, location, index, darsType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            darsTitle = itemView.findViewById(R.id.tv_course_name);
            scholar_or_category_name = itemView.findViewById(R.id.tv_sheikh_or_catagory_name);
            number_of_lessons = itemView.findViewById(R.id.tv_number_of_lessons);
            location = itemView.findViewById(R.id.tv_dars_location);
            index = itemView.findViewById(R.id.tv_course_index);
            darsType = itemView.findViewById(R.id.tv_type_of_dars);

            itemView.setOnClickListener(v -> darsOnClick.darsOnClickListener(darList.get(getAdapterPosition())));
        }
    }

    public interface DarsOnClick{
        void darsOnClickListener(ScholarsModel.Dar dar);
    }
}