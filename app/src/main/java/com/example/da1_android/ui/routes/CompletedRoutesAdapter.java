package com.example.da1_android.ui.routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.da1_android.R;
import com.example.da1_android.data.model.CompletedRouteDTO;

import java.util.ArrayList;
import java.util.List;

public class CompletedRoutesAdapter extends BaseAdapter {

    private Context context;
    private List<CompletedRouteDTO> routes = new ArrayList<>();

    public CompletedRoutesAdapter(Context context) {
        this.context = context;
    }

    public void setRoutes(List<CompletedRouteDTO> routes) {
        this.routes = routes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public Object getItem(int position) {
        return routes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return routes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_completed_route, parent, false);
            holder = new ViewHolder();
            holder.textAddress = convertView.findViewById(R.id.textAddress);
            holder.textStartedAt = convertView.findViewById(R.id.textStartedAt);
            holder.textFinishedAt = convertView.findViewById(R.id.textFinishedAt);
            holder.textZone = convertView.findViewById(R.id.textZone);
            holder.textStatus = convertView.findViewById(R.id.textStatus);
            holder.textClient = convertView.findViewById(R.id.textClient);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CompletedRouteDTO route = routes.get(position);

        holder.textAddress.setText(route.getAddress());
        holder.textZone.setText("Zona: " + route.getZone());
        holder.textStartedAt.setText("Inicio: " + route.getStartedAt());
        holder.textFinishedAt.setText("Fin: " + route.getFinishedAt());
        holder.textStatus.setText("Estado: " + route.getStatus());

        // Cambiar color del estado según valor
        String status = route.getStatus() != null ? route.getStatus().toUpperCase() : "";
        switch (status) {
            case "COMPLETED":
                holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.engineering_orange));
                break;
            case "PENDING":
                holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.engineering_orange));
                break;
            default:
                holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.coffee));
                break;
        }

        // Cliente (receptor del paquete)
        if (route.getPackageDTO() != null && route.getPackageDTO().getReceptor() != null) {
            holder.textClient.setText("Cliente: " + route.getPackageDTO().getReceptor());
        } else {
            holder.textClient.setText("Cliente: No disponible");
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView textAddress;
        TextView textStartedAt;
        TextView textFinishedAt;
        TextView textZone;
        TextView textStatus;
        TextView textClient;
    }
}
