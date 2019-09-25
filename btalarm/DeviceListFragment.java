package com.example.tom.btalarm;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


public class DeviceListFragment extends Fragment {

    private ListView mainList;
    private DeviceListAdapter deviceListAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View temp = super.onCreateView(inflater, container, savedInstanceState);
        View rootView;
        try {
            rootView = inflater.inflate(R.layout.fragmented_list_layout, container, false);
        } catch (Exception e) {
            Toast.makeText(MainActivity.instance.getApplicationContext(), "Loading fragment", Toast.LENGTH_SHORT).show();
            return temp;
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            mainList = (ListView) getView().findViewById(R.id.d_listview);
            deviceListAdapter = new DeviceListAdapter(getActivity(), MainActivity.instance.deviceMap);
            mainList.setAdapter(deviceListAdapter);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public void updateListView() {
        deviceListAdapter.notifyDataSetChanged();
    }


}
