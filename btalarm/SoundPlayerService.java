package com.example.tom.btalarm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by tom on 2/7/18.
 */

public class SoundPlayerService extends Service {
    public static final String SOUND_LOCATION = "SOUND_LOCATION";
    public static final String FROM_LISTENER = "FROM_LISTENER";
    private Uri lastSoundLocation = null;

    private MediaPlayer mp;

    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri soundLocation = Uri.parse(intent.getExtras().getString(SOUND_LOCATION));
        boolean fromListener = intent.getExtras().getBoolean(FROM_LISTENER);


        if (mp != null) {
        //if (mp.isPlaying()) {
            try {
                mp.stop();
                //mp.reset();
                mp.release();
            } catch (Exception e) {}
        }
        mp = null;
        if (!soundLocation.equals(lastSoundLocation) || fromListener) {


            mp = MediaPlayer.create(this, soundLocation);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //mp.reset();
                    mp.release();

                }
            });
            mp.start();

            lastSoundLocation = soundLocation;
        } else {lastSoundLocation = null;}



        return START_NOT_STICKY;
    }



}
