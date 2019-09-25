package com.example.tom.btalarm;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class DeviceListAdapter extends ArrayAdapter<String> {
    public HashMap<String,String> objects;

    public DeviceListAdapter(@NonNull Context context, @NonNull HashMap<String,String> objects) {
        super(context, 0, new ArrayList<String>(objects.keySet()));
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //final ShoppingListItem slItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_line,parent,false);
        }

        String address = objects.get(getItem(position));
        boolean temp = BTDCProfileData.instance.savedAddresses.contains(address);

        TextView tvItemName = (TextView)convertView.findViewById(R.id.d_tvDeviceName);
        tvItemName.setText(getItem(position));


        if (temp) {
            convertView.setBackgroundColor(Color.argb(125,125,125,125));
        } else {
            convertView.setBackgroundColor(Color.argb(0,0,0,0));
        }



        convertView.setOnClickListener(new View.OnClickListener() {
            String anonAddress;
            @Override
            public void onClick(View v) {

                boolean temp = BTDCProfileData.instance.savedAddresses.contains(anonAddress);
                if (temp) {
                    BTDCProfileData.instance.savedAddresses.remove(anonAddress);
                    v.setBackgroundColor(Color.argb(0,0,0,0));
                } else {
                    BTDCProfileData.instance.savedAddresses.add(anonAddress);
                   v.setBackgroundColor(Color.argb(125,125,125,125));
                }
                BTDCListenerService.instance.saveData();



            }

            public View.OnClickListener init(String m_address) {
                anonAddress = m_address;
                return this;
            }
        }.init(address));

        return convertView;
    }
}
