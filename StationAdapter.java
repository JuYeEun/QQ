package com.example.map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    public String plainNo1;
    public String stNm;
    ArrayList<StationAdapter> items = new ArrayList<StationAdapter>();
    public  void setItems(ArrayList<StationAdapter> items) {this.items = items;}

    public  void clearItems() {items.clear();}
    public  void addItem(StationAdapter stationAdapter){items.add(stationAdapter); }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itview = inflater.inflate(R.layout.item,parent,false);

        return new ViewHolder(itview) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StationAdapter item = items.get(position);
        holder.setItem(item);


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean checkRecAllData() {
        return false;
    }

    static  class ViewHolder extends RecyclerView.ViewHolder {

        TextView bus_id;
        TextView bus_nm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bus_id= itemView.findViewById(R.id.bus_id);
            bus_nm = itemView.findViewById(R.id.bus_nm);
        }


        public void setItem(StationAdapter stationAdapter) {
            bus_id.setText(stationAdapter.plainNo1);
            bus_nm.setText(stationAdapter.stNm);
        }
    }
}
