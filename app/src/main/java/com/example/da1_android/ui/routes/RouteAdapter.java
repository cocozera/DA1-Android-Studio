package com.example.da1_android.ui.routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.core.content.ContextCompat;

import com.example.da1_android.R;
import com.example.da1_android.data.model.RouteDTO;

import java.util.List;

public class RouteAdapter extends ArrayAdapter<RouteDTO> {

    public RouteAdapter(Context context, List<RouteDTO> routes) {
        super(context, 0, routes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_route, parent, false);
            holder = new ViewHolder();
            holder.txtZone = convertView.findViewById(R.id.routeZone);
            holder.txtStatus = convertView.findViewById(R.id.routeStatus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RouteDTO route = getItem(position);

        if (route != null) {
            String zone = route.getZone() != null ? route.getZone() : "Sin zona";
            String status = route.getStatus() != null ? route.getStatus().toUpperCase() : "Sin estado";

            holder.txtZone.setText(zone);
            holder.txtStatus.setText("Estado: " + status);

            // Color según estado
            int color;
            switch (status) {
                case "COMPLETED":
                    color = ContextCompat.getColor(getContext(), R.color.engineering_orange);
                    break;
                case "PENDING":
                    color = ContextCompat.getColor(getContext(), R.color.engineering_orange);
                    break;
                default:
                    color = ContextCompat.getColor(getContext(), R.color.coffee);
                    break;
            }
            holder.txtStatus.setTextColor(color);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView txtZone;
        TextView txtStatus;
    }
}
