package com.example.tom.btalarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class SoundListAdapter extends ArrayAdapter<String> {
    public HashMap<String,String> objects;

    public SoundListAdapter(@NonNull Context context, @NonNull HashMap<String,String> objects) {
        super(context, 0, new ArrayList<String>(objects.keySet()));
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //final ShoppingListItem slItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sound_list_line,parent,false);
        }


        String soundLocation = objects.get(getItem(position));
        if (soundLocation.equals(BTDCProfileData.instance.getSoundLocation())) {
            convertView.setBackgroundColor(Color.argb(125,125,125,125));
        } else {
            convertView.setBackgroundColor(Color.argb(0,0,0,0));
        }


        convertView.setTag(soundLocation);

        TextView tvItemName = (TextView)convertView.findViewById(R.id.d_tvSound);
        tvItemName.setText(getItem(position));

        ImageView ivPlayButton = (ImageView)convertView.findViewById(R.id.d_imageSound) ;
        ivPlayButton.setOnClickListener(new View.OnClickListener() {
            private String anonSoundLocation;
            @Override
            public void onClick(View v) {

                try {

                    Intent i = new Intent(getContext(), SoundPlayerService.class);
                    i.putExtra(SoundPlayerService.SOUND_LOCATION, anonSoundLocation);
                    getContext().startService(i);

                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            private View.OnClickListener init(String m_soundLocation) {
                this.anonSoundLocation = m_soundLocation;
                return this;
            }
        }.init(soundLocation));

        convertView.setOnClickListener(new View.OnClickListener() {
            SoundListAdapter adapter;
            @Override
            public void onClick(View v) {
                BTDCProfileData.instance.setSoundLocation((String) (v.getTag()));
                v.setBackgroundColor(Color.argb(125,125,125,125));
                BTDCListenerService.instance.saveData();
                adapter.notifyDataSetChanged();

            }

            public View.OnClickListener init (SoundListAdapter m_adapter) {
                adapter = m_adapter;
                return this;
            }

        }.init(this));

        return convertView;
    }
}
