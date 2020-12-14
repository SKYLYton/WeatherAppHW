package com.weather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.R;

import java.util.List;

public class CitiesWeatherAdapter extends RecyclerView.Adapter<CitiesWeatherAdapter.ViewHolder> {
    private List<CityItem> listCities;
    private OnItemSelect onItemSelect;

    public interface OnItemSelect {
        void itemSelected(int cityId, String cityName);
    }

    public void setOnItemSelect(OnItemSelect onItemSelect) {
        this.onItemSelect = onItemSelect;
    }

    public CitiesWeatherAdapter(List<CityItem> listDays) {
        this.listCities = listDays;
    }

    @NonNull
    @Override
    public CitiesWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityItem cityItem = listCities.get(position);

        holder.getTextViewName().setText(cityItem.getCityName());

        holder.getView().setOnClickListener(v -> {
            if(onItemSelect != null) {
                onItemSelect.itemSelected(cityItem.getId(), cityItem.getCityName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            view = itemView;
        }

        public TextView getTextViewName() {
            return textViewName;
        }

        public View getView() {
            return view;
        }
    }
}
