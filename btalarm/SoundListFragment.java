package com.example.tom.btalarm;

import android.app.Fragment;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class SoundListFragment extends Fragment {

    private ListView mainList;
    public ArrayList<String> list;
    public HashMap<String,String> soundMap;
    public SoundListAdapter soundListAdapter;
    public static final String SOUND_TYPE = "sound_type";
    int soundType;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View temp = super.onCreateView(inflater, container, savedInstanceState);
        View rootView;
        Bundle b = getArguments();
        soundType = b.getInt(SOUND_TYPE);



        list = new ArrayList<>();
        soundMap = new HashMap<>();
        try {
            rootView = inflater.inflate(R.layout.fragmented_list_layout, container, false);
        } catch (Exception e) {
            return temp;
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*if (soundType == RingtoneManager.TYPE_ALARM) {
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            SoundAlarmFragment frag = new SoundAlarmFragment();
            ft.replace(R.id.d_emptyFragmentListLayout,frag);
            ft.commit();
        }
        */

        RingtoneManager manager = new RingtoneManager(getActivity());
        manager.setType(soundType);
        Cursor cursor = manager.getCursor();

        try {
            String soundName;
            String soundLocation;
            int x;
            soundMap.clear();
            while (cursor.moveToNext()) {
                soundName = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                soundLocation = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)
                        + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
                if (soundMap.containsKey(soundName)) {
                    x = 2;
                    while (soundMap.containsKey(soundName + " - " + x)) {x++;}
                    soundMap.put(soundName + " - " + x, soundLocation);
                } else {
                    soundMap.put(soundName, soundLocation);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        while (cursor.moveToNext()) {
            list.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
        }

        try {
            mainList = (ListView) getView().findViewById(R.id.d_listview);
            soundListAdapter = new SoundListAdapter(getActivity(), soundMap);
            mainList.setAdapter(soundListAdapter);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
