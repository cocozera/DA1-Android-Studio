package com.example.da1_android.ui.routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import com.example.da1_android.R;
import com.example.da1_android.data.model.RouteDTO;
import java.util.List;

public class RouteAdapter extends ArrayAdapter<RouteDTO> {
    public RouteAdapter(Context context, List<RouteDTO> routes) {
        super(context, 0, routes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_route, parent, false);
        }

        RouteDTO route = getItem(position);

        TextView txtZone = convertView.findViewById(R.id.routeZone);
        TextView txtStatus = convertView.findViewById(R.id.routeStatus);


        if (route != null) {
            txtZone.setText(route.getZone() != null ? route.getZone() : "Sin zona");
            txtStatus.setText(route.getStatus() != null ? route.getStatus() : "Sin estado");
        }



        return convertView;
    }
}
