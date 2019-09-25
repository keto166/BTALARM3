package com.example.tom.btalarm;


import android.app.FragmentTransaction;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SoundMenuFragment extends Fragment {


    Button buttonBeep,buttonAlarm;
    View rootView;

    private void iniVars() {


        buttonBeep = (Button) rootView.findViewById(R.id.d_buttonBeep);
        buttonBeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    SoundListFragment frag = new SoundListFragment();
                    Bundle b = new Bundle();
                    b.putInt(SoundListFragment.SOUND_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                    frag.setArguments(b);
                    ft.replace(R.id.d_listFragment_Sound,frag);
                    ft.addToBackStack(null);
                    ft.commit();
                } catch (Exception e) {

                }

            }
        });

        buttonAlarm = (Button) rootView.findViewById(R.id.d_buttonAlarm);
        buttonAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    SoundListFragment frag = new SoundListFragment();
                    Bundle b = new Bundle();
                    b.putInt(SoundListFragment.SOUND_TYPE, RingtoneManager.TYPE_ALARM);
                    frag.setArguments(b);
                    ft.replace(R.id.d_listFragment_Sound,frag);
                    ft.addToBackStack(null);
                    ft.commit();
                } catch (Exception e) {

                }
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View temp = super.onCreateView(inflater, container, savedInstanceState);

        try {
            rootView = inflater.inflate(R.layout.sound_top_level_layout, container, false);
        } catch (Exception e) {
            return temp;
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniVars();
    }

}
