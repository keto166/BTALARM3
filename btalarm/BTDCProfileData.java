package com.example.tom.btalarm;


import android.media.RingtoneManager;

import java.io.Serializable;
import java.util.ArrayList;

public class BTDCProfileData implements Serializable {
    public static BTDCProfileData instance;
    public ArrayList<String> savedAddresses; //all the device addresses that have had an alarm set on them.
    private String soundLocation = null;
    private int soundType;



    public BTDCProfileData() {
        if (instance == null) {
            instance = this;
        }
        savedAddresses = new ArrayList<String>();
        savedAddresses.add("TEST");
        soundLocation = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString();
        soundType = RingtoneManager.TYPE_NOTIFICATION;
    }

    public static void updateSingleton(BTDCProfileData source) {
        instance.savedAddresses.clear();
        instance.savedAddresses.addAll(source.savedAddresses);
        if (instance.savedAddresses.isEmpty()) {
            instance.savedAddresses.add("TEST");
        }
        instance.setSoundLocation(source.getSoundLocation());
        instance.setSoundType(source.getSoundType());
    }

    public void setSoundLocation(String m_soundLocation) {
        soundLocation = m_soundLocation;
        BTDCListenerService.instance.saveData();
    }

    public String getSoundLocation() {
        return soundLocation;
    }

    public void setSoundType(int m_soundType) {
        soundType = m_soundType;
        BTDCListenerService.instance.saveData();
    }

    public int getSoundType() {
        return soundType;
    }




}
