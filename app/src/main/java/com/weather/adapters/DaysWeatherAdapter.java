package com.weather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.R;

import java.util.ArrayList;
import java.util.List;

public class DaysWeatherAdapter extends RecyclerView.Adapter<DaysWeatherAdapter.ViewHolder> {

    private List<DayItem> listDays;

    public DaysWeatherAdapter(List<DayItem> listDays) {
        this.listDays = listDays;
    }

    @NonNull
    @Override
    public DaysWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayItem dayItem = listDays.get(position);

        holder.getTextViewDate().setText(dayItem.getDate());
        holder.getTextViewName().setText(dayItem.getDayName());
        holder.getTextViewTemp().setText(dayItem.getTemperature());
    }

    @Override
    public int getItemCount() {
        return listDays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewName;
        private TextView textViewTemp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }

        public TextView getTextViewName() {
            return textViewName;
        }

        public TextView getTextViewTemp() {
            return textViewTemp;
        }
    }
}
